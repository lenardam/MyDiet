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
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.lenardam.mydiet.adapters.RecipeListAdapter;
import com.lenardam.mydiet.adapters.RecipeTagAdapter;
import com.lenardam.mydiet.database.model.Meals;
import com.lenardam.mydiet.database.model.RecipeFullData;
import com.lenardam.mydiet.database.model.Recipes;
import com.lenardam.mydiet.database.model.Tags;
import com.lenardam.mydiet.database.viewModel.MealsViewModel;
import com.lenardam.mydiet.database.viewModel.RecipesViewModel;
import com.lenardam.mydiet.database.viewModel.TagsViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeChooseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeChooseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String RECIPE_CHOOSE_SELECTED_TAG = "RECIPE_CHOOSE_SELECTED_TAG";

    // TODO: Rename and change types of parameters
    private RecipeListAdapter recipesAdapter;
    private RecyclerView recipeChooseRecycleView;
    private Recipes clickedRecipe;
    private Long selectedMealId;
    private Meals selectedMeal;
    private Button saveButton;
    private RecyclerView searchRecipeTegRecyclerView;
    private RecipeTagAdapter recipeTagAdapter;

    private List<RecipeFullData> allRecipes = new ArrayList<RecipeFullData>();
    private List<Tags> allTags = new ArrayList<Tags>();
    private Map<Long, Tags> tagsMap = new HashMap<>();

    private List<Tags> selectedTags = new ArrayList<Tags>();
    private String searchRecipeName = "";
    private boolean isSearchingState;

    private EditText searchRecipeNameEditText;
    private TextInputLayout searchRecipeNameTextInputLayout;

    private TagsViewModel tagsViewModel;
    private RecipesViewModel recipesViewModel;
    private MealsViewModel mealsViewModel;

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
    public static RecipeChooseFragment newInstance(Long selectedMealId) {
        RecipeChooseFragment fragment = new RecipeChooseFragment();
        Bundle args = new Bundle();
        args.putLong(RECIPE_CHOOSE_SELECTED_TAG, selectedMealId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            selectedMealId = getArguments().getLong(RECIPE_CHOOSE_SELECTED_TAG);
        }
        ((MainActivity) requireActivity()).setBottomNavigationItem(R.id.menu_bottom_item_diet_fragment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_choose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //ustawienie odpowiedniego itemu w BottomNavigationView
        ((MainActivity) requireActivity()).setBottomNavigationItem(R.id.menu_bottom_item_diet_fragment);

        initViews(view);
        initSearchTagRecycleView(view);
        initRecycleView(view);
        
    }

    private void initSearchTagRecycleView(View view) {
        searchRecipeTegRecyclerView = view.findViewById(R.id.fr_recipe_choose_rv_search_recipe_tag);
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
            public void onRecipeTagLongClick(int position,Tags tag, View view) {

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

    private void initViews(View view) {
        searchRecipeNameTextInputLayout = (TextInputLayout) view.findViewById(R.id.fr_recipe_choose_il_search_recipe_name);
        searchRecipeNameEditText = (EditText) view.findViewById(R.id.fr_recipe_choose_et_search_recipe_name);

        mealsViewModel = new ViewModelProvider(requireActivity()).get(MealsViewModel.class);

        tagsViewModel = new ViewModelProvider(this).get(TagsViewModel.class);
        tagsViewModel.getAllTags().observe(getViewLifecycleOwner(), new Observer<List<Tags>>() {
            @Override
            public void onChanged(List<Tags> tags) {
                for(int i=0; i<tags.size(); i++){
                    tagsMap.put(tags.get(i).getTagId(), tags.get(i));
                }
                recipeTagAdapter.setTags(tags);
                allTags = tags;
            }
        });

        recipesViewModel = new ViewModelProvider(this).get(RecipesViewModel.class);
        recipesViewModel.getRecipesFullData().observe(getViewLifecycleOwner(), new Observer<List<RecipeFullData>>() {
            @Override
            public void onChanged(List<RecipeFullData> recipes) {
                allRecipes = recipes;
                setFilteredRecipes();
            }
        });

        if (!searchRecipeName.isEmpty() || !selectedTags.isEmpty()) {
            setSearchingState(true);
        } else {
            setSearchingState(false);
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

        saveButton = (Button) view.findViewById(R.id.fr_recipe_choose_btn_recipe_choose_save);
        saveButton.setEnabled(false);
        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Long clickedRecipeId = clickedRecipe.getRecipeId();

                        mealsViewModel.getMealById(selectedMealId).observe(getViewLifecycleOwner(), new Observer<Meals>() {
                            @Override
                            public void onChanged(Meals meals) {
                                selectedMeal = meals;
                                Meals newMeal = new Meals(selectedMeal.getDietPlanId(), clickedRecipe.getRecipeId(), 1.0, false);
                                newMeal.setMealId(selectedMeal.getMealId());
                                mealsViewModel.update(newMeal);

                                requireActivity().getSupportFragmentManager().popBackStack();

                            }
                        });



                    }
                }
        );
    }

    private void setFilteredRecipes() {

        List<Recipes> filteredRecipes = new ArrayList<>();

        for (int i = 0; i< allRecipes.size(); i++) {
            Recipes recipe = allRecipes.get(i).recipe;
            List<Tags> recipeTags = new ArrayList<>();
            for (int j = 0; j < allRecipes.get(i).tags.size(); j++) {
                recipeTags.add(tagsMap.get(allRecipes.get(i).tags.get(j).getTagId()));
            }

            boolean nameMatches = recipe.getName().toLowerCase().contains(searchRecipeName.toLowerCase());
            boolean tagsMatch = selectedTags.isEmpty() || recipeTags.containsAll(selectedTags);;

            if (nameMatches && tagsMatch) {
                filteredRecipes.add(recipe);
            }
        }

        recipesAdapter.setRecipes(filteredRecipes);
    }

    private void initRecycleView(View view) {
        recipeChooseRecycleView = view.findViewById(R.id.fr_recipe_choose_rv_recipe_choose);
        recipeChooseRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        recipesAdapter = new RecipeListAdapter();
        recipesAdapter.setOnRecipeClickListener(new RecipeListAdapter.OnRecipeClickListener() {
            @Override
            public void onRecipeClick(int position, Recipes recipe) {
                clickedRecipe = recipe;
                saveButton.setEnabled(true);
                saveButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorSecondary, null));
                recipesAdapter.setSelectedItem(position);
            }

            @Override
            public void onRecipeLongClick(int position, Recipes recipe, View v) {

            }

            @Override
            public void onRecipeDeleteClick(int position, Recipes recipe) {

            }
        });
        recipesAdapter.setCanEdit(false);
        recipeChooseRecycleView.setAdapter(recipesAdapter);
        recipesViewModel = new ViewModelProvider(this).get(RecipesViewModel.class);
    }

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

}