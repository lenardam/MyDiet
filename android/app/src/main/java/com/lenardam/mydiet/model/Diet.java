package com.lenardam.mydiet.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Diet implements Serializable {

    private ArrayList<Recipe> all_recipes;
    private ArrayList<DietPlan> diet_plan;
    private Integer number_of_meals_for_diet;
    private ArrayList<String> all_tags;

    public Diet() {
        this.all_recipes = new ArrayList<Recipe>();
        this.diet_plan = new ArrayList<DietPlan>();
        this.all_tags = new ArrayList<String>();
        this.number_of_meals_for_diet = 3;
    }

    public Diet(ArrayList<Recipe> all_recipes, ArrayList<DietPlan> diet_plan, Integer number_of_meals_for_diet, ArrayList<String> all_tags) {
        if (all_recipes != null) {
            this.all_recipes = all_recipes;
        }
        else {
            this.all_recipes = new ArrayList<Recipe>();
        }

        if (diet_plan != null) {
            this.diet_plan = diet_plan;
        }
        else {
            this.diet_plan = new ArrayList<DietPlan>();
        }

        if (all_tags != null) {
            this.all_tags = all_tags;
        }
        else {
            this.all_tags = new ArrayList<String>();
        }

        this.number_of_meals_for_diet = number_of_meals_for_diet;
    }

    public ArrayList<Recipe> getAll_recipes() {
        return all_recipes;
    }

    public void setAll_recipes(ArrayList<Recipe> all_recipes) {
        this.all_recipes = all_recipes;
    }

    public ArrayList<DietPlan> getDiet_plan() {
        return diet_plan;
    }

    public void setDiet_plan(ArrayList<DietPlan> diet_plan) {
        this.diet_plan = diet_plan;
    }

    public Integer getNumber_of_meals_for_diet() {
        return number_of_meals_for_diet;
    }

    public void setNumber_of_meals_for_diet(Integer number_of_meals_for_diet) {
        this.number_of_meals_for_diet = number_of_meals_for_diet;
    }

    public ArrayList<String> getAll_tags() {
        return all_tags;
    }

    public void setAll_tags(ArrayList<String> all_tags) {
        this.all_tags = all_tags;
    }
}
