package com.lenardam.mydiet.model;

import java.util.ArrayList;
import java.util.Date;

public class DietPlan {

    private Date diet_plan_date;
    private Integer number_of_meals;
    private ArrayList<Meal> meals;

    public DietPlan(Date diet_plan_date, Integer number_of_meals, ArrayList<Meal> meals) {
        this.diet_plan_date = diet_plan_date;
        this.number_of_meals = number_of_meals;
        this.meals = meals;
    }

    public Date getDiet_plan_date() {
        return diet_plan_date;
    }

    public void setDiet_plan_date(Date diet_plan_date) {
        this.diet_plan_date = diet_plan_date;
    }

    public Integer getNumber_of_meals() {
        return number_of_meals;
    }

    public void setNumber_of_meals(Integer number_of_meals) {
        this.number_of_meals = number_of_meals;
    }

    public ArrayList<Meal> getMeals() {
        return meals;
    }

    public void setMeals(ArrayList<Meal> meals) {
        this.meals = meals;
    }
}
