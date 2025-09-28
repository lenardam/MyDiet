package com.lenardam.mydiet.database.model;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes",
        indices = {
                @Index(value = "recipeId"),
                @Index(value = "name")
        })
public class Recipes {

    @PrimaryKey(autoGenerate = true)
    private Long recipeId;

    private String name;
    private int caloriesAmount;
    private int proteinAmount;
    private int fatAmount;
    private int carbsAmount;
    private double servingSize;
    private boolean isFavorite;
    private String pictureUrl;

    public Recipes(String name, int caloriesAmount, int proteinAmount, int fatAmount, int carbsAmount, double servingSize, boolean isFavorite, String pictureUrl) {
        this.name = name;
        this.caloriesAmount = caloriesAmount;
        this.proteinAmount = proteinAmount;
        this.fatAmount = fatAmount;
        this.carbsAmount = carbsAmount;
        this.servingSize = servingSize;
        this.isFavorite = isFavorite;
        this.pictureUrl = pictureUrl;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

    public int getCaloriesAmount() {
        return caloriesAmount;
    }

    public int getProteinAmount() {
        return proteinAmount;
    }

    public int getFatAmount() {
        return fatAmount;
    }

    public int getCarbsAmount() {
        return carbsAmount;
    }

    public double getServingSize() {
        return servingSize;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }
}
