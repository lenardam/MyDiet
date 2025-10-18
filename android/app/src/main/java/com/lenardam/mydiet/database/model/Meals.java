package com.lenardam.mydiet.database.model;

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
                        onDelete = ForeignKey.SET_NULL
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
    private Long mealId;

    private Long dietPlanId; //FK do tabeli DietPlans.class
    private Long recipeId; //FK do tabeli Recipes.class
    private Integer mealPosition;
    private double portionOfRecipe;
    private boolean isEaten;
    private boolean isSkipped;

    public Meals(Long dietPlanId, Long recipeId, Integer mealPosition, double portionOfRecipe, boolean isEaten, boolean isSkipped) {
        this.dietPlanId = dietPlanId;
        this.recipeId = recipeId;
        this.mealPosition = mealPosition;
        this.portionOfRecipe = portionOfRecipe;
        this.isEaten = isEaten;
        this.isSkipped = isSkipped;
    }

    public Long getMealId() {
        return mealId;
    }

    public Long getDietPlanId() {
        return dietPlanId;
    }

    public void setDietPlanId(Long dietPlanId) {
        this.dietPlanId = dietPlanId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public double getPortionOfRecipe() {
        return portionOfRecipe;
    }

    public boolean isEaten() {
        return isEaten;
    }

    public void setEaten(boolean eaten) {
        isEaten = eaten;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    public Integer getMealPosition() {
        return mealPosition;
    }

    public void setMealPosition(Integer mealPosition) {
        this.mealPosition = mealPosition;
    }

    public boolean isSkipped() {
        return isSkipped;
    }

    public void setSkipped(boolean skipped) {
        isSkipped = skipped;
    }

    public void setPortionOfRecipe(double portionOfRecipe) {
        this.portionOfRecipe = portionOfRecipe;
    }
}
