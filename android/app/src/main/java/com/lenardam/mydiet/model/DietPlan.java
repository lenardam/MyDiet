package com.lenardam.mydiet.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class DietPlan implements Serializable {

    private LocalDate dietPlanDate;
    private Integer numberOfMeals;
    private ArrayList<Meal> meals;

    public DietPlan() {
        this.meals = new ArrayList<Meal>();
        this.dietPlanDate = LocalDate.now();
        this.numberOfMeals = 3;
    }

    public DietPlan(LocalDate dietPlanDate, Integer numberOfMeals, ArrayList<Meal> meals) {
        this.dietPlanDate = dietPlanDate;
        this.numberOfMeals = numberOfMeals;
        if (meals != null) {
            this.meals = meals;
        }
        else {
            this.meals = new ArrayList<Meal>();
            for (int i = 0; i < numberOfMeals; i++) {
                this.meals.add(new Meal());
            }
        }
    }

    public LocalDate getDietPlanDate() {
        return dietPlanDate;
    }

    public void setDietPlanDate(LocalDate dietPlanDate) {
        this.dietPlanDate = dietPlanDate;
    }

    public Integer getNumberOfMeals() {
        return numberOfMeals;
    }

    public void setNumberOfMeals(Integer numberOfMeals) {
        this.numberOfMeals = numberOfMeals;
    }

    public ArrayList<Meal> getMeals() {
        return meals;
    }

    public void setMeals(ArrayList<Meal> meals) {
        this.meals = meals;
    }
}
