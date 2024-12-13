package com.lenardam.mydiet;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lenardam.mydiet.adapters.IngredientAdapter;
import com.lenardam.mydiet.adapters.InstructionStepAdapter;
import com.lenardam.mydiet.model.Recipe;
import com.lenardam.mydiet.model.RecipeIngredient;

import java.util.ArrayList;

public class NewRecipeActivity extends AppCompatActivity {

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
    private Recipe new_recipe;
    private ArrayList<RecipeIngredient> ingredients;
    private ArrayList<String> instruction_steps;
    private ArrayList<String> tags;
    private RecyclerView ingredients_recycle_view;
    private RecyclerView instruction_steps_recycle_view;
    private IngredientAdapter ingredients_adapter;
    private InstructionStepAdapter instruction_steps_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLists();
        initViews();

    }

    private void initLists() {
        ingredients = new ArrayList<RecipeIngredient>();
        instruction_steps = new ArrayList<String>();
        tags = new ArrayList<String>();
    }

    private void initViews() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.new_recipe_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recipe_name_edit_text = (EditText) findViewById(R.id.recipe_name_edit_text);
        protein_edit_text = (EditText) findViewById(R.id.protein_edit_text);
        fat_edit_text = (EditText) findViewById(R.id.fat_edit_text);
        carbs_edit_text = (EditText) findViewById(R.id.editTextNumberDecimal2);
        calories_edit_text = (EditText) findViewById(R.id.calories_edit_text);
        serving_size_edit_text = (EditText) findViewById(R.id.editTextNumber2);
        add_ingredient_button = (Button) findViewById(R.id.add_ingredient_button);
        add_instruction_step_button = (Button) findViewById(R.id.add_instruction_step_button);
        add_recipe_button = (Button) findViewById(R.id.add_recipe_button);
        ingredients_recycle_view = findViewById(R.id.ingredients_recycle_view);
        instruction_steps_recycle_view = findViewById(R.id.instruction_steps_recycle_view);
        ingredients_adapter = new IngredientAdapter(ingredients);
        instruction_steps_adapter = new InstructionStepAdapter(instruction_steps);

        add_ingredient_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initNewIngredientDialog();
            }
        });

        add_instruction_step_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initNewInstructionStepDialog();
            }
        });

        add_recipe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewRecipe();
            }
        });

        ingredients_recycle_view.setLayoutManager(new LinearLayoutManager(this));
        ingredients_recycle_view.setAdapter(ingredients_adapter);
        instruction_steps_recycle_view.setLayoutManager(new LinearLayoutManager(this));
        instruction_steps_recycle_view.setAdapter(instruction_steps_adapter);

    }

    private void initNewIngredientDialog() {
        // Inflate widok z XML
        View dialogView = LayoutInflater.from(NewRecipeActivity.this).inflate(R.layout.new_ingredient_dialog, null);

        // Stwórz dialog
        AlertDialog dialog = new AlertDialog.Builder(NewRecipeActivity.this, R.style.CustomDialogTheme)
                .setView(dialogView)
                .setCancelable(true)
                .create();


        // Inicjalizacja elementów widoku
        EditText ingredient_name_edit_text = dialogView.findViewById(R.id.ingredient_name_edit_text);
        EditText ingredient_amount_edit_text = dialogView.findViewById(R.id.ingredient_amount_edit_text);
        Spinner ingredient_unit_spinner = dialogView.findViewById(R.id.ingredient_unit_spinner);
        Button saveButton = dialogView.findViewById(R.id.save_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);

        // Utwórz adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(NewRecipeActivity.this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ingredient_unit_spinner.setAdapter(adapter);

        saveButton.setOnClickListener(v -> {
            String new_recipe_name =  ingredient_name_edit_text.getText().toString();
            Double new_recipe_amount = Double.parseDouble(ingredient_amount_edit_text.getText().toString());
            String new_recipe_unit = ingredient_unit_spinner.getSelectedItem().toString();
            RecipeIngredient new_ingredient = new RecipeIngredient(new_recipe_name, new_recipe_amount, new_recipe_unit);
            ingredients.add(new_ingredient);
            ingredients_adapter.notifyDataSetChanged();
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void initNewInstructionStepDialog() {
        // Inflate widok z XML
        View dialogView = LayoutInflater.from(NewRecipeActivity.this).inflate(R.layout.new_instruction_step_dialog, null);

        // Stwórz dialog
        AlertDialog dialog = new AlertDialog.Builder(NewRecipeActivity.this, R.style.CustomDialogTheme)
                .setView(dialogView)
                .setCancelable(true)
                .create();


        // Inicjalizacja elementów widoku
        EditText ingredient_name_edit_text = dialogView.findViewById(R.id.instruction_step_edit_text);
        Button saveButton = dialogView.findViewById(R.id.save_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);

        saveButton.setOnClickListener(v -> {
            String new_instruction_step =  ingredient_name_edit_text.getText().toString();
            instruction_steps.add(new_instruction_step);
            instruction_steps_adapter.notifyDataSetChanged();
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void saveNewRecipe() {

    }
}