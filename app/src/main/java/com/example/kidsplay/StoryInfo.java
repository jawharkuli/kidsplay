package com.example.kidsplay;

public class StoryInfo {
    private final int id;
    private final int cid;
    private final String title;
    private final String description;
    private final String filetype;
    private final String file;
    private final String thumbnail;
    private final String createdAt;

    public StoryInfo(int id, int cid, String title, String description, String filetype,
                     String file, String thumbnail, String createdAt) {
        this.id = id;
        this.cid = cid;
        this.title = title;
        this.description = description;
        this.filetype = filetype;
        this.file = file;
        this.thumbnail = thumbnail;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public int getCid() {
        return cid;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getFiletype() {
        return filetype;
    }

    public String getFile() {
        return file;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}