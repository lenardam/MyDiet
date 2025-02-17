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
import android.widget.ImageButton;
import android.widget.TextView;

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


    public static final String MEAL_PRESENTATION_TAG = "MEAL_PRESENTATION_TAG";

    private Meal selectedMeal;
    private ArrayList<RecipeIngredient> recipeIngredients;
    private ArrayList<String> recipeSteps;
    private boolean hideIngredients = false;
    private boolean hideInstructionSteps = false;

    private TextView mealNameTextView;
    private TextView mealCaloriesAmountTextView;
    private TextView mealProteinCarbsFatAmountTextView;
    private TextView mealServingSizeTextView;

    private RecyclerView mealIngredientsRecycleView;
    private RecyclerView mealInstructionStepsRecycleView;
    private IngredientAdapter ingredientsAdapter;
    private InstructionStepAdapter instructionStepsAdapter;
    private ImageButton mealServingSizePlusButton;
    private ImageButton mealServingSizeMinusButton;

    private Double servingSize = 1.0;
    private Double portionOfRecipeDelta = 0.25;
    private double portionOfRecipe = 1.0;
    private ImageButton hideIngredientsButton;
    private ImageButton hideInstructionStepsButton;

    public MealPresentationFragment() {
        // Required empty public constructor
    }

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
        ((MainActivity) requireActivity()).setBottomNavigationItem(R.id.menu_bottom_item_diet_fragment);
    }

    @Override
    public void onPause() {
        super.onPause();
        //Wyłączamy podtrzymywanie włączonego ekranu
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ((MainActivity) requireActivity()).setBottomNavigationItem(R.id.menu_bottom_item_diet_fragment);
    }

    private void initViews(View view) {
        portionOfRecipe = selectedMeal.getPortionOfRecipe();
        servingSize = Double.valueOf(selectedMeal.getRecipe().getServingSize());


        recipeIngredients = new ArrayList<RecipeIngredient>();
        recipeSteps = selectedMeal.getRecipe().getInstructionSteps();

        if (recipeSteps == null) {
            recipeSteps = new ArrayList<String>();
        }

        mealNameTextView = (TextView) view.findViewById(R.id.fr_meal_presentation_et_meal_name);
        mealCaloriesAmountTextView = (TextView) view.findViewById(R.id.fr_meal_presentation_et_meal_calories_amount);
        mealServingSizeTextView = (TextView) view.findViewById(R.id.fr_meal_presentation_et_meal_serving_size);
        mealProteinCarbsFatAmountTextView = (TextView) view.findViewById(R.id.fr_meal_presentation_tv_protein_carbs_fat_amount);

        mealServingSizePlusButton = (ImageButton) view.findViewById(R.id.fr_meal_presentation_btn_meal_serving_plus);
        mealServingSizeMinusButton = (ImageButton) view.findViewById(R.id.fr_meal_presentation_btn_meal_serving_minus);
        hideIngredientsButton = (ImageButton) view.findViewById(R.id.fr_meal_presentation_btn_hide_ingredients);
        hideInstructionStepsButton = (ImageButton) view.findViewById(R.id.fr_meal_presentation_btn_hide_instruction_steps);

        mealNameTextView.setText(selectedMeal.getRecipe().getName());
        mealServingSizeTextView.setText(String.valueOf(portionOfRecipe));

        setMealParametersForServingSize(portionOfRecipe, servingSize);

        mealServingSizePlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                portionOfRecipe += portionOfRecipeDelta;
                setMealParametersForServingSize(portionOfRecipe, servingSize);
                ingredientsAdapter.notifyDataSetChanged();
            }
        });

        mealServingSizeMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(servingSize > portionOfRecipeDelta) {
                    portionOfRecipe -= portionOfRecipeDelta;
                    setMealParametersForServingSize(portionOfRecipe, servingSize);
                    ingredientsAdapter.notifyDataSetChanged();
                }
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

    }



    private void setIngredientsVisibility(boolean hideIngredients) {
        if (hideIngredients) {
            hideIngredientsButton.setImageResource(R.drawable.ic_down);
            mealIngredientsRecycleView.setVisibility(View.GONE);
        }
        else {
            hideIngredientsButton.setImageResource(R.drawable.ic_up);
            mealIngredientsRecycleView.setVisibility(View.VISIBLE);
        }
    }
    private void setInstructionStepsVisibility(boolean hideInstructionSteps) {
        if (hideInstructionSteps){
            hideInstructionStepsButton.setImageResource(R.drawable.ic_down);
            mealInstructionStepsRecycleView.setVisibility(View.GONE);
        }
        else {
            hideInstructionStepsButton.setImageResource(R.drawable.ic_up);
            mealInstructionStepsRecycleView.setVisibility(View.VISIBLE);
        }
    }

    private void setMealParametersForServingSize(Double portionOfRecipe, Double servingSize) {

        int recipeCalories = selectedMeal.getRecipe().getCaloriesAmount();
        int recipeProtein = selectedMeal.getRecipe().getProteinAmount();
        int recipeFat = selectedMeal.getRecipe().getFatAmount();
        int recipeCarbs = selectedMeal.getRecipe().getCarbsAmount();

        double mealCalories = (double) recipeCalories * portionOfRecipe;
        double mealProtein = (double) recipeProtein * portionOfRecipe;
        double mealFat = (double) recipeFat * portionOfRecipe;
        double mealCarbs = (double) recipeCarbs * portionOfRecipe;

        mealServingSizeTextView.setText(String.valueOf(portionOfRecipe));
        mealCaloriesAmountTextView.setText(String.format("%d kcal", (int)mealCalories ));
        mealProteinCarbsFatAmountTextView.setText(String.format("B: %dg, W: %dg, T: %dg", (int) mealProtein, (int) mealCarbs, (int) mealFat));

        recalculateIngredients(portionOfRecipe, servingSize);
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
        setIngredientsVisibility(hideIngredients);

        mealInstructionStepsRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mealInstructionStepsRecycleView.setAdapter(instructionStepsAdapter);
        setInstructionStepsVisibility(hideInstructionSteps);
    }

    @Override
    public void onRecipeIngredientClick(int position) {
        ingredientsAdapter.setSelectedItem(position);
    }

    @Override
    public void onRecipeIngredientLongClick(int position, View v) {

    }

    @Override
    public void onInstructionStepClick(int position) {
        instructionStepsAdapter.setSelectedItem(position);
    }

    @Override
    public void onInstructionStepLongClick(int position, View v) {

    }
}