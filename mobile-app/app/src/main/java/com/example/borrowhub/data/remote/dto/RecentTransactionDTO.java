package com.example.borrowhub.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class RecentTransactionDTO {

    @SerializedName("id")
    private int id;

    @SerializedName("item_name")
    private String itemName;

    @SerializedName("status")
    private String status;

    @SerializedName("date")
    private String date;

    // Getters
    public int getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }
}
