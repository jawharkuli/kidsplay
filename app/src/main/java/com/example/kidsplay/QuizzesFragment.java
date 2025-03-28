package com.example.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import java.util.List;
import java.util.Objects;

public class QuizzesFragment extends Fragment {

    private TableLayout tableLayout;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quizzes, container, false);

        tableLayout = view.findViewById(R.id.tableLayout);
        databaseHelper = new DatabaseHelper();

        fetchQuizzesFromDB();

        return view;
    }

    private void fetchQuizzesFromDB() {
        databaseHelper.fetchQuizzes(new DatabaseHelper.QuizCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResult(List<QuizInfo> quizzes) {
                if (quizzes.isEmpty()) {
                    Toast.makeText(getContext(), "No quizzes found", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (QuizInfo quiz : quizzes) {
                    TableRow row = new TableRow(getContext());

                    // Image View for Quiz Image
                    ImageView quizImage = new ImageView(getContext());
                    quizImage.setLayoutParams(new TableRow.LayoutParams(200, 200)); // Set size
                    Glide.with(requireContext()).load(quiz.getImageUrl()).into(quizImage);

                    // Text View for Options
                    TextView questionText = new TextView(getContext());
                    questionText.setText(quiz.getOptionA() + ", " + quiz.getOptionB() + ", " + quiz.getOptionC() + ", " + quiz.getOptionD());
                    questionText.setPadding(10, 10, 10, 10);

                    // Correct Answer
                    TextView correctAnswerText = new TextView(getContext());
                    correctAnswerText.setText(String.valueOf(quiz.getCorrectOption()));
                    correctAnswerText.setPadding(10, 10, 10, 10);

                    row.addView(quizImage);
                    row.addView(questionText);
                    row.addView(correctAnswerText);
                    tableLayout.addView(row);
                }
            }
        });
    }
}
