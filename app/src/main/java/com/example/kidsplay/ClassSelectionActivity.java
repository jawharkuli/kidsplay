package com.example.kidsplay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ClassSelectionActivity extends AppCompatActivity {

    private Spinner classSpinner;
    private Button exploreButton;
    private ProgressBar progressBar;
    private List<DatabaseHelper.ClassInfo> classInfoList = new ArrayList<>();
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_selection);

        // 🌟 Load animated background GIF
        ImageView gifBackground = findViewById(R.id.gif_background);
        if (gifBackground != null) {
            Glide.with(this)
                    .asGif()
                    .load(R.drawable.class_selection)
                    .into(gifBackground);
        }

        // 🔧 Initialize views
        classSpinner = findViewById(R.id.class_spinner);
        exploreButton = findViewById(R.id.explore_button);
        progressBar = findViewById(R.id.progress_bar);

        // 🎨 Set button background
        exploreButton.setBackgroundResource(R.drawable.button_background);

        // 📡 Database connection
        dbHelper = new DatabaseHelper(this);
        fetchClassList();

        // 🚀 Explore button click
        exploreButton.setOnClickListener(view -> handleExploreClick());
    }

    private void fetchClassList() {
        setLoading(true); // Show progress bar

        dbHelper.fetchClasses(new DatabaseHelper.ClassesCallback() {
            @Override
            public void onResult(List<DatabaseHelper.ClassInfo> classes) {
                runOnUiThread(() -> {
                    setLoading(false);

                    if (classes != null && !classes.isEmpty()) {
                        classInfoList.clear();
                        classInfoList.addAll(classes);
                        setupSpinner();
                    } else {
                        Toast.makeText(ClassSelectionActivity.this, "No classes found!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setupSpinner() {
        List<String> classNames = new ArrayList<>();
        classNames.add("Select Class"); // Placeholder

        for (DatabaseHelper.ClassInfo classInfo : classInfoList) {
            classNames.add(classInfo.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                classNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);
    }

    private void handleExploreClick() {
        if (classInfoList == null || classInfoList.isEmpty()) {
            Toast.makeText(this, "Class list is not loaded yet!", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPosition = classSpinner.getSelectedItemPosition();

        if (selectedPosition <= 0 || selectedPosition > classInfoList.size()) {
            Toast.makeText(this, "Please select a valid class!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper.ClassInfo selectedClass = classInfoList.get(selectedPosition - 1);
        String selectedClassName = selectedClass.getName();
        int selectedClassId = selectedClass.getId();

        Intent intent;
        if (selectedClassName.toLowerCase().contains("nursery") ||
                selectedClassName.toLowerCase().contains("kg") ||
                selectedClassName.toLowerCase().contains("pre")) {
            intent = new Intent(ClassSelectionActivity.this, PrePrimaryActivity.class);
        } else {
            intent = new Intent(ClassSelectionActivity.this, PrimaryActivity.class);
        }

        intent.putExtra("classId", selectedClassId);
        intent.putExtra("selectedClass", selectedClassName);
        startActivity(intent);
    }

    private void setLoading(boolean isLoading) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
        if (classSpinner != null) {
            classSpinner.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        }
        if (exploreButton != null) {
            exploreButton.setEnabled(!isLoading);
        }
    }
}
