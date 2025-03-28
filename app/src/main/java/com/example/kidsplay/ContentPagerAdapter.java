package com.example.login;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class ContentPagerAdapter extends FragmentStateAdapter {
    // Define positions for fragments
    public static final int POSITION_RHYMES = 0;
    public static final int POSITION_QUIZZES = 1;
    public static final int POSITION_TRAINING = 2;
    private static final int TOTAL_PAGES = 3;

    // Constructor
    public ContentPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<ContentItem> contentItems) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case POSITION_RHYMES:
                return new RhymesFragment();
            case POSITION_QUIZZES:
                return new QuizzesFragment();
            case POSITION_TRAINING:
                return new TrainingFragment();
            default:
                throw new IllegalArgumentException("Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return TOTAL_PAGES;
    }

    public RhymesFragment getRhymesFragment() {
        return new RhymesFragment();
    }

    public QuizzesFragment getQuizzesFragment() {
        return new QuizzesFragment();
    }

    public TrainingFragment getTrainingFragment() {
        return new TrainingFragment();
    }
}
