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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lenardam.mydiet.adapters.IngredientAdapter;
import com.lenardam.mydiet.adapters.InstructionStepAdapter;
import com.lenardam.mydiet.model.Meal;
import com.lenardam.mydiet.model.Recipe;
import com.lenardam.mydiet.model.RecipeIngredient;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewRecipeFragment extends Fragment implements IngredientAdapter.OnRecipeIngredientClickListener, InstructionStepAdapter.OnInstructionStepClickListener {

    public static final String NEW_RECIPE_TAG = "NEW_RECIPE_TAG";
    public static final String RECIPE_PRESENTATION_TAG = "RECIPE_PRESENTATION_TAG";
    private Recipe selected_recipe;
    private boolean is_editable;

    private EditText recipe_name_edit_text;
    private EditText protein_edit_text;
    private EditText fat_edit_text;
    private EditText carbs_edit_text;
    private EditText calories_edit_text;
    private EditText serving_size_edit_text;
    private Button add_ingredient_button;
    private Button add_instruction_step_button;
    private Button add_recipe_button;

    private String[] units = {"kilogram", "gram", "litr", "mililitr", "sztuk", "szczypta"};
    private ArrayList<RecipeIngredient> ingredients;
    private ArrayList<String> instruction_steps;
    private ArrayList<String> tags;
    private RecyclerView ingredients_recycle_view;
    private RecyclerView instruction_steps_recycle_view;
    private IngredientAdapter ingredients_adapter;
    private InstructionStepAdapter instruction_steps_adapter;
    private Button edit_recipe_button;

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
            selected_recipe = (Recipe) getArguments().getSerializable(RECIPE_PRESENTATION_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.new_recipe_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecipeData();
        initViews(view);
    }

    private void initRecipeData() {
        if (selected_recipe != null) {
            ingredients = selected_recipe.getIngredients();
            instruction_steps = selected_recipe.getInstruction_steps();
            tags = selected_recipe.getTags();
        }
        else
        {
            ingredients = new ArrayList<RecipeIngredient>();
            instruction_steps = new ArrayList<String>();
            tags = new ArrayList<String>();
        }
    }

    private void initViews(View view) {

        recipe_name_edit_text = (EditText) view.findViewById(R.id.meal_edit_text);
        protein_edit_text = (EditText) view.findViewById(R.id.meal_protein_edit_text);
        fat_edit_text = (EditText) view.findViewById(R.id.meal_fat_edit_text);
        carbs_edit_text = (EditText) view.findViewById(R.id.meal_carbs_edit_text);
        calories_edit_text = (EditText) view.findViewById(R.id.meal_calories_edit_text);
        serving_size_edit_text = (EditText) view.findViewById(R.id.meal_serving_size_edit_text);
        add_ingredient_button = (Button) view.findViewById(R.id.add_ingredient_button);
        add_instruction_step_button = (Button) view.findViewById(R.id.add_instruction_step_button);
        add_recipe_button = (Button) view.findViewById(R.id.add_recipe_button);
        edit_recipe_button = (Button) view.findViewById(R.id.edit_recipe_button);
        ingredients_recycle_view = view.findViewById(R.id.meal_ingredients_recycle_view);
        instruction_steps_recycle_view = view.findViewById(R.id.meal_instruction_steps_recycle_view);
        ingredients_adapter = new IngredientAdapter(ingredients, this);
        instruction_steps_adapter = new InstructionStepAdapter(instruction_steps, this);

        add_ingredient_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initNewIngredientDialog(-1);
            }
        });

        add_instruction_step_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initNewInstructionStepDialog(-1);
            }
        });

        add_recipe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRecipe();
            }
        });

        edit_recipe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEditability(true);
            }
        });

        setVisibility();

        ingredients_recycle_view.setLayoutManager(new LinearLayoutManager(getContext()));
        ingredients_recycle_view.setAdapter(ingredients_adapter);
        instruction_steps_recycle_view.setLayoutManager(new LinearLayoutManager(getContext()));
        instruction_steps_recycle_view.setAdapter(instruction_steps_adapter);

    }

    private void setEditability(boolean isEditable) {

        recipe_name_edit_text.setFocusable(isEditable);
        recipe_name_edit_text.setFocusableInTouchMode(isEditable);
        recipe_name_edit_text.setClickable(isEditable);
        recipe_name_edit_text.setCursorVisible(isEditable);

        calories_edit_text.setFocusable(isEditable);
        calories_edit_text.setFocusableInTouchMode(isEditable);
        calories_edit_text.setClickable(isEditable);
        calories_edit_text.setCursorVisible(isEditable);

        protein_edit_text.setFocusable(isEditable);
        protein_edit_text.setFocusableInTouchMode(isEditable);
        protein_edit_text.setClickable(isEditable);
        protein_edit_text.setCursorVisible(isEditable);

        fat_edit_text.setFocusable(isEditable);
        fat_edit_text.setFocusableInTouchMode(isEditable);
        fat_edit_text.setClickable(isEditable);
        fat_edit_text.setCursorVisible(isEditable);

        carbs_edit_text.setFocusable(isEditable);
        carbs_edit_text.setFocusableInTouchMode(isEditable);
        carbs_edit_text.setClickable(isEditable);
        carbs_edit_text.setCursorVisible(isEditable);

        serving_size_edit_text.setFocusable(isEditable);
        serving_size_edit_text.setFocusableInTouchMode(isEditable);
        serving_size_edit_text.setClickable(isEditable);
        serving_size_edit_text.setCursorVisible(isEditable);

        if (isEditable) {
            edit_recipe_button.setVisibility(View.INVISIBLE);
        }
        else {
            edit_recipe_button.setVisibility(View.VISIBLE);
        }

        if (isEditable) {
            add_recipe_button.setVisibility(View.VISIBLE);
        }
        else {
            add_recipe_button.setVisibility(View.INVISIBLE);
        }
        if (isEditable) {
            add_ingredient_button.setVisibility(View.VISIBLE);
            add_instruction_step_button.setVisibility(View.VISIBLE);
        }
        else {
            add_ingredient_button.setVisibility(View.INVISIBLE);
            add_instruction_step_button.setVisibility(View.INVISIBLE);
        }


    }

    private void setVisibility() {
        if (selected_recipe != null) {
            recipe_name_edit_text.setText(selected_recipe.getName());
            calories_edit_text.setText(String.valueOf(selected_recipe.getCalories_amount()));
            protein_edit_text.setText(String.valueOf(selected_recipe.getProtein_amount()));
            fat_edit_text.setText(String.valueOf(selected_recipe.getFat_amount()));
            carbs_edit_text.setText(String.valueOf(selected_recipe.getCarbs_amount()));
            serving_size_edit_text.setText(String.valueOf(selected_recipe.getServing_size()));

            setEditability(false);
        }
        else {
            edit_recipe_button.setVisibility(View.INVISIBLE);
            add_recipe_button.setVisibility(View.VISIBLE);
            add_ingredient_button.setVisibility(View.VISIBLE);
            add_instruction_step_button.setVisibility(View.VISIBLE);

            setEditability(true);
        }
    }

    private void saveRecipe() {
        if (isRecipeNameValid() == true) {
            String recipe_name = recipe_name_edit_text.getText().toString();

            Integer calories_amount;
            if (calories_edit_text.getText().toString().isEmpty()) {
                calories_amount = 0;
            } else {
                calories_amount = Integer.parseInt(calories_edit_text.getText().toString());
            }

            Integer protein_amount;
            if (protein_edit_text.getText().toString().isEmpty()) {
                protein_amount = 0;
            } else {
                protein_amount = Integer.parseInt(protein_edit_text.getText().toString());
            }

            Integer fat_amount;
            if (fat_edit_text.getText().toString().isEmpty()) {
                fat_amount = 0;
            } else {
                fat_amount = Integer.parseInt(fat_edit_text.getText().toString());
            }

            Integer carbs_amount;
            if (carbs_edit_text.getText().toString().isEmpty()) {
                carbs_amount = 0;
            } else {
                carbs_amount = Integer.parseInt(carbs_edit_text.getText().toString());
            }

            Integer serving_size;
            if (serving_size_edit_text.getText().toString().isEmpty()) {
                serving_size = 0;
            } else {
                serving_size = Integer.parseInt(serving_size_edit_text.getText().toString());
            }

            Recipe new_recipe = new Recipe(recipe_name, calories_amount, protein_amount, fat_amount, carbs_amount, serving_size, ingredients, instruction_steps, tags);

            Bundle result = new Bundle();
            result.putSerializable(NEW_RECIPE_TAG, new_recipe);
            getParentFragmentManager().setFragmentResult(RecipesListFragment.ADDED_RECIPE_KEY_TAG, result);
            requireActivity().getSupportFragmentManager().popBackStack();
        }

    }

    private boolean isRecipeNameValid(){
        if (recipe_name_edit_text.getText().toString().isEmpty()){
            recipe_name_edit_text.setError("Podaj nazwę przepisu");
            return false;
        }
        return true;
    }

    private void initNewIngredientDialog(int position) {

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.new_ingredient_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext())
                .setTitle("Dodaj nowy składnik")
                .setCancelable(false)
                .setView(dialogView);

        // Inicjalizacja elementów widoku
        EditText ingredient_name_edit_text = dialogView.findViewById(R.id.ingredient_name_edit_text);
        EditText ingredient_amount_edit_text = dialogView.findViewById(R.id.ingredient_amount_edit_text);
        Spinner ingredient_unit_spinner = dialogView.findViewById(R.id.ingredientUnitSpinner);

        // Utwórzenie adaptera przechowującego jednostki miary
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredient_unit_spinner.setAdapter(adapter);

        if(position != -1){
            ingredient_name_edit_text.setText(ingredients.get(position).getName());
            ingredient_amount_edit_text.setText(String.valueOf(ingredients.get(position).getAmount()));
            ingredient_unit_spinner.setSelection(adapter.getPosition(ingredients.get(position).getUnit()));
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
                boolean is_valid = true;
                String new_recipe_name =  ingredient_name_edit_text.getText().toString();
                String new_recipe_amount = ingredient_amount_edit_text.getText().toString();
                String new_recipe_unit = ingredient_unit_spinner.getSelectedItem().toString();

                if(ingredient_name_edit_text.getText().toString().isEmpty()){
                    ingredient_name_edit_text.setError("Podaj nazwę składnika!");
                    is_valid = false;
                }
                if(ingredient_amount_edit_text.getText().toString().isEmpty()){
                    ingredient_amount_edit_text.setError("Podaj ilość składnika!");
                    is_valid = false;
                }

                if (is_valid) {

                    if (position != -1) {
                        ingredients.get(position).setName(new_recipe_name);
                        ingredients.get(position).setAmount(Double.parseDouble(new_recipe_amount));
                        ingredients.get(position).setUnit(new_recipe_unit);
                        ingredients_adapter.notifyItemChanged(position);
                    } else {
                        RecipeIngredient new_ingredient = new RecipeIngredient(new_recipe_name, Double.parseDouble(new_recipe_amount), new_recipe_unit);
                        ingredients.add(new_ingredient);
                        ingredients_adapter.notifyDataSetChanged();
                    }

                    materialDialog.dismiss();
                }
            }
        });

    }

    private void initNewInstructionStepDialog(int position) {
        // Inflate widok z XML
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.new_instruction_step_dialog, null);

        // Stwórz dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext())
                .setTitle("Dodaj opis krok przepisu")
                .setView(dialogView);

        // Inicjalizacja elementów widoku
        EditText instruction_step_edit_text = dialogView.findViewById(R.id.instruction_step_edit_text);

        if (position != -1) {
            instruction_step_edit_text.setText(instruction_steps.get(position));
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
                boolean is_valid = true;
                String new_instruction_step = instruction_step_edit_text.getText().toString();

                if (new_instruction_step.isEmpty()) {
                    instruction_step_edit_text.setError("Opis nie może być pusty!");
                    is_valid = false;
                }

                if (is_valid) {
                    if (position != -1) {
                        instruction_steps.set(position, new_instruction_step);
                        instruction_steps_adapter.notifyItemChanged(position);
                    } else {
                        instruction_steps.add(new_instruction_step);
                        instruction_steps_adapter.notifyDataSetChanged();
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
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.getMenuInflater().inflate(R.menu.pop_up_edit_delete, popup.getMenu());
        popup.setGravity(Gravity.END);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.pop_up_edit){
                    editRecipeIngredient(position);
                }
                if(item.getItemId() == R.id.pop_up_delete){
                    deleteRecipeIngredient(position);
                }
                return true;
            }
        });
        popup.show();//showing popup menu
    }
    private void editRecipeIngredient(int position) {
        initNewIngredientDialog(position);
    }

    private void deleteRecipeIngredient(int position) {
        ingredients.remove(position);
        ingredients_adapter.notifyItemRemoved(position);
    }


    @Override
    public void onInstructionStepClick(int position) {

    }

    @Override
    public void onInstructionStepLongClick(int position, View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.getMenuInflater().inflate(R.menu.pop_up_edit_delete, popup.getMenu());
        popup.setGravity(Gravity.END);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.pop_up_edit){
                    editInstructionStep(position);
                }
                if(item.getItemId() == R.id.pop_up_delete){
                    deleteInstructionStep(position);
                }
                return true;
            }
        });
        popup.show();//showing popup menu
    }

    private void editInstructionStep(int position) {
        initNewInstructionStepDialog(position);
    }

    private void deleteInstructionStep(int position) {
        instruction_steps.remove(position);
        instruction_steps_adapter.notifyItemRemoved(position);
    }
}