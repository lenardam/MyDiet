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
    private int recipeIngredientId;

    private int recipeId; //FK do tabeli Recipes.class
    private String name;
    private double amount;
    private int unitId; //FK do tabeli Units.class

    public RecipeIngredients(int recipeId, String name, double amount, int unitId) {
        this.recipeId = recipeId;
        this.name = name;
        this.amount = amount;
        this.unitId = unitId;
    }

    public int getRecipeIngredientId() {
        return recipeIngredientId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setRecipeIngredientId(int recipeIngredientId) {
        this.recipeIngredientId = recipeIngredientId;
    }
}
