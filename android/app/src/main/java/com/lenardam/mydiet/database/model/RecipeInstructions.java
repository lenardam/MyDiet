package com.lenardam.mydiet.database.model;

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
    private Long recipeInstructionId;

    private Long recipeId; //FK do tabeli Recipes.class
    private String instruction;

    public RecipeInstructions(Long recipeId, String instruction) {
        this.recipeId = recipeId;
        this.instruction = instruction;
    }

    public Long getRecipeInstructionId() {
        return recipeInstructionId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setRecipeInstructionId(Long recipeInstructionId) {
        this.recipeInstructionId = recipeInstructionId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }
}
