package com.example.kidsplay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class SettingsFragment extends Fragment {

    private Switch notificationsSwitch, darkModeSwitch, privacySwitch;
    private TextView btnEditProfile, languageSettings, helpSupport;
    private Button btnLogout;
    private TextView appVersion;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false); // Make sure to create fragment_settings.xml from activity_settings.xml

        preferences = requireContext().getSharedPreferences("user_settings", getContext().MODE_PRIVATE);
        editor = preferences.edit();

        // Initialize Views
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        //languageSettings = view.findViewById(R.id.language_settings);
      //  helpSupport = view.findViewById(R.id.help_support);
        btnLogout = view.findViewById(R.id.btn_logout);
        appVersion = view.findViewById(R.id.app_version);

        notificationsSwitch = view.findViewById(R.id.notifications_switch);
        darkModeSwitch = view.findViewById(R.id.dark_mode);
        privacySwitch = view.findViewById(R.id.privacy_switch);

        // Load previous switch states
        notificationsSwitch.setChecked(preferences.getBoolean("notifications_enabled", true));
        darkModeSwitch.setChecked(preferences.getBoolean("dark_mode_enabled", false));
        privacySwitch.setChecked(preferences.getBoolean("data_sharing_enabled", true));

        // Switch Listeners
        notificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("notifications_enabled", isChecked);
            editor.apply();
            Toast.makeText(getContext(), isChecked ? "Notifications Enabled" : "Notifications Disabled", Toast.LENGTH_SHORT).show();
        });

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("dark_mode_enabled", isChecked);
            editor.apply();
            Toast.makeText(getContext(), isChecked ? "Dark Mode Enabled" : "Light Mode Enabled", Toast.LENGTH_SHORT).show();
        });

        privacySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("data_sharing_enabled", isChecked);
            editor.apply();
            Toast.makeText(getContext(), isChecked ? "Data Sharing On" : "Data Sharing Off", Toast.LENGTH_SHORT).show();
        });

        // Button Clicks
        btnEditProfile.setOnClickListener(v -> openProfileSettings());
       // languageSettings.setOnClickListener(v -> openLanguageSettings());
       // helpSupport.setOnClickListener(v -> openHelpSupport());
        btnLogout.setOnClickListener(v -> logoutUser());

        // Set App Version
        appVersion.setText("Version 1.0.0");

        return view;
    }
    public void openProfileSettings() {
        if (getActivity() != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new ProfileFragment());
            transaction.addToBackStack("ProfileFragment"); // Optional tag for back stack tracking
            transaction.commit();

        }
    }

    public void openLanguageSettings() {
        Toast.makeText(getContext(), "Opening Language Settings", Toast.LENGTH_SHORT).show();
        // You can replace this with actual navigation if you have a LanguageFragment or Activity
    }

    public void openHelpSupport() {
        Toast.makeText(getContext(), "Opening Help & Support", Toast.LENGTH_SHORT).show();
        // You can replace this with actual navigation if you have a HelpSupportFragment or Activity
    }

    public void logoutUser() {

        requireActivity().finish(); // finish the host activity


    Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}
