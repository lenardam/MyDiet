package com.lenardam.mydiet.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class ShoppingList implements Serializable {

    private ArrayList<ShoppingItem> shopping_items_to_buy;
    private LocalDate date_start;
    private LocalDate date_end;

    public ShoppingList() {
        this.shopping_items_to_buy = new ArrayList<ShoppingItem>();
        this.date_start = null;
        this.date_end = null;
    }

    public ShoppingList(ShoppingList other) {
        this.shopping_items_to_buy = new ArrayList<ShoppingItem>(other.shopping_items_to_buy);
        this.date_start = other.date_start;
        this.date_end = other.date_end;
    }

    public ShoppingList(LocalDate date_start, LocalDate date_end) {
        this.shopping_items_to_buy = new ArrayList<ShoppingItem>();
        this.date_start = date_start;
        this.date_end = date_end;
    }

    public ShoppingList(ArrayList<ShoppingItem> ingredient_to_buy, LocalDate date_start, LocalDate date_end) {
        this.shopping_items_to_buy = ingredient_to_buy;
        this.date_start = date_start;
        this.date_end = date_end;
    }

    public ArrayList<ShoppingItem> getIngredient_to_buy() {
        return shopping_items_to_buy;
    }

    public void setIngredient_to_buy(ArrayList<ShoppingItem> ingredient_to_buy) {
        this.shopping_items_to_buy = ingredient_to_buy;
    }

    public LocalDate getDate_start() {
        return date_start;
    }

    public void setDate_start(LocalDate date_start) {
        this.date_start = date_start;
    }

    public LocalDate getDate_end() {
        return date_end;
    }

    public void setDate_end(LocalDate date_end) {
        this.date_end = date_end;
    }

    public void addIngredientToBuy(ShoppingItem shoppingItem, double recipe_serving_size, double meal_portion_of_recipe) {
        boolean exists = false;
        double portion_of_recipe_ingredient = meal_portion_of_recipe / recipe_serving_size;
        double ingredient_amount = shoppingItem.getIngredient_to_buy().getAmount() * portion_of_recipe_ingredient;

        for (int i = 0; i < shopping_items_to_buy.size(); i++) {
            ShoppingItem existingIngredient = shopping_items_to_buy.get(i);


            // Sprawdź, czy nazwa i jednostka się zgadzają
            if (existingIngredient.getIngredient_to_buy().getName().equals(shoppingItem.getIngredient_to_buy().getName()) && existingIngredient.getIngredient_to_buy().getUnit().equals(shoppingItem.getIngredient_to_buy().getUnit())) {
                // Zwiększ ilość istniejącego składnika
                existingIngredient.getIngredient_to_buy().setAmount(existingIngredient.getIngredient_to_buy().getAmount() + ingredient_amount);
                exists = true;
                break;
            }
        }

        // Jeśli składnik nie istnieje w liście, dodaj go jako nowy
        if ( exists == false) {
            shopping_items_to_buy.add(new ShoppingItem(
                    new RecipeIngredient(
                            shoppingItem.getIngredient_to_buy().getName(),
                            ingredient_amount,
                            shoppingItem.getIngredient_to_buy().getUnit()
                    ), false
            ));
        }
    }

}
