package com.lenardam.mydiet.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.MyDietDatabase;
import com.lenardam.mydiet.database.dao.TagsDao;
import com.lenardam.mydiet.database.model.Tags;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class TagsRepository {

    private TagsDao tagsDao;
    private LiveData<List<Tags>> allTags;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TagsRepository(Application application) {
        MyDietDatabase database = MyDietDatabase.getInstance(application);
        tagsDao = database.tagsDao();
        allTags = tagsDao.getAllTags();
    }

    public void insert(Tags tag) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                tagsDao.insert(tag);
            }
        });
    }

    public void update(Tags tag) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                tagsDao.update(tag);
            }
        });
    }

    public void delete(Tags tag) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                tagsDao.delete(tag);
            }
        });
    }

    public void insertIfNotExists(final String name, final Consumer<Boolean> callback) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                long result = tagsDao.insert(new Tags(name));
                callback.accept(result != -1); // true = dodano, false = istniało
            }
        });
    }

    public LiveData<List<Tags>> getAllTags() {
        return allTags;
    }

    public Tags getTagByName(String name) {
        return tagsDao.getTagByName(name);
    }


}
