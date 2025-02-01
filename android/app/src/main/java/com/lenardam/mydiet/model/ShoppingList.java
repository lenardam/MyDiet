package com.lenardam.mydiet.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class ShoppingList implements Serializable {

    private ArrayList<ShoppingItem> shoppingItemsToBuy;
    private LocalDate dateStart;
    private LocalDate dateEnd;

    public ShoppingList() {
        this.shoppingItemsToBuy = new ArrayList<ShoppingItem>();
        this.dateStart = null;
        this.dateEnd = null;
    }

    public ShoppingList(ShoppingList other) {
        this.shoppingItemsToBuy = new ArrayList<ShoppingItem>(other.shoppingItemsToBuy);
        this.dateStart = other.dateStart;
        this.dateEnd = other.dateEnd;
    }

    public ShoppingList(LocalDate dateStart, LocalDate dateEnd) {
        this.shoppingItemsToBuy = new ArrayList<ShoppingItem>();
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public ShoppingList(ArrayList<ShoppingItem> ingredientToBuy, LocalDate dateStart, LocalDate dateEnd) {
        this.shoppingItemsToBuy = ingredientToBuy;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public ArrayList<ShoppingItem> getIngredientToBuy() {
        return shoppingItemsToBuy;
    }

    public void setIngredientToBuy(ArrayList<ShoppingItem> ingredientToBuy) {
        this.shoppingItemsToBuy = ingredientToBuy;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public void addIngredientToBuy(ShoppingItem shoppingItem, double recipeServingSize, double mealPortionOfRecipe) {
        boolean exists = false;
        double portionOfRecipeIngredient = mealPortionOfRecipe / recipeServingSize;
        double ingredientAmount = shoppingItem.getIngredientToBuy().getAmount() * portionOfRecipeIngredient;

        for (int i = 0; i < shoppingItemsToBuy.size(); i++) {
            ShoppingItem existingIngredient = shoppingItemsToBuy.get(i);


            // Sprawdź, czy nazwa i jednostka się zgadzają
            if (existingIngredient.getIngredientToBuy().getName().equals(shoppingItem.getIngredientToBuy().getName()) && existingIngredient.getIngredientToBuy().getUnit().equals(shoppingItem.getIngredientToBuy().getUnit())) {
                // Zwiększ ilość istniejącego składnika
                existingIngredient.getIngredientToBuy().setAmount(existingIngredient.getIngredientToBuy().getAmount() + ingredientAmount);
                exists = true;
                break;
            }
        }

        // Jeśli składnik nie istnieje w liście, dodaj go jako nowy
        if ( exists == false) {
            shoppingItemsToBuy.add(new ShoppingItem(
                    new RecipeIngredient(
                            shoppingItem.getIngredientToBuy().getName(),
                            ingredientAmount,
                            shoppingItem.getIngredientToBuy().getUnit()
                    ), false
            ));
        }
    }

    public void deleteBoughtItems() {
        for (int i = 0; i < shoppingItemsToBuy.size(); i++) {
            if (shoppingItemsToBuy.get(i).isBought()) {
                shoppingItemsToBuy.remove(i);
                i--;
            }
        }
    }
}
