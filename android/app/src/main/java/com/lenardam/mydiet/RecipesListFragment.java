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
import android.widget.EditText;
import android.widget.ImageButton;

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

    private ArrayList<Recipe> allRecipes;
    private ArrayList<String> allTags;

    private int selectedRecipePosition = -1;
    private Recipe selectedRecipe;
    private String searchRecipeName;
    private ArrayList<String> selectedTags;

    private ImageButton searchButton;
    private ImageButton clearSearchButton;
    private EditText searchRecipeNameEditText;
    private RecyclerView searchRecipeTegRecyclerView;
    private RecipeTagAdapter recipeTagAdapter;
    private RecipeListAdapter recipesListAdapter;
    private RecyclerView recipesRecycleView;


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
            selectedTags = savedInstanceState.getStringArrayList(RECIPE_SEARCH_TAGS_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipes_list, container, false);
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
        outState.putStringArrayList(RECIPE_SEARCH_TAGS_TAG, selectedTags);
    }

    private void initViews(View view) {
        searchRecipeNameEditText = (EditText) view.findViewById(R.id.fr_recipe_list_et_search_recipe_name);
        searchButton = (ImageButton) view.findViewById(R.id.fr_recipe_list_btn_search);
        clearSearchButton = (ImageButton) view.findViewById(R.id.fr_recipe_list_btn_clear_search);
        recipeListFAB = (FloatingActionButton) view.findViewById(R.id.fr_recipe_list_fab_recipe_list);

        allTags = MainActivity.myDiet.getAllTags();
        allRecipes = new ArrayList<Recipe>();

        if(selectedTags == null) {
            selectedTags = new ArrayList<String>();
        }

        if (searchRecipeName != null || !selectedTags.isEmpty()) {
            allRecipes.addAll(MainActivity.myDiet.filterRecipes(searchRecipeName, selectedTags));
            setSearchingState(true);
        }
        else {
            allRecipes.addAll(MainActivity.myDiet.getAllRecipes());
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
                allRecipes.clear();
                allRecipes.addAll(MainActivity.myDiet.filterRecipes(searchRecipeName, selectedTags));
                recipesListAdapter.notifyDataSetChanged();
                setSearchingState(true);
            }
        });

        clearSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchRecipeNameEditText.setText("");
                selectedTags.clear();
                for (int i = 0; i < allTags.size(); i++) {
                    recipeTagAdapter.setUnselectedItem(i);
                }
                allRecipes.clear();
                allRecipes.addAll(MainActivity.myDiet.getAllRecipes());
                recipesListAdapter.notifyDataSetChanged();
                setSearchingState(false);
            }
        });

        recipeListFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedRecipePosition = -1;

                // Rozpoczynamy transakcję fragmentu, aby przejść do fragmentu dziecka NewRecipeFragment
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.act_main_fragment_container_view, new NewRecipeFragment())
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
        searchRecipeTegRecyclerView = view.findViewById(R.id.fr_recipe_list_rv_search_recipe_tag);
        recipeTagAdapter = new RecipeTagAdapter(allTags, this, true);
        searchRecipeTegRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        searchRecipeTegRecyclerView.setAdapter(recipeTagAdapter);
    }

    private void initRecycleView(View view) {
        recipesRecycleView = view.findViewById(R.id.fr_recipe_list_rv_recipe_list);
        recipesListAdapter = new RecipeListAdapter(allRecipes, this, true);
        recipesRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipesRecycleView.setAdapter(recipesListAdapter);

        for (int i = 0; i < allTags.size(); i++) {
            if (selectedTags.contains(allTags.get(i))) {
                recipeTagAdapter.setSelectedItem(i);
            }
            else{
                recipeTagAdapter.setUnselectedItem(i);
            }
        }

        if (selectedRecipePosition != -1) {
            recipesRecycleView.scrollToPosition(selectedRecipePosition);
        }
    }

    @Override
    public void onRecipeClick(int position) {
        selectedRecipePosition = position;
        selectedRecipe = allRecipes.get(position);
        showRecipe(position);
    }

    @Override
    public void onRecipeLongClick(int position, View v) {
    }

    @Override
    public void onRecipeDeleteClick(int position) {
        if (position != RecyclerView.NO_POSITION)
        {
            Recipe recipeToDelete = allRecipes.get(position);
            allRecipes.remove(position);
            MainActivity.myDiet.getAllRecipes().remove(recipeToDelete);
            recipesListAdapter.notifyDataSetChanged();
        }
    }

    private void showRecipe(int position) {
        Recipe clickedRecipe = allRecipes.get(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable(NewRecipeFragment.RECIPE_PRESENTATION_TAG, clickedRecipe);      // Przekazanie obiektu serializowalnego

        NewRecipeFragment newRecipeFragment = new NewRecipeFragment();
        newRecipeFragment.setArguments(bundle); // Ustawienie argumentów

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.act_main_fragment_container_view, newRecipeFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onRecipeTagClick(int position, View view) {
        if (!isSearchingState) {
            if (!selectedTags.contains(allTags.get(position))) {
                recipeTagAdapter.setSelectedItem(position);
                // Usuwa zaznaczenie
                selectedTags.add(allTags.get(position));
            } else {
                recipeTagAdapter.setUnselectedItem(position);
                selectedTags.remove(allTags.get(position));
            }
        }
    }

    @Override
    public void onRecipeTagLongClick(int position, View view) {

    }
}