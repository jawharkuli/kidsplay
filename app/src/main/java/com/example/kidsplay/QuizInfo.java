package com.example.login;

import com.example.login.DatabaseHelper;

public class QuizInfo {
    private final String imagePath;
    private final String optionA;
    private final String optionB;
    private final String optionC;
    private final String optionD;
    private final char correctOption;

    public QuizInfo(int id, int cid, String imagePath, String optionA, String optionB, String optionC, String optionD, char correctOption) {
        this.imagePath = imagePath;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
    }

    // Assuming images are stored in a folder like "https://yourserver.com/uploads/"
    public String getImageUrl() {
        return DatabaseHelper.BASE_URL + imagePath;
    }

    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public char getCorrectOption() { return correctOption; }
}
