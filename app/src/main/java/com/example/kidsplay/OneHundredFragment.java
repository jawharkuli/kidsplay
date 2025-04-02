package com.example.kidsplay;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class OneHundredFragment extends Fragment {
    private ViewPager2 viewPager;
    private List<letters> letterList;
    private A_ZAdapter oneHundredAdapter;  // Ensure the correct adapter is used

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_100, container, false); // Ensure this layout exists

        viewPager = view.findViewById(R.id.viewPager);

        // Initialize the letters list
        initializeLetters();

        // Set up adapter
        oneHundredAdapter = new A_ZAdapter(requireContext(), letterList);
        viewPager.setAdapter(oneHundredAdapter);

        return view;
    }

    private void initializeLetters() {
        letterList = new ArrayList<>();

        letterList.add(new letters("", R.drawable.num, true, Color.RED));

        letterList.add(new letters("One", R.drawable.one, true, Color.RED));

        // Continue adding up to 100
    }
}
