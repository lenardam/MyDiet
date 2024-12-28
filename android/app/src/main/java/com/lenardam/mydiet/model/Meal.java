package com.lenardam.mydiet.model;

import java.io.Serializable;

public class Meal implements Serializable {
    private Recipe recipe;
    private Double portion_of_recipe;
    private Boolean is_eaten;

    public Meal(Recipe recipe, Double portion_of_recipe, Boolean is_eaten) {
        this.recipe = recipe;
        this.portion_of_recipe = portion_of_recipe;
        this.is_eaten = is_eaten;
    }

    public Meal() {
        this.recipe = null;
        this.portion_of_recipe = 1.0;
        this.is_eaten = false;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Double getPortion_of_recipe() {
        return portion_of_recipe;
    }

    public void setPortion_of_recipe(Double portion_of_recipe) {
        this.portion_of_recipe = portion_of_recipe;
    }

    public Boolean getIs_eaten() {
        return is_eaten;
    }

    public void setIs_eaten(Boolean is_eaten) {
        this.is_eaten = is_eaten;
    }
}
