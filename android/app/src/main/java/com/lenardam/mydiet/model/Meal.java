package com.lenardam.mydiet.model;

import java.io.Serializable;

public class Meal implements Serializable {
    private Recipe recipe;
    private Double portionOfRecipe;
    private Boolean isEaten;

    public Meal(Recipe recipe, Double portionOfRecipe, Boolean isEaten) {
        this.recipe = recipe;
        this.portionOfRecipe = portionOfRecipe;
        this.isEaten = isEaten;
    }

    public Meal() {
        this.recipe = null;
        this.portionOfRecipe = 1.0;
        this.isEaten = false;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Double getPortionOfRecipe() {
        return portionOfRecipe;
    }

    public void setPortionOfRecipe(Double portionOfRecipe) {
        this.portionOfRecipe = portionOfRecipe;
    }

    public Boolean getIsEaten() {
        return isEaten;
    }

    public void setIsEaten(Boolean isEaten) {
        this.isEaten = isEaten;
    }
}
