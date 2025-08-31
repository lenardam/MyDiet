package com.lenardam.mydiet.database.repository;

import android.app.Application;

import androidx.core.util.Consumer;
import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.MyDietDatabase;
import com.lenardam.mydiet.database.dao.RecipesDao;
import com.lenardam.mydiet.database.model.Recipes;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipesRepository {

    private RecipesDao recipesDao;
    private LiveData<List<Recipes>> allRecipes;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public RecipesRepository(Application application) {
        MyDietDatabase database = MyDietDatabase.getInstance(application);
        recipesDao = database.recipesDao();
        allRecipes = recipesDao.getAllRecipes();
    }

    public void insert(Recipes recipe) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recipesDao.insert(recipe);
            }
        });
    }

    public void update(Recipes recipe) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recipesDao.update(recipe);
            }
        });
    }

    public void delete(Recipes recipe) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recipesDao.delete(recipe);
            }
        });
    }

    public LiveData<List<Recipes>> getAllRecipes() {
        return allRecipes;
    }

    public LiveData<Recipes> getRecipeById(int recipeId) {
        return recipesDao.getRecipeById(recipeId);
    }

    public LiveData<List<Recipes>> getRecipesByName(String name) {
        return recipesDao.getRecipesByName(name);
    }


}
