package com.lenardam.mydiet.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable {
    private String name;
    private Integer calories_amount;
    private Integer protein_amount;
    private Integer fat_amount;
    private Integer carbs_amount;
    private Integer serving_size;
    private boolean is_favorite;
    private ArrayList<RecipeIngredient> ingredients;
    private ArrayList<String> instruction_steps;
    private ArrayList<String> tags;

    public Recipe() {
        this.name = "";
        this.calories_amount = 0;
        this.protein_amount = 0;
        this.fat_amount = 0;
        this.carbs_amount = 0;
        this.serving_size = 0;
        this.is_favorite = false;
        this.ingredients = new ArrayList<>();
        this.instruction_steps = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    public Recipe(Recipe other) {
        this.name = other.name;
        this.calories_amount = other.calories_amount;
        this.protein_amount = other.protein_amount;
        this.fat_amount = other.fat_amount;
        this.carbs_amount = other.carbs_amount;
        this.serving_size = other.serving_size;
        this.is_favorite = other.is_favorite;

        // Głębokie kopiowanie list
        this.ingredients = new ArrayList<>(other.ingredients);
        this.instruction_steps = new ArrayList<>(other.instruction_steps);
        this.tags = new ArrayList<>(other.tags);
    }

    public Recipe(String name, Integer calories_amount, Integer protein_amount, Integer fat_amount, Integer carbs_amount, Integer serving_size, ArrayList<RecipeIngredient> ingredients, ArrayList<String> instruction_steps, ArrayList<String> tags) {
        this.name = name;
        this.calories_amount = calories_amount;
        this.protein_amount = protein_amount;
        this.fat_amount = fat_amount;
        this.carbs_amount = carbs_amount;
        this.serving_size = serving_size;
        this.is_favorite = false;

        if (ingredients != null) {
            this.ingredients = ingredients;
        }
        else {
            this.ingredients = new ArrayList<RecipeIngredient>();
        }
        if (instruction_steps != null) {
            this.instruction_steps = instruction_steps;
        }
        else {
            this.instruction_steps = new ArrayList<String>();
        }

        if (tags != null) {
            this.tags = tags;
        }
        else {
            this.tags = new ArrayList<String>();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCalories_amount() {
        return calories_amount;
    }

    public void setCalories_amount(Integer calories_amount) {
        this.calories_amount = calories_amount;
    }

    public Integer getProtein_amount() {
        return protein_amount;
    }

    public void setProtein_amount(Integer protein_amount) {
        this.protein_amount = protein_amount;
    }

    public Integer getFat_amount() {
        return fat_amount;
    }

    public void setFat_amount(Integer fat_amount) {
        this.fat_amount = fat_amount;
    }

    public Integer getCarbs_amount() {
        return carbs_amount;
    }

    public void setCarbs_amount(Integer carbs_amount) {
        this.carbs_amount = carbs_amount;
    }

    public Integer getServing_size() {
        return serving_size;
    }

    public void setServing_size(Integer serving_size) {
        this.serving_size = serving_size;
    }

    public ArrayList<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<String> getInstruction_steps() {
        return instruction_steps;
    }

    public boolean isIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
