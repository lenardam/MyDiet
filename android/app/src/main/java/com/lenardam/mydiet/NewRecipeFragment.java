package com.lenardam.mydiet;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lenardam.mydiet.adapters.IngredientAdapter;
import com.lenardam.mydiet.adapters.InstructionStepAdapter;
import com.lenardam.mydiet.model.Recipe;
import com.lenardam.mydiet.model.RecipeIngredient;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewRecipeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public static final String NEW_RECIPE_TAG = "NEW_RECIPE_TAG";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText recipe_name_edit_text;
    private EditText protein_edit_text;
    private EditText fat_edit_text;
    private EditText carbs_edit_text;
    private EditText calories_edit_text;
    private EditText serving_size_edit_text;
    private Button add_ingredient_button;
    private Button add_instruction_step_button;
    private Button add_recipe_button;
    private BottomNavigationView navigationView;

    private String[] units = {"kilogram", "gram", "litr", "mililitr", "sztuk", "szczypta"};
    private Recipe new_recipe;
    private ArrayList<RecipeIngredient> ingredients;
    private ArrayList<String> instruction_steps;
    private ArrayList<String> tags;
    private RecyclerView ingredients_recycle_view;
    private RecyclerView instruction_steps_recycle_view;
    private IngredientAdapter ingredients_adapter;
    private InstructionStepAdapter instruction_steps_adapter;

    public NewRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewRecipeFragment newInstance(String param1, String param2) {
        NewRecipeFragment fragment = new NewRecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
        initLists();
        initViews(view);
    }

    private void initLists() {
        ingredients = new ArrayList<RecipeIngredient>();
        instruction_steps = new ArrayList<String>();
        tags = new ArrayList<String>();
    }

    private void initViews(View view) {

        recipe_name_edit_text = (EditText) view.findViewById(R.id.recipe_name_edit_text);
        protein_edit_text = (EditText) view.findViewById(R.id.protein_edit_text);
        fat_edit_text = (EditText) view.findViewById(R.id.fat_edit_text);
        carbs_edit_text = (EditText) view.findViewById(R.id.carbs_edit_text);
        calories_edit_text = (EditText) view.findViewById(R.id.calories_edit_text);
        serving_size_edit_text = (EditText) view.findViewById(R.id.serving_size_edit_text);
        add_ingredient_button = (Button) view.findViewById(R.id.add_ingredient_button);
        add_instruction_step_button = (Button) view.findViewById(R.id.add_instruction_step_button);
        add_recipe_button = (Button) view.findViewById(R.id.add_recipe_button);
        ingredients_recycle_view = view.findViewById(R.id.ingredients_recycle_view);
        instruction_steps_recycle_view = view.findViewById(R.id.instruction_steps_recycle_view);
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
                String recipe_name = recipe_name_edit_text.getText().toString();

                Integer calories_amount;
                if (calories_edit_text.getText().toString().isEmpty())
                {
                    calories_amount = 0;
                }
                else {
                    calories_amount = Integer.parseInt(calories_edit_text.getText().toString());
                }

                Integer protein_amount;
                if (protein_edit_text.getText().toString().isEmpty())
                {
                    protein_amount = 0;
                }
                else {
                    protein_amount = Integer.parseInt(protein_edit_text.getText().toString());
                }

                Integer fat_amount;
                if (fat_edit_text.getText().toString().isEmpty())
                {
                    fat_amount = 0;
                }
                else {
                    fat_amount = Integer.parseInt(fat_edit_text.getText().toString());
                }

                Integer carbs_amount;
                if (carbs_edit_text.getText().toString().isEmpty())
                {
                    carbs_amount = 0;
                }
                else {
                    carbs_amount = Integer.parseInt(carbs_edit_text.getText().toString());
                }

                Integer serving_size;
                if (serving_size_edit_text.getText().toString().isEmpty())
                {
                    serving_size = 0;
                }
                else {
                    serving_size = Integer.parseInt(serving_size_edit_text.getText().toString());
                }

                Recipe new_recipe = new Recipe(recipe_name, calories_amount, protein_amount, fat_amount, carbs_amount, serving_size, ingredients, instruction_steps, tags);

                Bundle result = new Bundle();
                result.putSerializable(NEW_RECIPE_TAG, new_recipe);
                getParentFragmentManager().setFragmentResult(RecipesListFragment.ADDED_RECIPE_KEY_TAG, result);
                requireActivity().getSupportFragmentManager().popBackStack();

            }
        });

        ingredients_recycle_view.setLayoutManager(new LinearLayoutManager(getContext()));
        ingredients_recycle_view.setAdapter(ingredients_adapter);
        instruction_steps_recycle_view.setLayoutManager(new LinearLayoutManager(getContext()));
        instruction_steps_recycle_view.setAdapter(instruction_steps_adapter);

    }

    private void initNewIngredientDialog() {

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.new_ingredient_dialog, null);

        MaterialAlertDialogBuilder materialDialogBuilder = new MaterialAlertDialogBuilder(getContext(), R.style.AppTheme_Dialog)
                .setTitle("Dodaj nowy składnik")
                .setView(dialogView);

        // Inicjalizacja elementów widoku
        EditText ingredient_name_edit_text = dialogView.findViewById(R.id.ingredient_name_edit_text);
        EditText ingredient_amount_edit_text = dialogView.findViewById(R.id.ingredient_amount_edit_text);
        Spinner ingredient_unit_spinner = dialogView.findViewById(R.id.ingredientUnitSpinner);

        // Utwórzenie adaptera przechowującego jednostki miary
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredient_unit_spinner.setAdapter(adapter);

        // Dodanie przycisków do dialogu
        materialDialogBuilder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        materialDialogBuilder.setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String new_recipe_name =  ingredient_name_edit_text.getText().toString();
                Double new_recipe_amount = Double.parseDouble(ingredient_amount_edit_text.getText().toString());
                String new_recipe_unit = ingredient_unit_spinner.getSelectedItem().toString();
                RecipeIngredient new_ingredient = new RecipeIngredient(new_recipe_name, new_recipe_amount, new_recipe_unit);
                ingredients.add(new_ingredient);
                ingredients_adapter.notifyDataSetChanged();
                dialogInterface.dismiss();
            }
        });

        // Wyświetlenie dialogu
        AlertDialog materialDialog = materialDialogBuilder.create();
        materialDialog.show();
    }

    private void initNewInstructionStepDialog() {
        // Inflate widok z XML
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.new_instruction_step_dialog, null);

        // Stwórz dialog
        MaterialAlertDialogBuilder materialDialogBuilder = new MaterialAlertDialogBuilder(getContext(), R.style.AppTheme_Dialog)
                .setTitle("Dodaj nowy krok przepisu")
                .setView(dialogView);

        // Inicjalizacja elementów widoku
        EditText ingredient_name_edit_text = dialogView.findViewById(R.id.instruction_step_edit_text);

        // Dodanie przycisków do dialogu
        materialDialogBuilder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        materialDialogBuilder.setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String new_instruction_step =  ingredient_name_edit_text.getText().toString();
                instruction_steps.add(new_instruction_step);
                instruction_steps_adapter.notifyDataSetChanged();
                dialogInterface.dismiss();
            }
        });

        // Wyświetlenie dialogu
        AlertDialog materialDialog = materialDialogBuilder.create();
        materialDialog.show();
    }

}