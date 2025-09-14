package com.lenardam.mydiet.utils;

import java.io.Serializable;

public class RecipeIngredientToExport implements Serializable {
    private String name;
    private Double amount;
    private String unit;

    public RecipeIngredientToExport() {
        this.name = "";
        this.amount = 0.0;
        this.unit = "";
    }

    public RecipeIngredientToExport(String name, Double amount, String unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
