package com.lenardam.mydiet.database.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.model.ShoppingList;
import com.lenardam.mydiet.database.repository.ShoppingListRepository;

import java.util.List;

public class ShoppingListViewModel extends AndroidViewModel {

    private ShoppingListRepository shoppingListRepository;
    private LiveData<List<ShoppingList>> allShoppingList;

    public ShoppingListViewModel(@NonNull Application application) {
        super(application);

        shoppingListRepository = new ShoppingListRepository(application);
        allShoppingList = shoppingListRepository.getAllShoppingList();
    }

    public void insert(ShoppingList shoppingList) {
        shoppingListRepository.insert(shoppingList);
    }

    public void update(ShoppingList shoppingList) {
        shoppingListRepository.update(shoppingList);
    }

    public void delete(ShoppingList shoppingList) {
        shoppingListRepository.delete(shoppingList);
    }

    public LiveData<List<ShoppingList>> getAllShoppingList() {
        return allShoppingList;
    }

}
