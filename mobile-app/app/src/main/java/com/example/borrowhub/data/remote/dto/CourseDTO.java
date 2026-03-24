package com.example.borrowhub.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class CourseDTO {

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    public CourseDTO() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
