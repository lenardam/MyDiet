package com.lenardam.mydiet;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
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
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import com.lenardam.mydiet.adapters.IngredientAdapter;
import com.lenardam.mydiet.adapters.InstructionStepAdapter;
import com.lenardam.mydiet.adapters.RecipeTagAdapter;
import com.lenardam.mydiet.adapters.UnitsAdapter;
import com.lenardam.mydiet.database.model.Tags;
import com.lenardam.mydiet.database.model.Units;
import com.lenardam.mydiet.database.viewModel.TagsViewModel;
import com.lenardam.mydiet.database.viewModel.UnitsViewModel;
import com.lenardam.mydiet.model.Recipe;
import com.lenardam.mydiet.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewRecipeFragment extends Fragment implements IngredientAdapter.OnRecipeIngredientClickListener, InstructionStepAdapter.OnInstructionStepClickListener {

    public static final String NEW_RECIPE_TAG = "NEW_RECIPE_TAG";
    public static final String RECIPE_PRESENTATION_TAG = "RECIPE_PRESENTATION_TAG";
    private Recipe selectedRecipe;
    private boolean isEditable;
    private boolean hideIngredients = false;
    private boolean hideInstructionSteps = false;
    private boolean hideTags = false;

    private TextView recipeNameTextView;
    private TextView caloriesAmountTextView;
    private TextView servingSizeTextView;
    private ImageButton addIngredientButton;
    private ImageButton addInstructionStepButton;
    private ImageButton addTagButton;
    private RecyclerView recipeTagRecycleView;
    private RecipeTagAdapter recipeTagAdapter;
    private RecyclerView newRecipeTagRecycleView;
    private RecipeTagAdapter newRecipeTagAdapter;
    private ImageButton saveEditButton;
    private ImageButton editRecipeParametersButton;
    private ImageButton servingSizePlusButton;
    private ImageButton servingSizeMinusButton;

    private String[] units;

    private ArrayList<RecipeIngredient> ingredients;
    private ArrayList<String> instructionSteps;
    private ArrayList<String> tags;
    private RecyclerView ingredientsRecycleView;
    private RecyclerView instructionStepsRecycleView;
    private IngredientAdapter ingredientsAdapter;
    private InstructionStepAdapter instructionStepsAdapter;
    private TextView proteinCarbsFatAmountTextView;

    private Integer proteinAmount = 0;
    private Integer fatAmount = 0;
    private Integer carbsAmount = 0;
    private Integer caloriesAmount = 0;
    private String recipeName = "";
    private Double servingSizeDelta = 0.25;
    private Double servingSize = 1.0;
    private ImageButton hideIngredientsButton;
    private ImageButton hideInstructionStepsButton;
    private ImageButton hideTagsButton;

    private TagsViewModel tagsViewModel;

    public NewRecipeFragment() {
        // Required empty public constructor
    }

    public static NewRecipeFragment newInstance(Recipe recipe) {
        NewRecipeFragment fragment = new NewRecipeFragment();
        Bundle args = new Bundle();
        args.putSerializable(RECIPE_PRESENTATION_TAG, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ((MainActivity) requireActivity()).setBottomNavigationItem(R.id.menu_bottom_item_recipe_list_fragment);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ((MainActivity) requireActivity()).setBottomNavigationItem(R.id.menu_bottom_item_recipe_list_fragment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedRecipe = (Recipe) getArguments().getSerializable(RECIPE_PRESENTATION_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecipeData();
        initViews(view);
        initIngredientsRecycleView(view);
        initInstructionStepsRecycleView(view);
        initTagRecycleView(view);
        initTagsViewModel();
    }

    private void initRecipeData() {
        if (selectedRecipe != null) {
            ingredients = selectedRecipe.getIngredients();
            instructionSteps = selectedRecipe.getInstructionSteps();
            tags = selectedRecipe.getTags();
        }
        else
        {
            ingredients = new ArrayList<RecipeIngredient>();
            instructionSteps = new ArrayList<String>();
            tags = new ArrayList<String>();
        }
    }

    private void initViews(View view) {

        saveEditButton = (ImageButton) view.findViewById(R.id.fr_new_recipe_btn_edit_save);
        editRecipeParametersButton = (ImageButton) view.findViewById(R.id.fr_new_recipe_btn_edit_recipe_name_and_parameters);

        recipeNameTextView = (TextView) view.findViewById(R.id.fr_new_recipe_et_recipe_name);
        proteinCarbsFatAmountTextView = (TextView) view.findViewById(R.id.fr_new_recipe_tv_protein_carbs_fat_amount);
        caloriesAmountTextView = (TextView) view.findViewById(R.id.fr_new_recipe_et_recipe_calories_amount);
        servingSizeTextView = (TextView) view.findViewById(R.id.fr_new_recipe_et_recipe_serving_size);

        servingSizePlusButton = (ImageButton) view.findViewById(R.id.fr_new_recipe_btn_recipe_serving_plus);
        servingSizeMinusButton = (ImageButton) view.findViewById(R.id.fr_new_recipe_btn_recipe_serving_minus);

        addIngredientButton = (ImageButton) view.findViewById(R.id.fr_new_recipe_btn_add_ingredient);
        addInstructionStepButton = (ImageButton) view.findViewById(R.id.fr_new_recipe_btn_add_instruction_step);
        addTagButton = (ImageButton) view.findViewById(R.id.fr_new_recipe_btn_add_tag);

        hideIngredientsButton = (ImageButton) view.findViewById(R.id.fr_new_recipe_btn_hide_ingredients);
        hideInstructionStepsButton = (ImageButton) view.findViewById(R.id.fr_new_recipe_btn_hide_instruction_step);
        hideTagsButton = (ImageButton) view.findViewById(R.id.fr_new_recipe_btn_hide_tags);

        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initNewIngredientDialog(-1);
            }
        });

        addInstructionStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initNewInstructionStepDialog(-1);
            }
        });

        editRecipeParametersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initChangeRecipeParametersDialog();
            }
        });

        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditable) {
                    saveRecipe();
                }
                else {
                    setEditability(true);
                }
            }
        });

        servingSizePlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servingSize += servingSizeDelta;
                servingSizeTextView.setText(String.valueOf(servingSize));
            }
        });

        servingSizeMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(servingSize > servingSizeDelta) {
                    servingSize -= servingSizeDelta;
                    servingSizeTextView.setText(String.valueOf(servingSize));
                }
            }
        });


        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initNewTagDialog();
            }
        });

        hideIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideIngredients = !hideIngredients;
                setIngredientsVisibility(hideIngredients);
            }
        });

        hideInstructionStepsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                hideInstructionSteps = !hideInstructionSteps;
                setInstructionStepsVisibility(hideInstructionSteps);
            }
        });

        hideTagsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                hideTags = !hideTags;
                setTagsVisibility(hideTags);
            }
        });

        setVisibility();

    }

    private void setTagsVisibility(boolean hideTags) {
        if (hideTags) {
            hideTagsButton.setImageResource(R.drawable.ic_down);
            recipeTagRecycleView.setVisibility(View.GONE);
        }
        else {
            hideTagsButton.setImageResource(R.drawable.ic_up);
            recipeTagRecycleView.setVisibility(View.VISIBLE);
        }
    }

    private void setIngredientsVisibility(boolean hideIngredients) {
        if (hideIngredients) {
            hideIngredientsButton.setImageResource(R.drawable.ic_down);
            ingredientsRecycleView.setVisibility(View.GONE);
        }
        else {
            hideIngredientsButton.setImageResource(R.drawable.ic_up);
            ingredientsRecycleView.setVisibility(View.VISIBLE);
        }
    }
    private void setInstructionStepsVisibility(boolean hideInstructionSteps) {
        if (hideInstructionSteps){
            hideInstructionStepsButton.setImageResource(R.drawable.ic_down);
            instructionStepsRecycleView.setVisibility(View.GONE);
        }
        else {
            hideInstructionStepsButton.setImageResource(R.drawable.ic_up);
            instructionStepsRecycleView.setVisibility(View.VISIBLE);
        }
    }

    private void initIngredientsRecycleView(View view) {
        ingredientsRecycleView = view.findViewById(R.id.fr_new_recipe_rv_recipe_ingredients);
        ingredientsAdapter = new IngredientAdapter(ingredients, this);
        ingredientsRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        ingredientsRecycleView.setAdapter(ingredientsAdapter);
        setIngredientsVisibility(hideIngredients);
    }

    private void initInstructionStepsRecycleView(View view) {
        instructionStepsRecycleView = view.findViewById(R.id.fr_new_recipe_rv_recipe_instruction_steps);
        instructionStepsAdapter = new InstructionStepAdapter(instructionSteps, this);
        instructionStepsRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        instructionStepsRecycleView.setAdapter(instructionStepsAdapter);
        setInstructionStepsVisibility(hideInstructionSteps);

    }

    private void initTagRecycleView(View view) {
        recipeTagRecycleView = view.findViewById(R.id.fr_new_recipe_rv_new_recipe_tag);
        recipeTagRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recipeTagAdapter = new RecipeTagAdapter();
        recipeTagAdapter.setCanEdit(false);
        recipeTagAdapter.setOnRecipeTagClickListener(new RecipeTagAdapter.OnRecipeTagClickListener() {
            @Override
            public void onRecipeTagClick(int position, View view) {
            }

            @Override
            public void onRecipeTagLongClick(int position, View view) {
                if (isEditable) {
                    PopupMenu popup = new PopupMenu(getContext(), view);
                    popup.getMenuInflater().inflate(R.menu.menu_pop_up_delete, popup.getMenu());
                    popup.setGravity(Gravity.END);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.menu_pop_up_ed_item_delete) {
                                deleteRecipeTag(position);
                            }
                            return true;
                        }
                    });
                    popup.show();//showing popup menu
                }
            }
        });
        recipeTagRecycleView.setAdapter(recipeTagAdapter);

    }

    private void initTagsViewModel() {
        tagsViewModel = new ViewModelProvider(this).get(TagsViewModel.class);
        tagsViewModel.getAllTags().observe(getViewLifecycleOwner(), new Observer<List<Tags>>() {
            @Override
            public void onChanged(List<Tags> tags) {
                recipeTagAdapter.setTags(tags);
                //newRecipeTagAdapter.setTags(tags);
            }
        });
    }

    private void setEditability(boolean isEditable) {
        this.isEditable = isEditable;


        if (isEditable) {
            saveEditButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_save));

        }
        else {
            saveEditButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_edit));
        }

        if (isEditable) {
            addIngredientButton.setVisibility(View.VISIBLE);
            addInstructionStepButton.setVisibility(View.VISIBLE);
            addTagButton.setVisibility(View.VISIBLE);
            editRecipeParametersButton.setVisibility(View.VISIBLE);
            servingSizePlusButton.setVisibility(View.VISIBLE);
            servingSizeMinusButton.setVisibility(View.VISIBLE);
            addIngredientButton.setVisibility(View.VISIBLE);
            addInstructionStepButton.setVisibility(View.VISIBLE);
        }
        else {
            addIngredientButton.setVisibility(View.INVISIBLE);
            addInstructionStepButton.setVisibility(View.INVISIBLE);
            addTagButton.setVisibility(View.INVISIBLE);
            editRecipeParametersButton.setVisibility(View.INVISIBLE);
            servingSizePlusButton.setVisibility(View.INVISIBLE);
            servingSizeMinusButton.setVisibility(View.INVISIBLE);
            addIngredientButton.setVisibility(View.INVISIBLE);
            addInstructionStepButton.setVisibility(View.INVISIBLE);
        }



    }

    private void setVisibility() {
        if (selectedRecipe != null) {
            recipeName = selectedRecipe.getName();
            proteinAmount = selectedRecipe.getProteinAmount();
            fatAmount = selectedRecipe.getFatAmount();
            carbsAmount = selectedRecipe.getCarbsAmount();
            caloriesAmount = selectedRecipe.getCaloriesAmount();
            servingSize = selectedRecipe.getServingSize();
            setEditability(false);
        }
        else {

            setEditability(true);
        }
        if(recipeName.isEmpty()){
            recipeNameTextView.setText(R.string.recipe_name_text);
        }
        else {
            recipeNameTextView.setText(recipeName);
        }

        caloriesAmountTextView.setText(String.format(getString(R.string.calories_formated_text), caloriesAmount));
        servingSizeTextView.setText(String.valueOf(servingSize));
        proteinCarbsFatAmountTextView.setText(getString(R.string.protein_carbs_fat_amount_formated_text, proteinAmount, carbsAmount, fatAmount));
    }

    private void saveRecipe() {
        if (isRecipeNameValid()) {

            if (selectedRecipe != null){
                //aktualizacja wybranego przepisu
                //list nie aktualizujemy tu, zostały przekazane jako referencje więc są już zaktualizowane
                selectedRecipe.setName(recipeName);
                selectedRecipe.setCaloriesAmount(caloriesAmount);
                selectedRecipe.setProteinAmount(proteinAmount);
                selectedRecipe.setFatAmount(fatAmount);
                selectedRecipe.setCarbsAmount(carbsAmount);
                selectedRecipe.setServingSize(servingSize);
            }
            else{
                //dodanie nowego przepisu
                Recipe newRecipe = new Recipe(recipeName, caloriesAmount, proteinAmount, fatAmount, carbsAmount, servingSize, ingredients, instructionSteps, tags);
                MainActivity.myDiet.getAllRecipes().add(newRecipe);
            }
            requireActivity().getSupportFragmentManager().popBackStack();
        }

    }

    private void initNewTagDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_recipe_tag, null);
        ArrayList<String> allTags = MainActivity.myDiet.getAllTags();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.dialog_choose_tag_title_text)
                .setCancelable(false)
                .setView(dialogView);

        newRecipeTagRecycleView = dialogView.findViewById(R.id.dia_new_recipe_tag_rv_new_recipe_tag);
        newRecipeTagRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        newRecipeTagAdapter = new RecipeTagAdapter();
        newRecipeTagAdapter.setCanEdit(true);
        newRecipeTagAdapter.setOnRecipeTagClickListener(new RecipeTagAdapter.OnRecipeTagClickListener() {
            @Override
            public void onRecipeTagClick(int position, View view) {
                if (!tags.contains(allTags.get(position))){
                    newRecipeTagAdapter.setSelectedItem(position);
                    // Usuwa zaznaczenie
                    tags.add(allTags.get(position));
                }
                else {
                    newRecipeTagAdapter.setUnselectedItem(position);
                    tags.remove(allTags.get(position));
                }

            }

            @Override
            public void onRecipeTagLongClick(int position, View view) {
            }
        });

        newRecipeTagRecycleView.setAdapter(newRecipeTagAdapter);

        for (int i = 0; i < allTags.size(); i++) {
            if (tags.contains(allTags.get(i))) {
                newRecipeTagAdapter.setSelectedItem(i);
            } else {
                newRecipeTagAdapter.setUnselectedItem(i);
            }
        }

        // Dodanie przycisków do dialogu
        alertDialogBuilder.setNegativeButton(R.string.dialog_negative_button_abort_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.setPositiveButton(R.string.dialog_positive_button_save_text,null);

        // Wyświetlenie dialogu
        AlertDialog materialDialog = alertDialogBuilder.create();
        materialDialog.show();
        materialDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeTagAdapter.notifyDataSetChanged();
                materialDialog.dismiss();
            }
        });
    }



    private boolean isRecipeNameValid(){
        if (recipeName.isEmpty() || recipeName.equals(getString(R.string.empty_recipe_name))){
            recipeNameTextView.setError(getString(R.string.recipe_name_error_text));
            return false;
        }
        return true;
    }

    private void initNewIngredientDialog(int position) {

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_ingredient, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.dialog_new_ingredient_title_text)
                .setCancelable(false)
                .setView(dialogView);

        // Inicjalizacja elementów widoku
        EditText ingredientNameEditText = dialogView.findViewById(R.id.dia_new_ingredient_et_ingredient_name);
        EditText ingredientAmountEditText = dialogView.findViewById(R.id.dia_new_ingredient_et_ingredient_amount);
        Spinner ingredientUnitSpinner = dialogView.findViewById(R.id.dia_new_ingredient_spin_ingredient_unit);

        // Utwórzenie adaptera przechowującego jednostki miary
        UnitsAdapter unitsAdapter = new UnitsAdapter(requireContext(), new ArrayList<>());
        ingredientUnitSpinner.setAdapter(unitsAdapter);

        UnitsViewModel unitsViewModel = new ViewModelProvider(this).get(UnitsViewModel.class);
        unitsViewModel.getAllUnits().observe(this, new Observer<List<Units>>() {
            @Override
            public void onChanged(List<Units> units) {
                unitsAdapter.clear();
                unitsAdapter.addAll(units);
                unitsAdapter.notifyDataSetChanged();
            }
        });

        if(position != -1){
            ingredientNameEditText.setText(ingredients.get(position).getName());
            ingredientAmountEditText.setText(String.valueOf(ingredients.get(position).getAmount()));
            //ingredientUnitSpinner.setSelection(unitsAdapter.getPosition(ingredients.get(position).getUnit()));
        }

        // Dodanie przycisków do dialogu
        alertDialogBuilder.setNegativeButton(R.string.dialog_negative_button_abort_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.setPositiveButton(R.string.dialog_positive_button_save_text,null);

        // Wyświetlenie dialogu
        AlertDialog materialDialog = alertDialogBuilder.create();
        materialDialog.show();
        materialDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = true;
                String newRecipeName =  ingredientNameEditText.getText().toString();
                String newRecipeAmount = ingredientAmountEditText.getText().toString();
                String newRecipeUnit = ingredientUnitSpinner.getSelectedItem().toString();

                if(ingredientNameEditText.getText().toString().isEmpty()){
                    ingredientNameEditText.setError(getString(R.string.dialog_add_ingredient_error_name_text));
                    isValid = false;
                }
                if(ingredientAmountEditText.getText().toString().isEmpty()){
                    ingredientAmountEditText.setError(getString(R.string.dialog_add_ingredient_error_amount_text));
                    isValid = false;
                }

                if (isValid) {

                    if (position != -1) {
                        ingredients.get(position).setName(newRecipeName);
                        ingredients.get(position).setAmount(Double.parseDouble(newRecipeAmount));
                        ingredients.get(position).setUnit(newRecipeUnit);
                        ingredientsAdapter.notifyItemChanged(position);
                    } else {
                        RecipeIngredient newIngredient = new RecipeIngredient(newRecipeName, Double.parseDouble(newRecipeAmount), newRecipeUnit);
                        ingredients.add(newIngredient);
                        ingredientsAdapter.notifyDataSetChanged();
                    }

                    materialDialog.dismiss();
                }
            }
        });

    }

    private void initNewInstructionStepDialog(int position) {
        // Inflate widok z XML
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_instruction_step, null);

        // Stwórz dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.dialog_add_instruction_step_title_text)
                .setView(dialogView);

        // Inicjalizacja elementów widoku
        EditText instructionStepEditText = dialogView.findViewById(R.id.dia_new_instr_step_et_new_instruction_step);

        if (position != -1) {
            instructionStepEditText.setText(instructionSteps.get(position));
        }

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
                String newInstructionStep = instructionStepEditText.getText().toString();

                if (newInstructionStep.isEmpty()) {
                    instructionStepEditText.setError(getString(R.string.dialog_add_instruction_step_error_text));
                    isValid = false;
                }

                if (isValid) {
                    if (position != -1) {
                        instructionSteps.set(position, newInstructionStep);
                        instructionStepsAdapter.notifyItemChanged(position);
                    } else {
                        instructionSteps.add(newInstructionStep);
                        instructionStepsAdapter.notifyDataSetChanged();
                    }
                    materialDialog.dismiss();
                }
            }
        });
    }

    private void initChangeRecipeParametersDialog() {
        // Inflate widok z XML
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_recipe_parameters, null);

        // Stwórz dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.dialog_recipe_parameters_title_text)
                .setView(dialogView);

        // Inicjalizacja elementów widoku
        EditText recipeNameEditText = dialogView.findViewById(R.id.dia_new_recipe_param_et_recipe_name);
        EditText proteinAmountEditText = dialogView.findViewById(R.id.dia_new_recipe_param_et_protein_amount);
        EditText carbsAmountEditText = dialogView.findViewById(R.id.dia_new_recipe_param_et_carbs_amount);
        EditText fatAmountEditText = dialogView.findViewById(R.id.dia_new_recipe_param_et_fat_amount);
        EditText caloriesAmountEditText = dialogView.findViewById(R.id.dia_new_recipe_param_et_calories_amount);

        if (selectedRecipe != null) {
            recipeNameEditText.setText(recipeName);
            proteinAmountEditText.setText(String.valueOf(proteinAmount));
            carbsAmountEditText.setText(String.valueOf(carbsAmount));
            fatAmountEditText.setText(String.valueOf(fatAmount));
            caloriesAmountEditText.setText(String.valueOf(caloriesAmount));
        }

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
                String newRecipeName = recipeNameEditText.getText().toString();
                String newProteinAmount = proteinAmountEditText.getText().toString();
                String newCarbsAmount = carbsAmountEditText.getText().toString();
                String newFatAmount = fatAmountEditText.getText().toString();
                String newCaloriesAmount = caloriesAmountEditText.getText().toString();

                int newProteinAmountVal = 0;
                int newCarbsAmountVal = 0;
                int newFatAmountVal = 0;
                int newCaloriesAmountVal = 0;


                if (!newProteinAmount.isEmpty()) {
                    newProteinAmountVal = Integer.parseInt(newProteinAmount);
                }

                if (!newCarbsAmount.isEmpty()) {
                    newCarbsAmountVal = Integer.parseInt(newCarbsAmount);
                }

                if (!newFatAmount.isEmpty()) {
                    newFatAmountVal = Integer.parseInt(newFatAmount);
                }

                if (!newCaloriesAmount.isEmpty()) {
                    newCaloriesAmountVal = Integer.parseInt(newCaloriesAmount);
                }


                if (newRecipeName.isEmpty()) {
                    recipeNameEditText.setError(getString(R.string.dialog_recipe_parameters_error_name_text));
                    isValid = false;
                }

                if (isValid) {
                    recipeName = newRecipeName;
                    proteinAmount = newProteinAmountVal;
                    fatAmount = newFatAmountVal;
                    carbsAmount = newCarbsAmountVal;
                    caloriesAmount = newCaloriesAmountVal;
                    materialDialog.dismiss();

                    recipeNameTextView.setText(recipeName);
                    caloriesAmountTextView.setText(String.format(getString(R.string.calories_formated_text), caloriesAmount));
                    proteinCarbsFatAmountTextView.setText(getString(R.string.protein_carbs_fat_amount_formated_text, proteinAmount, carbsAmount, fatAmount));
                }
            }
        });
    }


    @Override
    public void onRecipeIngredientClick(int position) {

    }

    @Override
    public void onRecipeIngredientLongClick(int position, View v) {
        if (isEditable) {
            PopupMenu popup = new PopupMenu(getContext(), v);
            popup.getMenuInflater().inflate(R.menu.menu_pop_up_edit_delete, popup.getMenu());
            popup.setGravity(Gravity.END);

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.menu_pop_up_ed_item_edit) {
                        editRecipeIngredient(position);
                    }
                    if (item.getItemId() == R.id.menu_pop_up_ed_item_delete) {
                        deleteRecipeIngredient(position);
                    }
                    return true;
                }
            });
            popup.show();//showing popup menu
        }
    }
    private void editRecipeIngredient(int position) {
        initNewIngredientDialog(position);
    }

    private void deleteRecipeIngredient(int position) {
        ingredients.remove(position);
        ingredientsAdapter.notifyItemRemoved(position);
    }


    @Override
    public void onInstructionStepClick(int position) {

    }

    @Override
    public void onInstructionStepLongClick(int position, View v) {
        if (isEditable) {
            PopupMenu popup = new PopupMenu(getContext(), v);
            popup.getMenuInflater().inflate(R.menu.menu_pop_up_edit_delete, popup.getMenu());
            popup.setGravity(Gravity.END);

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.menu_pop_up_ed_item_edit) {
                        editInstructionStep(position);
                    }
                    if (item.getItemId() == R.id.menu_pop_up_ed_item_delete) {
                        deleteInstructionStep(position);
                    }
                    return true;
                }
            });
            popup.show();
        }
    }

    private void deleteRecipeTag(int position) {
        tags.remove(position);
        recipeTagAdapter.notifyItemRemoved(position);
    }

    private void editInstructionStep(int position) {
        initNewInstructionStepDialog(position);
    }

    private void deleteInstructionStep(int position) {
        instructionSteps.remove(position);
        instructionStepsAdapter.notifyItemRemoved(position);
    }


}