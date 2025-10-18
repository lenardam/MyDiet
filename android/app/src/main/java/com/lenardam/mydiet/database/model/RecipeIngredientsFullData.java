package com.lenardam.mydiet.database.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class RecipeIngredientsFullData {

    @Embedded
    public RecipeIngredients recipeIngredient;

    @Relation(
            parentColumn = "unitId",
            entityColumn = "unitId",
            entity = Units.class
    )
    public Units unit;
}
