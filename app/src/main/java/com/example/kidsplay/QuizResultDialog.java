package com.example.kidsplay;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

public class QuizResultDialog {
    public static void showResultDialog(Fragment fragment, int correctAnswers, int totalQuestions) {
        if (fragment.getContext() == null) {
            return; // Avoid crash if Fragment is not attached
        }

        Dialog dialog = new Dialog(fragment.requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_quiz_result);
        dialog.setCancelable(false);

        TextView resultText = dialog.findViewById(R.id.result_text);
        ImageView gifImage = dialog.findViewById(R.id.gif_image);

        // Set result text
        resultText.setText("Well done! You scored " + correctAnswers + " out of " + totalQuestions);

        // Load GIF using Glide
        Glide.with(fragment)
                .load(R.raw.well_done) // Replace with your GIF name
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(gifImage);

        dialog.findViewById(R.id.btn_ok).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
