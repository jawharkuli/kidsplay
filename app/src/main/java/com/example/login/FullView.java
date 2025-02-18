package com.example.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class FullView extends AppCompatActivity {
    private ViewPager2 viewPager;
    private ContentPagerAdapter adapter;
    private List<ContentItem> contentItems;
    private String[] letters;
    private String[] examples;
    private int[] images;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_full_view);

        // Get the intent that started this activity
        Intent i = getIntent();

        // Get the position and arrays from intent
        currentPosition = i.getIntExtra("position", 0);
        letters = i.getStringArrayExtra("letters");
        examples = i.getStringArrayExtra("examples");
        images = i.getIntArrayExtra("images");

        // Initialize ViewPager2
        viewPager = findViewById(R.id.viewPager);

        // Initialize content items
        contentItems = new ArrayList<>();

        // Add all items to the list
        for(int j = 0; j < letters.length; j++) {
            contentItems.add(new ContentItem(letters[j], examples[j], images[j]));
        }

        // Set up adapter
        adapter = new ContentPagerAdapter(contentItems);
        viewPager.setAdapter(adapter);

        // Set the initial position
        viewPager.setCurrentItem(currentPosition, false);

        // Optional: Add page transition animation
        viewPager.setPageTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setAlpha(0.25f + r * 0.75f);
            page.setScaleX(0.85f + r * 0.15f);
        });
    }
}
