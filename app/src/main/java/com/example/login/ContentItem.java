package com.example.login;

public class ContentItem {
    private String letter;
    private String example;
    private int imageResource;

    public ContentItem(String letter, String example, int imageResource) {
        this.letter = letter;
        this.example = example;
        this.imageResource = imageResource;
    }

    public String getLetter() { return letter; }
    public String getExample() { return example; }
    public int getImageResource() { return imageResource; }
}