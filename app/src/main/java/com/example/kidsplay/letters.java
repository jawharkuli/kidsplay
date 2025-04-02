package com.example.kidsplay;

public class letters {
    private final String name;
    private final int imageResId;
    private final boolean isFruit;
    private final int backgroundColor;

    public letters(String name, int imageResId, boolean isFruit, int backgroundColor) {
        this.name = name;
        this.imageResId = imageResId;
        this.isFruit = isFruit;
        this.backgroundColor = backgroundColor;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public boolean isFruit() {
        return isFruit;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }
}