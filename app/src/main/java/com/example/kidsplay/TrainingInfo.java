package com.example.login;

public class TrainingInfo {
    private final int id;
    private final int cid;
    private final String sentences;

    public TrainingInfo(int id, int cid, String sentences) {
        this.id = id;
        this.cid = cid;
        this.sentences = sentences;
    }

    public int getId() { return id; }
    public int getCid() { return cid; }
    public String getSentences() { return sentences; }
}