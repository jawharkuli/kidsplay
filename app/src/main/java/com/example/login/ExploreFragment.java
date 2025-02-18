package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

public class ExploreFragment extends Fragment {

    private Spinner classSpinner;
    private Button exploreButton;  // Add this button
    private String selectedClass;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        classSpinner = view.findViewById(R.id.class_spinner);
        exploreButton = view.findViewById(R.id.explore_button);  // Initialize button

        // Sample data (Replace with dynamic data if needed)
        String[] classList = {"Select Class", "LKG", "UKG", "Class 1", "Class 2", "Class 3", "Class 4"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, classList);
        classSpinner.setAdapter(adapter);

        // Handle Spinner selection
        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) { // Avoid "Select Class" option
                    selectedClass = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set Button Click Listener
        exploreButton.setOnClickListener(v -> {
            if (selectedClass != null && !selectedClass.equals("Select Class")) {
                openContentActivity(selectedClass);
            } else {
                Toast.makeText(getActivity(), "Please select a class!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void openContentActivity(String selectedClass) {
        Intent intent = new Intent(getActivity(), ContentActivity.class);
        intent.putExtra("selectedClass", selectedClass);
        startActivity(intent);
    }
}
