package com.lenardam.mydiet.database.model;

public class ShoppingItem {
    private String itemName;
    private Double amount;
    private String unitName;

    public ShoppingItem(String itemName, Double amount, String unitName) {
        this.itemName = itemName;
        this.amount = amount;
        this.unitName = unitName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
