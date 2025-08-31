package com.lenardam.mydiet.database.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.model.RecipeTags;
import com.lenardam.mydiet.database.repository.RecipeTagsRepository;

import java.util.List;

public class RecipeTagsViewModel extends AndroidViewModel {

    private RecipeTagsRepository recipeTagsRepository;

    public RecipeTagsViewModel(@NonNull Application application) {
        super(application);

        recipeTagsRepository = new RecipeTagsRepository(application);

    }

    public void insert(RecipeTags recipeTag) {
        recipeTagsRepository.insert(recipeTag);
    }

    public void update(RecipeTags recipeTag) {
        recipeTagsRepository.update(recipeTag);
    }

    public void delete(RecipeTags recipeTag) {
        recipeTagsRepository.delete(recipeTag);
    }

    public LiveData<List<RecipeTags>> getRecipeTagsByRecipeId(int recipeId) {
        return recipeTagsRepository.getRecipeTagsByRecipeId(recipeId);
    }

    public LiveData<List<RecipeTags>> getRecipeTagsByTagList(List<Integer> tagIds) {
        return recipeTagsRepository.getRecipeTagsByTagList(tagIds);
    }

}
