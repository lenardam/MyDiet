package com.lenardam.mydiet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.lenardam.mydiet.adapters.RecipeListAdapter;
import com.lenardam.mydiet.adapters.RecipeTagAdapter;
import com.lenardam.mydiet.database.model.Recipes;
import com.lenardam.mydiet.database.model.Tags;
import com.lenardam.mydiet.database.viewModel.RecipesViewModel;
import com.lenardam.mydiet.database.viewModel.TagsViewModel;
import com.lenardam.mydiet.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipesListFragment extends Fragment {

    public static final String ADDED_RECIPE_KEY_TAG = "ADDED_RECIPE_KEY_TAG";
    public static final String EDITED_RECIPE_KEY_TAG = "EDITED_RECIPE_KEY_TAG";
    private static final String RECIPE_SELECTED_POSITION_TAG = "RECIPE_SELECTED_POSITION_TAG";
    private static final String RECIPE_SELECTED_TAG = "RECIPE_SELECTED_TAG";
    private static final String RECIPE_SEARCH_NAME_TAG = "RECIPE_SEARCH_NAME_TAG";
    private static final String RECIPE_SEARCH_TAGS_TAG = "RECIPE_SEARCH_TAGS_TAG";

    private List<Recipes> allRecipes = new ArrayList<Recipes>();
    private List<Tags> allTags = new ArrayList<Tags>();

    private int selectedRecipePosition = -1;
    private Recipes selectedRecipe;
    private String searchRecipeName = "";
    private ArrayList<Tags> selectedTags = new ArrayList<Tags>();

    private EditText searchRecipeNameEditText;
    private RecyclerView searchRecipeTegRecyclerView;
    private RecipeTagAdapter recipeTagAdapter;
    private RecipeListAdapter recipesListAdapter;
    private RecyclerView recipesRecycleView;


    private boolean isSearchingState;
    private FloatingActionButton recipeListFAB;
    private TextInputLayout searchRecipeNameTextInputLayout;

    private TagsViewModel tagsViewModel;
    private RecipesViewModel recipesViewModel;

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
            //selectedRecipe = (Recipe) savedInstanceState.getSerializable(RECIPE_SELECTED_POSITION_TAG);
            searchRecipeName = savedInstanceState.getString(RECIPE_SEARCH_NAME_TAG);
            //selectedTags = savedInstanceState.getStringArrayList(RECIPE_SEARCH_TAGS_TAG);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setBottomNavigationItem(R.id.menu_bottom_item_recipe_list_fragment);
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
        //outState.putSerializable(RECIPE_SELECTED_POSITION_TAG, selectedRecipe);
        outState.putString(RECIPE_SEARCH_NAME_TAG, searchRecipeName);
        //outState.putStringArrayList(RECIPE_SEARCH_TAGS_TAG, selectedTags);
    }

    private void initViews(View view) {
        searchRecipeNameTextInputLayout = (TextInputLayout) view.findViewById(R.id.fr_recipe_list_il_search_recipe_name);
        searchRecipeNameEditText = (EditText) view.findViewById(R.id.fr_recipe_list_et_search_recipe_name);
        recipeListFAB = (FloatingActionButton) view.findViewById(R.id.fr_recipe_list_fab_recipe_list);

//        allTags = MainActivity.myDiet.getAllTags();
//        allRecipes = new ArrayList<Recipe>();

//        if(selectedTags == null) {
//            selectedTags = new ArrayList<String>();
//        }

        if (!searchRecipeName.isEmpty() || !selectedTags.isEmpty()) {
            //allRecipes.addAll(MainActivity.myDiet.filterRecipes(searchRecipeName, selectedTags));
            setSearchingState(true);
        }
        else {
            //allRecipes.addAll(MainActivity.myDiet.getAllRecipes());
            setSearchingState(false);
        }

        if(!searchRecipeName.isEmpty()){
            searchRecipeNameEditText.setText(searchRecipeName);
            setSearchingState(true);
        }

        searchRecipeNameTextInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!searchRecipeNameEditText.getText().toString().isEmpty()) {
                    if (!isSearchingState) {
                        searchRecipeName = String.valueOf(searchRecipeNameEditText.getText());
                        setFilteredRecipes();
                        searchRecipeNameTextInputLayout.setEndIconDrawable(R.drawable.ic_clear);
                        setSearchingState(true);
                    } else {
                        searchRecipeNameEditText.setText("");
                        searchRecipeName = String.valueOf(searchRecipeNameEditText.getText());
                        setFilteredRecipes();
                        searchRecipeNameTextInputLayout.setEndIconDrawable(R.drawable.ic_search);
                        setSearchingState(false);
                    }
                }
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

