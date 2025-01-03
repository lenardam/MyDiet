package com.lenardam.mydiet.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ShoppingList implements Serializable {

    private ArrayList<ShoppingItem> shopping_items_to_buy;
    private Date date_start;
    private Date date_end;

    public ShoppingList() {
        this.shopping_items_to_buy = new ArrayList<ShoppingItem>();
        this.date_start = new Date();
        this.date_end = new Date();
    }

    public ShoppingList(Date date_start, Date date_end) {
        this.shopping_items_to_buy = new ArrayList<ShoppingItem>();
        this.date_start = date_start;
        this.date_end = date_end;
    }

    public ShoppingList(ArrayList<ShoppingItem> ingredient_to_buy, Date date_start, Date date_end) {
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

    public void addIngredientToBuy(ShoppingItem shoppingItem) {
        boolean exists = false;

        for (int i = 0; i < shopping_items_to_buy.size(); i++) {
            ShoppingItem existingIngredient = shopping_items_to_buy.get(i);

            // Sprawdź, czy nazwa i jednostka się zgadzają
            if (existingIngredient.getIngredient_to_buy().getName().equals(shoppingItem.getIngredient_to_buy().getName()) && existingIngredient.getIngredient_to_buy().getUnit().equals(shoppingItem.getIngredient_to_buy().getUnit())) {
                // Zwiększ ilość istniejącego składnika
                existingIngredient.getIngredient_to_buy().setAmount(existingIngredient.getIngredient_to_buy().getAmount() + shoppingItem.getIngredient_to_buy().getAmount());
                exists = true;
                break;
            }
        }

        // Jeśli składnik nie istnieje w liście, dodaj go jako nowy
        if ( exists == false) {
            shopping_items_to_buy.add(shoppingItem);
        }
    }

}
