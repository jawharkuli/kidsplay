// ContentActivity.java (Activity that loads fragments)
package com.example.kidsplay;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ContentPrimaryActivity extends AppCompatActivity {
    public static String selectedClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);
        Intent intent = getIntent();
        selectedClass = intent.getStringExtra("selectedClass");

        // Get the category from the intent
        int categoryId = getIntent().getIntExtra(PrimaryActivity.EXTRA_CATEGORY, -1);

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
            case PrimaryActivity.CATEGORY_SCIENCE:
                fragment = new RhymesFragment();
                title = "Rhymes";
                break;
            case PrimaryActivity.CATEGORY_MATHS:
                fragment = new QuizzesFragment();
                title = "Quiz";
                break;
            case PrimaryActivity.CATEGORY_GK:
                fragment = new DrawingFragment();
                title = "Training";
                break;
            case PrimaryActivity.CATEGORY_ENGLISH:
                fragment = new A_ZFragment();
                title = "A_Z";
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