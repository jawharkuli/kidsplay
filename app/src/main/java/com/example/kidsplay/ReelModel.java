package com.example.kidsplay;

public class ReelModel {
    int id;
    String title, videoUrl;

    public ReelModel(int id, String title, String videoUrl) {
        this.id = id;
        this.title = title;
        this.videoUrl = videoUrl;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getVideoUrl() { return videoUrl; }
}
