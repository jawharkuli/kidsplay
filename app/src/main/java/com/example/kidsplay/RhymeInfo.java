package com.example.kidsplay;

public class RhymeInfo {
    private final int id;
    private final int cid;
    private final String title;
    private final String description;
    private final String fileType;
    private final String file;
    private final String thumbnail;
    private final String duration;
    private final String uploadDate;

    public RhymeInfo(int id, int cid, String title, String description,
                     String fileType, String file, String thumbnail,
                     String duration, String uploadDate) {
        this.id = id;
        this.cid = cid;
        this.title = title;
        this.description = description;
        this.fileType = fileType;
        this.file = file;
        this.thumbnail = thumbnail;
        this.duration = duration;
        this.uploadDate = uploadDate;
    }

    public int getId() { return id; }
    public int getCid() { return cid; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getFileType() { return fileType; }
    public String getFile() { return file; }
    public String getThumbnail() { return thumbnail; }
    public String getDuration() { return duration; }
    public String getUploadDate() { return uploadDate; }
}