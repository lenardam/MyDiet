package com.lenardam.mydiet.model;

import java.io.Serializable;

public class DietSettings implements Serializable {
    private int numberOfMealsForDiet;
    private String[] units;
    private double[] mealPortion;

    public DietSettings() {
        this.numberOfMealsForDiet = 3;
        this.units = new String[]{"gram", "kilogram", "mililitr", "litr", "sztuk", "szczypta"};
        this.mealPortion = new double[]{0.25, 0.5, 1, 1.5, 2, 3, 4, 5};
    }

    public int getNumberOfMealsForDiet() {
        return numberOfMealsForDiet;
    }

    public void setNumberOfMealsForDiet(int numberOfMealsForDiet) {
        this.numberOfMealsForDiet = numberOfMealsForDiet;
    }
}