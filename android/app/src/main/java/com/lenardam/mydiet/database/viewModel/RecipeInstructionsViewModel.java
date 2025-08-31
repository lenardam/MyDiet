package com.lenardam.mydiet.database.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.model.RecipeInstructions;
import com.lenardam.mydiet.database.repository.RecipeInstructionsRepository;

import java.util.List;

public class RecipeInstructionsViewModel extends AndroidViewModel {

    private RecipeInstructionsRepository recipeInstructionsRepository;

    public RecipeInstructionsViewModel(@NonNull Application application) {
        super(application);

        recipeInstructionsRepository = new RecipeInstructionsRepository(application);
    }

    public void insert(RecipeInstructions recipeInstruction) {
        recipeInstructionsRepository.insert(recipeInstruction);
    }

    public void update(RecipeInstructions recipeInstruction) {
        recipeInstructionsRepository.update(recipeInstruction);
    }

    public void delete(RecipeInstructions recipeInstruction) {
        recipeInstructionsRepository.delete(recipeInstruction);
    }

    public LiveData<List<RecipeInstructions>> getRecipeInstructionsByRecipeId(int recipeId) {
        return recipeInstructionsRepository.getRecipeInstructionsByRecipeId(recipeId);
    }

}
