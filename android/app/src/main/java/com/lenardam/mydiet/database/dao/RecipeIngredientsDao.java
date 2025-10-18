package com.lenardam.mydiet.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.lenardam.mydiet.database.model.RecipeIngredients;
import com.lenardam.mydiet.database.model.RecipeIngredientsFullData;

import java.util.List;

@Dao
public interface RecipeIngredientsDao {

    @Insert
    void insert(RecipeIngredients recipeIngredient);

    @Update
    void update(RecipeIngredients recipeIngredient);

    @Delete
    void delete(RecipeIngredients recipeIngredient);

    @Transaction
    @Query("SELECT * FROM recipe_ingredients WHERE recipeId = :recipeId")
    LiveData<List<RecipeIngredientsFullData>> getRecipeIngredientsByRecipeId(Long recipeId);

}
