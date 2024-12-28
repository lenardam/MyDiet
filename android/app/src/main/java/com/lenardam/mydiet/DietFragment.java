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

import com.lenardam.mydiet.adapters.DatePlanAdapter;
import com.lenardam.mydiet.adapters.MealsAdapter;
import com.lenardam.mydiet.model.DietPlan;
import com.lenardam.mydiet.model.Meal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DietFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DietFragment extends Fragment implements DatePlanAdapter.OnDateClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String DIET_PLAN_TAG = "DIET_PLAN_TAG";

    // TODO: Rename and change types of parameters
    private ArrayList<DietPlan> diet_plan;
    private ArrayList<Meal> selected_meals;

    private DatePlanAdapter date_plan_adapter;
    private RecyclerView date_recycle_view;
    private MealsAdapter meals_adapter;
    private RecyclerView meals_recycle_view;


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
    public static DietFragment newInstance(ArrayList<DietPlan> diet_plans) {
        DietFragment fragment = new DietFragment();
        Bundle args = new Bundle();
        args.putSerializable(DIET_PLAN_TAG, diet_plans);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            diet_plan = (ArrayList<DietPlan>) getArguments().getSerializable(DIET_PLAN_TAG);
        }
        else {
            diet_plan = new ArrayList<DietPlan>();
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
        initViews(view);
        initRecycleView(view);
        
    }

    private void initRecycleView(View view) {
        date_recycle_view = view.findViewById(R.id.dateRecyclerView);
        date_plan_adapter = new DatePlanAdapter(diet_plan, this);
        date_recycle_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        date_recycle_view.setAdapter(date_plan_adapter);

        meals_recycle_view = view.findViewById(R.id.MealsRecyclerView);
        meals_adapter = new MealsAdapter(selected_meals);
        meals_recycle_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        meals_recycle_view.setAdapter(meals_adapter);

        int todayIndex = getTodayIndex(diet_plan);
        if (todayIndex != -1) {
            date_recycle_view.scrollToPosition(todayIndex); // Przewiń do dzisiejszego elementu
            onDateClick(todayIndex);    // Zaktualizuj drugi RecyclerView
        }

    }

    private void initViews(View view) {
        if (diet_plan == null) {
            diet_plan = new ArrayList<>();
        }

        if (selected_meals == null) {
            selected_meals = new ArrayList<>();
        }
    }

    @Override
    public void onDateClick(int position) {
        DietPlan selected_diet_plan = diet_plan.get(position);
        selected_meals.clear();
        selected_meals.addAll(selected_diet_plan.getMeals());
        meals_adapter.notifyDataSetChanged();
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
}