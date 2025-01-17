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
import android.widget.EditText;
import android.widget.ImageButton;

import com.lenardam.mydiet.adapters.RecipeListAdapter;
import com.lenardam.mydiet.adapters.RecipeTagAdapter;
import com.lenardam.mydiet.model.Recipe;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeChooseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeChooseFragment extends Fragment implements RecipeListAdapter.OnRecipeClickListener, RecipeTagAdapter.OnRecipeTagClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String RECIPE_CHOOSE_SELECTED_TAG = "RECIPE_CHOOSE_SELECTED_TAG";

    // TODO: Rename and change types of parameters
    private ArrayList<Recipe> all_recipes;
    private RecipeListAdapter recipes_adapter;
    private RecyclerView recipe_choose_recycle_view;
    private Recipe clickedRecipe;
    private Button save_button;
    private RecyclerView searchRecipeTegRecyclerView;
    private RecipeTagAdapter recipeTagAdapter;

    private ArrayList<String> all_tags;
    private ArrayList<String> selected_tags;
    private String searchRecipeName;
    private boolean isSearchingState;

    private EditText searchRecipeNameEditText;
    private ImageButton searchButton;
    private ImageButton clearSearchButton;

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
        initSearchTagRecycleView(view);
        initRecycleView(view);
        
    }

    private void initSearchTagRecycleView(View view) {
        searchRecipeTegRecyclerView = view.findViewById(R.id.searchRecipeTegRecyclerView);
        recipeTagAdapter = new RecipeTagAdapter(all_tags, this, true);
        searchRecipeTegRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        searchRecipeTegRecyclerView.setAdapter(recipeTagAdapter);
    }

    private void initViews(View view) {

        searchRecipeNameEditText = (EditText) view.findViewById(R.id.searchRecipeNameEditText);
        searchButton = (ImageButton) view.findViewById(R.id.searchButton);
        clearSearchButton = (ImageButton) view.findViewById(R.id.clearSearchButton);

        all_tags = MainActivity.myDiet.getAll_tags();
        all_recipes = new ArrayList<Recipe>();

        if (selected_tags == null) {
            selected_tags = new ArrayList<String>();
        }

        if (searchRecipeName != null || !selected_tags.isEmpty()) {
            all_recipes.addAll(MainActivity.myDiet.filterRecipes(searchRecipeName, selected_tags));
            setSearchingState(true);
        } else {
            all_recipes.addAll(MainActivity.myDiet.getAll_recipes());
            setSearchingState(false);
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

    private void initRecycleView(View view) {
        recipe_choose_recycle_view = view.findViewById(R.id.recipe_choose_recycle_view);
        recipes_adapter = new RecipeListAdapter(all_recipes, this, false);
        recipe_choose_recycle_view.setLayoutManager(new LinearLayoutManager(getContext()));
        recipe_choose_recycle_view.setAdapter(recipes_adapter);
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



    @Override
    public void onRecipeClick(int position) {
        clickedRecipe = all_recipes.get(position);
        save_button.setEnabled(true);
    }

    @Override
    public void onRecipeLongClick(int position, View v) {

    }

    @Override
    public void onRecipeDeleteClick(int position) {

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