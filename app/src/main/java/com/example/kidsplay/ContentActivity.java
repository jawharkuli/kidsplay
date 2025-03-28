// ContentActivity.java (Activity that loads fragments)
package com.example.login;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.login.DynamicContentActivity;
import com.example.login.QuizzesFragment;
import com.example.login.R;
import com.example.login.RhymesFragment;
import com.example.login.StoryFragment;
import com.example.login.TrainingFragment;

public class ContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        // Get the category from the intent
        int categoryId = getIntent().getIntExtra(DynamicContentActivity.EXTRA_CATEGORY, -1);

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
            case DynamicContentActivity.CATEGORY_RHYMES:
                fragment = new RhymesFragment();
                title = "Rhymes";
                break;
            case DynamicContentActivity.CATEGORY_QUIZ:
                fragment = new QuizzesFragment();
                title = "Quiz";
                break;
            case DynamicContentActivity.CATEGORY_TRAINING:
                fragment = new TrainingFragment();
                title = "Training";
                break;
            case DynamicContentActivity.CATEGORY_ALPHABETS:
                fragment = new ABCFragment();
                title = "Alphabet";
                break;
            case DynamicContentActivity.CATEGORY_DRAWING:
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