package com.lenardam.mydiet.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ShoppingList implements Serializable {

    private ArrayList<RecipeIngredient> ingredient_to_buy;
    private Date date_start;
    private Date date_end;

    public ShoppingList() {
        this.ingredient_to_buy = new ArrayList<RecipeIngredient>();
        this.date_start = new Date();
        this.date_end = new Date();
    }

    public ShoppingList(Date date_start, Date date_end) {
        this.ingredient_to_buy = new ArrayList<RecipeIngredient>();
        this.date_start = date_start;
        this.date_end = date_end;
    }

    public ShoppingList(ArrayList<RecipeIngredient> ingredient_to_buy, Date date_start, Date date_end, ArrayList<RecipeIngredient> ingredient_bought) {
        this.ingredient_to_buy = ingredient_to_buy;
        this.date_start = date_start;
        this.date_end = date_end;
    }

    public ArrayList<RecipeIngredient> getIngredient_to_buy() {
        return ingredient_to_buy;
    }

    public void setIngredient_to_buy(ArrayList<RecipeIngredient> ingredient_to_buy) {
        this.ingredient_to_buy = ingredient_to_buy;
    }

    public Date getDate_start() {
        return date_start;
    }

    public void setDate_start(Date date_start) {
        this.date_start = date_start;
    }

    public Date getDate_end() {
        return date_end;
    }

    public void setDate_end(Date date_end) {
        this.date_end = date_end;
    }

    public void addIngredientToBuy(RecipeIngredient ingredient) {
        boolean exists = false;

        for (int i = 0; i < ingredient_to_buy.size(); i++) {
            RecipeIngredient existingIngredient = ingredient_to_buy.get(i);

            // Sprawdź, czy nazwa i jednostka się zgadzają
            if (existingIngredient.getName().equals(ingredient.getName()) && existingIngredient.getUnit().equals(ingredient.getUnit())) {
                // Zwiększ ilość istniejącego składnika
                existingIngredient.setAmount(existingIngredient.getAmount() + ingredient.getAmount());
                exists = true;
                break;
            }
        }

        // Jeśli składnik nie istnieje w liście, dodaj go jako nowy
        if ( exists == false) {
            ingredient_to_buy.add(ingredient);
        }
    }

}
