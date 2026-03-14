package com.example.borrowhub.viewmodel.dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class DashboardViewModel extends AndroidViewModel {

    private final MutableLiveData<Integer> totalItems = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentlyBorrowed = new MutableLiveData<>();
    private final MutableLiveData<Integer> availableNow = new MutableLiveData<>();
    private final MutableLiveData<Integer> dueToday = new MutableLiveData<>();

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        // Initialize with some mock data reflecting the prototype for now
        totalItems.setValue(248);
        currentlyBorrowed.setValue(42);
        availableNow.setValue(206);
        dueToday.setValue(18);
    }

    public LiveData<Integer> getTotalItems() {
        return totalItems;
    }

    public LiveData<Integer> getCurrentlyBorrowed() {
        return currentlyBorrowed;
    }

    public LiveData<Integer> getAvailableNow() {
        return availableNow;
    }

    public LiveData<Integer> getDueToday() {
        return dueToday;
    }
}
