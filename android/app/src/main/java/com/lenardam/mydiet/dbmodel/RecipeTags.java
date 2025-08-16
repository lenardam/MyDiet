package com.lenardam.mydiet.dbmodel;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "recipe_tags",
        primaryKeys = {"recipeId", "tagId"},
        foreignKeys = {
            @ForeignKey(
                entity = Recipes.class,
                parentColumns = "recipeId",
                childColumns = "recipeId",
                onDelete = ForeignKey.CASCADE
        ),  @ForeignKey(
                entity = Tags.class,
                parentColumns = "tagId",
                childColumns = "tagId",
                onDelete = ForeignKey.CASCADE
        )},
        indices = {
                @Index(value = "recipeId"),
                @Index(value = "tagId")
        }
)
public class RecipeTags {

    private int recipeId; //FK do tabeli Recipes.class
    private int tagId; //FK do tabeli Tags.class

    public RecipeTags(int recipeId, int tagId) {
        this.recipeId = recipeId;
        this.tagId = tagId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getTagId() {
        return tagId;
    }



}
