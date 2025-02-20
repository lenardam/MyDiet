package com.lenardam.mydiet.model;

import java.io.Serializable;

public class ShoppingItem implements Serializable {
    RecipeIngredient ingredientToBuy;
    boolean isBought;

    public ShoppingItem(RecipeIngredient ingredientToBuy, boolean isBought) {
        this.ingredientToBuy = ingredientToBuy;
        this.isBought = isBought;
    }

    public ShoppingItem() {
        this.ingredientToBuy = new RecipeIngredient();
        this.isBought = false;
    }

    public RecipeIngredient getIngredientToBuy() {
        return ingredientToBuy;
    }

    public void setIngredientToBuy(RecipeIngredient ingredientToBuy) {
        this.ingredientToBuy = ingredientToBuy;
    }

    public boolean isBought() {
        return isBought;
    }

    public void setBought(boolean bought) {
        this.isBought = bought;
    }
}
