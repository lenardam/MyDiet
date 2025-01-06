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
import android.widget.Button;
import android.widget.Toast;

import com.lenardam.mydiet.adapters.RecipesAdapter;
import com.lenardam.mydiet.model.Meal;
import com.lenardam.mydiet.model.Recipe;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeChooseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeChooseFragment extends Fragment implements RecipesAdapter.OnRecipeClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String RECIPE_CHOOSE_SELECTED_TAG = "RECIPE_CHOOSE_SELECTED_TAG";

    // TODO: Rename and change types of parameters
    private ArrayList<Recipe> all_recipes;
    private RecipesAdapter recipes_adapter;
    private RecyclerView recipe_choose_recycle_view;
    private Recipe clickedRecipe;
    private Button save_button;

    public RecipeChooseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecipeChooseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeChooseFragment newInstance() {
        RecipeChooseFragment fragment = new RecipeChooseFragment();
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
        return inflater.inflate(R.layout.recipe_choose_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initRecycleView(view);
        
    }

    private void initRecycleView(View view) {
        all_recipes = MainActivity.myDiet.getAll_recipes();
        recipe_choose_recycle_view = view.findViewById(R.id.recipe_choose_recycle_view);
        recipes_adapter = new RecipesAdapter(all_recipes, this);
        recipe_choose_recycle_view.setLayoutManager(new LinearLayoutManager(getContext()));
        recipe_choose_recycle_view.setAdapter(recipes_adapter);
    }

    private void initViews(View view) {

        save_button = (Button) view.findViewById(R.id.recipe_choose_save_button);
        save_button.setEnabled(false);
        save_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle result = new Bundle();
                        result.putSerializable(RECIPE_CHOOSE_SELECTED_TAG, clickedRecipe);
                        getParentFragmentManager().setFragmentResult(DietFragment.DIET_RECIPE_CHOOSE_SELECTED_TAG, result);
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                }
        );

    }

    @Override
    public void onRecipeClick(int position) {
        clickedRecipe = all_recipes.get(position);
        save_button.setEnabled(true);
    }
}