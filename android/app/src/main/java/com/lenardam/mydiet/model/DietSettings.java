package com.lenardam.mydiet.model;

import java.io.Serializable;

public class DietSettings implements Serializable {
    private int numberOfMealsForDiet;

    public DietSettings() {
        this.numberOfMealsForDiet = 3;
    }

    public int getNumberOfMealsForDiet() {
        return numberOfMealsForDiet;
    }

    public void setNumberOfMealsForDiet(int numberOfMealsForDiet) {
        this.numberOfMealsForDiet = numberOfMealsForDiet;
    }
}