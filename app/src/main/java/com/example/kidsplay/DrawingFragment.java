package com.example.login;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import yuku.ambilwarna.AmbilWarnaDialog; // Import Ambilwarna color picker

public class DrawingFragment extends Fragment {
    private DrawingView drawingView;
    private int currentColor = Color.BLACK;
    private int currentBrushSize = 8;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_drawing, container, false);

        drawingView = rootView.findViewById(R.id.drawing_view);
        Button clearButton = rootView.findViewById(R.id.clear_button);
        Button undoButton = rootView.findViewById(R.id.btnUndo);
        Button redoButton = rootView.findViewById(R.id.btnRedo);
        Button colorButton = rootView.findViewById(R.id.btnColor);
        Button brushButton = rootView.findViewById(R.id.btnBrush);

        if (drawingView == null) {
            Log.e("DrawingFragment", "Error: drawingView is null. Check XML ID.");
            return rootView;
        }

        drawingView.setBrushColor(currentColor);
        drawingView.setBrushSize(currentBrushSize);

        clearButton.setOnClickListener(view -> drawingView.clearDrawing());
        undoButton.setOnClickListener(view -> drawingView.undo());
        redoButton.setOnClickListener(view -> drawingView.redo());
        colorButton.setOnClickListener(view -> openColorPicker());
        brushButton.setOnClickListener(view -> openBrushSizeDialog());

        return rootView;
    }

    private void openColorPicker() {
        if (drawingView == null) {
            Log.e("DrawingFragment", "Cannot open color picker. drawingView is null.");
            return;
        }

        new AmbilWarnaDialog(requireContext(), currentColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                currentColor = color;
                drawingView.setBrushColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // User canceled color selection
            }
        }).show();
    }

    private void openBrushSizeDialog() {
        if (drawingView == null) {
            Log.e("DrawingFragment", "Cannot open brush size dialog. drawingView is null.");
            return;
        }

        new BrushSizeDialog(requireContext(), currentBrushSize, size -> {
            currentBrushSize = size;
            drawingView.setBrushSize(size);
        }).show();
    }
}
