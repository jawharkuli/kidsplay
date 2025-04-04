package com.example.kidsplay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;
    private ProgressBar progressBar;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize DatabaseHelper and SharedPreferences
        databaseHelper = new DatabaseHelper();
        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        buttonLogin.setOnClickListener(v -> loginUser());
        textViewRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
        );
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (!validateInput(email, password)) {
            return;
        }

        showLoading(true);

        databaseHelper.loginUser(email, password, new DatabaseHelper.DatabaseCallback() {
            @Override
            public void onResult(boolean success, String message) {
                runOnUiThread(() -> {
                    showLoading(false);

                    if (success) {
                        saveUserData();
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        navigateToMain();
                    } else {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private boolean validateInput(String email, String password) {
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Password should be at least 6 characters");
            editTextPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void saveUserData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", databaseHelper.getUsername());
        editor.putString("email", databaseHelper.getUserEmail());
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        buttonLogin.setEnabled(!isLoading);
        editTextEmail.setEnabled(!isLoading);
        editTextPassword.setEnabled(!isLoading);
        textViewRegister.setEnabled(!isLoading);
    }

    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is already logged in
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            navigateToMain();
        }
    }
}