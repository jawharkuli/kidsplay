package com.example.kidsplay;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DrawingActivity extends AppCompatActivity {
    private DrawingView drawingView;
    private ImageView btnUndo, btnRedo, btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        // Initialize the DrawingView
        drawingView = findViewById(R.id.drawing_view);

        // Initialize buttons
        btnUndo = findViewById(R.id.btnUndo);
        btnRedo = findViewById(R.id.btnRedo);
        btnClear = findViewById(R.id.clear_button);

        // Set up button listeners
        btnUndo.setOnClickListener(view -> drawingView.undo());
        btnRedo.setOnClickListener(view -> drawingView.redo());
        btnClear.setOnClickListener(view -> drawingView.clearDrawing());
    }
}
