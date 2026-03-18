package com.example.borrowhub.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class ItemEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;
    @NonNull
    public String name;
    @NonNull
    public String type;
    @NonNull
    public String status;
    public int totalQuantity;
    public int availableQuantity;

    public ItemEntity() {
    }

    @Ignore
    public ItemEntity(long id, String name, String type, String status, int totalQuantity, int availableQuantity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    @NonNull
    public String getStatus() {
        return status;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}
