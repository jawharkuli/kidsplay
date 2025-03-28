package com.example.kidsplay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.Nullable;

public class ProfileFragment extends Fragment {
    private ImageView profileImage, navProfileImage;
    private EditText nameEditText, bioEditText, classEditText;
    private TextView displayName, displayBio, displayClass, navEmail, navName;
    private LinearLayout displayLayout, editLayout;
    private Button saveProfileButton, logoutButton;  // Declare logoutButton
    private SharedPreferences sharedPreferences;
    private Uri selectedImageUri;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // ProfileFragment Views
        profileImage = view.findViewById(R.id.profileImage);
        displayName = view.findViewById(R.id.display_name);
        displayClass = view.findViewById(R.id.display_class);
        displayBio = view.findViewById(R.id.display_bio);
        Button editProfileButton = view.findViewById(R.id.btn_edit_profile);
        displayLayout = view.findViewById(R.id.profile_display_layout);
        editLayout = view.findViewById(R.id.profile_edit_layout);
        nameEditText = view.findViewById(R.id.edit_name);
        classEditText = view.findViewById(R.id.edit_class);
        bioEditText = view.findViewById(R.id.edit_bio);
        saveProfileButton = view.findViewById(R.id.btn_save_profile);
        logoutButton = view.findViewById(R.id.btn_logout);  // Initialize logoutButton

        // Get NavigationView from MainActivity
        NavigationView navigationView = requireActivity().findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        // Navigation Drawer Header Views
        navProfileImage = headerView.findViewById(R.id.header_image);
        navName = headerView.findViewById(R.id.userName);
        navEmail = headerView.findViewById(R.id.userEmail);

        // Initialize SharedPreferences and DatabaseHelper
        sharedPreferences = requireContext().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        databaseHelper = new DatabaseHelper();

        // Load saved profile data
        loadUserProfile();

        // Edit Profile Button Click
        editProfileButton.setOnClickListener(v -> switchToEditMode());

        // Save Profile Button Click
        saveProfileButton.setOnClickListener(v -> saveProfileData());

        // Profile Image Click - Change Image
        profileImage.setOnClickListener(v -> selectProfileImage());

        // Logout Button Click
        logoutButton.setOnClickListener(v -> logoutUser());

        return view;
    }

    private void switchToEditMode() {
        displayLayout.setVisibility(View.GONE);
        editLayout.setVisibility(View.VISIBLE);

        // Pre-fill fields with existing data
        nameEditText.setText(displayName.getText().toString());
        classEditText.setText(displayClass.getText().toString().replace("Class: ", ""));
        bioEditText.setText(displayBio.getText().toString());
    }

    private void saveProfileData() {
        String name = nameEditText.getText().toString().trim();
        String className = classEditText.getText().toString().trim();
        String bio = bioEditText.getText().toString().trim();

        if (name.isEmpty() || className.isEmpty() || bio.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("class", className);
        editor.putString("bio", bio);
        if (selectedImageUri != null) {
            editor.putString("profileImageUri", selectedImageUri.toString());
        }
        editor.apply();

        // Update UI with new values
        loadProfileData();

        // Switch back to display mode
        displayLayout.setVisibility(View.VISIBLE);
        editLayout.setVisibility(View.GONE);
    }

    private void loadProfileData() {
        String name = sharedPreferences.getString("name", "Your Name");
        String userClass = sharedPreferences.getString("class", "Not Set");
        String bio = sharedPreferences.getString("bio", "Short Bio");
        String imageUri = sharedPreferences.getString("profileImageUri", "");

        // Set profile fragment views
        displayName.setText(name);
        displayClass.setText("Class: " + userClass);
        displayBio.setText(bio);

        // Load profile image if available
        if (!imageUri.isEmpty()) {
            Glide.with(this).load(Uri.parse(imageUri)).into(profileImage);
            Glide.with(this).load(Uri.parse(imageUri)).into(navProfileImage);
        }
    }

    private void selectProfileImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void loadUserProfile() {
        // Get user profile data from the database
        String username = databaseHelper.getUsername();
        String userEmail = databaseHelper.getUserEmail();

        if (username != null && userEmail != null) {
            navName.setText(username);
            navEmail.setText(userEmail);
        } else {
            navName.setText("No name available");
            navEmail.setText("No email available");
        }
    }

    private void logoutUser() {
        // Clear SharedPreferences (user session)
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();  // Clear all user-related preferences
        editor.apply();

        // Redirect to Login Activity or main activity
        Intent intent = new Intent(getActivity(), LoginActivity.class); // Adjust with your login activity
        startActivity(intent);
        requireActivity().finish();  // Close the current activity (ProfileActivity)
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (getActivity() != null && result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();  // Correctly obtain the image URI
                        Glide.with(ProfileFragment.this).load(selectedImageUri).into(profileImage);
                        Glide.with(ProfileFragment.this).load(selectedImageUri).into(navProfileImage);

                        // Save new image URI instantly
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("profileImageUri", selectedImageUri.toString());
                        editor.apply();
                    }
                }
            });
}


