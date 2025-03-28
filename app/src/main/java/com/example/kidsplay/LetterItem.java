package com.example.kidsplay;

public class LetterItem {
    private String letter;
    private String word;
    private int imageResId;
    private String letterSound;

    public LetterItem(String letter, String word, int imageResId, String letterSound) {
        this.letter = letter;
        this.word = word;
        this.imageResId = imageResId;
        this.letterSound = letterSound;
    }

    public String getLetter() {
        return letter;
    }

    public String getWord() {
        return word;
    }

    public int getImageResId() {
        return imageResId;
    }

    // Updated method to return proper pronunciation
    public String getPronunciation() {
        switch (letter.toUpperCase()) {
            case "A": return "A";
            case "B": return "B";
            case "C": return "C";
            case "D": return "D";
            case "E": return "E";
            case "F": return "F";
            case "G": return "G";
            case "H": return "H";
            case "I": return "I";
            case "J": return "J";
            case "K": return "K";
            case "L": return "L";
            case "M": return "M";
            case "N": return "N";
            case "O": return "O";
            case "P": return "P";
            case "Q": return "Q";
            case "R": return "R";
            case "S": return "S";
            case "T": return "T";
            case "U": return "U";
            case "V": return "V";
            case "W": return "W";
            case "X": return "X";
            case "Y": return "Y";
            case "Z": return "Z"; // Change to "Zed" for British English
            default: return letterSound; // Fallback if not found
        }
    }
}
