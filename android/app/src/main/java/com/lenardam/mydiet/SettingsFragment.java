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

import com.lenardam.mydiet.adapters.RecipeTagAdapter;
import com.lenardam.mydiet.model.DietPlan;
import com.lenardam.mydiet.model.Meal;
import com.lenardam.mydiet.model.Recipe;
import com.lenardam.mydiet.utils.SharedPreferencesSaver;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements RecipeTagAdapter.OnRecipeTagClickListener {

    private Button addTagButton;
    private Spinner numberOfMealsSpinner;
    private String[] number_of_meals_options = {"1","2","3","4","5","6"};
    private RecyclerView allTagsRecyclerView;
    private RecipeTagAdapter recipeTagAdapter;

    private static final int REQUEST_CODE_SAVE = 1;
    private static final int REQUEST_CODE_LOAD = 2;
    private Button saveRecipesToFileButton;
    private Button loadRecipesFromFileButton;

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

            //jeżeli nowa liczba posiłków jest większa niż poprzednia to dodajemy nowe posiłki do diety
            if (selectedNumberOfMeals > oldNumberOfMeals) {
                for (int i = 0; i < MainActivity.myDiet.getDietPlan().size(); i++) {
                    DietPlan currentDietPlan = MainActivity.myDiet.getDietPlan().get(i);
                    if (currentDietPlan.getDietPlanDate().isAfter(LocalDate.now()) && currentDietPlan.getNumberOfMeals() < selectedNumberOfMeals) {
                        int numberOfNewMeals = selectedNumberOfMeals - currentDietPlan.getMeals().size();
                        for (int j = 0; j < numberOfNewMeals; j++) {
                            currentDietPlan.getMeals().add(new Meal());
                        }
                        currentDietPlan.setNumberOfMeals(selectedNumberOfMeals);
                    }

                }
            }
            //jeżeli jest mniejsza, to usuwamy posiłki z diety o ile nie są już zaplanowane posiłki
            else if (selectedNumberOfMeals < oldNumberOfMeals) {
                for (int i = 0; i < MainActivity.myDiet.getDietPlan().size(); i++) {
                    DietPlan currentDietPlan = MainActivity.myDiet.getDietPlan().get(i);
                    if (currentDietPlan.getDietPlanDate().isAfter(LocalDate.now()) && currentDietPlan.getNumberOfMeals() > selectedNumberOfMeals) {
                        int numberOfRemovedMeals = currentDietPlan.getMeals().size() - selectedNumberOfMeals;
                        int removedMeals = 0;
                        for (int j = currentDietPlan.getMeals().size() - 1; j >= 0; j--) {
                            Meal current_meal = currentDietPlan.getMeals().get(j);
                            if (current_meal.getRecipe() == null) {
                                currentDietPlan.getMeals().remove(j);
                                removedMeals++;
                            }
                            // Zatrzymaj pętlę, jeśli osiągnięto limit usuniętych posiłków
                            if (removedMeals >= numberOfRemovedMeals) {
                                break;
                            }
                        }
                        currentDietPlan.setNumberOfMeals(selectedNumberOfMeals);
                    }
                }
            }
        }
    }

    private void initAllTagsRecycleView(View view) {
        allTagsRecyclerView = view.findViewById(R.id.fr_settings_rv_all_tags);
        recipeTagAdapter = new RecipeTagAdapter(MainActivity.myDiet.getAllTags(), this, false);
        //TO DO: zamienić na flexbox, żeby zawijał się do nowej linii
        allTagsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
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

                if (MainActivity.myDiet.getAllTags().contains(newTagName)) {
                    newTagEditText.setError(getString(R.string.dialog_add_new_tag_error_existing_text));
                    isValid = false;
                }

                if (isValid) {
                    MainActivity.myDiet.getAllTags().add(newTagName);
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
    public void onRecipeTagClick(int position, View view) {

    }

    @Override
    public void onRecipeTagLongClick(int position, View view) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.getMenuInflater().inflate(R.menu.menu_pop_up_delete, popup.getMenu());
        popup.setGravity(Gravity.END);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.menu_pop_up_ed_item_delete){
                    MainActivity.myDiet.getAllTags().remove(position);
                    recipeTagAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });
        popup.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                if (requestCode == REQUEST_CODE_SAVE) {
                    SharedPreferencesSaver.saveRecipesToFile(getContext(), uri, MainActivity.myDiet.getAllRecipes());
                } else if (requestCode == REQUEST_CODE_LOAD) {
                    ArrayList<Recipe> newRecipes = new ArrayList<Recipe>();
                    newRecipes = SharedPreferencesSaver.loadRecipesFromFile(getContext(), uri);
                    for (int i = 0; i < newRecipes.size(); i++) {
                        MainActivity.myDiet.loadRecipe(newRecipes.get(i));
                    }
                }
            }
            recipeTagAdapter.notifyDataSetChanged();
        }
    }
}

