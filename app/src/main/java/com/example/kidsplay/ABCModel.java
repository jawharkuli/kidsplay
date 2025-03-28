package com.example.kidsplay;

public class ABCModel {
    private String letter;
    private int imageResId;
    private int soundResId;

    public ABCModel(String letter, int imageResId, int soundResId) {
        this.letter = letter;
        this.imageResId = imageResId;
        this.soundResId = soundResId;
    }

    public String getLetter() {
        return letter;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getSoundResId() {
        return soundResId;
    }
}
