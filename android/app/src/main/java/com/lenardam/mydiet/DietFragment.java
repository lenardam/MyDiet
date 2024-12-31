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
import android.widget.Toast;

import com.lenardam.mydiet.adapters.DatePlanAdapter;
import com.lenardam.mydiet.adapters.MealsAdapter;
import com.lenardam.mydiet.model.DietPlan;
import com.lenardam.mydiet.model.Meal;
import com.lenardam.mydiet.model.Recipe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DietFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DietFragment extends Fragment implements DatePlanAdapter.OnDateClickListener, MealsAdapter.OnMealClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String DIET_PLAN_TAG = "DIET_PLAN_TAG";
    public static final String DIET_RECIPE_LIST_TAG = "DIET_RECIPE_LIST_TAG";
    public static final String DIET_RECIPE_CHOOSE_SELECTED_TAG = "DIET_RECIPE_CHOOSE_SELECTED_TAG";
    public static final String DIET_CHANGED_DIET_PLAN_TAG = "DIET_CHANGED_DIET_PLAN_TAG";
    private static final String DIET_DATE_SELECTED_POSITION_TAG = "DIET_DATE_SELECTED_POSITION_TAG";
    private static final String DIET_MEAL_SELECTED_POSITION_TAG = "DIET_MEAL_SELECTED_POSITION_TAG";

    // TODO: Rename and change types of parameters
    private ArrayList<DietPlan> diet_plan;
    private ArrayList<Recipe> all_recipes;
    private ArrayList<Meal> selected_meals;

    private DatePlanAdapter date_plan_adapter;
    private RecyclerView date_recycle_view;
    private MealsAdapter meals_adapter;
    private RecyclerView meals_recycle_view;
    private int selectedDatePosition = -1;
    private int selectedMealPosition = -1;


    public DietFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DietFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DietFragment newInstance(ArrayList<DietPlan> diet_plans, ArrayList<Recipe> all_recipes) {
        DietFragment fragment = new DietFragment();
        Bundle args = new Bundle();
        args.putSerializable(DIET_PLAN_TAG, diet_plans);
        args.putSerializable(DIET_RECIPE_LIST_TAG, all_recipes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            diet_plan = (ArrayList<DietPlan>) getArguments().getSerializable(DIET_PLAN_TAG);
            all_recipes = (ArrayList<Recipe>) getArguments().getSerializable(DIET_RECIPE_LIST_TAG);
        }
        else {
            diet_plan = new ArrayList<DietPlan>();
            all_recipes = new ArrayList<Recipe>();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.diet_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            selectedDatePosition = savedInstanceState.getInt(DIET_DATE_SELECTED_POSITION_TAG, RecyclerView.NO_POSITION);
            selectedMealPosition = savedInstanceState.getInt(DIET_MEAL_SELECTED_POSITION_TAG, RecyclerView.NO_POSITION);
        }
        initViews(view);
        initRecycleView(view);
        
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DIET_DATE_SELECTED_POSITION_TAG, selectedDatePosition);
        outState.putInt(DIET_MEAL_SELECTED_POSITION_TAG, selectedMealPosition);
    }


    private void initRecycleView(View view) {
        date_recycle_view = view.findViewById(R.id.dateRecyclerView);
        date_plan_adapter = new DatePlanAdapter(diet_plan, this);
        date_recycle_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        date_recycle_view.setAdapter(date_plan_adapter);

        meals_recycle_view = view.findViewById(R.id.MealsRecyclerView);
        meals_adapter = new MealsAdapter(selected_meals, this);
        meals_recycle_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        meals_recycle_view.setAdapter(meals_adapter);

        //przeskocz do wybranego dnia
        //jeżeli został wcześniej zapisany stan to przejdź do niego
        if (selectedDatePosition != RecyclerView.NO_POSITION) {
            date_recycle_view.scrollToPosition(selectedDatePosition);
            onDateClick(selectedDatePosition);    // Zaktualizuj drugi RecyclerView
        }
        //w przeciwnym przypadku, ustaw obecny dzień
        else {
            int todayIndex = getTodayIndex(diet_plan);
            if (todayIndex != -1) {
                date_recycle_view.scrollToPosition(todayIndex); // Przewiń do dzisiejszego elementu
                onDateClick(todayIndex);    // Zaktualizuj drugi RecyclerView
            }
        }

    }

    private void initViews(View view) {
        if (diet_plan == null) {
            diet_plan = new ArrayList<>();
        }

        if (selected_meals == null) {
            selected_meals = new ArrayList<>();
        }

        if (all_recipes == null) {
            all_recipes = new ArrayList<>();
        }

        getParentFragmentManager().setFragmentResultListener(DIET_RECIPE_CHOOSE_SELECTED_TAG, getViewLifecycleOwner(), (requestKey, result) -> {
            // Odbieramy Bundle
            if (result != null) {
                // Pobieramy dane z Bundle
                Recipe selected_recipe = (Recipe) result.getSerializable(RecipeChooseFragment.RECIPE_CHOOSE_SELECTED_TAG);

                if (selectedDatePosition != RecyclerView.NO_POSITION && selectedMealPosition != RecyclerView.NO_POSITION)
                {
                    diet_plan.get(selectedDatePosition).getMeals().get(selectedMealPosition).setRecipe(selected_recipe);

                    Bundle result_bundle = new Bundle();
                    result.putSerializable(DIET_CHANGED_DIET_PLAN_TAG, diet_plan);
                    getParentFragmentManager().setFragmentResult(DIET_CHANGED_DIET_PLAN_TAG, result);

                }
            }
        });
    }

    private int getTodayIndex(ArrayList<DietPlan> dietPlans) {
        Date today = new Date(); // Dzisiejsza data
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (int i = 0; i < dietPlans.size(); i++) {
            // Porównaj daty bez czasu (np. tylko yyyy-MM-dd)
            if (sdf.format(today).equals(sdf.format(dietPlans.get(i).getDiet_plan_date()))) {
                return i; // Zwróć indeks, gdy znajdziesz dzisiejszą datę
            }
        }
        return -1; // Jeśli nie znaleziono dzisiejszej daty
    }

    @Override
    public void onDateClick(int position) {
        selectedDatePosition = position;
        DietPlan selected_diet_plan = diet_plan.get(position);
        selected_meals.clear();
        selected_meals.addAll(selected_diet_plan.getMeals());
        meals_adapter.notifyDataSetChanged();
    }

    @Override
    public void onMealClick(int position) {
        selectedMealPosition = position;
        Meal clickedMeal = selected_meals.get(position);
        Bundle bundle = new Bundle();
        Fragment selectedFragment = null;

        //jeżeli wybrany posiłek nie ma wybranego przepisu to przejdź do fragmentu RecipeChooseFragment
        if (clickedMeal.getRecipe() == null) {
            //ustawienie wybranego fragmentu i dodanie parametrów do bundle
            selectedFragment = new RecipeChooseFragment();
            bundle.putSerializable(RecipeChooseFragment.RECIPE_CHOOSE_LIST_TAG, all_recipes);
            selectedFragment.setArguments(bundle);

            // Rozpoczynamy transakcję fragmentu, aby przejść do fragmentu dziecka RecipeChooseFragment
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, selectedFragment)
                    .addToBackStack(null) // Dodajemy do back stack, by móc wrócić
                    .commit();
        }
        //jeżeli ma wybrany przepis to przejdź do fragmentu MealPresentationFragment
        else {
            //ustawienie wybranego fragmentu i dodanie parametrów do bundle
            selectedFragment = new MealPresentationFragment();
            bundle.putSerializable(MealPresentationFragment.MEAL_PRESENTATION_TAG, clickedMeal);
            selectedFragment.setArguments(bundle);

            // Rozpoczynamy transakcję fragmentu, aby przejść do fragmentu dziecka NewRecipeFragment
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, selectedFragment)
                    .addToBackStack(null) // Dodajemy do back stack, by móc wrócić
                    .commit();
        }
    }

    @Override
    public void onMealLongClick(int position) {
        //przy dłuższym kliknięciu zmieniamy przepis
        Meal clickedMeal = selected_meals.get(position);

        Bundle bundle = new Bundle();
        Fragment selectedFragment = null;

        selectedFragment = new RecipeChooseFragment();
        bundle.putSerializable(RecipeChooseFragment.RECIPE_CHOOSE_LIST_TAG, all_recipes);
        selectedFragment.setArguments(bundle);

        // Rozpoczynamy transakcję fragmentu, aby przejść do fragmentu dziecka RecipeChooseFragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, selectedFragment)
                .addToBackStack(null) // Dodajemy do back stack, by móc wrócić
                .commit();
    }
}