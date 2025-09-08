package com.lenardam.mydiet.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.MyDietDatabase;
import com.lenardam.mydiet.database.dao.RecipeIngredientsDao;
import com.lenardam.mydiet.database.model.RecipeIngredients;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeIngredientsRepository {

    private RecipeIngredientsDao recipeIngredientsDao;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public RecipeIngredientsRepository(Application application) {
        MyDietDatabase database = MyDietDatabase.getInstance(application);
        recipeIngredientsDao = database.recipeIngredientsDao();
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

    public LiveData<List<RecipeIngredients>> getRecipeIngredientsByRecipeId(Long recipeId) {
        return recipeIngredientsDao.getRecipeIngredientsByRecipeId(recipeId);
    }

}