//    private void filterRecipes(String searchRecipeName, ArrayList<String> selectedTags) {
//        allRecipes.clear();
//        allRecipes.addAll(MainActivity.myDiet.filterRecipes(searchRecipeName, selectedTags));
//        recipesListAdapter.notifyDataSetChanged();
//
//    }

    private void setSearchingState(boolean inSearchingState) {
        isSearchingState = inSearchingState;

        if (inSearchingState) {
            searchRecipeNameEditText.setFocusable(false);
            searchRecipeNameEditText.setEnabled(false);
        }
        else {
            searchRecipeNameEditText.setFocusable(true);
            searchRecipeNameEditText.setFocusableInTouchMode(true);
            searchRecipeNameEditText.setEnabled(true);
        }

    }

    private void initSearchTagRecycleView(View view) {
        searchRecipeTegRecyclerView = view.findViewById(R.id.fr_recipe_list_rv_search_recipe_tag);
        searchRecipeTegRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recipeTagAdapter = new RecipeTagAdapter();
        recipeTagAdapter.setCanEdit(true);
        recipeTagAdapter.setOnRecipeTagClickListener(new RecipeTagAdapter.OnRecipeTagClickListener() {
            @Override
            public void onRecipeTagClick(int position, Tags tag, View view) {
                if (!selectedTags.contains(allTags.get(position))) {
                    recipeTagAdapter.setSelectedItem(position);
                    selectedTags.add(allTags.get(position));
                    setFilteredRecipes();
                } else {
                    recipeTagAdapter.setUnselectedItem(position);
                    selectedTags.remove(allTags.get(position));
                    setFilteredRecipes();
                }
            }

            @Override
            public void onRecipeTagLongClick(int position, Tags tag, View view) {

            }
        });

        searchRecipeTegRecyclerView.setAdapter(recipeTagAdapter);

        tagsViewModel = new ViewModelProvider(this).get(TagsViewModel.class);
        tagsViewModel.getAllTags().observe(getViewLifecycleOwner(), new Observer<List<Tags>>() {
            @Override
            public void onChanged(List<Tags> tags) {
                recipeTagAdapter.setTags(tags);
            }
        });
    }

    private void initRecycleView(View view) {
        recipesRecycleView = view.findViewById(R.id.fr_recipe_list_rv_recipe_list);
        recipesRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        recipesListAdapter = new RecipeListAdapter();
        recipesListAdapter.setOnRecipeClickListener(new RecipeListAdapter.OnRecipeClickListener() {
            @Override
            public void onRecipeClick(int position, Recipes recipe) {
                selectedRecipePosition = position;
                selectedRecipe = recipe;
                showRecipe(recipe);
            }

            @Override
            public void onRecipeLongClick(int position, Recipes recipe, View v) {
            }

            @Override
            public void onRecipeDeleteClick(int position, Recipes recipe) {
                if (position != RecyclerView.NO_POSITION)
                {
                    recipesViewModel.delete(recipe);
                }
            }
        });
        recipesListAdapter.setCanEdit(true);

        recipesRecycleView.setAdapter(recipesListAdapter);

        recipesViewModel = new ViewModelProvider(this).get(RecipesViewModel.class);
        recipesViewModel.getAllRecipes().observe(getViewLifecycleOwner(), new Observer<List<Recipes>>() {
            @Override
            public void onChanged(List<Recipes> recipes) {
                setFilteredRecipes();
            }
        });

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

    private void setFilteredRecipes() {
        recipesListAdapter.setRecipes(allRecipes);
    }


    private void showRecipe(Recipes recipe) {


        Bundle bundle = new Bundle();
        bundle.putLong(NewRecipeFragment.RECIPE_PRESENTATION_TAG, recipe.getRecipeId());      // Przekazanie obiektu serializowalnego

        NewRecipeFragment newRecipeFragment = new NewRecipeFragment();
        newRecipeFragment.setArguments(bundle); // Ustawienie argumentów

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.act_main_fragment_container_view, newRecipeFragment)
                .addToBackStack(null)
                .commit();
    }


}