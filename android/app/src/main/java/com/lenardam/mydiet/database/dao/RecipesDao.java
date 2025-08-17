package com.lenardam.mydiet.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.lenardam.mydiet.database.model.Recipes;

import java.util.List;

@Dao
public interface RecipesDao {

    @Insert
    void insert(Recipes recipe);

    @Update
    void update(Recipes recipe);

    @Delete
    void delete(Recipes recipe);

    @Query("SELECT * FROM recipes")
    LiveData<List<Recipes>> getAllRecipes();

    @Query("SELECT * FROM recipes WHERE recipeId = :recipeId")
    LiveData<Recipes> getRecipeById(int recipeId);

    @Query("SELECT * FROM recipes WHERE name LIKE :name")
    LiveData<List<Recipes>> getRecipesByName(String name);

}
