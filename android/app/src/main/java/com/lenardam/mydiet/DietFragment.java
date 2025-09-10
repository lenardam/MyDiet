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
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lenardam.mydiet.adapters.DietPlanDateAdapter;
import com.lenardam.mydiet.adapters.MealListAdapter;
import com.lenardam.mydiet.database.model.DietPlans;
import com.lenardam.mydiet.database.model.Meals;
import com.lenardam.mydiet.database.model.Units;
import com.lenardam.mydiet.database.viewModel.DietPlansViewModel;
import com.lenardam.mydiet.database.viewModel.MealsViewModel;
import com.lenardam.mydiet.model.DietPlan;
import com.lenardam.mydiet.model.Meal;
import com.lenardam.mydiet.model.Recipe;
import com.lenardam.mydiet.utils.CalendarUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DietFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DietFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String DIET_RECIPE_CHOOSE_SELECTED_TAG = "DIET_RECIPE_CHOOSE_SELECTED_TAG";
    public static final String DIET_CHANGED_DIET_PLAN_TAG = "DIET_CHANGED_DIET_PLAN_TAG";
    private static final String DIET_MEAL_SELECTED_POSITION_TAG = "DIET_MEAL_SELECTED_POSITION_TAG";
    private static final String DIET_DATE_SELECTED_TAG = "DIET_DATE_SELECTED_POSITION_TAG";

    // TODO: Rename and change types of parameters
    //private ArrayList<Meals> selectedMeals;
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
    private boolean isChooseingMeal = false;

    private DietPlansViewModel dietPlansViewModel;
    private MealsViewModel mealsViewModel;

    private Map<LocalDate, DietPlans> allDietPlansMap = new HashMap<>();


    public DietFragment() {
        // Required empty public constructor
    }

    public static DietFragment newInstance() {
        DietFragment fragment = new DietFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setBottomNavigationItem(R.id.menu_bottom_item_diet_fragment);
        requireActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
        initViewModels(view);
        initWeekRecycleView(view);
        initMealRecycleView(view);
//        initFragmentResultListeners();
        
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DIET_MEAL_SELECTED_POSITION_TAG, selectedMealPosition);
        outState.putSerializable(DIET_DATE_SELECTED_TAG, selectedDate);
    }

    private void initViews(View view) {

        FloatingActionButton dietFAB = (FloatingActionButton) view.findViewById(R.id.fr_diet_fab_diet);

        if (selectedDate == null  || isChooseingMeal == false) {
            selectedDate = LocalDate.now();
        }

        isChooseingMeal = false;

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
                if (selectedDate != null) {
                    //dodaj posiłem, zaktualizuj liczbę posiłków i odśwież recyclerview
                    DietPlans selectedDietPlan = allDietPlansMap.get(selectedDate);

                    Meals newMeal = new Meals(selectedDietPlan.getDietPlanId(), null, 1.0, false);
                    mealsViewModel.insert(newMeal);

                }
            }

        });

    }

    private void initViewModels(View view) {
        dietPlansViewModel = new ViewModelProvider(this).get(DietPlansViewModel.class);
        dietPlansViewModel.getAllDietPlans().observe(getViewLifecycleOwner(), dietPlans -> {

            allDietPlansMap.clear();
            for (DietPlans u : dietPlans){
                allDietPlansMap.put(u.getDate(), u);
            }

        });

        mealsViewModel = new ViewModelProvider(this).get(MealsViewModel.class);
    }

    private void setPreviousWeek() {
        LocalDate currentDate = selectedWeek.get(0);
        LocalDate prevMonday = mondayForDate(currentDate.minusWeeks(1));
        monthYearTV.setText(monthYearFromDate(prevMonday));
        selectedWeek.clear();
        selectedWeek.addAll(daysInWeekArray(prevMonday));
        datePlanAdapter.setWeekDays(selectedWeek);
    }

    private void setNextWeek() {
        LocalDate currentDate = selectedWeek.get(0);
        LocalDate nextMonday = mondayForDate(currentDate.plusWeeks(1));
        monthYearTV.setText(monthYearFromDate(nextMonday));
        selectedWeek.clear();
        selectedWeek.addAll(daysInWeekArray(nextMonday));
        datePlanAdapter.setWeekDays(selectedWeek);
    }

    private void initWeekRecycleView(View view) {
        monthYearTV.setText(monthYearFromDate(selectedDate));
        selectedWeek = daysInWeekArray(selectedDate);

        dateRecycleView = view.findViewById(R.id.fr_diet_rv_date);
        dateRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        datePlanAdapter = new DietPlanDateAdapter();

        datePlanAdapter.setWeekDays(selectedWeek);
        datePlanAdapter.setOnDateClickListener(new DietPlanDateAdapter.OnDateClickListener() {
            @Override
            public void onDateClick(int position) {
                selectedDate = selectedWeek.get(position);
                dateTV.setText(formattedDate(selectedDate));
                setMealRecycleView(selectedDate);
            }
        });

        dateRecycleView.setAdapter(datePlanAdapter);
        dateRecycleView.scrollToPosition(CalendarUtils.getIndexInWeekArray(selectedDate, selectedWeek));

    }

    private void initMealRecycleView(View view) {

        mealsRecycleView = view.findViewById(R.id.fr_diet_rv_meals);
        mealsRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        mealsAdapter = new MealListAdapter();
        //mealsAdapter = new MealListAdapter(selectedMeals, this);
        setMealRecycleView(selectedDate);
        mealsAdapter.setOnMealClickListener(new MealListAdapter.OnMealClickListener() {
            @Override
            public void onMealClick(int position, Meals clickedMeal) {
                selectedMealPosition = position;
                isChooseingMeal = true;
                Bundle bundle = new Bundle();
                Fragment selectedFragment = null;

                //jeżeli wybrany posiłek nie ma wybranego przepisu to przejdź do fragmentu RecipeChooseFragment
                if (clickedMeal.getRecipeId() == null) {

                    //ustawienie wybranego fragmentu i dodanie parametrów do bundle
                    selectedFragment = new RecipeChooseFragment();
                    bundle.putLong(RecipeChooseFragment.RECIPE_CHOOSE_SELECTED_TAG, clickedMeal.getMealId());
                    selectedFragment.setArguments(bundle);

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
                    bundle.putLong(MealPresentationFragment.MEAL_PRESENTATION_TAG, clickedMeal.getMealId());
                    selectedFragment.setArguments(bundle);

                    // Rozpoczynamy transakcję fragmentu, aby przejść do fragmentu dziecka NewRecipeFragment
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.act_main_fragment_container_view, selectedFragment)
                            .addToBackStack(null) // Dodajemy do back stack, by móc wrócić
                            .commit();
                }
            }

            @Override
            public void onMealEatedClick(int position, Meals selectedMeal) {
                selectedMealPosition = position;
                boolean isEated = selectedMeal.isEaten();
                selectedMeal.setEaten(!isEated);
                mealsViewModel.update(selectedMeal);
                mealsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onMealReplaceClick(int position, Meals clickedMeal) {
                isChooseingMeal = true;
                selectedMealPosition = position;

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
            public void onMealDeleteClick(int position, Meals meal) {
                if (selectedDate != null && position != RecyclerView.NO_POSITION)
                {
                    mealsViewModel.delete(meal);
                }
                else{
                    Toast newToast = Toast.makeText(getContext(), R.string.diet_fragment_select_day_text, Toast.LENGTH_SHORT);
                }

            }
        });

        mealsRecycleView.setAdapter(mealsAdapter);
        setMealRecycleView(selectedDate);
    }

//    private void initFragmentResultListeners() {
//        getParentFragmentManager().setFragmentResultListener(DIET_RECIPE_CHOOSE_SELECTED_TAG, getViewLifecycleOwner(), (requestKey, result) -> {
//            // Odbieramy Bundle
//            if (result != null) {
//                isChooseingMeal = true;
//
//                // Pobieramy dane z Bundle
//                Recipe selectedRecipe = (Recipe) result.getSerializable(RecipeChooseFragment.RECIPE_CHOOSE_SELECTED_TAG);
//
//                if (selectedDate != null && selectedMealPosition != RecyclerView.NO_POSITION && selectedRecipe != null)
//                {
//                    for (int i = 0; i < MainActivity.myDiet.getDietPlan().size(); i++) {
//                        if (MainActivity.myDiet.getDietPlan().get(i).getDietPlanDate().equals(selectedDate)){
//                            MainActivity.myDiet.getDietPlan().get(i).getMeals().get(selectedMealPosition).setRecipe(new Recipe(selectedRecipe));
//                            MainActivity.myDiet.getDietPlan().get(i).getMeals().get(selectedMealPosition).setIsEaten(false);
//                            MainActivity.myDiet.getDietPlan().get(i).getMeals().get(selectedMealPosition).setPortionOfRecipe(1.0);
//                        }
//                    }
//                }
//            }
//        });
//    }

    private void setMealRecycleView(LocalDate selectedDate) {
        DietPlans selectedDietPlan = allDietPlansMap.get(selectedDate);
        //selectedMeals.clear();
        if (selectedDietPlan != null) {
            //selectedMeals.addAll(selectedDietPlan.getMeals());

            Long dietPlanId = selectedDietPlan.getDietPlanId();
            mealsViewModel.getMealsByDietPlanId(dietPlanId)
                    .observe(getViewLifecycleOwner(), meals -> {
                        mealsAdapter.setMeals(meals);
                    });

        }
        else {
            DietPlans newDietPlan = new DietPlans(selectedDate);
            List<Meals> newMeals = new ArrayList<>();
            for (int i = 0; i < MainActivity.myDiet.getDietSettings().getNumberOfMealsForDiet(); i++) {
                newMeals.add(new Meals(null, null, 1.0, false));
            }

            dietPlansViewModel.getNewDietPlanId().observe(getViewLifecycleOwner(), dietPlanId -> {
                if (dietPlanId != null) {
                    // aktualizujemy HashMapę i RecyclerView
                    newDietPlan.setDietPlanId(dietPlanId);
                    allDietPlansMap.put(selectedDate, newDietPlan);

                    mealsAdapter.setMeals(newMeals); // na początku może być pusta lista
                }
            });
        }

        datePlanAdapter.notifyDataSetChanged();
    }


}