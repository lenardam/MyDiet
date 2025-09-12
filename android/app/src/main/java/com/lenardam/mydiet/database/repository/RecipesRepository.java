package com.lenardam.mydiet.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.MyDietDatabase;
import com.lenardam.mydiet.database.dao.RecipeIngredientsDao;
import com.lenardam.mydiet.database.dao.RecipeInstructionsDao;
import com.lenardam.mydiet.database.dao.RecipeTagsDao;
import com.lenardam.mydiet.database.dao.RecipesDao;
import com.lenardam.mydiet.database.dao.TagsDao;
import com.lenardam.mydiet.database.dao.UnitsDao;
import com.lenardam.mydiet.database.model.RecipeFullData;
import com.lenardam.mydiet.database.model.RecipeIngredients;
import com.lenardam.mydiet.database.model.RecipeInstructions;
import com.lenardam.mydiet.database.model.RecipeTags;
import com.lenardam.mydiet.database.model.Recipes;
import com.lenardam.mydiet.database.model.Tags;
import com.lenardam.mydiet.database.model.Units;
import com.lenardam.mydiet.model.RecipeIngredient;

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
    private TagsDao tagsDao;
    private UnitsDao unitsDao;
    private LiveData<List<Recipes>> allRecipes;
    private LiveData<List<RecipeFullData>> allRecipesFullData;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public RecipesRepository(Application application) {
        database = MyDietDatabase.getInstance(application);
        recipesDao = database.recipesDao();
        recipeIngredientsDao = database.recipeIngredientsDao();
        recipeInstructionsDao = database.recipeInstructionsDao();
        recipeTagsDao = database.recipeTagsDao();
        allRecipes = recipesDao.getAllRecipes();
        unitsDao = database.unitsDao();
        tagsDao = database.tagsDao();
        allRecipesFullData = recipesDao.getRecipesFullData();
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

    public LiveData<RecipeFullData> getRecipeFullDataByRecipeId(long recipeId) {
        return recipesDao.getRecipeWithDetails(recipeId);
    }

    public LiveData<List<RecipeFullData>> getRecipesFullData() {
        return allRecipesFullData;
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

    public void updateRecipeWithIngredientsInstructionsTags(Recipes recipe, List<RecipeIngredients> ingredients, List<RecipeInstructions> instructions, List<RecipeTags> newTags, List<RecipeTags> deletedTags) {
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

                for (RecipeTags tag : newTags) {
                    tag.setRecipeId(recipeId);
                    recipeTagsDao.insert(tag);
                }

                for (RecipeTags tag : deletedTags) {
                    tag.setRecipeId(recipeId);
                    recipeTagsDao.delete(tag);
                }

            });
        });
    }

    public void loadRecipeWithIngredientsInstructionsTags(Recipes recipe, List<RecipeIngredient> ingredients, List<String> instructions, List<String> tags) {
        executorService.execute(() -> {
            // Room nie pozwala na @Transaction między różnymi DAO, ale możesz użyć transakcji bazy ręcznie

            database.runInTransaction(() -> {
                Long recipeId = recipesDao.insert(recipe);

                for (int i=0; i<ingredients.size(); i++) {
                    String ingredientName = ingredients.get(i).getName();
                    Double ingredientAmount = ingredients.get(i).getAmount();
                    Units unit = unitsDao.getUnitByName(ingredientName);
                    Long ingredientUnitId;

                    if (unit == null) {
                        unit = new Units(ingredients.get(i).getUnit());
                        ingredientUnitId = unitsDao.insert(unit);
                    }
                    else {
                        ingredientUnitId = unit.getUnitId();
                    }

                    RecipeIngredients ingredient = new RecipeIngredients(recipeId, ingredientName, ingredientAmount, ingredientUnitId);
                    recipeIngredientsDao.insert(ingredient);
                }

                for (int i=0; i<instructions.size(); i++) {
                    String instructionStep = instructions.get(i);

                    RecipeInstructions instruction = new RecipeInstructions(recipeId, instructionStep);
                    recipeInstructionsDao.insert(instruction);
                }

                for (int i=0; i<tags.size(); i++) {
                    String tagName = tags.get(i);
                    Tags tag = tagsDao.getTagByName(tagName);
                    Long tagId;

                    if (tag == null) {
                        tag = new Tags(tagName);
                        tagId = tagsDao.insert(tag);
                    }
                    else {
                        tagId = tag.getTagId();
                    }

                    RecipeTags recipeTag = new RecipeTags(recipeId, tagId);
                    recipeTagsDao.insert(recipeTag);
                }


            });
        });

    }

}
