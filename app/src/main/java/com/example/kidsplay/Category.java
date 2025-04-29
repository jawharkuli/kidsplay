package com.example.kidsplay;

import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("id")
    private int id;  // ✅ Changed from String to int

    @SerializedName("name")
    private String name;

    @SerializedName("gif_url")
    private String gifUrl;

    public int getId() {
        return id;  // ✅ Now returns int
    }

    public String getName() {
        return name;
    }

    public String getGifUrl() {
        return gifUrl;
    }
}
