package com.example.borrowhub.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class DashboardStatsDTO {

    @SerializedName("total_borrowed")
    private int totalBorrowed;

    @SerializedName("active_requests")
    private int activeRequests;

    @SerializedName("overdue_items")
    private int overdueItems;

    // Getters
    public int getTotalBorrowed() {
        return totalBorrowed;
    }

    public int getActiveRequests() {
        return activeRequests;
    }

    public int getOverdueItems() {
        return overdueItems;
    }
}
