package com.lenardam.mydiet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lenardam.mydiet.adapters.RecipesAdapter;
import com.lenardam.mydiet.model.Recipe;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipesListFragment extends Fragment {

    public static final String ADDED_RECIPE_KEY_TAG = "ADDED_RECIPE_KEY_TAG";

    private ArrayList<Recipe> all_recipes;
    private RecipesAdapter recipes_adapter;
    private RecyclerView recipes_recycle_view;

    public RecipesListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecipesListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipesListFragment newInstance() {
        RecipesListFragment fragment = new RecipesListFragment();
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
        return inflater.inflate(R.layout.recipes_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initRecycleView(view);
        initFragmentResultListeners();

    }

    private void initRecycleView(View view) {
        all_recipes = MainActivity.myDiet.getAll_recipes();
        recipes_recycle_view = view.findViewById(R.id.recipes_recycle_view);
        recipes_adapter = new RecipesAdapter(all_recipes, null);
        recipes_recycle_view.setLayoutManager(new LinearLayoutManager(getContext()));
        recipes_recycle_view.setAdapter(recipes_adapter);
    }

    private void initViews(View view) {

        Button add_new_recipe = (Button) view.findViewById(R.id.add_new_recipe);
        add_new_recipe.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Rozpoczynamy transakcję fragmentu, aby przejść do fragmentu dziecka NewRecipeFragment
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainerView, new NewRecipeFragment())
                                .addToBackStack(null) // Dodajemy do back stack, by móc wrócić
                                .commit();
                    }
                }
        );

    }

    private void initFragmentResultListeners() {
        getParentFragmentManager().setFragmentResultListener(ADDED_RECIPE_KEY_TAG, getViewLifecycleOwner(), (requestKey, result) -> {
            // Odbieramy Bundle
            if (result != null) {
                // Pobieramy dane z Bundle
                Recipe new_recipe = (Recipe) result.getSerializable(NewRecipeFragment.NEW_RECIPE_TAG);

                if (new_recipe != null)
                {
                    MainActivity.myDiet.getAll_recipes().add(new_recipe);
                    recipes_adapter.notifyDataSetChanged();

                }
            }
        });
    }
}