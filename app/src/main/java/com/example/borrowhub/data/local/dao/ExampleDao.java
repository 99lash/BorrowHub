package com.example.borrowhub.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.borrowhub.data.local.entity.Example;

import java.util.List;

@Dao
public interface ExampleDao {

    @Insert
    void insert(Example example);

    @Update
    void update(Example example);

    @Delete
    void delete(Example example);

    @Query("SELECT * FROM examples")
    LiveData<List<Example>> getAllExamples();
}
