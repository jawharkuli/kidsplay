package com.example.kidsplay;

import static com.example.kidsplay.QuizResultDialog.showResultDialog;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
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
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizzesFragment extends Fragment {

    private LinearLayout quizContainer;
    private DatabaseHelper databaseHelper;
    private List<QuizInfo> quizList = new ArrayList<>();
    private Map<Integer, Character> userAnswers = new HashMap<>();
    private Button submitAllButton;
    private MediaPlayer backgroundMusic;
    private MediaPlayer resultSound;
    private MediaPlayer optionSelectSound;

    // Maximum number of quizzes to display
    private static final int MAX_QUIZZES = 10;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quizzes, container, false);

        quizContainer = view.findViewById(R.id.quizContainer);
        submitAllButton = view.findViewById(R.id.submitAllButton);
        databaseHelper = new DatabaseHelper(requireContext());

        submitAllButton.setOnClickListener(v -> submitAllQuizzes());

        // Start background music with safe handling
        startBackgroundMusic();

        // Load quiz questions
        fetchRandomQuizzesFromDB();

        return view;
    }

    private void fetchRandomQuizzesFromDB() {
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

                // Randomize the quizzes list
                Collections.shuffle(quizzes);

                // Take only up to MAX_QUIZZES
                quizList = quizzes.size() > MAX_QUIZZES ?
                        quizzes.subList(0, MAX_QUIZZES) :
                        quizzes;

                quizContainer.removeAllViews();

                for (int i = 0; i < quizList.size(); i++) {
                    final QuizInfo quiz = quizList.get(i);
                    final int quizIndex = i;

                    View quizView = LayoutInflater.from(getContext()).inflate(R.layout.item_quiz, quizContainer, false);

                    TextView quizNumberText = quizView.findViewById(R.id.quizNumberText);
                    ImageView quizImage = quizView.findViewById(R.id.quizImage);
                    TextView questionText = quizView.findViewById(R.id.questionText);
                    RadioGroup optionsGroup = quizView.findViewById(R.id.optionsGroup);

                    quizNumberText.setText("Question " + (i + 1) + " of " + quizList.size());
                    questionText.setText(quiz.getQuestion());

                    if (quiz.getImageUrl() != null && !quiz.getImageUrl().isEmpty()) {
                        quizImage.setVisibility(View.VISIBLE);
                        Glide.with(requireContext()).load(quiz.getImageUrl()).into(quizImage);
                    } else {
                        quizImage.setVisibility(View.GONE);
                    }

                    String[] options = {quiz.getOptionA(), quiz.getOptionB(), quiz.getOptionC(), quiz.getOptionD()};

                    for (int j = 0; j < options.length; j++) {
                        RadioButton optionButton = new RadioButton(getContext());
                        optionButton.setText(options[j]);
                        optionButton.setId(j);
                        optionsGroup.addView(optionButton);
                    }

                    optionsGroup.setOnCheckedChangeListener((group, checkedId) -> {
                        char selectedOption = (char) ('A' + checkedId);
                        userAnswers.put(quizIndex, selectedOption);

                        // Play selection sound safely
                        playOptionSelectSound();
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

        for (int i = 0; i < quizList.size(); i++) {
            QuizInfo quiz = quizList.get(i);
            Character userAnswer = userAnswers.get(i);

            if (userAnswer != null && userAnswer == quiz.getCorrectOption()) {
                correctCount++;
            }
        }

        // Play result sound safely
        playResultSound();

        // Show GIF-based result dialog
        showResultDialog(this, correctCount, quizList.size());
    }

    private void startBackgroundMusic() {
        if (backgroundMusic == null) {
            backgroundMusic = MediaPlayer.create(requireContext(), R.raw.quize);
            backgroundMusic.setLooping(true);
        }
        if (!backgroundMusic.isPlaying()) {
            backgroundMusic.start();
        }
    }

    private void playOptionSelectSound() {
        releaseMediaPlayer(optionSelectSound);
        optionSelectSound = MediaPlayer.create(requireContext(), R.raw.option_select);
        optionSelectSound.start();
    }

    private void playResultSound() {
        releaseMediaPlayer(resultSound);
        resultSound = MediaPlayer.create(requireContext(), R.raw.well_done_sound);
        resultSound.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startBackgroundMusic();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer(backgroundMusic);
        releaseMediaPlayer(resultSound);
        releaseMediaPlayer(optionSelectSound);
    }

    private void releaseMediaPlayer(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}