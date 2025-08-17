package com.lenardam.mydiet.database.repository;

import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.dao.ShoppingListDao;
import com.lenardam.mydiet.database.model.ShoppingList;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShoppingListRepository {

    private ShoppingListDao shoppingListDao;
    private LiveData<List<ShoppingList>> allShoppingList;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ShoppingListRepository(ShoppingListDao shoppingListDao) {
        this.shoppingListDao = shoppingListDao;
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
