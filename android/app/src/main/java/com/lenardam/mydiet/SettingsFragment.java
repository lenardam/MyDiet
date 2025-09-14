package com.lenardam.mydiet;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.lenardam.mydiet.adapters.RecipeTagAdapter;
import com.lenardam.mydiet.database.model.RecipeFullData;
import com.lenardam.mydiet.database.model.RecipeIngredients;
import com.lenardam.mydiet.database.model.RecipeInstructions;
import com.lenardam.mydiet.database.model.RecipeTags;
import com.lenardam.mydiet.database.model.Recipes;
import com.lenardam.mydiet.database.model.Tags;
import com.lenardam.mydiet.database.model.Units;
import com.lenardam.mydiet.database.viewModel.RecipesViewModel;
import com.lenardam.mydiet.database.viewModel.TagsViewModel;
import com.lenardam.mydiet.database.viewModel.UnitsViewModel;
import com.lenardam.mydiet.utils.RecipeToExport;
import com.lenardam.mydiet.utils.RecipeIngredientToExport;
import com.lenardam.mydiet.utils.SharedPreferencesSaver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private Button addTagButton;
    private Spinner numberOfMealsSpinner;
    private String[] number_of_meals_options = {"1","2","3","4","5","6"};
    private RecyclerView allTagsRecyclerView;
    private RecipeTagAdapter recipeTagAdapter;

    private static final int REQUEST_CODE_SAVE = 1;
    private static final int REQUEST_CODE_LOAD = 2;
    private Button saveRecipesToFileButton;
    private Button loadRecipesFromFileButton;

    private TagsViewModel tagsViewModel;
    private RecipesViewModel reciecesViewModel;
    private UnitsViewModel unitsViewModel;
    private Map<Long, Units> unitsIdMap = new HashMap<>();
    private Map<String, Units> unitsNameMap = new HashMap<>();
    private Map<Long, Tags> tagsIdMap = new HashMap<>();
    private Map<String, Tags> tagsNameMap = new HashMap<>();
    private List<RecipeFullData> allRecipes = new ArrayList<>();

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setBottomNavigationItem(R.id.menu_bottom_item_empty);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) requireActivity()).setBottomNavigationItem(R.id.menu_bottom_item_empty);
        initViews(view);
        initAllTagsRecycleView(view);
    }

    private void initViews(View view) {
        saveRecipesToFileButton = (Button) view.findViewById(R.id.fr_settings_btn_save_recipes_to_file);
        loadRecipesFromFileButton = (Button) view.findViewById(R.id.fr_settings_btn_load_recipes_from_file);
        numberOfMealsSpinner = (Spinner) view.findViewById(R.id.fr_settings_spin_number_of_meals);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, number_of_meals_options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numberOfMealsSpinner.setAdapter(adapter);
        numberOfMealsSpinner.setSelection(adapter.getPosition(String.valueOf(MainActivity.myDiet.getDietSettings().getNumberOfMealsForDiet())));

        tagsViewModel = new ViewModelProvider(this).get(TagsViewModel.class);
        tagsViewModel.getAllTags().observe(getViewLifecycleOwner(), new Observer<List<Tags>>() {
            @Override
            public void onChanged(List<Tags> tags) {
                recipeTagAdapter.setTags(tags);
                for (int i = 0; i < tags.size(); i++) {
                    tagsIdMap.put(tags.get(i).getTagId(), tags.get(i));
                    tagsNameMap.put(tags.get(i).getName(), tags.get(i));
                }

            }
        });

        unitsViewModel = new ViewModelProvider(this).get(UnitsViewModel.class);
        unitsViewModel.getAllUnits().observe(getViewLifecycleOwner(), new Observer<List<Units>>() {
            @Override
            public void onChanged(List<Units> units) {
                for (int i = 0; i < units.size(); i++) {
                    unitsIdMap.put(units.get(i).getUnitId(), units.get(i));
                    unitsNameMap.put(units.get(i).getName(), units.get(i));
                }
            }
        });

        reciecesViewModel = new ViewModelProvider(this).get(RecipesViewModel.class);
        reciecesViewModel.getRecipesFullData().observe(getViewLifecycleOwner(), new Observer<List<RecipeFullData>>() {
            @Override
            public void onChanged(List<RecipeFullData> list) {
                allRecipes = list;
            }
        });


        numberOfMealsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                numberOfMealsChanged(adapterView, view, position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addTagButton = (Button) view.findViewById(R.id.fr_settings_btn_add_tag_button);
        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initNewTagDialog();
            }
        });

        saveRecipesToFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRecipes();
            }
        });

        loadRecipesFromFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRecipes();
            }
        });
    }

    private void saveRecipes() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.recipes_file_name_json));
        startActivityForResult(intent, REQUEST_CODE_SAVE);
    }

    private void loadRecipes() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/json");
        startActivityForResult(intent, REQUEST_CODE_LOAD);
    }

    private void numberOfMealsChanged(AdapterView<?> adapterView, View view, int position, long id) {
        int oldNumberOfMeals = MainActivity.myDiet.getDietSettings().getNumberOfMealsForDiet();

        if (String.valueOf(adapterView.getItemAtPosition(position)) != null) {
            String selectedNumberOfMealsText = String.valueOf(adapterView.getItemAtPosition(position));
            int selectedNumberOfMeals = Integer.valueOf(selectedNumberOfMealsText);
            MainActivity.myDiet.getDietSettings().setNumberOfMealsForDiet(selectedNumberOfMeals);
        }
    }

    private void initAllTagsRecycleView(View view) {

        allTagsRecyclerView = view.findViewById(R.id.fr_settings_rv_all_tags);
        //TO DO: zamienić na flexbox, żeby zawijał się do nowej linii
        allTagsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        //recipeTagAdapter = new RecipeTagAdapter(MainActivity.myDiet.getAllTags(), this, false);
        recipeTagAdapter = new RecipeTagAdapter();
        recipeTagAdapter.setCanEdit(false);
        recipeTagAdapter.setOnRecipeTagClickListener(new RecipeTagAdapter.OnRecipeTagClickListener() {
            @Override
            public void onRecipeTagClick(int position, Tags tag, View view) {

            }

            @Override
            public void onRecipeTagLongClick(int position, Tags tag, View view) {
                PopupMenu popup = new PopupMenu(getContext(), view);
                popup.getMenuInflater().inflate(R.menu.menu_pop_up_delete, popup.getMenu());
                popup.setGravity(Gravity.END);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.menu_pop_up_d_item_delete){
                            tagsViewModel.delete(tag);
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        allTagsRecyclerView.setAdapter(recipeTagAdapter);





    }

    private void initNewTagDialog() {
        // Inflate widok z XML
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_tag, null);

        // Stwórz dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.dialog_add_new_tag_title_text)
                .setView(dialogView);

        // Inicjalizacja elementów widoku
        EditText newTagEditText = dialogView.findViewById(R.id.dia_new_tag_et_new_tag);

        // Dodanie przycisków do dialogu
        alertDialogBuilder.setNegativeButton(R.string.dialog_negative_button_abort_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.setPositiveButton(R.string.dialog_positive_button_save_text, null);

        // Wyświetlenie dialogu
        AlertDialog materialDialog = alertDialogBuilder.create();
        materialDialog.show();
        materialDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = true;
                String newTagName = newTagEditText.getText().toString();

                if (newTagName.isEmpty()) {
                    newTagEditText.setError(getString(R.string.dialog_add_new_tag_error_name_text));
                    isValid = false;
                }

                if (isValid) {
                    Tags newTag = new Tags(newTagName);
                    tagsViewModel.insert(newTag);

                    tagsViewModel.insertIfNotExists(newTagName, new Consumer<Boolean>() {
                        @Override
                        public void accept(final Boolean success) {
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!success) {
                                        Toast.makeText(getContext(), getString(R.string.dialog_add_new_tag_error_existing_text), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                    materialDialog.dismiss();
                    recipeTagAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                if (requestCode == REQUEST_CODE_SAVE) {
                    List<RecipeToExport> recipesToExport = new ArrayList<>();

                    for(int i=0; i< allRecipes.size(); i++){
                        Recipes recipe = allRecipes.get(i).recipe;
                        RecipeToExport newRecipe = new RecipeToExport();
                        newRecipe.setName(recipe.getName());
                        newRecipe.setCaloriesAmount(recipe.getCaloriesAmount());
                        newRecipe.setProteinAmount(recipe.getProteinAmount());
                        newRecipe.setFatAmount(recipe.getFatAmount());
                        newRecipe.setCarbsAmount(recipe.getCarbsAmount());
                        newRecipe.setServingSize(recipe.getServingSize());

                        for(int j=0; j< allRecipes.get(i).ingredients.size(); j++){
                            RecipeIngredients recipeIngredient = allRecipes.get(i).ingredients.get(j);
                            RecipeIngredientToExport newIngredient = new RecipeIngredientToExport();
                            newIngredient.setName(recipeIngredient.getName());
                            newIngredient.setAmount(recipeIngredient.getAmount());
                            newIngredient.setUnit(unitsIdMap.get(recipeIngredient.getUnitId()).getName());

                            newRecipe.getIngredients().add(newIngredient);
                        }

                        for(int j=0; j< allRecipes.get(i).instructions.size(); j++){
                            RecipeInstructions recipeInstructions = allRecipes.get(i).instructions.get(j);
                            newRecipe.getInstructionSteps().add(recipeInstructions.getInstruction());
                        }

                        for(int j=0; j< allRecipes.get(i).tags.size(); j++){
                            RecipeTags recipeTags = allRecipes.get(i).tags.get(j);
                            newRecipe.getTags().add(tagsIdMap.get(recipeTags.getTagId()).getName());
                        }

                        recipesToExport.add(newRecipe);
                    }

                    SharedPreferencesSaver.saveRecipesToFile(getContext(), uri, recipesToExport);
                } else if (requestCode == REQUEST_CODE_LOAD) {
                    List<RecipeToExport> newRecipes = new ArrayList<RecipeToExport>();

                    newRecipes = SharedPreferencesSaver.loadRecipesFromFile(getContext(), uri);
                    for (int i = 0; i < newRecipes.size(); i++) {
                        boolean isNew = true;
                        for(int j=0; j< allRecipes.size(); j++){
                            if(allRecipes.get(j).recipe.getName().toLowerCase().equals(newRecipes.get(i).getName().toLowerCase())){
                                isNew = false;
                                break;
                            }
                        }

                        if(isNew) {

                            RecipeToExport newRecipe = new RecipeToExport(newRecipes.get(i));
                            List<String> tags = newRecipe.getTags();
                            List<RecipeIngredientToExport> ingredients = newRecipe.getIngredients();
                            List<String> instructions = newRecipe.getInstructionSteps();

                            //Załadowanie Przepisów
                            Recipes recipeToLoad = new Recipes(newRecipe.getName(), newRecipe.getCaloriesAmount(), newRecipe.getProteinAmount(), newRecipe.getFatAmount(), newRecipe.getCarbsAmount(), newRecipe.getServingSize(), false, null);

                            reciecesViewModel.loadRecipeWithIngredientsInstructionsTags(recipeToLoad, ingredients, instructions, tags);

                        }
                    }
                }
            }
        }
    }
}

