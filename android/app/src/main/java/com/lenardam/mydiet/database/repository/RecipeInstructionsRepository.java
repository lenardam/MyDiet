package com.lenardam.mydiet.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.MyDietDatabase;
import com.lenardam.mydiet.database.dao.RecipeInstructionsDao;
import com.lenardam.mydiet.database.model.RecipeInstructions;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeInstructionsRepository {

    private MyDietDatabase database;
    private RecipeInstructionsDao recipeInstructionsDao;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public RecipeInstructionsRepository(Application application) {
        database = MyDietDatabase.getInstance(application);
        recipeInstructionsDao = database.recipeInstructionsDao();
    }

    public void insert(RecipeInstructions recipeInstruction) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recipeInstructionsDao.insert(recipeInstruction);
            }
        });

    }

    public void update(RecipeInstructions recipeInstruction) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recipeInstructionsDao.update(recipeInstruction);
            }
        });

    }

    public void delete(RecipeInstructions recipeInstruction) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recipeInstructionsDao.delete(recipeInstruction);
            }
        });

    }

    public LiveData<List<RecipeInstructions>> getRecipeInstructionsByRecipeId(Long recipeId) {
        return recipeInstructionsDao.getRecipeInstructionsByRecipeId(recipeId);
    }

}
