package com.lenardam.mydiet.database.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class RecipeFullData {

    @Embedded
    public Recipes recipe;

    @Relation(
            parentColumn = "recipeId",
            entityColumn = "recipeId",
            entity = RecipeIngredients.class
    )
    public List<RecipeIngredientsFullData> ingredients;

    @Relation(
            parentColumn = "recipeId",
            entityColumn = "recipeId",
            entity = RecipeInstructions.class
    )
    public List<RecipeInstructions> instructions;

    @Relation(
            parentColumn = "recipeId",
            entityColumn = "recipeId",
            entity = RecipeTags.class
    )
    public List<RecipeTags> tags;

}
