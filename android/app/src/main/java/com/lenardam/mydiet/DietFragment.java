package com.lenardam.mydiet;

import static com.lenardam.mydiet.utils.CalendarUtils.daysInWeekArray;
import static com.lenardam.mydiet.utils.CalendarUtils.monthYearFromDate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lenardam.mydiet.adapters.DietPlanDateAdapter;
import com.lenardam.mydiet.adapters.MealListAdapter;
import com.lenardam.mydiet.model.DietPlan;
import com.lenardam.mydiet.model.Meal;
import com.lenardam.mydiet.model.Recipe;
import com.lenardam.mydiet.utils.CalendarUtils;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DietFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DietFragment extends Fragment implements DietPlanDateAdapter.OnDateClickListener, MealListAdapter.OnMealClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String DIET_RECIPE_CHOOSE_SELECTED_TAG = "DIET_RECIPE_CHOOSE_SELECTED_TAG";
    public static final String DIET_CHANGED_DIET_PLAN_TAG = "DIET_CHANGED_DIET_PLAN_TAG";
    private static final String DIET_MEAL_SELECTED_POSITION_TAG = "DIET_MEAL_SELECTED_POSITION_TAG";
    private static final String DIET_DATE_SELECTED_TAG = "DIET_DATE_SELECTED_POSITION_TAG";

    // TODO: Rename and change types of parameters
    private ArrayList<Meal> selected_meals;
    private ArrayList<LocalDate> selected_week;

    private DietPlanDateAdapter date_plan_adapter;
    private RecyclerView date_recycle_view;
    private MealListAdapter meals_adapter;
    private RecyclerView meals_recycle_view;
    private int selectedMealPosition = -1;
    public static LocalDate selectedDate;
    private TextView monthYearTV;
    private ImageButton buttonPreviousWeek;
    private ImageButton buttonNextWeek;


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
    public static DietFragment newInstance() {
        DietFragment fragment = new DietFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            selectedMealPosition = savedInstanceState.getInt(DIET_MEAL_SELECTED_POSITION_TAG, RecyclerView.NO_POSITION);
            selectedDate = (LocalDate) savedInstanceState.getSerializable(DIET_DATE_SELECTED_TAG);
        }
        initViews(view);
        initWeekRecycleView(view);
        initMealRecycleView(view);
        initFragmentResultListeners();
        
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DIET_MEAL_SELECTED_POSITION_TAG, selectedMealPosition);
        outState.putSerializable(DIET_DATE_SELECTED_TAG, selectedDate);
    }

    private void initViews(View view) {

        if (selectedDate == null) {
            selectedDate = LocalDate.now();
        }

        if (selected_meals == null) {
            selected_meals = new ArrayList<Meal>();
        }

        monthYearTV = (TextView) view.findViewById(R.id.monthYearTV);
        buttonPreviousWeek = (ImageButton) view.findViewById(R.id.buttonPreviousWeek);
        buttonNextWeek = (ImageButton) view.findViewById(R.id.buttonNextWeek);

        buttonPreviousWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPreviousWeek(view);
            }
        });

        buttonNextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNextWeek(view);
            }
        });

    }

    private void setPreviousWeek(View view) {
        selectedDate = selectedDate.minusWeeks(1);
        monthYearTV.setText(monthYearFromDate(selectedDate));
        selected_week.clear();
        selected_week.addAll(daysInWeekArray(selectedDate));
        date_plan_adapter.notifyDataSetChanged();
    }

    private void setNextWeek(View view) {
        selectedDate = selectedDate.plusWeeks(1);
        monthYearTV.setText(monthYearFromDate(selectedDate));
        selected_week.clear();
        selected_week.addAll(daysInWeekArray(selectedDate));
        date_plan_adapter.notifyDataSetChanged();
    }

    private void initWeekRecycleView(View view) {
        monthYearTV.setText(monthYearFromDate(selectedDate));
        selected_week = daysInWeekArray(selectedDate);

        date_recycle_view = view.findViewById(R.id.dateRecyclerView);
        date_plan_adapter = new DietPlanDateAdapter(selected_week, this);
        date_recycle_view.setLayoutManager(new GridLayoutManager(getContext(), 7));
        date_recycle_view.setAdapter(date_plan_adapter);
        date_recycle_view.scrollToPosition(CalendarUtils.getIndexInWeekArray(selectedDate, selected_week));
    }

    private void initMealRecycleView(View view) {


        meals_recycle_view = view.findViewById(R.id.MealsRecyclerView);
        meals_adapter = new MealListAdapter(selected_meals, this);
        meals_recycle_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        meals_recycle_view.setAdapter(meals_adapter);

        DietPlan selected_diet_plan = MainActivity.myDiet.getDietPlan_for_date(selectedDate);
        setMealRecycleView(selected_diet_plan);
    }

    private void initFragmentResultListeners() {
        getParentFragmentManager().setFragmentResultListener(DIET_RECIPE_CHOOSE_SELECTED_TAG, getViewLifecycleOwner(), (requestKey, result) -> {
            // Odbieramy Bundle
            if (result != null) {
                // Pobieramy dane z Bundle
                Recipe selected_recipe = (Recipe) result.getSerializable(RecipeChooseFragment.RECIPE_CHOOSE_SELECTED_TAG);

                if (selectedDate != null && selectedMealPosition != RecyclerView.NO_POSITION)
                {
                    for (int i = 0; i < MainActivity.myDiet.getDiet_plan().size(); i++) {
                        if (MainActivity.myDiet.getDiet_plan().get(i).getDiet_plan_date().equals(selectedDate)){
                            MainActivity.myDiet.getDiet_plan().get(i).getMeals().get(selectedMealPosition).setRecipe(selected_recipe);
                            MainActivity.myDiet.getDiet_plan().get(i).getMeals().get(selectedMealPosition).setIs_eaten(false);
                            MainActivity.myDiet.getDiet_plan().get(i).getMeals().get(selectedMealPosition).setPortion_of_recipe(1.0);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onDateClick(int position) {
        selectedDate = selected_week.get(position);
        DietPlan selected_diet_plan = MainActivity.myDiet.getDietPlan_for_date(selectedDate);
        setMealRecycleView(selected_diet_plan);

    }

    private void setMealRecycleView(DietPlan selectedDietPlan) {
        selected_meals.clear();
        if (selectedDietPlan != null) {
            selected_meals.addAll(selectedDietPlan.getMeals());
        }
        else {
            selectedDietPlan = new DietPlan(selectedDate, MainActivity.myDiet.getNumber_of_meals_for_diet(), null);
            MainActivity.myDiet.getDiet_plan().add(selectedDietPlan);
            selected_meals.addAll(selectedDietPlan.getMeals());
        }
        meals_adapter.notifyDataSetChanged();
        date_plan_adapter.notifyDataSetChanged();
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
        //Przy dłuższym kliknięciu ustawiamy przyciski Replace i Delete na widoczność
        meals_adapter.notifyItemChanged(position);
    }

    @Override
    public void onMealReplaceClick(int position) {
        Meal clickedMeal = selected_meals.get(position);

        Bundle bundle = new Bundle();
        Fragment selectedFragment = new RecipeChooseFragment();

        // Rozpoczynamy transakcję fragmentu, aby przejść do fragmentu dziecka RecipeChooseFragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, selectedFragment)
                .addToBackStack(null) // Dodajemy do back stack, by móc wrócić
                .commit();

        meals_adapter.notifyItemChanged(position);
    }

    @Override
    public void onMealDeleteClick(int position) {
        if (selectedDate != null && position != RecyclerView.NO_POSITION)
        {
            for (int i = 0; i < MainActivity.myDiet.getDiet_plan().size(); i++) {
                if (MainActivity.myDiet.getDiet_plan().get(i).getDiet_plan_date().equals(selectedDate)){
                    MainActivity.myDiet.getDiet_plan().get(i).getMeals().get(position).setRecipe(null);
                    MainActivity.myDiet.getDiet_plan().get(i).getMeals().get(position).setIs_eaten(false);
                    MainActivity.myDiet.getDiet_plan().get(i).getMeals().get(position).setPortion_of_recipe(1.0);
                }
            }
        }
        meals_adapter.notifyItemChanged(position);
    }
}