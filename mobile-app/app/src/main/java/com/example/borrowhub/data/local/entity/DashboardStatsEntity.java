package com.example.borrowhub.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "dashboard_stats")
public class DashboardStatsEntity {

    @PrimaryKey
    public int id = 1; // Assuming only one record for stats

    public int totalBorrowed;
    public int activeRequests;
    public int overdueItems;

    public DashboardStatsEntity(int totalBorrowed, int activeRequests, int overdueItems) {
        this.totalBorrowed = totalBorrowed;
        this.activeRequests = activeRequests;
        this.overdueItems = overdueItems;
    }
}
