package com.lenardam.mydiet.database.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lenardam.mydiet.database.model.RecipeIngredients;
import com.lenardam.mydiet.database.model.RecipeInstructions;
import com.lenardam.mydiet.database.model.RecipeTags;
import com.lenardam.mydiet.database.model.Recipes;
import com.lenardam.mydiet.database.model.Tags;
import com.lenardam.mydiet.database.repository.RecipesRepository;

import java.util.ArrayList;
import java.util.List;

public class RecipesViewModel extends AndroidViewModel {

    private RecipesRepository recipesRepository;
    private LiveData<List<Recipes>> allRecipes;
    private final MutableLiveData<Long> newRecipeId = new MutableLiveData<>();

    public RecipesViewModel(@NonNull Application application) {
        super(application);

        recipesRepository = new RecipesRepository(application);
        allRecipes = recipesRepository.getAllRecipes();
    }

    public void insert(Recipes recipe) {
        recipesRepository.insert(recipe);
    }

    public void update(Recipes recipe) {
        recipesRepository.update(recipe);
    }

    public void delete(Recipes recipe) {
        recipesRepository.delete(recipe);
    }

    public LiveData<List<Recipes>> getAllRecipes() {
        return allRecipes;
    }

    public LiveData<Recipes> getRecipeById(Long recipeId) {
        return recipesRepository.getRecipeById(recipeId);
    }

    public LiveData<List<Recipes>> getRecipesByName(String name) {
        return recipesRepository.getRecipesByName(name);
    }

    public LiveData<List<Recipes>> getFilteredRecipesByNameAndTags(String name, List<Tags> tags) {
        return recipesRepository.getFilteredRecipesByNameAndTags(name, tags);
    }

    // metoda do wstawienia Przepis wraz z jego składnikami, instrukcjami i tagami
    public void insertRecipeWithIngredientsInstructionsTags(Recipes recipe, List<RecipeIngredients> ingredients, List<RecipeInstructions> instructions, List<RecipeTags> tags) {
        recipesRepository.insertRecipetWithIngredientsInstructionsTags(recipe, ingredients, instructions, tags, recipeId -> {
            // Przekazujemy ID do LiveData, żeby Fragment mógł je obserwować
            newRecipeId.postValue(recipeId);
        });
    }

    // metoda do aktualizuje Przepis wraz z jego składnikami, instrukcjami i tagami
    public void updateRecipeWithIngredientsInstructionsTags(Recipes recipe, List<RecipeIngredients> ingredients, List<RecipeInstructions> instructions, List<RecipeTags> tags) {
        recipesRepository.updateRecipetWithIngredientsInstructionsTags(recipe, ingredients, instructions, tags);
    }

}
