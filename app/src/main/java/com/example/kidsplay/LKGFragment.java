package com.example.kidsplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class LKGFragment extends Fragment {
    private ImageView A_ZFragment, ABCFragment;

    private static final String TOOLBAR_TITLE = "toolbar_title";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_l_k_g, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        A_ZFragment = view.findViewById(R.id.ABCDLearning);
        ABCFragment = view.findViewById(R.id.ABCDPhonicLearning);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set initial toolbar title
        updateToolbarTitle("LKG Learning");

        // Setup back stack listener for toolbar title updates
        requireActivity().getSupportFragmentManager()
                .addOnBackStackChangedListener(() -> {
                    Fragment currentFragment = requireActivity().getSupportFragmentManager()
                            .findFragmentById(R.id.fragment_container);
                    if (currentFragment != null && currentFragment.getArguments() != null) {
                        String savedTitle = currentFragment.getArguments().getString(TOOLBAR_TITLE, "LKG Learning");
                        updateToolbarTitle(savedTitle);
                    }
                });

        setupClickListeners();
    }

    private void setupClickListeners() {
        A_ZFragment.setOnClickListener(v -> navigateToFragment(new A_ZFragment(), "A-Z Learning"));
        ABCFragment.setOnClickListener(v -> navigateToFragment(new ABCFragment(), "ABC Phonics Learning"));
    }

    private void navigateToFragment(Fragment fragment, String title) {
        // Save title in fragment arguments
        Bundle args = new Bundle();
        args.putString(TOOLBAR_TITLE, title);
        fragment.setArguments(args);

        // Update toolbar title
        updateToolbarTitle(title);

        // Perform fragment transaction with animations
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                )
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void updateToolbarTitle(String title) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateToolbarTitle(title);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update toolbar title when returning to this fragment
        updateToolbarTitle("LKG Learning");
    }
}
