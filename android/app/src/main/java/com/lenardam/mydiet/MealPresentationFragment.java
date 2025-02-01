package com.lenardam.mydiet;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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

    private Meal selectedMeal;
    private ArrayList<RecipeIngredient> recipeIngredients;
    private ArrayList<String> recipeSteps;
    private EditText mealNameEditText;
    private EditText mealCaloriesAmountEditText;
    private EditText mealServingSizeEditText;
    private EditText mealProteinAmountEditText;
    private EditText mealFatAmountEditText;
    private EditText mealCarbsAmountEditText;
    private RecyclerView mealIngredientsRecycleView;
    private RecyclerView mealInstructionStepsRecycleView;
    private IngredientAdapter ingredientsAdapter;
    private InstructionStepAdapter instructionStepsAdapter;

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
            selectedMeal = (Meal) getArguments().getSerializable(MEAL_PRESENTATION_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_presentation, container, false);
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
        double portionOfRecipe = selectedMeal.getPortionOfRecipe();
        double servingSize = Double.valueOf(selectedMeal.getRecipe().getServingSize());


        recipeIngredients = new ArrayList<RecipeIngredient>();
        recalculateIngredients(portionOfRecipe, servingSize);

        recipeSteps = selectedMeal.getRecipe().getInstructionSteps();

        if (recipeSteps == null) {
            recipeSteps = new ArrayList<String>();
        }

        mealNameEditText = (EditText) view.findViewById(R.id.fr_meal_presentation_et_meal_name);
        mealCaloriesAmountEditText = (EditText) view.findViewById(R.id.fr_meal_presentation_et_meal_calories_amount);
        mealServingSizeEditText = (EditText) view.findViewById(R.id.fr_meal_presentation_et_meal_serving_size);
        mealProteinAmountEditText = (EditText) view.findViewById(R.id.fr_meal_presentation_et_meal_protein_amount);
        mealFatAmountEditText = (EditText) view.findViewById(R.id.fr_meal_presentation_et_meal_fat_amount);
        mealCarbsAmountEditText = (EditText)view.findViewById(R.id.fr_meal_presentation_et_meal_carbs_amount);

        mealNameEditText.setText(selectedMeal.getRecipe().getName());
        mealCaloriesAmountEditText.setText(String.valueOf(selectedMeal.getRecipe().getCaloriesAmount()));
        mealServingSizeEditText.setText(String.valueOf(selectedMeal.getPortionOfRecipe()));
        mealProteinAmountEditText.setText(String.valueOf(selectedMeal.getRecipe().getProteinAmount()));
        mealFatAmountEditText.setText(String.valueOf(selectedMeal.getRecipe().getFatAmount()));
        mealCarbsAmountEditText.setText(String.valueOf(selectedMeal.getRecipe().getCarbsAmount()));

        mealServingSizeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Wykonuje się po zmianie tekstu
                if (!editable.toString().isEmpty()) {
                    double new_portion_of_recipe = Double.parseDouble(editable.toString());
                    recalculateIngredients(new_portion_of_recipe, servingSize);
                    ingredientsAdapter.notifyDataSetChanged();
                }
            }
        });

        mealServingSizeEditText.setOnEditorActionListener((textView, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Schowanie klawiatury
                InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                mealServingSizeEditText.clearFocus();
                return true; // Zatrzymuje dalsze propagowanie zdarzenia
            }
            return false; // Pozwala na dalsze przetwarzanie
        });


    }

    private void recalculateIngredients(double portionOfRecipe, double servingSize) {
        double portionOfMeal = portionOfRecipe / servingSize;

        recipeIngredients.clear();

        for (int i = 0; i < selectedMeal.getRecipe().getIngredients().size(); i++) {
            RecipeIngredient selectedMealIngredient = new RecipeIngredient(
                    selectedMeal.getRecipe().getIngredients().get(i).getName(),
                    selectedMeal.getRecipe().getIngredients().get(i).getAmount() * portionOfMeal,
                    selectedMeal.getRecipe().getIngredients().get(i).getUnit()
            );
            recipeIngredients.add(selectedMealIngredient);
        }
    }

    private void initRecycleView(View view) {
        ingredientsAdapter = new IngredientAdapter(recipeIngredients, this);
        instructionStepsAdapter = new InstructionStepAdapter(recipeSteps, this);

        mealIngredientsRecycleView = view.findViewById(R.id.fr_meal_presentation_rv_meal_ingredients);
        mealInstructionStepsRecycleView = view.findViewById(R.id.fr_meal_presentation_rv_meal_instruction_steps);

        mealIngredientsRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mealIngredientsRecycleView.setAdapter(ingredientsAdapter);
        mealInstructionStepsRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mealInstructionStepsRecycleView.setAdapter(instructionStepsAdapter);
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