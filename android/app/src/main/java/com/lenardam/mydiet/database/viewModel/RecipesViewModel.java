package com.lenardam.mydiet.database.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.model.Recipes;
import com.lenardam.mydiet.database.repository.RecipesRepository;

import java.util.List;

public class RecipesViewModel extends AndroidViewModel {

    private RecipesRepository recipesRepository;
    private LiveData<List<Recipes>> allRecipes;

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

}
