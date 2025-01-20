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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lenardam.mydiet.adapters.RecipeListAdapter;
import com.lenardam.mydiet.adapters.RecipeTagAdapter;
import com.lenardam.mydiet.model.Recipe;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipesListFragment extends Fragment implements RecipeListAdapter.OnRecipeClickListener, RecipeTagAdapter.OnRecipeTagClickListener {

    public static final String ADDED_RECIPE_KEY_TAG = "ADDED_RECIPE_KEY_TAG";
    public static final String EDITED_RECIPE_KEY_TAG = "EDITED_RECIPE_KEY_TAG";
    private static final String RECIPE_SELECTED_POSITION_TAG = "RECIPE_SELECTED_POSITION_TAG";
    private static final String RECIPE_SELECTED_TAG = "RECIPE_SELECTED_TAG";
    private static final String RECIPE_SEARCH_NAME_TAG = "RECIPE_SEARCH_NAME_TAG";
    private static final String RECIPE_SEARCH_TAGS_TAG = "RECIPE_SEARCH_TAGS_TAG";

    private ArrayList<Recipe> all_recipes;
    private ArrayList<String> all_tags;

    private int selectedRecipePosition = -1;
    private Recipe selectedRecipe;
    private String searchRecipeName;
    private ArrayList<String> selected_tags;

    private ImageButton searchButton;
    private ImageButton clearSearchButton;
    private EditText searchRecipeNameEditText;
    private RecyclerView searchRecipeTegRecyclerView;
    private RecipeTagAdapter recipeTagAdapter;
    private RecipeListAdapter recipes_adapter;
    private RecyclerView recipes_recycle_view;


    private boolean isSearchingState;
    private FloatingActionButton recipeListFAB;

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
            selectedRecipe = (Recipe) savedInstanceState.getSerializable(RECIPE_SELECTED_POSITION_TAG);
            searchRecipeName = savedInstanceState.getString(RECIPE_SEARCH_NAME_TAG);
            selected_tags = savedInstanceState.getStringArrayList(RECIPE_SEARCH_TAGS_TAG);
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
        initSearchTagRecycleView(view);
        initRecycleView(view);
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RECIPE_SELECTED_POSITION_TAG, selectedRecipePosition);
        outState.putSerializable(RECIPE_SELECTED_POSITION_TAG, selectedRecipe);
        outState.putString(RECIPE_SEARCH_NAME_TAG, searchRecipeName);
        outState.putStringArrayList(RECIPE_SEARCH_TAGS_TAG, selected_tags);
    }

    private void initViews(View view) {
        searchRecipeNameEditText = (EditText) view.findViewById(R.id.searchRecipeNameEditText);
        searchButton = (ImageButton) view.findViewById(R.id.searchButton);
        clearSearchButton = (ImageButton) view.findViewById(R.id.clearSearchButton);
        recipeListFAB = (FloatingActionButton) view.findViewById(R.id.recipeListFAB);

        all_tags = MainActivity.myDiet.getAll_tags();
        all_recipes = new ArrayList<Recipe>();

        if(selected_tags == null) {
            selected_tags = new ArrayList<String>();
        }

        if (searchRecipeName != null || !selected_tags.isEmpty()) {
            all_recipes.addAll(MainActivity.myDiet.filterRecipes(searchRecipeName, selected_tags));
            setSearchingState(true);
        }
        else {
            all_recipes.addAll(MainActivity.myDiet.getAll_recipes());
            setSearchingState(false);
        }

        if(searchRecipeName != null ){
            searchRecipeNameEditText.setText(searchRecipeName);
            setSearchingState(true);
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchRecipeName = String.valueOf(searchRecipeNameEditText.getText());
                all_recipes.clear();
                all_recipes.addAll(MainActivity.myDiet.filterRecipes(searchRecipeName, selected_tags));
                recipes_adapter.notifyDataSetChanged();
                setSearchingState(true);
            }
        });

        clearSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchRecipeNameEditText.setText("");
                selected_tags.clear();
                for (int i = 0; i < all_tags.size(); i++) {
                    recipeTagAdapter.setUnselectedItem(i);
                }
                all_recipes.clear();
                all_recipes.addAll(MainActivity.myDiet.getAll_recipes());
                recipes_adapter.notifyDataSetChanged();
                setSearchingState(false);
            }
        });

        recipeListFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedRecipePosition = -1;

                // Rozpoczynamy transakcję fragmentu, aby przejść do fragmentu dziecka NewRecipeFragment
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, new NewRecipeFragment())
                        .addToBackStack(null) // Dodajemy do back stack, by móc wrócić
                        .commit();
            }
        });

    }

    private void setSearchingState(boolean inSearchingState) {
        isSearchingState = inSearchingState;

        searchRecipeNameEditText.setFocusable(!inSearchingState);
        searchRecipeNameEditText.setFocusableInTouchMode(!inSearchingState);
        searchRecipeNameEditText.setClickable(!inSearchingState);
        searchRecipeNameEditText.setCursorVisible(!inSearchingState);

        //jeżeli wyszukujemy to chowamy przycisk wyszukiwania i blokujemy edytowalność pól wyszukiwania
        if (inSearchingState){
            clearSearchButton.setVisibility(View.VISIBLE);
            searchButton.setVisibility(View.INVISIBLE);
        }
        else {
            clearSearchButton.setVisibility(View.INVISIBLE);
            searchButton.setVisibility(View.VISIBLE);
        }
    }

    private void initSearchTagRecycleView(View view) {
        searchRecipeTegRecyclerView = view.findViewById(R.id.searchRecipeTegRecyclerView);
        recipeTagAdapter = new RecipeTagAdapter(all_tags, this, true);
        searchRecipeTegRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        searchRecipeTegRecyclerView.setAdapter(recipeTagAdapter);
    }

    private void initRecycleView(View view) {
        recipes_recycle_view = view.findViewById(R.id.recipes_recycle_view);
        recipes_adapter = new RecipeListAdapter(all_recipes, this, true);
        recipes_recycle_view.setLayoutManager(new LinearLayoutManager(getContext()));
        recipes_recycle_view.setAdapter(recipes_adapter);

        for (int i = 0; i < all_tags.size(); i++) {
            if (selected_tags.contains(all_tags.get(i))) {
                recipeTagAdapter.setSelectedItem(i);
            }
            else{
                recipeTagAdapter.setUnselectedItem(i);
            }
        }

        if (selectedRecipePosition != -1) {
            recipes_recycle_view.scrollToPosition(selectedRecipePosition);
        }
    }

    @Override
    public void onRecipeClick(int position) {
        selectedRecipePosition = position;
        selectedRecipe = all_recipes.get(position);
        showRecipe(position);
    }

    @Override
    public void onRecipeLongClick(int position, View v) {
    }

    @Override
    public void onRecipeDeleteClick(int position) {
        if (position != RecyclerView.NO_POSITION)
        {
            Recipe recipe_to_delete = all_recipes.get(position);
            all_recipes.remove(position);
            MainActivity.myDiet.getAll_recipes().remove(recipe_to_delete);
            recipes_adapter.notifyDataSetChanged();
        }
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

    @Override
    public void onRecipeTagClick(int position, View view) {
        if (!isSearchingState) {
            if (!selected_tags.contains(all_tags.get(position))) {
                recipeTagAdapter.setSelectedItem(position);
                // Usuwa zaznaczenie
                selected_tags.add(all_tags.get(position));
            } else {
                recipeTagAdapter.setUnselectedItem(position);
                selected_tags.remove(all_tags.get(position));
            }
        }
    }

    @Override
    public void onRecipeTagLongClick(int position, View view) {

    }
}