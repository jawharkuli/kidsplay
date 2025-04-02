package com.example.kidsplay;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizzesFragment extends Fragment {

    private LinearLayout quizContainer;
    private DatabaseHelper databaseHelper;
    private int classId = 1; // Change this dynamically based on user selection
    private List<QuizInfo> quizList = new ArrayList<>();
    private Map<Integer, Character> userAnswers = new HashMap<>();
    private Button submitAllButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quizzes, container, false);

        quizContainer = view.findViewById(R.id.quizContainer);
        submitAllButton = view.findViewById(R.id.submitAllButton);
        databaseHelper = new DatabaseHelper();

        submitAllButton.setOnClickListener(v -> submitAllQuizzes());
        String className = ContentActivity.selectedClass;
        fetchQuizzesFromDB();

        return view;
    }

    private void fetchQuizzesFromDB() {
        String className = ContentActivity.selectedClass;
        databaseHelper.fetchQuizzesByClassName(className, new DatabaseHelper.QuizCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResult(List<QuizInfo> quizzes) {
                if (quizzes.isEmpty()) {
                    Toast.makeText(getContext(), "No quizzes found for this class", Toast.LENGTH_SHORT).show();
                    submitAllButton.setVisibility(View.GONE);
                    return;
                }

                quizList = quizzes;
                quizContainer.removeAllViews(); // Clear previous content

                // Populate quizzes from bottom to top (reverse order)
                for (int i = quizzes.size() - 1; i >= 0; i--) {
                    final QuizInfo quiz = quizzes.get(i);
                    final int quizIndex = i;

                    View quizView = LayoutInflater.from(getContext()).inflate(R.layout.item_quiz, quizContainer, false);

                    @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView quizNumberText = quizView.findViewById(R.id.quizNumberText);
                    ImageView quizImage = quizView.findViewById(R.id.quizImage);
                    TextView questionText = quizView.findViewById(R.id.questionText);
                    RadioGroup optionsGroup = quizView.findViewById(R.id.optionsGroup);

                    // Set quiz number
                    quizNumberText.setText("Question " + (quizzes.size() - i) + " of " + quizzes.size());

                    // Set question text
                    questionText.setText(quiz.getQuestion());

                    // Load Image
                    if (quiz.getImageUrl() != null && !quiz.getImageUrl().isEmpty()) {
                        quizImage.setVisibility(View.VISIBLE);
                        Glide.with(requireContext()).load(quiz.getImageUrl()).into(quizImage);
                    } else {
                        quizImage.setVisibility(View.GONE);
                    }

                    // Set Options
                    String[] options = {quiz.getOptionA(), quiz.getOptionB(), quiz.getOptionC(), quiz.getOptionD()};

                    for (int j = 0; j < options.length; j++) {
                        RadioButton optionButton = new RadioButton(getContext());
                        optionButton.setText(options[j]);
                        optionButton.setId(j);
                        optionsGroup.addView(optionButton);
                    }

                    // Track user selection
                    optionsGroup.setOnCheckedChangeListener((group, checkedId) -> {
                        char selectedOption = (char) ('A' + checkedId);
                        userAnswers.put(quizIndex, selectedOption);
                    });

                    quizContainer.addView(quizView);
                }

                submitAllButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void submitAllQuizzes() {
        int correctCount = 0;
        int totalAnswered = userAnswers.size();

        if (totalAnswered == 0) {
            Toast.makeText(getContext(), "Please answer at least one question", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder resultMessage = new StringBuilder();

        for (int i = 0; i < quizList.size(); i++) {
            QuizInfo quiz = quizList.get(i);
            Character userAnswer = userAnswers.get(i);

            if (userAnswer != null) {
                if (userAnswer == quiz.getCorrectOption()) {
                    correctCount++;
                    resultMessage.append("Question ").append(i + 1).append(": Correct\n");
                } else {
                    resultMessage.append("Question ").append(i + 1)
                            .append(": Wrong (Correct: ").append(quiz.getCorrectOption()).append(")\n");
                }
            } else {
                resultMessage.append("Question ").append(i + 1).append(": Not answered\n");
            }
        }

        // Display results
        Toast.makeText(getContext(), "You got " + correctCount + " out of " +
                totalAnswered + " correct!", Toast.LENGTH_LONG).show();

        // You might want to show a more detailed result in a dialog
        // showResultDialog(resultMessage.toString(), correctCount, quizList.size());
    }
}