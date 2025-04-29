package com.example.kidsplay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class PrimaryActivity extends AppCompatActivity {
    private static final String TAG = "PrimaryActivity";
    public static final String EXTRA_CATEGORY = "com.example.category selector.CATEGORY";
    public static final String EXTRA_SUBJECT_ID = "com.example.kidsplay.SUBJECT_ID";
    public static final String EXTRA_SUBJECT_NAME = "com.example.kidsplay.SUBJECT_NAME";

    private String selectedClass;
    private DatabaseHelper dbHelper;
    private SubjectAdapter adapter;
    private final List<DatabaseHelper.SubjectInfo> subjectsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);

        Intent i = getIntent();
        selectedClass = i.getStringExtra("selectedClass");

        // Initialize recycler view
        RecyclerView recyclerView = findViewById(R.id.recycler_subjects);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Display in 2 columns

        // Update adapter initialization to pass selectedClass instead of listener
        adapter = new SubjectAdapter(this, subjectsList, selectedClass);
        recyclerView.setAdapter(adapter);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Load subjects from database
        loadSubjects();
    }

    private void loadSubjects() {
        dbHelper.fetchSubjects(new DatabaseHelper.SubjectsCallback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResult(List<DatabaseHelper.SubjectInfo> subjects) {
                subjectsList.clear();
                subjectsList.addAll(subjects);
                adapter.notifyDataSetChanged();

                if (subjects.isEmpty()) {
                    Toast.makeText(PrimaryActivity.this, "No subjects found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(PrimaryActivity.this, "Error loading subjects: " + errorMessage, Toast.LENGTH_SHORT).show();
                Timber.tag(TAG).e("Error loading subjects: %s", errorMessage);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.cleanup();
        }
    }
}