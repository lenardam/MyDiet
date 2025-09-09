package com.lenardam.mydiet.database.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.model.Tags;
import com.lenardam.mydiet.database.repository.TagsRepository;

import java.util.List;
import java.util.function.Consumer;

public class TagsViewModel extends AndroidViewModel {

    private TagsRepository tagsRepository;
    private LiveData<List<Tags>> allTags;

    public TagsViewModel(@NonNull Application application) {
        super(application);

        tagsRepository = new TagsRepository(application);
        allTags = tagsRepository.getAllTags();
    }

    public void insert(Tags tag) {
        tagsRepository.insert(tag);
    }

    public void update(Tags tag) {
        tagsRepository.update(tag);
    }

    public void delete(Tags tag) {
        tagsRepository.delete(tag);
    }

    public void insertIfNotExists(final String name, final Consumer<Boolean> callback) {
        tagsRepository.insertIfNotExists(name, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean success) {
                callback.accept(success);
            }
        });
    }

    public LiveData<List<Tags>> getAllTags() {
        return allTags;
    }

    public Tags getTagByName(String name) {
        return tagsRepository.getTagByName(name);
    }

    public LiveData<List<Tags>> getTagsByRecipeId(Long recipeId) {
        return tagsRepository.getTagsByRecipeId(recipeId);
    }

}
