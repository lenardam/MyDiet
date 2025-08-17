package com.lenardam.mydiet.database.repository;

import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.dao.RecipeTagsDao;
import com.lenardam.mydiet.database.model.RecipeTags;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeTagsRepository {

    private RecipeTagsDao recipeTagsDao;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public RecipeTagsRepository(RecipeTagsDao recipeTagsDao) {
        this.recipeTagsDao = recipeTagsDao;
    }

    public void insert(RecipeTags recipeTag) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recipeTagsDao.insert(recipeTag);
            }
        });
    }

    public void update(RecipeTags recipeTag) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recipeTagsDao.update(recipeTag);
            }
        });
    }

    public void delete(RecipeTags recipeTag) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recipeTagsDao.delete(recipeTag);
            }
        });
    }

    public LiveData<List<RecipeTags>> getRecipeTagsByRecipeId(int recipeId) {
        return recipeTagsDao.getRecipeTagsByRecipeId(recipeId);
    }

    public LiveData<List<RecipeTags>> getRecipeTagsByTagList(List<Integer> tagIds) {
        return recipeTagsDao.getRecipeTagsByTagList(tagIds);
    }

}
