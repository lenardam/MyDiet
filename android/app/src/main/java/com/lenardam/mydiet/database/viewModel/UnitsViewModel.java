package com.lenardam.mydiet.database.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.model.Units;
import com.lenardam.mydiet.database.repository.UnitsRepository;

import java.util.List;

public class UnitsViewModel extends AndroidViewModel {

    private UnitsRepository unitsRepository;
    private LiveData<List<Units>> allUnits;

    public UnitsViewModel(@NonNull Application application) {
        super(application);

        unitsRepository = new UnitsRepository(application);
        allUnits = unitsRepository.getAllUnits();
    }

    public void insert(Units unit) {
        unitsRepository.insert(unit);
    }

    public void update(Units unit) {
        unitsRepository.update(unit);
    }

    public void delete(Units unit) {
        unitsRepository.delete(unit);
    }

    public LiveData<List<Units>> getAllUnits() {
        return allUnits;
    }

}
