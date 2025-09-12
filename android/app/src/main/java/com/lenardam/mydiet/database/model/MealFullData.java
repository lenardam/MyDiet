package com.lenardam.mydiet.database.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.lenardam.mydiet.model.DietPlan;
import com.lenardam.mydiet.model.Meal;

import java.util.List;

public class MealFullData {

    @Embedded
    public Meals meal;

    @Relation(
            entity = Recipes.class,
            parentColumn = "recipeId",
            entityColumn = "recipeId"
    )
    public RecipeFullData recipe;

    @Relation(
            parentColumn = "dietPlanId",
            entityColumn = "dietPlanId",
            entity = DietPlans.class
    )
    public DietPlans dietPlan;

}
