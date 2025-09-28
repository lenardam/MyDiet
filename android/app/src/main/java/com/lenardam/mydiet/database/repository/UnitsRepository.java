package com.lenardam.mydiet.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.MyDietDatabase;
import com.lenardam.mydiet.database.dao.UnitsDao;
import com.lenardam.mydiet.database.model.Units;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import android.os.Handler;
import android.os.Looper;

public class UnitsRepository {

    private MyDietDatabase database;
    private UnitsDao unitsDao;
    private LiveData<List<Units>> allUnits;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public UnitsRepository(Application application) {
        database = MyDietDatabase.getInstance(application);
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

    public void getUnitById(Long unitId, Consumer<Units> callback) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Units unit = unitsDao.getUnitById(unitId); // pobranie z DB
                // wynik przekazujemy na główny wątek
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.accept(unit);
                    }
                });
            }
        });
    }

    public void getUnitByName(String name, Consumer<Units> callback) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Units unit = unitsDao.getUnitByName(name); // pobranie z DB
                // wynik przekazujemy na główny wątek
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.accept(unit);
                    }
                });
            }
        });
    }

}
