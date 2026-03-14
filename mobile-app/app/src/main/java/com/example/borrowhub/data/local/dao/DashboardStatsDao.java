package com.example.borrowhub.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.borrowhub.data.local.entity.DashboardStatsEntity;

@Dao
public interface DashboardStatsDao {

    @Query("SELECT * FROM dashboard_stats LIMIT 1")
    LiveData<DashboardStatsEntity> getDashboardStats();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DashboardStatsEntity stats);

    @Query("DELETE FROM dashboard_stats")
    void deleteAll();
}
