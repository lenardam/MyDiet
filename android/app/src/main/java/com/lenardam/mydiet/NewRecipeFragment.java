package com.lenardam.mydiet;

import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;

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

    private EditText recipeNameEditText;
    private EditText proteinAmountEditText;
    private EditText fatamountEditText;
    private EditText carbsAmountEditText;
    private EditText caloriesAmountEditText;
    private EditText servingSizeEditText;
    private Button addIngredientButton;
    private Button addInstructionStepButton;
    private Button saveRecipeButton;

    private String[] units = {"gram", "kilogram", "mililitr", "litr",  "sztuk", "szczypta", "łyżeczka"};
    private ArrayList<RecipeIngredient> ingredients;
    private ArrayList<String> instructionSteps;
    private ArrayList<String> tags;
    private RecyclerView ingredientsRecycleView;
    private RecyclerView instructionStepsRecycleView;
    private IngredientAdapter ingredientsAdapter;
    private InstructionStepAdapter instructionStepsAdapter;
    private Button editRecipeButton;
    private RecyclerView recipeTagRecycleView;
    private RecipeTagAdapter recipeTagAdapter;
    private Button addTagButton;
    private RecyclerView newRecipeTagRecycleView;
    private RecipeTagAdapter newRecipeTagAdapter;

    public NewRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewRecipeFragment newInstance(Recipe recipe) {
        NewRecipeFragment fragment = new NewRecipeFragment();
        Bundle args = new Bundle();
        args.putSerializable(RECIPE_PRESENTATION_TAG, recipe);
        fragment.setArguments(args);
        return fragment;
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

        recipeNameEditText = (EditText) view.findViewById(R.id.fr_new_recipe_et_recipe_name);
        proteinAmountEditText = (EditText) view.findViewById(R.id.fr_new_recipe_et_recipe_protein_amount);
        fatamountEditText = (EditText) view.findViewById(R.id.fr_new_recipe_et_recipe_fat_amount);
        carbsAmountEditText = (EditText) view.findViewById(R.id.fr_new_recipe_et_recipe_carbs_amount);
        caloriesAmountEditText = (EditText) view.findViewById(R.id.fr_new_recipe_et_recipe_calories_amount);
        servingSizeEditText = (EditText) view.findViewById(R.id.fr_new_recipe_et_recipe_serving_size);
        addIngredientButton = (Button) view.findViewById(R.id.fr_new_recipe_btn_add_ingredient);
        addInstructionStepButton = (Button) view.findViewById(R.id.fr_new_recipe_btn_add_instruction_step);
        saveRecipeButton = (Button) view.findViewById(R.id.fr_new_recipe_btn_save_recipe);
        editRecipeButton = (Button) view.findViewById(R.id.fr_new_recipe_btn_edit_recipe);
        addTagButton = (Button) view.findViewById(R.id.fr_new_recipe_btn_add_tag);

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

        saveRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRecipe();
            }
        });

        editRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEditability(true);
            }
        });

        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initNewTagDialog();
            }
        });

        setVisibility();

    }

    private void initIngredientsRecycleView(View view) {
        ingredientsRecycleView = view.findViewById(R.id.fr_new_recipe_rv_recipe_ingredients);
        ingredientsAdapter = new IngredientAdapter(ingredients, this);
        ingredientsRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        ingredientsRecycleView.setAdapter(ingredientsAdapter);
    }

    private void initInstructionStepsRecycleView(View view) {
        instructionStepsRecycleView = view.findViewById(R.id.fr_new_recipe_rv_recipe_instruction_steps);
        instructionStepsAdapter = new InstructionStepAdapter(instructionSteps, this);
        instructionStepsRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        instructionStepsRecycleView.setAdapter(instructionStepsAdapter);

    }

    private void initTagRecycleView(View view) {
        recipeTagRecycleView = view.findViewById(R.id.fr_new_recipe_rv_new_recipe_tag);
        recipeTagAdapter = new RecipeTagAdapter(tags, this, false);
        recipeTagRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recipeTagRecycleView.setAdapter(recipeTagAdapter);

    }

    private void setEditability(boolean isEditable) {
        this.isEditable = isEditable;
        recipeNameEditText.setFocusable(isEditable);
        recipeNameEditText.setFocusableInTouchMode(isEditable);
        recipeNameEditText.setClickable(isEditable);
        recipeNameEditText.setCursorVisible(isEditable);

        caloriesAmountEditText.setFocusable(isEditable);
        caloriesAmountEditText.setFocusableInTouchMode(isEditable);
        caloriesAmountEditText.setClickable(isEditable);
        caloriesAmountEditText.setCursorVisible(isEditable);

        proteinAmountEditText.setFocusable(isEditable);
        proteinAmountEditText.setFocusableInTouchMode(isEditable);
        proteinAmountEditText.setClickable(isEditable);
        proteinAmountEditText.setCursorVisible(isEditable);

        fatamountEditText.setFocusable(isEditable);
        fatamountEditText.setFocusableInTouchMode(isEditable);
        fatamountEditText.setClickable(isEditable);
        fatamountEditText.setCursorVisible(isEditable);

        carbsAmountEditText.setFocusable(isEditable);
        carbsAmountEditText.setFocusableInTouchMode(isEditable);
        carbsAmountEditText.setClickable(isEditable);
        carbsAmountEditText.setCursorVisible(isEditable);

        servingSizeEditText.setFocusable(isEditable);
        servingSizeEditText.setFocusableInTouchMode(isEditable);
        servingSizeEditText.setClickable(isEditable);
        servingSizeEditText.setCursorVisible(isEditable);

        if (isEditable) {
            editRecipeButton.setVisibility(View.INVISIBLE);
        }
        else {
            editRecipeButton.setVisibility(View.VISIBLE);
        }

        if (isEditable) {
            saveRecipeButton.setVisibility(View.VISIBLE);
        }
        else {
            saveRecipeButton.setVisibility(View.INVISIBLE);
        }
        if (isEditable) {
            addIngredientButton.setVisibility(View.VISIBLE);
            addInstructionStepButton.setVisibility(View.VISIBLE);
            addTagButton.setVisibility(View.VISIBLE);
        }
        else {
            addIngredientButton.setVisibility(View.INVISIBLE);
            addInstructionStepButton.setVisibility(View.INVISIBLE);
            addTagButton.setVisibility(View.INVISIBLE);
        }



    }

    private void setVisibility() {
        if (selectedRecipe != null) {
            recipeNameEditText.setText(selectedRecipe.getName());
            caloriesAmountEditText.setText(String.valueOf(selectedRecipe.getCaloriesAmount()));
            proteinAmountEditText.setText(String.valueOf(selectedRecipe.getProteinAmount()));
            fatamountEditText.setText(String.valueOf(selectedRecipe.getFatAmount()));
            carbsAmountEditText.setText(String.valueOf(selectedRecipe.getCarbsAmount()));
            servingSizeEditText.setText(String.valueOf(selectedRecipe.getServingSize()));

            setEditability(false);
        }
        else {
            editRecipeButton.setVisibility(View.INVISIBLE);
            saveRecipeButton.setVisibility(View.VISIBLE);
            addIngredientButton.setVisibility(View.VISIBLE);
            addInstructionStepButton.setVisibility(View.VISIBLE);

            setEditability(true);
        }
    }

    private void saveRecipe() {
        if (isRecipeNameValid()) {
            String recipeName = recipeNameEditText.getText().toString();

            Integer caloriesAmount;
            if (caloriesAmountEditText.getText().toString().isEmpty()) {
                caloriesAmount = 0;
            } else {
                caloriesAmount = Integer.parseInt(caloriesAmountEditText.getText().toString());
            }

            Integer proteinAmount;
            if (proteinAmountEditText.getText().toString().isEmpty()) {
                proteinAmount = 0;
            } else {
                proteinAmount = Integer.parseInt(proteinAmountEditText.getText().toString());
            }

            Integer fatAmount;
            if (fatamountEditText.getText().toString().isEmpty()) {
                fatAmount = 0;
            } else {
                fatAmount = Integer.parseInt(fatamountEditText.getText().toString());
            }

            Integer carbsAmount;
            if (carbsAmountEditText.getText().toString().isEmpty()) {
                carbsAmount = 0;
            } else {
                carbsAmount = Integer.parseInt(carbsAmountEditText.getText().toString());
            }

            Double servingSize;
            if (servingSizeEditText.getText().toString().isEmpty()) {
                servingSize = 1.0;
            } else {
                servingSize = Double.parseDouble(servingSizeEditText.getText().toString());
            }

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
        if (recipeNameEditText.getText().toString().isEmpty()){
            recipeNameEditText.setError("Podaj nazwę przepisu");
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