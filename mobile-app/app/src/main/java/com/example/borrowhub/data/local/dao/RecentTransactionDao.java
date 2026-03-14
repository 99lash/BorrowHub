package com.example.borrowhub.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.borrowhub.data.local.entity.RecentTransactionEntity;

import java.util.List;

@Dao
public interface RecentTransactionDao {

    @Query("SELECT * FROM recent_transactions ORDER BY date DESC")
    LiveData<List<RecentTransactionEntity>> getRecentTransactions();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<RecentTransactionEntity> transactions);

    @Query("DELETE FROM recent_transactions")
    void deleteAll();
}
