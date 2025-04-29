package com.example.kidsplay;

public class HomeContent {

    private String title;
    private String imageUrl;  // Changed from mediaUrl to match JSON key
    private String type;

    // Empty constructor
    public HomeContent() {}

    // Constructor with parameters
    public HomeContent(String title, String imageUrl, String type) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getType() {
        return type;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setType(String type) {
        this.type = type;
    }
}
