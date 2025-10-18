package com.lenardam.mydiet.database.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "shopping_list",
        indices = @Index(value = "itemName"))
public class ShoppingList {

    @PrimaryKey(autoGenerate = true)
    private Long shoppingListId;

    private String itemName;
    private Integer itemPosition;
    private boolean isBought;

    public ShoppingList(String itemName, Integer itemPosition, boolean isBought) {
        this.itemName = itemName;
        this.itemPosition = itemPosition;
        this.isBought = isBought;
    }

    public Long getShoppingListId() {
        return shoppingListId;
    }

    public String getItemName() {
        return itemName;
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

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }
}
