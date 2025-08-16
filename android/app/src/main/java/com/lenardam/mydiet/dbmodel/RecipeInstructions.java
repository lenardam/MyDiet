package com.lenardam.mydiet.dbmodel;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe_instructions",
        foreignKeys = @ForeignKey(
                        entity = Recipes.class,
                        parentColumns = "recipeId",
                        childColumns = "recipeId",
                        onDelete = ForeignKey.CASCADE
        ),
        indices = @Index(value = "recipeId")
        )
public class RecipeInstructions {

    @PrimaryKey(autoGenerate = true)
    private int recipeInstructionId;

    private int recipeId; //FK do tabeli Recipes.class
    private String instruction;

    public RecipeInstructions(int recipeId, String instruction) {
        this.recipeId = recipeId;
        this.instruction = instruction;
    }

    public int getRecipeInstructionId() {
        return recipeInstructionId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setRecipeInstructionId(int recipeInstructionId) {
        this.recipeInstructionId = recipeInstructionId;
    }
}
