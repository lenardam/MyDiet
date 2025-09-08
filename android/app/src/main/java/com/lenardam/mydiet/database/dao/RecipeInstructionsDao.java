package com.lenardam.mydiet.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.lenardam.mydiet.database.model.RecipeInstructions;

import java.util.List;

@Dao
public interface RecipeInstructionsDao {

    @Insert
    void insert(RecipeInstructions recipeInstruction);

    @Update
    void update(RecipeInstructions recipeInstruction);

    @Delete
    void delete(RecipeInstructions recipeInstruction);

    @Query("SELECT * FROM recipe_instructions WHERE recipeId = :recipeId")
    LiveData<List<RecipeInstructions>> getRecipeInstructionsByRecipeId(Long recipeId);

}
