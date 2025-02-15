package com.lenardam.mydiet;

import static com.lenardam.mydiet.utils.CalendarUtils.daysInWeekArray;
import static com.lenardam.mydiet.utils.CalendarUtils.formattedDate;
import static com.lenardam.mydiet.utils.CalendarUtils.mondayForDate;
import static com.lenardam.mydiet.utils.CalendarUtils.monthYearFromDate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private ArrayList<Meal> selectedMeals;
    private ArrayList<LocalDate> selectedWeek;
    public static LocalDate selectedDate;

    private DietPlanDateAdapter datePlanAdapter;
    private RecyclerView dateRecycleView;
    private MealListAdapter mealsAdapter;
    private RecyclerView mealsRecycleView;
    private int selectedMealPosition = -1;
    private TextView monthYearTV;
    private ImageButton buttonPreviousWeek;
    private ImageButton buttonNextWeek;
    private TextView dateTV;


    public DietFragment() {
        // Required empty public constructor
    }

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
        return inflater.inflate(R.layout.fragment_diet, container, false);
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

        FloatingActionButton dietFAB = (FloatingActionButton) view.findViewById(R.id.fr_diet_fab_diet);

        if (selectedDate == null) {
            selectedDate = LocalDate.now();
        }

        if (selectedMeals == null) {
            selectedMeals = new ArrayList<Meal>();
        }

        dateTV = (TextView) view.findViewById(R.id.fr_diet_tv_diet_date_label);
        monthYearTV = (TextView) view.findViewById(R.id.fr_diet_tv_month_year);
        buttonPreviousWeek = (ImageButton) view.findViewById(R.id.fr_diet_btn_previous_week);
        buttonNextWeek = (ImageButton) view.findViewById(R.id.fr_diet_btn_next_week);

        dateTV.setText(formattedDate(selectedDate));
        buttonPreviousWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPreviousWeek();
            }
        });

        buttonNextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNextWeek();
            }
        });

        dietFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selectedMeals.isEmpty()) {
                    //dodaj posiłem, zaktualizuj liczbę posiłków i odśwież recyclerview

                    if (selectedDate != null)
                    {
                        MainActivity.myDiet.getDietPlanForDate(selectedDate).getMeals().add(new Meal());
                        MainActivity.myDiet.getDietPlanForDate(selectedDate).setNumberOfMeals(MainActivity.myDiet.getDietPlanForDate(selectedDate).getMeals().size());
                        setMealRecycleView(selectedDate);
                    }
                }
            }

        });

    }

    private void setPreviousWeek() {
        LocalDate currentDate = selectedWeek.get(0);
        LocalDate prevMonday = mondayForDate(currentDate.minusWeeks(1));
        monthYearTV.setText(monthYearFromDate(prevMonday));
        selectedWeek.clear();
        selectedWeek.addAll(daysInWeekArray(prevMonday));
        datePlanAdapter.notifyDataSetChanged();
    }

    private void setNextWeek() {
        LocalDate currentDate = selectedWeek.get(0);
        LocalDate nextMonday = mondayForDate(currentDate.plusWeeks(1));
        monthYearTV.setText(monthYearFromDate(nextMonday));
        selectedWeek.clear();
        selectedWeek.addAll(daysInWeekArray(nextMonday));
        datePlanAdapter.notifyDataSetChanged();
    }

    private void initWeekRecycleView(View view) {
        monthYearTV.setText(monthYearFromDate(selectedDate));
        selectedWeek = daysInWeekArray(selectedDate);

        dateRecycleView = view.findViewById(R.id.fr_diet_rv_date);
        datePlanAdapter = new DietPlanDateAdapter(selectedWeek, this);
        dateRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        dateRecycleView.setAdapter(datePlanAdapter);

        dateRecycleView.scrollToPosition(CalendarUtils.getIndexInWeekArray(selectedDate, selectedWeek));
    }

    private void initMealRecycleView(View view) {


        mealsRecycleView = view.findViewById(R.id.fr_diet_rv_meals);
        mealsAdapter = new MealListAdapter(selectedMeals, this);
        mealsRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mealsRecycleView.setAdapter(mealsAdapter);
        setMealRecycleView(selectedDate);
    }

    private void initFragmentResultListeners() {
        getParentFragmentManager().setFragmentResultListener(DIET_RECIPE_CHOOSE_SELECTED_TAG, getViewLifecycleOwner(), (requestKey, result) -> {
            // Odbieramy Bundle
            if (result != null) {
                // Pobieramy dane z Bundle
                Recipe selectedRecipe = (Recipe) result.getSerializable(RecipeChooseFragment.RECIPE_CHOOSE_SELECTED_TAG);

                if (selectedDate != null && selectedMealPosition != RecyclerView.NO_POSITION && selectedRecipe != null)
                {
                    for (int i = 0; i < MainActivity.myDiet.getDietPlan().size(); i++) {
                        if (MainActivity.myDiet.getDietPlan().get(i).getDietPlanDate().equals(selectedDate)){
                            MainActivity.myDiet.getDietPlan().get(i).getMeals().get(selectedMealPosition).setRecipe(new Recipe(selectedRecipe));
                            MainActivity.myDiet.getDietPlan().get(i).getMeals().get(selectedMealPosition).setIsEaten(false);
                            MainActivity.myDiet.getDietPlan().get(i).getMeals().get(selectedMealPosition).setPortionOfRecipe(1.0);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onDateClick(int position) {
        selectedDate = selectedWeek.get(position);
        dateTV.setText(formattedDate(selectedDate));
        setMealRecycleView(selectedDate);
    }

    private void setMealRecycleView(LocalDate selectedDate) {
        DietPlan selectedDietPlan = MainActivity.myDiet.getDietPlanForDate(selectedDate);
        selectedMeals.clear();
        if (selectedDietPlan != null) {
            selectedMeals.addAll(selectedDietPlan.getMeals());
        }
        else {
            selectedDietPlan = new DietPlan(selectedDate, MainActivity.myDiet.getDietSettings().getNumberOfMealsForDiet(), null);
            MainActivity.myDiet.getDietPlan().add(selectedDietPlan);
            selectedMeals.addAll(selectedDietPlan.getMeals());
        }
        mealsAdapter.notifyDataSetChanged();
        datePlanAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMealClick(int position) {
        selectedMealPosition = position;
        Meal clickedMeal = selectedMeals.get(position);
        Bundle bundle = new Bundle();
        Fragment selectedFragment = null;

        //jeżeli wybrany posiłek nie ma wybranego przepisu to przejdź do fragmentu RecipeChooseFragment
        if (clickedMeal.getRecipe() == null) {
            //ustawienie wybranego fragmentu i dodanie parametrów do bundle
            selectedFragment = new RecipeChooseFragment();

            // Rozpoczynamy transakcję fragmentu, aby przejść do fragmentu dziecka RecipeChooseFragment
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.act_main_fragment_container_view, selectedFragment)
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
                    .replace(R.id.act_main_fragment_container_view, selectedFragment)
                    .addToBackStack(null) // Dodajemy do back stack, by móc wrócić
                    .commit();
        }
    }

    @Override
    public void onMealLongClick(int position) {
        selectedMealPosition = position;
        //Przy dłuższym kliknięciu ustawiamy przyciski Replace i Delete na widoczność
        mealsAdapter.notifyItemChanged(position);
    }

    @Override
    public void onMealReplaceClick(int position) {
        selectedMealPosition = position;
        Meal clickedMeal = selectedMeals.get(position);

        Bundle bundle = new Bundle();
        Fragment selectedFragment = new RecipeChooseFragment();

        // Rozpoczynamy transakcję fragmentu, aby przejść do fragmentu dziecka RecipeChooseFragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.act_main_fragment_container_view, selectedFragment)
                .addToBackStack(null) // Dodajemy do back stack, by móc wrócić
                .commit();

        mealsAdapter.notifyItemChanged(position);
    }

    @Override
    public void onMealDeleteClick(int position) {
        if (selectedDate != null && position != RecyclerView.NO_POSITION)
        {
            for (int i = 0; i < MainActivity.myDiet.getDietPlan().size(); i++) {
                if (MainActivity.myDiet.getDietPlan().get(i).getDietPlanDate().equals(selectedDate)){
                    MainActivity.myDiet.getDietPlan().get(i).getMeals().remove(position);
                    MainActivity.myDiet.getDietPlan().get(i).setNumberOfMeals(MainActivity.myDiet.getDietPlan().get(i).getMeals().size());
                }
            }
            setMealRecycleView(selectedDate);
        }
        else{
            Toast newToast = Toast.makeText(getContext(), "Wybierz dzień", Toast.LENGTH_SHORT);
        }

    }
}