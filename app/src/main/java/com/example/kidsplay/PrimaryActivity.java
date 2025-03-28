package com.example.kidsplay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PrimaryActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);

        TextView classNameTextView = findViewById(R.id.class_name_text_view);
        TextView classIdTextView = findViewById(R.id.class_id_text_view);

        Intent intent = getIntent();
        String selectedClass = intent.getStringExtra("selectedClass");
        int classId = intent.getIntExtra("classId", -1);

        if (selectedClass != null && classId != -1) {
            classNameTextView.setText("Class Name: " + selectedClass);
            classIdTextView.setText("Class ID: " + classId);
        } else {
            Toast.makeText(this, "Error retrieving class data!", Toast.LENGTH_SHORT).show();
        }
    }
}