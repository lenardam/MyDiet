package com.lenardam.mydiet.dbmodel;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "diet_plans",
        primaryKeys = {"dietPlanId"},
        indices = {
                @Index(value = "dietPlanId"),
                @Index(value = "date")
        })
public class DietPlans {

    @PrimaryKey(autoGenerate = true)
    private int dietPlanId;

    private LocalDate date;

    public DietPlans(LocalDate date) {
        this.date = date;
    }

    public int getDietPlanId() {
        return dietPlanId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDietPlanId(int dietPlanId) {
        this.dietPlanId = dietPlanId;
    }
}
