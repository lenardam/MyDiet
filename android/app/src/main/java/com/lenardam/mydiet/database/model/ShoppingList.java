package com.lenardam.mydiet.database.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "shopping_list",
        foreignKeys = @ForeignKey(
                entity = Units.class,
                parentColumns = "unitId",
                childColumns = "unitId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index(value = "unitId")
)
public class ShoppingList {

    @PrimaryKey(autoGenerate = true)
    private int shoppingListId;

    private String itemName;
    private double amount;
    private int unitId; //FK do tabeli Units.class
    private boolean isBought;

    public ShoppingList(String itemName, double amount, int unitId, boolean isBought) {
        this.itemName = itemName;
        this.amount = amount;
        this.unitId = unitId;
        this.isBought = isBought;
    }

    public int getShoppingListId() {
        return shoppingListId;
    }

    public String getItemName() {
        return itemName;
    }

    public double getAmount() {
        return amount;
    }

    public int getUnitId() {
        return unitId;
    }

    public boolean isBought() { return isBought; }

    public void setShoppingListId(int shoppingListId) {
        this.shoppingListId = shoppingListId;
    }
}
