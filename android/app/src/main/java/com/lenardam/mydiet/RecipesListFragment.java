package com.lenardam.mydiet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import com.lenardam.mydiet.adapters.RecipeListAdapter;
import com.lenardam.mydiet.model.Recipe;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipesListFragment extends Fragment implements RecipeListAdapter.OnRecipeClickListener {

    public static final String ADDED_RECIPE_KEY_TAG = "ADDED_RECIPE_KEY_TAG";
    public static final String EDITED_RECIPE_KEY_TAG = "EDITED_RECIPE_KEY_TAG";
    private static final String RECIPE_SELECTED_POSITION_TAG = "RECIPE_SELECTED_POSITION_TAG";

    private ArrayList<Recipe> all_recipes;
    private RecipeListAdapter recipes_adapter;
    private RecyclerView recipes_recycle_view;

    private int selectedRecipePosition = -1;

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
        if (savedInstanceState != null) {
            selectedRecipePosition = savedInstanceState.getInt(RECIPE_SELECTED_POSITION_TAG, RecyclerView.NO_POSITION);
        }
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RECIPE_SELECTED_POSITION_TAG, selectedRecipePosition);
    }

    private void initRecycleView(View view) {
        all_recipes = MainActivity.myDiet.getAll_recipes();
        recipes_recycle_view = view.findViewById(R.id.recipes_recycle_view);
        recipes_adapter = new RecipeListAdapter(all_recipes, this, true);
        recipes_recycle_view.setLayoutManager(new LinearLayoutManager(getContext()));
        recipes_recycle_view.setAdapter(recipes_adapter);
    }

    private void initViews(View view) {

        Button add_new_recipe = (Button) view.findViewById(R.id.add_new_recipe);
        add_new_recipe.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedRecipePosition = -1;

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
                    if (selectedRecipePosition != -1){
                        MainActivity.myDiet.getAll_recipes().set(selectedRecipePosition, new_recipe);
                    }
                    else {
                        MainActivity.myDiet.getAll_recipes().add(new_recipe);
                    }
                    recipes_adapter.notifyDataSetChanged();
                }
            }
        });

        getParentFragmentManager().setFragmentResultListener(EDITED_RECIPE_KEY_TAG, getViewLifecycleOwner(), (requestKey, result) -> {
            // Odbieramy Bundle
            if (result != null) {
                // Pobieramy dane z Bundle
                Recipe new_recipe = (Recipe) result.getSerializable(NewRecipeFragment.NEW_RECIPE_TAG);


            }
        });
    }

    @Override
    public void onRecipeClick(int position) {
        selectedRecipePosition = position;
        showRecipe(position);
    }

    @Override
    public void onRecipeLongClick(int position, View v) {
        selectedRecipePosition = position;

        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.getMenuInflater().inflate(R.menu.pop_up_delete, popup.getMenu());
        popup.setGravity(Gravity.END);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.pop_up_delete){
                    deleteRecipe(position);
                }
                return true;
            }
        });
        popup.show();//showing popup menu
    }

    @Override
    public void onRecipeDeleteClick(int position) {
        if (position != RecyclerView.NO_POSITION)
        {
            MainActivity.myDiet.getAll_recipes().remove(position);
            recipes_adapter.notifyItemRemoved(position);
        }
    }

    private void deleteRecipe(int position) {
        MainActivity.myDiet.getAll_recipes().remove(position);
        recipes_adapter.notifyDataSetChanged();
    }

    private void showRecipe(int position) {
        Recipe clickedRecipe = all_recipes.get(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable(NewRecipeFragment.RECIPE_PRESENTATION_TAG, clickedRecipe);      // Przekazanie obiektu serializowalnego

        NewRecipeFragment newRecipeFragment = new NewRecipeFragment();
        newRecipeFragment.setArguments(bundle); // Ustawienie argumentów

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, newRecipeFragment)
                .addToBackStack(null)
                .commit();
    }
}