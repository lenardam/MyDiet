package com.lenardam.mydiet.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.lenardam.mydiet.database.model.RecipeTags;

import java.util.List;

@Dao
public interface RecipeTagsDao {

    @Insert
    void insert(RecipeTags recipeTag);

    @Update
    void update(RecipeTags recipeTag);

    @Delete
    void delete(RecipeTags recipeTag);

    @Query("SELECT * FROM recipe_tags WHERE recipeId = :recipeId")
    LiveData<List<RecipeTags>> getRecipeTagsByRecipeId(int recipeId);

    @Query("SELECT * FROM recipe_tags WHERE tagId IN (:tagIds)")
    LiveData<List<RecipeTags>> getRecipeTagsByTagList(List<Integer> tagIds);


}
