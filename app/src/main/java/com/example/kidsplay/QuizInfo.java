package com.example.kidsplay;

public class QuizInfo {
    private final int id;
    private final int cid;
    private final String question;
    private final String imagePath;
    private final String optionA;
    private final String optionB;
    private final String optionC;
    private final String optionD;
    private final char correctOption;

    public QuizInfo(int id, int cid, String question, String imagePath,
                    String optionA, String optionB, String optionC,
                    String optionD, char correctOption) {
        this.id = id;
        this.cid = cid;
        this.question = question;
        this.imagePath = imagePath;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
    }

    public int getId() { return id; }
    public int getCid() { return cid; }
    public String getQuestion() { return question; } // Added getter for question
    public String getImageUrl() {
        return DatabaseHelper.BASE_URL + imagePath;
    }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public char getCorrectOption() { return correctOption; }
}