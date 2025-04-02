package com.example.kidsplay;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class BrushSizeDialog extends Dialog {

    private int currentSize;
    private OnBrushSizeSelectedListener listener;

    public BrushSizeDialog(Context context, int brushSize, OnBrushSizeSelectedListener listener) {
        super(context);
        this.currentSize = brushSize;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_brush_size); // Ensure XML file exists

        SeekBar seekBar = findViewById(R.id.brush_size_seekbar);
        TextView sizeValue = findViewById(R.id.brush_size_value);
        Button applyButton = findViewById(R.id.btn_apply_brush_size);

        // Set the current brush size
        seekBar.setProgress(currentSize);
        sizeValue.setText("Size: " + currentSize);

        // Listen for SeekBar changes
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 1) progress = 1; // Ensure minimum size is 1
                currentSize = progress;
                sizeValue.setText("Size: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Apply button click
        applyButton.setOnClickListener(view -> {
            listener.onSizeSelected(currentSize);
            dismiss();
        });
    }

    public interface OnBrushSizeSelectedListener {
        void onSizeSelected(int size);
    }
}
