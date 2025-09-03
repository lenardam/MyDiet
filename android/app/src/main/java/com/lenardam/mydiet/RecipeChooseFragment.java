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
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputLayout;
import com.lenardam.mydiet.adapters.RecipeListAdapter;
import com.lenardam.mydiet.adapters.RecipeTagAdapter;
import com.lenardam.mydiet.database.model.Tags;
import com.lenardam.mydiet.database.viewModel.TagsViewModel;
import com.lenardam.mydiet.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeChooseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeChooseFragment extends Fragment implements RecipeListAdapter.OnRecipeClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String RECIPE_CHOOSE_SELECTED_TAG = "RECIPE_CHOOSE_SELECTED_TAG";

    // TODO: Rename and change types of parameters
    private ArrayList<Recipe> allRecipes;
    private RecipeListAdapter recipesAdapter;
    private RecyclerView recipeChooseRecycleView;
    private Recipe clickedRecipe;
    private Button saveButton;
    private RecyclerView searchRecipeTegRecyclerView;
    private RecipeTagAdapter recipeTagAdapter;

    private ArrayList<String> allTags;
    private ArrayList<String> selectedTags;
    private String searchRecipeName = "";
    private boolean isSearchingState;

    private EditText searchRecipeNameEditText;
    private TextInputLayout searchRecipeNameTextInputLayout;

    private TagsViewModel tagsViewModel;

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
    public void onResume() {
        super.onResume();
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
            public void onRecipeTagClick(int position, View view) {
                if (!selectedTags.contains(allTags.get(position))) {
                    recipeTagAdapter.setSelectedItem(position);
                    selectedTags.add(allTags.get(position));
                    filterRecipes(searchRecipeName, selectedTags);
                } else {
                    recipeTagAdapter.setUnselectedItem(position);
                    selectedTags.remove(allTags.get(position));
                    filterRecipes(searchRecipeName, selectedTags);
                }
            }

            @Override
            public void onRecipeTagLongClick(int position, View view) {

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

        allTags = MainActivity.myDiet.getAllTags();
        allRecipes = new ArrayList<Recipe>();

        if (selectedTags == null) {
            selectedTags = new ArrayList<String>();
        }

        if (!searchRecipeName.isEmpty() || !selectedTags.isEmpty()) {
            allRecipes.addAll(MainActivity.myDiet.filterRecipes(searchRecipeName, selectedTags));
            setSearchingState(true);
        } else {
            allRecipes.addAll(MainActivity.myDiet.getAllRecipes());
            setSearchingState(false);
        }

        searchRecipeNameTextInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!searchRecipeNameEditText.getText().toString().isEmpty()) {
                    if (!isSearchingState) {
                        searchRecipeName = String.valueOf(searchRecipeNameEditText.getText());
                        filterRecipes(searchRecipeName, selectedTags);
                        searchRecipeNameTextInputLayout.setEndIconDrawable(R.drawable.ic_clear);
                        setSearchingState(true);
                    } else {
                        searchRecipeNameEditText.setText("");
                        searchRecipeName = String.valueOf(searchRecipeNameEditText.getText());
                        filterRecipes(searchRecipeName, selectedTags);
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
                        Bundle result = new Bundle();
                        result.putSerializable(RECIPE_CHOOSE_SELECTED_TAG, clickedRecipe);
                        getParentFragmentManager().setFragmentResult(DietFragment.DIET_RECIPE_CHOOSE_SELECTED_TAG, result);
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                }
        );
    }

    private void initRecycleView(View view) {
        recipeChooseRecycleView = view.findViewById(R.id.fr_recipe_choose_rv_recipe_choose);
        recipesAdapter = new RecipeListAdapter(allRecipes, this, false);
        recipeChooseRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeChooseRecycleView.setAdapter(recipesAdapter);
    }

    private void filterRecipes(String searchRecipeName, ArrayList<String> selectedTags) {
        allRecipes.clear();
        allRecipes.addAll(MainActivity.myDiet.filterRecipes(searchRecipeName, selectedTags));
        recipesAdapter.notifyDataSetChanged();

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



    @Override
    public void onRecipeClick(int position) {
        clickedRecipe = allRecipes.get(position);
        saveButton.setEnabled(true);
        saveButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorSecondary, null));
        recipesAdapter.setSelectedItem(position);
    }

    @Override
    public void onRecipeLongClick(int position, View v) {

    }

    @Override
    public void onRecipeDeleteClick(int position) {

    }


}