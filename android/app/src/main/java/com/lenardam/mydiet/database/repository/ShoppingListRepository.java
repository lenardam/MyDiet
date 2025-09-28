package com.lenardam.mydiet.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.MyDietDatabase;
import com.lenardam.mydiet.database.dao.ShoppingListDao;
import com.lenardam.mydiet.database.model.ShoppingList;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShoppingListRepository {

    private MyDietDatabase database;
    private ShoppingListDao shoppingListDao;
    private LiveData<List<ShoppingList>> allShoppingList;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ShoppingListRepository(Application application) {
        database = MyDietDatabase.getInstance(application);
        shoppingListDao = database.shoppingListDao();
        allShoppingList = shoppingListDao.getAllShoppingList();
    }

    public void insert(ShoppingList shoppingList) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                shoppingListDao.insert(shoppingList);
            }
        });
    }

    public void update(ShoppingList shoppingList) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                shoppingListDao.update(shoppingList);
            }
        });
    }

    public void delete(ShoppingList shoppingList) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                shoppingListDao.delete(shoppingList);
            }
        });
    }

    public LiveData<List<ShoppingList>> getAllShoppingList() {
        return allShoppingList;
    }

}
