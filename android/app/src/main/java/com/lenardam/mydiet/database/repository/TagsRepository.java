package com.lenardam.mydiet.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.MyDietDatabase;
import com.lenardam.mydiet.database.dao.TagsDao;
import com.lenardam.mydiet.database.model.Tags;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public LiveData<List<Tags>> getAllTags() {
        return allTags;
    }

}
