package com.lenardam.mydiet.database.model;

import androidx.annotation.NonNull;
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

    @NonNull
    private Long recipeId; //FK do tabeli Recipes.class
    @NonNull
    private Long tagId; //FK do tabeli Tags.class

    public RecipeTags(Long recipeId, Long tagId) {
        this.recipeId = recipeId;
        this.tagId = tagId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public Long getTagId() {
        return tagId;
    }



}
