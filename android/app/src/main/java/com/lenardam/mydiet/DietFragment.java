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
import com.lenardam.mydiet.adapters.RecipesAdapter;
import com.lenardam.mydiet.model.DietPlan;
import com.lenardam.mydiet.model.Recipe;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DietFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DietFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String DIET_PLAN_TAG = "DIET_PLAN_TAG";

    // TODO: Rename and change types of parameters
    private ArrayList<DietPlan> diet_plan;

    private DatePlanAdapter date_plan_adapter;
    private RecyclerView date_recycle_view;
    private RecyclerView diet_plan_recycle_view;

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
        date_plan_adapter = new DatePlanAdapter(diet_plan);
        date_recycle_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        date_recycle_view.setAdapter(date_plan_adapter);

    }

    private void initViews(View view) {
    }
}