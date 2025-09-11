package com.lenardam.mydiet.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.MyDietDatabase;
import com.lenardam.mydiet.database.dao.RecipeIngredientsDao;
import com.lenardam.mydiet.database.dao.RecipeInstructionsDao;
import com.lenardam.mydiet.database.dao.RecipeTagsDao;
import com.lenardam.mydiet.database.dao.RecipesDao;
import com.lenardam.mydiet.database.model.RecipeIngredients;
import com.lenardam.mydiet.database.model.RecipeInstructions;
import com.lenardam.mydiet.database.model.RecipeTags;
import com.lenardam.mydiet.database.model.Recipes;
import com.lenardam.mydiet.database.model.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class RecipesRepository {

    private MyDietDatabase database;
    private RecipesDao recipesDao;
    private RecipeIngredientsDao recipeIngredientsDao;
    private RecipeInstructionsDao recipeInstructionsDao;
    private RecipeTagsDao recipeTagsDao;
    private LiveData<List<Recipes>> allRecipes;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public RecipesRepository(Application application) {
        database = MyDietDatabase.getInstance(application);
        recipesDao = database.recipesDao();
        recipeIngredientsDao = database.recipeIngredientsDao();
        recipeInstructionsDao = database.recipeInstructionsDao();
        recipeTagsDao = database.recipeTagsDao();
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

    public LiveData<Recipes> getRecipeById(Long recipeId) {
        return recipesDao.getRecipeById(recipeId);
    }

    public LiveData<List<Recipes>> getRecipesByName(String name) {
        return recipesDao.getRecipesByName(name);
    }

    public LiveData<List<Recipes>> getFilteredRecipesByNameAndTags(String name, List<Tags> tags) {

        if (name == null && tags == null) {
            return allRecipes;
        }

        else {
            List<Long> tagsIds = new ArrayList<>();
            for (Tags tag : tags) {
                tagsIds.add(tag.getTagId());
            }

            return recipesDao.getFilteredRecipesByNameAndTags(name, tagsIds);
        }
    }


    public void insertRecipetWithIngredientsInstructionsTags(Recipes recipe, List<RecipeIngredients> ingredients, List<RecipeInstructions> instructions, List<RecipeTags> tags, Consumer<Long> callback) {
        executorService.execute(() -> {
            // Room nie pozwala na @Transaction między różnymi DAO, ale możesz użyć transakcji bazy ręcznie
            final long[] recipeIdHolder = new long[1];

            database.runInTransaction(() -> {
                Long recipeId = recipesDao.insert(recipe);
                recipeIdHolder[0] = recipeId;

                for (RecipeIngredients ingredient : ingredients) {
                    ingredient.setRecipeId(recipeId);
                    recipeIngredientsDao.insert(ingredient);
                }

                for (RecipeInstructions instruction : instructions) {
                    instruction.setRecipeId(recipeId);
                    recipeInstructionsDao.insert(instruction);
                }

                for (RecipeTags tag : tags) {
                    tag.setRecipeId(recipeId);
                    recipeTagsDao.insert(tag);
                }


            });
            callback.accept(recipeIdHolder[0]);
        });

    }

    public void updateRecipetWithIngredientsInstructionsTags(Recipes recipe, List<RecipeIngredients> ingredients, List<RecipeInstructions> instructions, List<RecipeTags> tags) {
        executorService.execute(() -> {
            // Room nie pozwala na @Transaction między różnymi DAO, ale możesz użyć transakcji bazy ręcznie
            database.runInTransaction(() -> {
                Long recipeId = recipe.getRecipeId();
                recipesDao.update(recipe);

                for (RecipeIngredients ingredient : ingredients) {
                    if (ingredient.getRecipeIngredientId() != null) {
                        ingredient.setRecipeId(recipeId);
                        recipeIngredientsDao.update(ingredient);
                    }
                    else {
                        ingredient.setRecipeId(recipeId);
                        recipeIngredientsDao.insert(ingredient);
                    }
                }

                for (RecipeInstructions instruction : instructions) {
                    if (instruction.getRecipeInstructionId() != null) {
                        instruction.setRecipeId(recipeId);
                        recipeInstructionsDao.update(instruction);
                    }
                    else {
                        instruction.setRecipeId(recipeId);
                        recipeInstructionsDao.insert(instruction);
                    }
                }

                for (RecipeTags tag : tags) {
                    if (tag.getTagId() != null) {
                        tag.setRecipeId(recipeId);
                        recipeTagsDao.update(tag);
                    }
                    else {
                        tag.setRecipeId(recipeId);
                        recipeTagsDao.insert(tag);
                    }
                }
            });
        });
    }

}
