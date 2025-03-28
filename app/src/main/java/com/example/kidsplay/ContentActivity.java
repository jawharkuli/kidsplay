// ContentActivity.java (Activity that loads fragments)
package com.example.kidsplay;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ContentActivity extends AppCompatActivity {
    public static String selectedClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        Intent intent = getIntent();
        selectedClass = intent.getStringExtra("selectedClass");

        // Get the category from the intent
        int categoryId = getIntent().getIntExtra(PrePrimaryActivity.EXTRA_CATEGORY, -1);

        // Load the appropriate fragment based on the category
        if (categoryId != -1) {
            loadFragmentForCategory(categoryId);
        }
    }

    private void loadFragmentForCategory(int categoryId) {
        Fragment fragment = null;
        String title = "";

        // Create the appropriate fragment based on category ID
        switch (categoryId) {
            case PrePrimaryActivity.CATEGORY_RHYMES:
                fragment = new RhymesFragment();
                title = "Rhymes";
                break;
            case PrePrimaryActivity.CATEGORY_QUIZ:
                fragment = new QuizzesFragment();
                title = "Quiz";
                break;
            case PrePrimaryActivity.CATEGORY_TRAINING:
                fragment = new TrainingFragment();
                title = "Training";
                break;
            case PrePrimaryActivity.CATEGORY_ALPHABETS:
                fragment = new ABCFragment();
                title = "Alphabet";
                break;
            case PrePrimaryActivity.CATEGORY_DRAWING:
                fragment = new DrawingFragment();
                title = "Drawing";
                break;
        }

        // Set the activity title
        setTitle(title);

        // Load the fragment
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}