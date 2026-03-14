package com.example.borrowhub.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recent_transactions")
public class RecentTransactionEntity {

    @PrimaryKey
    public int id;

    public String itemName;
    public String status;
    public String date;

    public RecentTransactionEntity(int id, String itemName, String status, String date) {
        this.id = id;
        this.itemName = itemName;
        this.status = status;
        this.date = date;
    }
}
