package com.lenardam.mydiet.database.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.model.RecipeIngredients;
import com.lenardam.mydiet.database.model.RecipeIngredientsFullData;
import com.lenardam.mydiet.database.repository.RecipeIngredientsRepository;

import java.util.List;

public class RecipeIngredientsViewModel extends AndroidViewModel {

    private RecipeIngredientsRepository recipeIngredientsRepository;

    public RecipeIngredientsViewModel(@NonNull Application application) {
        super(application);

        recipeIngredientsRepository = new RecipeIngredientsRepository(application);
    }

    public void insert(RecipeIngredients recipeIngredient) {
        recipeIngredientsRepository.insert(recipeIngredient);
    }

    public void update(RecipeIngredients recipeIngredient) {
        recipeIngredientsRepository.update(recipeIngredient);
    }

    public void delete(RecipeIngredients recipeIngredient) {
        recipeIngredientsRepository.delete(recipeIngredient);
    }

    public LiveData<List<RecipeIngredientsFullData>> getRecipeIngredientsByRecipeId(Long recipeId) {
        return recipeIngredientsRepository.getRecipeIngredientsByRecipeId(recipeId);
    }

}
