package com.lenardam.mydiet.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.lenardam.mydiet.database.model.RecipeIngredients;

import java.util.List;

@Dao
public interface RecipeIngredientsDao {

    @Insert
    void insert(RecipeIngredients recipeIngredient);

    @Update
    void update(RecipeIngredients recipeIngredient);

    @Delete
    void delete(RecipeIngredients recipeIngredient);

    @Query("SELECT * FROM recipe_ingredients WHERE recipeId = :recipeId")
    LiveData<List<RecipeIngredients>> getRecipeIngredientsByRecipeId(Long recipeId);

}
