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
    private ArrayList<RecipeIngredient> ingredients;
    private ArrayList<String> instruction_steps;
    private ArrayList<String> tags;

    public Recipe(String name, Integer calories_amount, Integer protein_amount, Integer fat_amount, Integer carbs_amount, Integer serving_size, ArrayList<RecipeIngredient> ingredients, ArrayList<String> instruction_steps, ArrayList<String> tags) {
        this.name = name;
        this.calories_amount = calories_amount;
        this.protein_amount = protein_amount;
        this.fat_amount = fat_amount;
        this.carbs_amount = carbs_amount;
        this.serving_size = serving_size;

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

}
