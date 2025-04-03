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

        // Check category ID and load fragment accordingly
        switch (categoryId) {
            case PrePrimaryActivity.CATEGORY_ALPHABETS:
                if ("Nursery".equals(selectedClass)) {
                    fragment = new A_ZFragment();  // Static content for Nursery
                    title = "A-Z Alphabets";
                } else if ("LKG".equals(selectedClass)) {
                    fragment = new LKGFragment();  // Static content for LKG
                    title = "LKG Alphabets";
                } else if ("UKG".equals(selectedClass)) {
                    fragment = new Animals();  // Static content for UKG
                    title = "UKG Alphabets";
                }
                // If the selected class is not Nursery, LKG, or UKG, don't load any fragment
                break;

            case PrePrimaryActivity.CATEGORY_RHYMES:
                fragment = new RhymesFragment();
                title = "Rhymes";
                break;
            case PrePrimaryActivity.CATEGORY_QUIZ:
                fragment = new QuizzesFragment();
                title = "Quiz";
                break;
            case PrePrimaryActivity.CATEGORY_TRAINING:
                fragment = new DrawingFragment();
                title = "Training";
                break;
            case PrePrimaryActivity.CATEGORY_NUMBERS:
                fragment = new OneHundredFragment();
                title = "Numbers";
                break;
            case PrePrimaryActivity.CATEGORY_STORY:
                fragment = new StoriesFragment();
                title = "Stories";
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
