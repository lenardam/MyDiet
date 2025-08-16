package com.lenardam.mydiet.dbmodel;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "units")
public class Units {

    @PrimaryKey(autoGenerate = true)
    private int unitId;

    private String name;

    public Units(String name) {
        this.name = name;
    }

    public int getUnitId() {
        return unitId;
    }

    public String getName() {
        return name;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }
}
