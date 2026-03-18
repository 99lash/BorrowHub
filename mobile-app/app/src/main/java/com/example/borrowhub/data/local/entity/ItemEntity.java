package com.example.borrowhub.data.local.entity;

public class ItemEntity {

    public long id;
    public String name;
    public String type;
    public String status;
    public int totalQuantity;
    public int availableQuantity;

    public ItemEntity(long id, String name, String type, String status, int totalQuantity, int availableQuantity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
    }
}
