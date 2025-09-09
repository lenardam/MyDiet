package com.lenardam.mydiet.database.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe_ingredients",
        foreignKeys = {
            @ForeignKey(
                            entity = Recipes.class,
                            parentColumns = "recipeId",
                            childColumns = "recipeId",
                            onDelete = ForeignKey.CASCADE
            ),
            @ForeignKey(
                            entity = Units.class,
                            parentColumns = "unitId",
                            childColumns = "unitId",
                            onDelete = ForeignKey.CASCADE
            ),
        },
        indices = {
                @Index(value = "recipeId"),
                @Index(value = "unitId")
        }
)
public class RecipeIngredients {

    @PrimaryKey(autoGenerate = true)
    private Long recipeIngredientId;

    private Long recipeId; //FK do tabeli Recipes.class
    private String name;
    private double amount;
    private Long unitId; //FK do tabeli Units.class

    public RecipeIngredients(Long recipeId, String name, double amount, Long unitId) {
        this.recipeId = recipeId;
        this.name = name;
        this.amount = amount;
        this.unitId = unitId;
    }

    public Long getRecipeIngredientId() {
        return recipeIngredientId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setRecipeIngredientId(Long recipeIngredientId) {
        this.recipeIngredientId = recipeIngredientId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }
}
