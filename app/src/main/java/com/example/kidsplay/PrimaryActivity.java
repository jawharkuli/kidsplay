// CategorySelectionActivity.java (Main Activity)
package com.example.kidsplay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PrimaryActivity extends AppCompatActivity implements View.OnClickListener {

    // Constants for category identification
    public static final String EXTRA_CATEGORY = "com.example.category selector.CATEGORY";
    public static final int CATEGORY_MATHS = 0;
    public static final int CATEGORY_ENGLISH = 2;
    public static final int CATEGORY_SCIENCE = 3;
    public static final int CATEGORY_GK = 4;

    public String selectedClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);
        Intent i = getIntent();
        selectedClass = i.getStringExtra("selectedClass");

        // Initialize buttons
        Button btnMaths = findViewById(R.id.btn_maths);
        Button btnEnglish = findViewById(R.id.btn_english);
        Button btnScience = findViewById(R.id.btn_science);
        Button btnGk = findViewById(R.id.btn_gk);



        // Set click listeners
        btnMaths.setOnClickListener(this);
        btnEnglish.setOnClickListener(this);
        btnScience.setOnClickListener(this);
        btnGk.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        int categoryId = -1;

        // Determine which category was selected
        int id = view.getId();
        if (id == R.id.btn_rhymes) {
            categoryId = CATEGORY_MATHS;
            Toast.makeText(getApplicationContext(), selectedClass, Toast.LENGTH_SHORT).show(); // Display toast message with selected class name (for debugging purposes)
        } else if (id == R.id.btn_quiz) {
            categoryId = CATEGORY_ENGLISH;
        } else if (id == R.id.btn_training) {
            categoryId = CATEGORY_SCIENCE;
        } else if (id == R.id.btn_story) {
            categoryId = CATEGORY_GK;

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