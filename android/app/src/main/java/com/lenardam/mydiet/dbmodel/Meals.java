package com.lenardam.mydiet.dbmodel;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "meals",
        foreignKeys = {
                @ForeignKey(
                        entity = Recipes.class,
                        parentColumns = "recipeId",
                        childColumns = "recipeId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = DietPlans.class,
                        parentColumns = "dietPlanId",
                        childColumns = "dietPlanId",
                        onDelete = ForeignKey.CASCADE
                ),
        },
        indices = {
                @Index(value = "recipeId"),
                @Index(value = "dietPlanId")
        })
public class Meals {

    @PrimaryKey(autoGenerate = true)
    private int mealId;

    private int dietPlanId; //FK do tabeli DietPlans.class
    private int recipeId; //FK do tabeli Recipes.class
    private double portionOfRecipe;
    private boolean isEaten;

    public Meals(int dietPlanId, int recipeId, double portionOfRecipe, boolean isEaten) {
        this.dietPlanId = dietPlanId;
        this.recipeId = recipeId;
        this.portionOfRecipe = portionOfRecipe;
        this.isEaten = isEaten;
    }

    public int getMealId() {
        return mealId;
    }

    public int getDietPlanId() {
        return dietPlanId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public double getPortionOfRecipe() {
        return portionOfRecipe;
    }

    public boolean isEaten() {
        return isEaten;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }
}
