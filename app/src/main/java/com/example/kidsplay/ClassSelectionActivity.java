package com.example.kidsplay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ClassSelectionActivity extends AppCompatActivity {

    private Spinner classSpinner;
    private Button exploreButton;
    private ProgressBar progressBar;
    private List<DatabaseHelper.ClassInfo> classInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_selection);

        classSpinner = findViewById(R.id.class_spinner);
        exploreButton = findViewById(R.id.explore_button);
        progressBar = findViewById(R.id.progress_bar);

        // Fixed initialization
        DatabaseHelper dbHelper = new DatabaseHelper(); // Pass context

        setLoading(true);

        dbHelper.fetchClasses(new DatabaseHelper.ClassesCallback() {
            @Override
            public void onResult(List<DatabaseHelper.ClassInfo> classes) {
                if (classes != null && !classes.isEmpty()) {
                    classInfoList = classes;
                    setupSpinner();
                } else {
                    Toast.makeText(ClassSelectionActivity.this, "No classes found!", Toast.LENGTH_SHORT).show();
                }
                setLoading(false);
            }
        });

        exploreButton.setOnClickListener(view -> {
            if (classInfoList == null || classInfoList.isEmpty()) {
                Toast.makeText(this, "Class list is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedPosition = classSpinner.getSelectedItemPosition();
            if (selectedPosition > 0) { // Ignore "Select Class"
                DatabaseHelper.ClassInfo selectedClassInfo = classInfoList.get(selectedPosition - 1);
                String selectedClassName = selectedClassInfo.getName();
                int selectedClassId = selectedClassInfo.getId();

                Intent intent;
                if (selectedClassName.toLowerCase().contains("class")) {
                    intent = new Intent(ClassSelectionActivity.this, PrimaryActivity.class);
                } else {
                    intent = new Intent(ClassSelectionActivity.this, PrePrimaryActivity.class);
                }

                intent.putExtra("classId", selectedClassId);
                intent.putExtra("selectedClass", selectedClassName);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select a class!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpinner() {
        List<String> displayList = new ArrayList<>();
        displayList.add("Select Class");

        for (DatabaseHelper.ClassInfo classInfo : classInfoList) {
            displayList.add(classInfo.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, displayList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        classSpinner.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        exploreButton.setEnabled(!isLoading);
    }
}