package com.lenardam.mydiet.database.repository;

import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.dao.RecipeIngredientsDao;
import com.lenardam.mydiet.database.model.RecipeIngredients;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeIngredientsRepository {

    private RecipeIngredientsDao recipeIngredientsDao;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public RecipeIngredientsRepository(RecipeIngredientsDao recipeIngredientsDao) {
        this.recipeIngredientsDao = recipeIngredientsDao;
    }

    public void insert(RecipeIngredients recipeIngredient) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recipeIngredientsDao.insert(recipeIngredient);
            }
        });

    }

    public void update(RecipeIngredients recipeIngredient) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recipeIngredientsDao.update(recipeIngredient);
            }
        });

    }

    public void delete(RecipeIngredients recipeIngredient) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recipeIngredientsDao.delete(recipeIngredient);
            }
        });

    }

    public LiveData<List<RecipeIngredients>> getRecipeIngredientsByRecipeId(int recipeId) {
        return recipeIngredientsDao.getRecipeIngredientsByRecipeId(recipeId);
    }

}
