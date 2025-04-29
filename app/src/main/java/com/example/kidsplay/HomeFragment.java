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
import com.bumptech.glide.Glide;

public class HomeFragment extends Fragment {
    private CardView animals, flowers, fruits, insects;
    private static final String TOOLBAR_TITLE = "toolbar_title";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        animals = view.findViewById(R.id.animals);
        flowers = view.findViewById(R.id.flowers);
        fruits = view.findViewById(R.id.fruits);
        insects = view.findViewById(R.id.insects);

        // Load GIFs from raw folder using Glide
        Glide.with(this)
                .asGif()
                .load(R.raw.animals_gif) // Load GIF from raw folder
                .into((ImageView) view.findViewById(R.id.animals_image));

        Glide.with(this)
                .asGif()
                .load(R.raw.flowers_image) // Load GIF from raw folder
                .into((ImageView) view.findViewById(R.id.flowers_image));

        Glide.with(this)
                .asGif()
                .load(R.raw.fruit_gif) // Load GIF from raw folder
                .into((ImageView) view.findViewById(R.id.fruits_image));

        Glide.with(this)
                .asGif()
                .load(R.raw.insect_gif) // Load GIF from raw folder
                .into((ImageView) view.findViewById(R.id.insects_image));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set initial toolbar title
        updateToolbarTitle("Home");

        // Setup back stack listener
        requireActivity().getSupportFragmentManager()
                .addOnBackStackChangedListener(() -> {
                    Fragment currentFragment = requireActivity().getSupportFragmentManager()
                            .findFragmentById(R.id.fragment_container);
                    if (currentFragment != null) {
                        String savedTitle = currentFragment.getArguments() != null ?
                                currentFragment.getArguments().getString(TOOLBAR_TITLE) : "Home";
                        updateToolbarTitle(savedTitle);
                    }
                });

        setupClickListeners();
    }

    private void setupClickListeners() {
        animals.setOnClickListener(v -> navigateToFragment(new Animals(), "Animals"));
        flowers.setOnClickListener(v -> navigateToFragment(new Flowers(), "Flowers"));
        fruits.setOnClickListener(v -> navigateToFragment(new Fruits(), "Fruits"));
        insects.setOnClickListener(v -> navigateToFragment(new Insects(), "Insects"));
    }

    private void navigateToFragment(Fragment fragment, String title) {
        // Save title in fragment arguments
        Bundle args = new Bundle();
        args.putString(TOOLBAR_TITLE, title);
        fragment.setArguments(args);

        // Update toolbar title
        updateToolbarTitle(title);

        // Perform fragment transaction
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
        updateToolbarTitle("Home");
    }
}
