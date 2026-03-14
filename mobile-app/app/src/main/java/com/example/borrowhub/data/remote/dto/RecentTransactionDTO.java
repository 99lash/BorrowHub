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

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
