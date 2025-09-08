package com.lenardam.mydiet.database.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "units")
public class Units {

    @PrimaryKey(autoGenerate = true)
    private Long unitId;

    private String name;

    public Units(String name) {
        this.name = name;
    }

    public Long getUnitId() {
        return unitId;
    }

    public String getName() {
        return name;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

}
