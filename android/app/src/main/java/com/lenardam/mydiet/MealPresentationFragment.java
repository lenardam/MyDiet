package com.lenardam.mydiet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
import com.lenardam.mydiet.database.model.Meals;
import com.lenardam.mydiet.database.model.RecipeIngredients;
import com.lenardam.mydiet.database.model.RecipeIngredientsFullData;
import com.lenardam.mydiet.database.model.RecipeInstructions;
import com.lenardam.mydiet.database.model.Recipes;
import com.lenardam.mydiet.database.model.Units;
import com.lenardam.mydiet.database.viewModel.MealsViewModel;
import com.lenardam.mydiet.database.viewModel.RecipeIngredientsViewModel;
import com.lenardam.mydiet.database.viewModel.RecipeInstructionsViewModel;
import com.lenardam.mydiet.database.viewModel.RecipesViewModel;
import com.lenardam.mydiet.database.viewModel.UnitsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealPresentationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MealPresentationFragment extends Fragment {


    public static final String MEAL_PRESENTATION_TAG = "MEAL_PRESENTATION_TAG";

    private Meals selectedMeal;
    private Recipes selectedMealRecipe;
    private Long selectedMealRecipeId;
    private List<RecipeIngredientsFullData> selectedRecipeIngredients = new ArrayList<>();
    private List<RecipeInstructions> selectedRecipeInstructions = new ArrayList<>();
    private List<RecipeIngredients> selectedMealIngredients = new ArrayList<>();

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
    private Double portionOfRecipe = 1.0;
    private ImageButton hideIngredientsButton;
    private ImageButton hideInstructionStepsButton;

    Long selectedMealId;

    MealsViewModel mealsViewModel;
    RecipesViewModel recipesViewModel;
    RecipeIngredientsViewModel recipeIngredientsViewModel;
    RecipeInstructionsViewModel recipeInstructionsViewModel;

    public MealPresentationFragment() {
        // Required empty public constructor
    }

    public static MealPresentationFragment newInstance(Long selectedMealId) {
        MealPresentationFragment fragment = new MealPresentationFragment();
        Bundle args = new Bundle();
        args.putLong(MEAL_PRESENTATION_TAG, selectedMealId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedMealId = getArguments().getLong(MEAL_PRESENTATION_TAG);
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
        mealsViewModel = new ViewModelProvider(this).get(MealsViewModel.class);
        recipesViewModel = new ViewModelProvider(this).get(RecipesViewModel.class);

        mealsViewModel.getMealById(selectedMealId).observe(getViewLifecycleOwner(), meal -> {
            selectedMeal = meal;
            selectedMealRecipeId = meal.getRecipeId();

            recipesViewModel.getRecipeById(selectedMealRecipeId).observe(getViewLifecycleOwner(), recipe -> {
                selectedMealRecipe = recipe;

                initViews(view);
                initRecycleView(view);

            });

        });
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
        servingSize = Double.valueOf(selectedMealRecipe.getServingSize());

        mealNameTextView = (TextView) view.findViewById(R.id.fr_meal_presentation_et_meal_name);
        mealCaloriesAmountTextView = (TextView) view.findViewById(R.id.fr_meal_presentation_et_meal_calories_amount);
        mealServingSizeTextView = (TextView) view.findViewById(R.id.fr_meal_presentation_et_meal_serving_size);
        mealProteinCarbsFatAmountTextView = (TextView) view.findViewById(R.id.fr_meal_presentation_tv_protein_carbs_fat_amount);

        mealServingSizePlusButton = (ImageButton) view.findViewById(R.id.fr_meal_presentation_btn_meal_serving_plus);
        mealServingSizeMinusButton = (ImageButton) view.findViewById(R.id.fr_meal_presentation_btn_meal_serving_minus);
        hideIngredientsButton = (ImageButton) view.findViewById(R.id.fr_meal_presentation_btn_hide_ingredients);
        hideInstructionStepsButton = (ImageButton) view.findViewById(R.id.fr_meal_presentation_btn_hide_instruction_steps);

        mealNameTextView.setText(selectedMealRecipe.getName());
        mealServingSizeTextView.setText(String.valueOf(portionOfRecipe));

        mealServingSizePlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                portionOfRecipe += portionOfRecipeDelta;
                setMealParametersForServingSize(portionOfRecipe, servingSize);
            }
        });

        mealServingSizeMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(servingSize > portionOfRecipeDelta) {
                    portionOfRecipe -= portionOfRecipeDelta;
                    setMealParametersForServingSize(portionOfRecipe, servingSize);
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
            hideIngredientsButton.setImageResource(R.drawable.ic_up);
            mealIngredientsRecycleView.setVisibility(View.GONE);
        }
        else {
            hideIngredientsButton.setImageResource(R.drawable.ic_down);
            mealIngredientsRecycleView.setVisibility(View.VISIBLE);
        }
    }
    private void setInstructionStepsVisibility(boolean hideInstructionSteps) {
        if (hideInstructionSteps){
            hideInstructionStepsButton.setImageResource(R.drawable.ic_up);
            mealInstructionStepsRecycleView.setVisibility(View.GONE);
        }
        else {
            hideInstructionStepsButton.setImageResource(R.drawable.ic_down);
            mealInstructionStepsRecycleView.setVisibility(View.VISIBLE);
        }
    }

    private void setMealParametersForServingSize(Double portionOfRecipe, Double servingSize) {

        int recipeCalories = selectedMealRecipe.getCaloriesAmount();
        int recipeProtein = selectedMealRecipe.getProteinAmount();
        int recipeFat = selectedMealRecipe.getFatAmount();
        int recipeCarbs = selectedMealRecipe.getCarbsAmount();

        double mealCalories = (double) recipeCalories * portionOfRecipe;
        double mealProtein = (double) recipeProtein * portionOfRecipe;
        double mealFat = (double) recipeFat * portionOfRecipe;
        double mealCarbs = (double) recipeCarbs * portionOfRecipe;

        mealServingSizeTextView.setText(String.valueOf(portionOfRecipe));
        mealCaloriesAmountTextView.setText(String.format(getString(R.string.calories_formated_text), (int)mealCalories ));
        mealProteinCarbsFatAmountTextView.setText(getString(R.string.protein_carbs_fat_amount_formated_text, (int) mealProtein, (int) mealCarbs, (int) mealFat));

        recalculateIngredients(portionOfRecipe, servingSize);
    }

    private void recalculateIngredients(double portionOfRecipe, double servingSize) {
        double portionOfMeal = portionOfRecipe / servingSize;

        selectedMealIngredients.clear();

        for (int i = 0; i < selectedRecipeIngredients.size(); i++) {
            RecipeIngredients selectedMealIngredient = new RecipeIngredients(
                    null,
                    selectedRecipeIngredients.get(i).recipeIngredient.getName(),
                    selectedRecipeIngredients.get(i).recipeIngredient.getAmount() * portionOfMeal,
                    selectedRecipeIngredients.get(i).recipeIngredient.getUnitId()
            );
            selectedMealIngredients.add(selectedMealIngredient);
        }

        ingredientsAdapter.setIngredients(selectedMealIngredients);
    }

    private void initRecycleView(View view) {
        ingredientsAdapter = new IngredientAdapter();
        mealIngredientsRecycleView = view.findViewById(R.id.fr_meal_presentation_rv_meal_ingredients);
        ingredientsAdapter.setOnRecipeIngredientClickListener(new IngredientAdapter.OnRecipeIngredientClickListener() {
            @Override
            public void onRecipeIngredientClick(int position) {
                ingredientsAdapter.setSelectedItem(position);
            }

            @Override
            public void onRecipeIngredientLongClick(int position, View v) {

            }
        });
        recipeIngredientsViewModel = new ViewModelProvider(this).get(RecipeIngredientsViewModel.class);
        recipeIngredientsViewModel.getRecipeIngredientsByRecipeId(selectedMealRecipeId).observe(getViewLifecycleOwner(), new Observer<List<RecipeIngredientsFullData>>() {
                    @Override
                    public void onChanged(List<RecipeIngredientsFullData> recipeIngredients) {
                        selectedRecipeIngredients = recipeIngredients;
                        setMealParametersForServingSize(portionOfRecipe, servingSize);
                }
        });
        setIngredientsVisibility(hideIngredients);

        mealIngredientsRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mealIngredientsRecycleView.setAdapter(ingredientsAdapter);



        instructionStepsAdapter = new InstructionStepAdapter();
        mealInstructionStepsRecycleView = view.findViewById(R.id.fr_meal_presentation_rv_meal_instruction_steps);
        instructionStepsAdapter.setOnInstructionStepClickListener(new InstructionStepAdapter.OnInstructionStepClickListener() {
            @Override
            public void onInstructionStepClick(int position) {
                instructionStepsAdapter.setSelectedItem(position);
            }

            @Override
            public void onInstructionStepLongClick(int position, View v) {

            }
        });
        recipeInstructionsViewModel = new ViewModelProvider(this).get(RecipeInstructionsViewModel.class);
        recipeInstructionsViewModel.getRecipeInstructionsByRecipeId(selectedMealRecipeId).observe(getViewLifecycleOwner(), new Observer<List<RecipeInstructions>>() {
                    @Override
                    public void onChanged(List<RecipeInstructions> recipeInstructions) {
                        selectedRecipeInstructions = recipeInstructions;
                        instructionStepsAdapter.setRecipeInstructions(recipeInstructions);
                }
        });


        mealInstructionStepsRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mealInstructionStepsRecycleView.setAdapter(instructionStepsAdapter);
        setInstructionStepsVisibility(hideInstructionSteps);

        UnitsViewModel unitsViewModel = new ViewModelProvider(this).get(UnitsViewModel.class);
        unitsViewModel.getAllUnits().observe(getViewLifecycleOwner(), new Observer<List<Units>>() {
            @Override
            public void onChanged(List<Units> units) {
                ingredientsAdapter.setUnits(units);
            }
        });

    }
}