package com.example.borrowhub.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class DashboardStatsDTO {

    @SerializedName("total_borrowed")
    private int totalBorrowed;

    @SerializedName("active_requests")
    private int activeRequests;

    @SerializedName("overdue_items")
    private int overdueItems;

    // Getters and Setters
    public int getTotalBorrowed() {
        return totalBorrowed;
    }

    public void setTotalBorrowed(int totalBorrowed) {
        this.totalBorrowed = totalBorrowed;
    }

    public int getActiveRequests() {
        return activeRequests;
    }

    public void setActiveRequests(int activeRequests) {
        this.activeRequests = activeRequests;
    }

    public int getOverdueItems() {
        return overdueItems;
    }

    public void setOverdueItems(int overdueItems) {
        this.overdueItems = overdueItems;
    }
}
