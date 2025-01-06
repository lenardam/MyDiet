package com.lenardam.mydiet.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class DietPlan implements Serializable {

    private LocalDate diet_plan_date;
    private Integer number_of_meals;
    private ArrayList<Meal> meals;

    public DietPlan(LocalDate diet_plan_date, Integer number_of_meals, ArrayList<Meal> meals) {
        this.diet_plan_date = diet_plan_date;
        this.number_of_meals = number_of_meals;
        if (meals != null) {
            this.meals = meals;
        }
        else {
            this.meals = new ArrayList<Meal>();
            for (int i = 0; i < number_of_meals; i++) {
                this.meals.add(new Meal());
            }
        }
    }

    public LocalDate getDiet_plan_date() {
        return diet_plan_date;
    }

    public void setDiet_plan_date(LocalDate diet_plan_date) {
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
