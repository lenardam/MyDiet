package com.lenardam.mydiet;

import static com.lenardam.mydiet.utils.CalendarUtils.daysInWeekArray;
import static com.lenardam.mydiet.utils.CalendarUtils.formattedDate;
import static com.lenardam.mydiet.utils.CalendarUtils.mondayForDate;
import static com.lenardam.mydiet.utils.CalendarUtils.monthYearFromDate;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.lenardam.mydiet.adapters.DietPlanDateAdapter;
import com.lenardam.mydiet.adapters.MealListAdapter;
import com.lenardam.mydiet.database.model.DietPlanFullData;
import com.lenardam.mydiet.database.model.DietPlans;
import com.lenardam.mydiet.database.model.MealFullData;
import com.lenardam.mydiet.database.model.Meals;
import com.lenardam.mydiet.database.viewModel.DietPlansViewModel;
import com.lenardam.mydiet.database.viewModel.MealsViewModel;
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
    private static final String DIET_DATE_SELECTED_TAG = "DIET_DATE_SELECTED_POSITION_TAG";
    private static final int ANIMATION_DURATION = 400;

    // TODO: Rename and change types of parameters
    private ArrayList<LocalDate> selectedWeek = new ArrayList<LocalDate>();
    private LocalDate selectedDate;

    private DietPlanDateAdapter datePlanAdapter;
    private RecyclerView dateRecycleView;
    private MealListAdapter mealsAdapter;
    private RecyclerView mealsRecycleView;
    private ItemTouchHelper mealItemTouchHelper;
    private ItemTouchHelper dateItemTouchHelper;


    private TextView monthYearTV;
    private ImageButton buttonPreviousWeek;
    private ImageButton buttonNextWeek;
    private TextView dateTV;
    private MaterialButton todayButton;

    private boolean isChooseingMeal = false;

    private DietPlansViewModel dietPlansViewModel;
    private MealsViewModel mealsViewModel;
    
    private DietPlanFullData selectedDietPlan;
    private Map<LocalDate, DietPlanFullData> allDietPlans = new HashMap<>();


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
            selectedDate = (LocalDate) savedInstanceState.getSerializable(DIET_DATE_SELECTED_TAG);
            selectedWeek = daysInWeekArray(selectedDate);
        }
        initViewModels(view);
        initViews(view);
        initWeekRecycleView(view);
        initMealRecycleView(view);

    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(DIET_DATE_SELECTED_TAG, selectedDate);
    }

    private void initViewModels(View view) {
        dietPlansViewModel = new ViewModelProvider(this).get(DietPlansViewModel.class);
        mealsViewModel = new ViewModelProvider(this).get(MealsViewModel.class);

        dietPlansViewModel.getAllDietPlans().observe(getViewLifecycleOwner(), new Observer<List<DietPlanFullData>>() {
            @Override
            public void onChanged(List<DietPlanFullData> dietPlanFullData) {
                for (DietPlanFullData plan : dietPlanFullData) {
                    // Sortowanie meals rosnąco po mealPosition
                    if (plan.meals != null) {
                        plan.meals.sort((m1, m2) -> Integer.compare(m1.meal.getMealPosition(), m2.meal.getMealPosition()));
                    }

                    allDietPlans.put(plan.dietPlan.getDate(), plan);
                }
                setMealRecycleView(selectedDate);
            }
        });
    }

    private void initViews(View view) {

        if (selectedDate == null  || isChooseingMeal == false) {
            selectedDate = LocalDate.now();
            selectedWeek = daysInWeekArray(selectedDate);
        }

        isChooseingMeal = false;

        dateTV = (TextView) view.findViewById(R.id.fr_diet_tv_diet_date_label);
        monthYearTV = (TextView) view.findViewById(R.id.fr_diet_tv_month_year);
        buttonPreviousWeek = (ImageButton) view.findViewById(R.id.fr_diet_btn_previous_week);
        buttonNextWeek = (ImageButton) view.findViewById(R.id.fr_diet_btn_next_week);
        todayButton = (MaterialButton) view.findViewById(R.id.fr_diet_btn_go_to_current_day);

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

        setTodayButtonVisibility();
        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDate today = LocalDate.now();
                setDay(today);
            }
        });

    }

    private void setDay(LocalDate newSelectedDay) {

        LocalDate oldSelectedDate = selectedDate;

        if (selectedWeek.contains(newSelectedDay)) {
            // Dzień w tym samym tygodniu
            selectedDate = newSelectedDay;
            datePlanAdapter.setSelectedDate(selectedDate);
            datePlanAdapter.notifyDataSetChanged();
        } else {
            // Nowy tydzień
            selectedDate = newSelectedDay;
            monthYearTV.setText(monthYearFromDate(selectedDate));
            selectedWeek = daysInWeekArray(selectedDate);

            // Ustaw dane w adapterze
            datePlanAdapter.setSelectedDate(selectedDate);
            datePlanAdapter.setWeekDays(selectedWeek);
            datePlanAdapter.notifyDataSetChanged();

//            // Animacja przesunięcia dateRecycleView
//            if (oldSelectedDate.isAfter(newSelectedDay)) {
//                dateRecycleView.setTranslationX(-dateRecycleView.getWidth() / 2f);
//            } else {
//                dateRecycleView.setTranslationX(dateRecycleView.getWidth() / 2f);
//            }
//            dateRecycleView.animate()
//                    .translationX(0)
//                    .setDuration(ANIMATION_DURATION)
//                    .start();
        }

        // Aktualizacja widoku dnia
        dateTV.setText(formattedDate(selectedDate));

//        // Animacja przesunięcia mealsRecycleView przy każdej zmianie dnia
//        if (oldSelectedDate.isAfter(newSelectedDay)) {
//            mealsRecycleView.setTranslationX(-mealsRecycleView.getWidth() / 2f);
//        } else {
//            mealsRecycleView.setTranslationX(mealsRecycleView.getWidth() / 2f);
//        }
//
//        // odtworzenie animacji w mealsRV
//        setMealRecycleView(selectedDate);
//        mealsRecycleView.animate()
//                .translationX(0)
//                .setDuration(ANIMATION_DURATION)
//                .start();

        setTodayButtonVisibility();
    }

    private void setTodayButtonVisibility() {
        LocalDate today = LocalDate.now();

        if(!today.equals(selectedDate) || !selectedWeek.contains(today)){
            todayButton.setVisibility(View.VISIBLE);
        }
        else {
            todayButton.setVisibility(View.INVISIBLE);
        }

    }

    private void setPreviousWeek() {
        LocalDate currentDate = selectedWeek.get(0);
        LocalDate prevMonday = mondayForDate(currentDate.minusWeeks(1));
        monthYearTV.setText(monthYearFromDate(prevMonday));

        selectedWeek.clear();
        selectedWeek.addAll(daysInWeekArray(prevMonday));
        datePlanAdapter.setWeekDays(selectedWeek);
        datePlanAdapter.notifyDataSetChanged();

//        // Animacja przesunięcia dateRecycleView z lewej
//        dateRecycleView.setTranslationX(-dateRecycleView.getWidth() / 2f);
//        dateRecycleView.animate()
//                .translationX(0)
//                .setDuration(ANIMATION_DURATION)
//                .start();
    }

    private void setNextWeek() {
        LocalDate currentDate = selectedWeek.get(0);
        LocalDate nextMonday = mondayForDate(currentDate.plusWeeks(1));
        monthYearTV.setText(monthYearFromDate(nextMonday));

        selectedWeek.clear();
        selectedWeek.addAll(daysInWeekArray(nextMonday));
        datePlanAdapter.setWeekDays(selectedWeek);
        datePlanAdapter.notifyDataSetChanged();

//        // Animacja przesunięcia dateRecycleView z prawej
//        dateRecycleView.setTranslationX(dateRecycleView.getWidth() / 2f);
//        dateRecycleView.animate()
//                .translationX(0)
//                .setDuration(ANIMATION_DURATION)
//                .start();
    }

    private void initWeekRecycleView(View view) {
        monthYearTV.setText(monthYearFromDate(selectedDate));
        selectedWeek = daysInWeekArray(selectedDate);

        dateRecycleView = view.findViewById(R.id.fr_diet_rv_date);
        dateRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        datePlanAdapter = new DietPlanDateAdapter();

        datePlanAdapter.setSelectedDate(selectedDate);
        datePlanAdapter.setWeekDays(selectedWeek);
        datePlanAdapter.setOnDateClickListener(new DietPlanDateAdapter.OnDateClickListener() {
            @Override
            public void onDateClick(int position) {
                selectedDate = selectedWeek.get(position);
                datePlanAdapter.setSelectedDate(selectedDate);
                dateTV.setText(formattedDate(selectedDate));
                setMealRecycleView(selectedDate);
                setTodayButtonVisibility();
            }
        });

        dateRecycleView.setAdapter(datePlanAdapter);
        dateRecycleView.scrollToPosition(CalendarUtils.getIndexInWeekArray(selectedDate, selectedWeek));


        dateItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // nie obsługujemy drag & drop
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    setNextWeek();
                } else if (direction == ItemTouchHelper.RIGHT) {
                    setPreviousWeek();
                }
            }

            @Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 0.3f;
            }

            @Override
            public float getSwipeEscapeVelocity(float defaultValue) {
                return defaultValue * 1.5f;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                // Nie przesuwaj elementu - tylko reaguj na gest
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    // Ustaw przesunięcie na 0, żeby item się nie ruszał
                    super.onChildDraw(c, recyclerView, viewHolder, 0, 0, actionState, isCurrentlyActive);
                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        });

        dateItemTouchHelper.attachToRecyclerView(dateRecycleView);
    }

    private void initMealRecycleView(View view) {

        mealsRecycleView = view.findViewById(R.id.fr_diet_rv_meals);
        mealsRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        mealsAdapter = new MealListAdapter();

        mealsAdapter.setOnMealClickListener(new MealListAdapter.OnMealClickListener() {
            @Override
            public void onMealClick(int position, Meals clickedMeal) {
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
                if(selectedMeal.isEaten() || selectedMeal.isSkipped()){
                    selectedMeal.setEaten(false);
                    selectedMeal.setSkipped(false);
                }
                else {
                    selectedMeal.setEaten(true);
                    selectedMeal.setSkipped(false);
                }
                mealsViewModel.update(selectedMeal);
            }

            @Override
            public void onMealReplaceClick(int position, Meals clickedMeal) {
                isChooseingMeal = true;

                Bundle bundle = new Bundle();
                Fragment selectedFragment = new RecipeChooseFragment();
                bundle.putLong(RecipeChooseFragment.RECIPE_CHOOSE_SELECTED_TAG, clickedMeal.getMealId());
                selectedFragment.setArguments(bundle);

                // Rozpoczynamy transakcję fragmentu, aby przejść do fragmentu dziecka RecipeChooseFragment
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.act_main_fragment_container_view, selectedFragment)
                        .addToBackStack(null) // Dodajemy do back stack, by móc wrócić
                        .commit();

                mealsAdapter.notifyItemChanged(position);
            }

            @Override
            public void onMealRemoveClick(int position, Meals meal) {
                if (selectedDate != null && position != RecyclerView.NO_POSITION)
                {
                    mealsViewModel.delete(meal);
                }
                else{
                    Toast newToast = Toast.makeText(getContext(), R.string.diet_fragment_select_day_text, Toast.LENGTH_SHORT);
                }

            }

            @Override
            public void onMealSkipClick(int position, Meals meal) {
                meal.setSkipped(true);
                mealsViewModel.update(meal);
            }

            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                mealItemTouchHelper.startDrag(viewHolder);
            }

            @Override
            public void onMealAddButtonClick() {
                if (selectedDate != null && selectedDietPlan != null) {
                    int newMealPosition = selectedDietPlan.meals.size() + 1;
                    Meals newMeal = new Meals(selectedDietPlan.dietPlan.getDietPlanId(),  null, newMealPosition, 1.0, false, false);
                    mealsViewModel.insert(newMeal);

                }
            }
        });

        mealsRecycleView.setAdapter(mealsAdapter);

        mealItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                mealsAdapter.onItemMove(viewHolder.getBindingAdapterPosition(),target.getBindingAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    setDay(selectedDate.plusDays(1));
                } else if (direction == ItemTouchHelper.RIGHT) {
                    setDay(selectedDate.minusDays(1));
                }
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);

                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder != null) {
                    // Zmiana tła na lekko szary, gdy zaczynasz przesuwać
                    View layout = viewHolder.itemView.findViewById(R.id.it_meal_layout_recipe_name_delete_button);
                    layout.setBackgroundResource(R.drawable.background_light_grey_rounded);
                }
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

                // Przywrócenie oryginalnego koloru po zakończeniu przesuwania
                View layout = viewHolder.itemView.findViewById(R.id.it_meal_layout_recipe_name_delete_button);
                layout.setBackgroundResource(R.drawable.background_green_rounded);

                // Pobierz aktualną listę z adaptera
                List<MealFullData> items = mealsAdapter.getCurrentItems();

                List<Meals> mealsToUpdate = new ArrayList<>();

                // Ustaw itemPosition = indeks w liście
                for (int i = 0; i < items.size(); i++) {
                    items.get(i).meal.setMealPosition(i);
                    mealsToUpdate.add(items.get(i).meal);
                }

                // Wywołaj ViewModel (zapis w repozytorium na background thread)
                mealsViewModel.updateAll(mealsToUpdate);
            }

            @Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 0.3f;
            }

            @Override
            public float getSwipeEscapeVelocity(float defaultValue) {
                return defaultValue * 1.5f;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                // Nie przesuwaj elementu - tylko reaguj na gest
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    // Ustaw przesunięcie na 0, żeby item się nie ruszał
                    super.onChildDraw(c, recyclerView, viewHolder, 0, 0, actionState, isCurrentlyActive);
                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }

        });

        mealItemTouchHelper.attachToRecyclerView(mealsRecycleView);


    }

    private void setMealRecycleView(LocalDate selectedDate) {

        selectedDietPlan = allDietPlans.get(selectedDate);

        if (selectedDietPlan == null) {
            DietPlans newDietPlan = new DietPlans(selectedDate);
            List<Meals> newMeals = new ArrayList<>();
            for (int i = 0; i < MainActivity.myDiet.getDietSettings().getNumberOfMealsForDiet(); i++) {
                newMeals.add(new Meals(null, null, i+1,1.0, false, false));
            }
            dietPlansViewModel.insertWithMeals(newDietPlan, newMeals);
        }
        else {
            mealsAdapter.setMeals(allDietPlans.get(selectedDate).meals);
        }

        datePlanAdapter.notifyDataSetChanged();
    }

}