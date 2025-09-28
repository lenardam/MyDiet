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
    private Long shoppingListId;

    private String itemName;
    private Integer itemPosition;
    private double amount;
    private Long unitId; //FK do tabeli Units.class
    private boolean isBought;

    public ShoppingList(String itemName, Integer itemPosition, double amount, Long unitId, boolean isBought) {
        this.itemName = itemName;
        this.itemPosition = itemPosition;
        this.amount = amount;
        this.unitId = unitId;
        this.isBought = isBought;
    }

    public Long getShoppingListId() {
        return shoppingListId;
    }

    public String getItemName() {
        return itemName;
    }

    public double getAmount() {
        return amount;
    }

    public Long getUnitId() {
        return unitId;
    }

    public boolean isBought() { return isBought; }

    public void setShoppingListId(Long shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public Integer getItemPosition() {
        return itemPosition;
    }

    public void setItemPosition(Integer itemPosition) {
        this.itemPosition = itemPosition;
    }
}
