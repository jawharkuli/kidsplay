package com.example.login;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.DynamicContentActivity;
import com.example.login.R;

public class ClassSelectionActivity extends AppCompatActivity {

    private Spinner classSpinner;
    private Button exploreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_selection);

        classSpinner = findViewById(R.id.class_spinner);
        exploreButton = findViewById(R.id.explore_button);

        // Populate spinner with class options
        String[] classList = {"LKG", "UKG", "1", "2", "3", "4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);

        exploreButton.setOnClickListener(view -> {
            String selectedClass = classSpinner.getSelectedItem().toString();
            Intent intent = new Intent(ClassSelectionActivity.this, DynamicContentActivity.class);
            intent.putExtra("selectedClass", selectedClass);
            startActivity(intent);
        });
    }
}
