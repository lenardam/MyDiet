package com.lenardam.mydiet.utils;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipeToExport implements Serializable {
    private String name;
    private Integer caloriesAmount;
    private Integer proteinAmount;
    private Integer fatAmount;
    private Integer carbsAmount;
    private Double servingSize;
    private boolean isFavorite;
    private ArrayList<RecipeIngredientToExport> ingredients;
    private ArrayList<String> instructionSteps;
    private ArrayList<String> tags;

    public RecipeToExport() {
        this.name = "";
        this.caloriesAmount = 0;
        this.proteinAmount = 0;
        this.fatAmount = 0;
        this.carbsAmount = 0;
        this.servingSize = 1.0;
        this.isFavorite = false;
        this.ingredients = new ArrayList<>();
        this.instructionSteps = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    public RecipeToExport(RecipeToExport other) {
        this.name = other.name;
        this.caloriesAmount = other.caloriesAmount;
        this.proteinAmount = other.proteinAmount;
        this.fatAmount = other.fatAmount;
        this.carbsAmount = other.carbsAmount;
        this.servingSize = other.servingSize;
        this.isFavorite = other.isFavorite;

        // Głębokie kopiowanie list
        this.ingredients = new ArrayList<>(other.ingredients);
        this.instructionSteps = new ArrayList<>(other.instructionSteps);
        this.tags = new ArrayList<>(other.tags);
    }

    public RecipeToExport(String name, Integer caloriesAmount, Integer proteinAmount, Integer fatAmount, Integer carbsAmount, Double servingSize, ArrayList<RecipeIngredientToExport> ingredients, ArrayList<String> instructionSteps, ArrayList<String> tags) {
        this.name = name;
        this.caloriesAmount = caloriesAmount;
        this.proteinAmount = proteinAmount;
        this.fatAmount = fatAmount;
        this.carbsAmount = carbsAmount;
        this.servingSize = servingSize;
        this.isFavorite = false;

        if (ingredients != null) {
            this.ingredients = ingredients;
        }
        else {
            this.ingredients = new ArrayList<RecipeIngredientToExport>();
        }
        if (instructionSteps != null) {
            this.instructionSteps = instructionSteps;
        }
        else {
            this.instructionSteps = new ArrayList<String>();
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

    public Integer getCaloriesAmount() {
        return caloriesAmount;
    }

    public void setCaloriesAmount(Integer caloriesAmount) {
        this.caloriesAmount = caloriesAmount;
    }

    public Integer getProteinAmount() {
        return proteinAmount;
    }

    public void setProteinAmount(Integer proteinAmount) {
        this.proteinAmount = proteinAmount;
    }

    public Integer getFatAmount() {
        return fatAmount;
    }

    public void setFatAmount(Integer fatAmount) {
        this.fatAmount = fatAmount;
    }

    public Integer getCarbsAmount() {
        return carbsAmount;
    }

    public void setCarbsAmount(Integer carbsAmount) {
        this.carbsAmount = carbsAmount;
    }

    public Double getServingSize() {
        return servingSize;
    }

    public void setServingSize(Double servingSize) {
        this.servingSize = servingSize;
    }

    public ArrayList<RecipeIngredientToExport> getIngredients() {
        return ingredients;
    }

    public ArrayList<String> getInstructionSteps() {
        return instructionSteps;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
