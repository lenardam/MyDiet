package com.lenardam.mydiet;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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

import com.google.android.material.button.MaterialButton;
import com.lenardam.mydiet.adapters.IngredientAdapter;
import com.lenardam.mydiet.adapters.InstructionStepAdapter;
import com.lenardam.mydiet.adapters.RecipeTagAdapter;
import com.lenardam.mydiet.model.Recipe;
import com.lenardam.mydiet.model.RecipeIngredient;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewRecipeFragment extends Fragment implements IngredientAdapter.OnRecipeIngredientClickListener, InstructionStepAdapter.OnInstructionStepClickListener, RecipeTagAdapter.OnRecipeTagClickListener {

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


    private String[] units = {"gram", "kilogram", "mililitr", "litr",  "sztuk", "szczypta", "łyżeczka"};

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
        recipeTagAdapter = new RecipeTagAdapter(tags, this, false);
        recipeTagRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recipeTagRecycleView.setAdapter(recipeTagAdapter);

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
            recipeNameTextView.setText("Nazwa Przepisu");
        }
        else {
            recipeNameTextView.setText(recipeName);
        }

        caloriesAmountTextView.setText(String.format("%d kcal", caloriesAmount));
        servingSizeTextView.setText(String.valueOf(servingSize));
        proteinCarbsFatAmountTextView.setText(String.format("B: %dg, W: %dg, T: %dg", proteinAmount, carbsAmount, fatAmount));
    }

    private void saveRecipe() {
        if (isRecipeNameValid()) {
            recipeName = recipeNameTextView.getText().toString();

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
                .setTitle("Wybierz kategorie")
                .setCancelable(false)
                .setView(dialogView);

        newRecipeTagRecycleView = dialogView.findViewById(R.id.dia_new_recipe_tag_rv_new_recipe_tag);
        newRecipeTagAdapter = new RecipeTagAdapter(allTags, new RecipeTagAdapter.OnRecipeTagClickListener() {
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
        }, true);
        newRecipeTagRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        newRecipeTagRecycleView.setAdapter(newRecipeTagAdapter);

        for (int i = 0; i < allTags.size(); i++) {
            if (tags.contains(allTags.get(i))) {
                newRecipeTagAdapter.setSelectedItem(i);
            } else {
                newRecipeTagAdapter.setUnselectedItem(i);
            }
        }

        // Dodanie przycisków do dialogu
        alertDialogBuilder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.setPositiveButton("Zapisz",null);

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
        if (recipeNameTextView.getText().toString().isEmpty()){
            recipeNameTextView.setError("Podaj nazwę przepisu");
            return false;
        }
        return true;
    }

    private void initNewIngredientDialog(int position) {

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_ingredient, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext())
                .setTitle("Dodaj nowy składnik")
                .setCancelable(false)
                .setView(dialogView);

        // Inicjalizacja elementów widoku
        EditText ingredientNameEditText = dialogView.findViewById(R.id.dia_new_ingredient_et_ingredient_name);
        EditText ingredientAmountEditText = dialogView.findViewById(R.id.dia_new_ingredient_et_ingredient_amount);
        Spinner ingredientUnitSpinner = dialogView.findViewById(R.id.dia_new_ingredient_spin_ingredient_unit);

        // Utwórzenie adaptera przechowującego jednostki miary
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredientUnitSpinner.setAdapter(adapter);

        if(position != -1){
            ingredientNameEditText.setText(ingredients.get(position).getName());
            ingredientAmountEditText.setText(String.valueOf(ingredients.get(position).getAmount()));
            ingredientUnitSpinner.setSelection(adapter.getPosition(ingredients.get(position).getUnit()));
        }

        // Dodanie przycisków do dialogu
        alertDialogBuilder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.setPositiveButton("Zapisz",null);

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
                    ingredientNameEditText.setError("Podaj nazwę składnika!");
                    isValid = false;
                }
                if(ingredientAmountEditText.getText().toString().isEmpty()){
                    ingredientAmountEditText.setError("Podaj ilość składnika!");
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
                .setTitle("Dodaj opis krok przepisu")
                .setView(dialogView);

        // Inicjalizacja elementów widoku
        EditText instructionStepEditText = dialogView.findViewById(R.id.dia_new_instr_step_et_new_instruction_step);

        if (position != -1) {
            instructionStepEditText.setText(instructionSteps.get(position));
        }

        // Dodanie przycisków do dialogu
        alertDialogBuilder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.setPositiveButton("Zapisz", null);

        // Wyświetlenie dialogu
        AlertDialog materialDialog = alertDialogBuilder.create();
        materialDialog.show();
        materialDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = true;
                String newInstructionStep = instructionStepEditText.getText().toString();

                if (newInstructionStep.isEmpty()) {
                    instructionStepEditText.setError("Opis nie może być pusty!");
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
                .setTitle("Podaj dane przepisu")
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
        alertDialogBuilder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.setPositiveButton("Zapisz", null);

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
                    recipeNameEditText.setError("Nazwa przepisu nie może być pusta!");
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
                    caloriesAmountTextView.setText(String.format("%d kcal", caloriesAmount));
                    proteinCarbsFatAmountTextView.setText(String.format("B: %dg, W: %dg, T: %dg", proteinAmount, carbsAmount, fatAmount));
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