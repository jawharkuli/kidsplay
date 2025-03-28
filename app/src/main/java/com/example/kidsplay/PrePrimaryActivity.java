// CategorySelectionActivity.java (Main Activity)
package com.example.kidsplay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PrePrimaryActivity extends AppCompatActivity implements View.OnClickListener {

    // Constants for category identification
    public static final String EXTRA_CATEGORY = "com.example.category selector.CATEGORY";
    public static final int CATEGORY_RHYMES = 0;
    public static final int CATEGORY_QUIZ = 1;
    public static final int CATEGORY_TRAINING = 2;
    public static final int CATEGORY_STORY = 3;
    public static final int CATEGORY_ALPHABETS = 4;
    public static final int CATEGORY_NUMBERS = 5;
    public static final int CATEGORY_DRAWING =6 ;
    public String selectedClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_content);
        Intent i = getIntent();
        selectedClass = i.getStringExtra("selectedClass");

        // Initialize buttons
        Button btnRhymes = findViewById(R.id.btn_rhymes);
        Button btnQuiz = findViewById(R.id.btn_quiz);
        Button btnTraining = findViewById(R.id.btn_training);
        Button btnStory = findViewById(R.id.btn_story);
        Button btnAlphabets = findViewById(R.id.btn_alphabets);
        Button btnNumber = findViewById(R.id.btn_numbers);
        Button btnDrawing = findViewById(R.id.btn_drawing_board);


        // Set click listeners
        btnRhymes.setOnClickListener(this);
        btnQuiz.setOnClickListener(this);
        btnTraining.setOnClickListener(this);
        btnStory.setOnClickListener(this);
        btnAlphabets.setOnClickListener(this);
        btnNumber.setOnClickListener(this);
        btnDrawing.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int categoryId = -1;

        // Determine which category was selected
        int id = view.getId();
        if (id == R.id.btn_rhymes) {
            categoryId = CATEGORY_RHYMES;
            Toast.makeText(getApplicationContext(), selectedClass, Toast.LENGTH_SHORT).show(); // Display toast message with selected class name (for debugging purposes)
        } else if (id == R.id.btn_quiz) {
            categoryId = CATEGORY_QUIZ;
        } else if (id == R.id.btn_training) {
            categoryId = CATEGORY_TRAINING;
        } else if (id == R.id.btn_story) {
            categoryId = CATEGORY_STORY;
        } else if (id == R.id.btn_alphabets) {
            categoryId = CATEGORY_ALPHABETS;
        } else if (id == R.id.btn_numbers) {
            categoryId = CATEGORY_NUMBERS;
        } else if (id == R.id.btn_drawing_board) {
            categoryId = CATEGORY_DRAWING;

        }

        // Launch the content activity with the selected category
        if (categoryId != -1) {
            Intent intent = new Intent(this, ContentActivity.class);
            intent.putExtra("selectedClass", selectedClass);
            intent.putExtra(EXTRA_CATEGORY, categoryId);
            startActivity(intent);
        }
    }
}