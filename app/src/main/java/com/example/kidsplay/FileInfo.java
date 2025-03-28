package com.example.login;

public class FileInfo {
    private int id;
    private int cid;
    private int sid;
    private String filetype;
    private String filePath;
    private long fileSize;
    private String fileLink;
    private String filename;
    private String linkText;
    private String uploadedAt;

    // Default Constructor (Required for certain operations like Firebase or Reflection)
    public FileInfo() {
    }

    // Constructor with all parameters
    public FileInfo(int id, int cid, int sid, String filetype, String filePath,
                    long fileSize, String fileLink, String filename,
                    String linkText, String uploadedAt) {
        this.id = id;
        this.cid = cid;
        this.sid = sid;
        this.filetype = filetype;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.fileLink = fileLink;
        this.filename = filename;
        this.linkText = linkText;
        this.uploadedAt = uploadedAt;
    }

    // Getters and Setters (Optional, but recommended)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCid() { return cid; }
    public void setCid(int cid) { this.cid = cid; }

    public int getSid() { return sid; }
    public void setSid(int sid) { this.sid = sid; }

    public String getFiletype() { return filetype; }
    public void setFiletype(String filetype) { this.filetype = filetype; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public String getFileLink() { return fileLink; }
    public void setFileLink(String fileLink) { this.fileLink = fileLink; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getLinkText() { return linkText; }
    public void setLinkText(String linkText) { this.linkText = linkText; }

    public String getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(String uploadedAt) { this.uploadedAt = uploadedAt; }
}
