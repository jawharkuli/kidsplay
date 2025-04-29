package com.example.kidsplay;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CategoryResponse {

    @SerializedName("success")
    private boolean success;  // Renamed to 'success' for consistency

    @SerializedName("message")
    private String message;

    @SerializedName("data")  // Changed to 'data' to be consistent with the response structure
    private List<Category> categories;

    // Getter methods
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Category> getCategories() {
        return categories;
    }
}
