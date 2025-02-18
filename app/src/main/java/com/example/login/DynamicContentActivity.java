package com.example.login;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DynamicContentActivity extends AppCompatActivity {

    private TextView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_content);

        contentView = findViewById(R.id.content_view);

        // Get the selected class from the intent
        String selectedClass = getIntent().getStringExtra("selectedClass");

        // Load dynamic content based on class
        loadDynamicContent(selectedClass);
    }

    private void loadDynamicContent(String selectedClass) {
        String content;
        switch (selectedClass) {
            case "LKG":
                content = "Dynamic content for LKG...";
                break;
            case "UKG":
                content = "Dynamic content for UKG...";
                break;
            default:
                content = "Dynamic content for Class " + selectedClass;
                break;
        }
        contentView.setText(content);
    }
}
