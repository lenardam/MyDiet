package com.lenardam.mydiet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.lenardam.mydiet.adapters.IngredientAdapter;
import com.lenardam.mydiet.adapters.InstructionStepAdapter;
import com.lenardam.mydiet.model.Meal;
import com.lenardam.mydiet.model.RecipeIngredient;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealPresentationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MealPresentationFragment extends Fragment implements IngredientAdapter.OnRecipeIngredientClickListener, InstructionStepAdapter.OnInstructionStepClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String MEAL_PRESENTATION_TAG = "MEAL_PRESENTATION_TAG";

    private Meal selected_meal;
    private ArrayList<RecipeIngredient> recipe_ingredients;
    private ArrayList<String> recipe_steps;
    private EditText meal_edit_text;
    private EditText meal_calories_edit_text;
    private EditText meal_serving_size_edit_text;
    private EditText meal_protein_edit_text;
    private EditText meal_fat_edit_text;
    private EditText meal_carbs_edit_text;
    private RecyclerView meal_ingredients_recycle_view;
    private RecyclerView meal_instruction_steps_recycle_view;
    private IngredientAdapter ingredients_adapter;
    private InstructionStepAdapter instruction_steps_adapter;

    public MealPresentationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecipePresentationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MealPresentationFragment newInstance(Meal meal) {
        MealPresentationFragment fragment = new MealPresentationFragment();
        Bundle args = new Bundle();
        args.putSerializable(MEAL_PRESENTATION_TAG, meal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selected_meal = (Meal) getArguments().getSerializable(MEAL_PRESENTATION_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.meal_presentation_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initRecycleView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Ustawiamy, aby ekran się nie wyłączał
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onPause() {
        super.onPause();
        //Wyłączamy podtrzymywanie włączonego ekranu
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void initViews(View view) {
        double portion_of_recipe = selected_meal.getPortion_of_recipe();
        double serving_size = Double.valueOf(selected_meal.getRecipe().getServing_size());
        double portion_of_meal = portion_of_recipe / serving_size;

        recipe_ingredients = new ArrayList<RecipeIngredient>();
        for (int i = 0; i < selected_meal.getRecipe().getIngredients().size(); i++) {
            RecipeIngredient selected_meal_ingredient = new RecipeIngredient(
                    selected_meal.getRecipe().getIngredients().get(i).getName(),
                    selected_meal.getRecipe().getIngredients().get(i).getAmount() * portion_of_meal,
                    selected_meal.getRecipe().getIngredients().get(i).getUnit()
            );
            recipe_ingredients.add(selected_meal_ingredient);
        }

        recipe_steps = selected_meal.getRecipe().getInstruction_steps();

        if (recipe_steps == null) {
            recipe_steps = new ArrayList<String>();
        }

        meal_edit_text = (EditText) view.findViewById(R.id.meal_edit_text);
        meal_calories_edit_text = (EditText) view.findViewById(R.id.meal_calories_edit_text);
        meal_serving_size_edit_text = (EditText) view.findViewById(R.id.meal_serving_size_edit_text);
        meal_protein_edit_text = (EditText) view.findViewById(R.id.meal_protein_edit_text);
        meal_fat_edit_text = (EditText) view.findViewById(R.id.meal_fat_edit_text);
        meal_carbs_edit_text = (EditText)view.findViewById(R.id.meal_carbs_edit_text);

        meal_edit_text.setText(selected_meal.getRecipe().getName());
        meal_calories_edit_text.setText(String.valueOf(selected_meal.getRecipe().getCalories_amount()));
        meal_serving_size_edit_text.setText(String.valueOf(selected_meal.getRecipe().getServing_size()));
        meal_protein_edit_text.setText(String.valueOf(selected_meal.getRecipe().getProtein_amount()));
        meal_fat_edit_text.setText(String.valueOf(selected_meal.getRecipe().getFat_amount()));
        meal_carbs_edit_text.setText(String.valueOf(selected_meal.getRecipe().getCarbs_amount()));

    }

    private void initRecycleView(View view) {
        ingredients_adapter = new IngredientAdapter(recipe_ingredients, this);
        instruction_steps_adapter = new InstructionStepAdapter(recipe_steps, this);

        meal_ingredients_recycle_view = view.findViewById(R.id.meal_ingredients_recycle_view);
        meal_instruction_steps_recycle_view = view.findViewById(R.id.meal_instruction_steps_recycle_view);

        meal_ingredients_recycle_view.setLayoutManager(new LinearLayoutManager(getContext()));
        meal_ingredients_recycle_view.setAdapter(ingredients_adapter);
        meal_instruction_steps_recycle_view.setLayoutManager(new LinearLayoutManager(getContext()));
        meal_instruction_steps_recycle_view.setAdapter(instruction_steps_adapter);
    }

    @Override
    public void onRecipeIngredientClick(int position) {

    }

    @Override
    public void onRecipeIngredientLongClick(int position, View v) {

    }

    @Override
    public void onInstructionStepClick(int position) {

    }

    @Override
    public void onInstructionStepLongClick(int position, View v) {

    }
}