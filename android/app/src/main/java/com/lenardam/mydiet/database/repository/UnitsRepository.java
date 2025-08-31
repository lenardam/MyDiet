package com.lenardam.mydiet.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.MyDietDatabase;
import com.lenardam.mydiet.database.dao.UnitsDao;
import com.lenardam.mydiet.database.model.Units;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UnitsRepository {

    private UnitsDao unitsDao;
    private LiveData<List<Units>> allUnits;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public UnitsRepository(Application application) {
        MyDietDatabase database = MyDietDatabase.getInstance(application);
        unitsDao = database.unitsDao();
        allUnits = unitsDao.getAllUnits();
    }

    public void insert(Units unit) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                unitsDao.insert(unit);
            }
        });
    }

    public void update(Units unit) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                unitsDao.update(unit);
            }
        });
    }

    public void delete(Units unit) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                unitsDao.delete(unit);
            }
        });
    }

    public LiveData<List<Units>> getAllUnits() {
        return allUnits;
    }

}
