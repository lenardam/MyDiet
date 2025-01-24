package com.lenardam.mydiet.model;

import java.io.Serializable;

public class ShoppingItem implements Serializable {
    RecipeIngredient ingredient_to_buy;
    boolean is_bought;

    public ShoppingItem(RecipeIngredient ingredient_to_buy, boolean is_bought) {
        this.ingredient_to_buy = ingredient_to_buy;
        this.is_bought = is_bought;
    }

    public ShoppingItem() {
        this.ingredient_to_buy = new RecipeIngredient();
        this.is_bought = false;
    }

    public RecipeIngredient getIngredient_to_buy() {
        return ingredient_to_buy;
    }

    public void setIngredient_to_buy(RecipeIngredient ingredient_to_buy) {
        this.ingredient_to_buy = ingredient_to_buy;
    }

    public boolean isIs_bought() {
        return is_bought;
    }

    public void setIs_bought(boolean is_bought) {
        this.is_bought = is_bought;
    }
}
