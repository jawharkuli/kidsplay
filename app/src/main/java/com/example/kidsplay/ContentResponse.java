package com.example.kidsplay;

import java.util.List;

public class ContentResponse {
    private boolean success;
    private List<ContentModel> data;

    // Constructor
    public ContentResponse(boolean success, List<ContentModel> data) {
        this.success = success;
        this.data = data;
    }

    // Getter and setter for success
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    // Getter and setter for data
    public List<ContentModel> getData() {
        return data;
    }

    public void setData(List<ContentModel> data) {
        this.data = data;
    }
}
