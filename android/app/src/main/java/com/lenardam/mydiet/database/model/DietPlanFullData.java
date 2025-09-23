package com.lenardam.mydiet.database.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class DietPlanFullData {

    @Embedded
    public DietPlans dietPlan;

    @Relation(
            parentColumn = "dietPlanId",
            entityColumn = "dietPlanId",
            entity = Meals.class
    )
    public List<MealFullData> meals;
}
