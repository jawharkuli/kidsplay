package com.example.kidsplay;

/**
 * Model class for content items displayed in the ViewPager
 */
public class ContentItem {
    private String letter;
    private String example;
    private int imageResourceId;

    public ContentItem(String letter, String example, int imageResourceId) {
        this.letter = letter;
        this.example = example;
        this.imageResourceId = imageResourceId;
    }

    public String getLetter() {
        return letter;
    }

    public String getExample() {
        return example;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}