package com.lenardam.mydiet.model;

import java.io.Serializable;

public class DietSettings implements Serializable {
    private int number_of_meals_for_diet;
    private String[] units;
    private double[] meal_portion;

    public DietSettings() {
        this.number_of_meals_for_diet = 3;
        this.units = new String[]{"gram", "kilogram", "mililitr", "litr", "sztuk", "szczypta"};
        this.meal_portion = new double[]{0.25, 0.5, 1, 1.5, 2, 3, 4, 5};
    }

    public int getNumber_of_meals_for_diet() {
        return number_of_meals_for_diet;
    }

    public void setNumber_of_meals_for_diet(int number_of_meals_for_diet) {
        this.number_of_meals_for_diet = number_of_meals_for_diet;
    }
}