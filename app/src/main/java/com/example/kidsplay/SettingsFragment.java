package com.example.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatDelegate;

import org.jetbrains.annotations.Nullable;

public class SettingsFragment extends Fragment {
    private Switch darkModeSwitch;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize Views
        darkModeSwitch = view.findViewById(R.id.dark_mode);
        sharedPreferences = requireContext().getSharedPreferences("UserSettings", Context.MODE_PRIVATE);

        // Load settings
        loadSettings();

        // Handle dark mode toggle
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save dark mode preference in shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("darkMode", isChecked);
            editor.apply();

            // Apply the theme change without restarting the activity
            applyTheme(isChecked);
        });

        return view;
    }

    // Load the settings (dark mode in this case) when the fragment is created
    private void loadSettings() {
        boolean isDarkMode = sharedPreferences.getBoolean("darkMode", false);
        darkModeSwitch.setChecked(isDarkMode);

        // Apply the theme if the user has selected dark mode previously
        applyTheme(isDarkMode);
    }

    // Method to apply the selected theme (dark or light mode)
    private void applyTheme(boolean isDarkMode) {
        if (isDarkMode) {
            // Apply dark theme using AppCompatDelegate
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            // Apply light theme using AppCompatDelegate
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Avoid restarting the activity to prevent blinking
        // Instead, we apply the theme without restarting the whole activity
        // Changes will take effect immediately
    }
}
