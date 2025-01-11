package com.lenardam.mydiet.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Diet implements Serializable {

    private ArrayList<Recipe> all_recipes;
    private ArrayList<DietPlan> diet_plan;
    private Integer number_of_meals_for_diet;
    private ArrayList<String> all_tags;
    private ShoppingList shopping_list;

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

    public void set_diet_plan(ArrayList<DietPlan> diet_plan) {
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

    public ShoppingList getShopping_list() {
        return shopping_list;
    }

    public void setShopping_list(ShoppingList shopping_list) {
        this.shopping_list = new ShoppingList(shopping_list);
    }

    public DietPlan getDietPlan_for_date(LocalDate date) {
        for (int i = 0; i < diet_plan.size(); i++) {
            DietPlan dp = diet_plan.get(i);
            if (dp.getDiet_plan_date().equals(date)) {
                return dp;
            }
        }
        return null;
    }
}
