package com.example.borrowhub.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.borrowhub.data.local.AppDatabase;
import com.example.borrowhub.data.local.dao.ExampleDao;
import com.example.borrowhub.data.local.entity.Example;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExampleRepository {

    private final ExampleDao exampleDao;
    private final LiveData<List<Example>> allExamples;
    private final ExecutorService executorService;

    public ExampleRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        exampleDao = database.exampleDao();
        allExamples = exampleDao.getAllExamples();
        // Background thread para sa database operations
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Example>> getAllExamples() {
        return allExamples;
    }

    public void insert(Example example) {
        executorService.execute(() -> exampleDao.insert(example));
    }

    public void update(Example example) {
        executorService.execute(() -> exampleDao.update(example));
    }

    public void delete(Example example) {
        executorService.execute(() -> exampleDao.delete(example));
    }
}
