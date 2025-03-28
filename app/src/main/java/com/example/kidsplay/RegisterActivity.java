package com.example.kidsplay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextPhone, editTextEmail, editTextPassword, editTextConfirmPassword, editTextUsername;
    private Spinner spinnerClass;
    private Button buttonRegister;
    private ProgressBar progressBar;
    private DatabaseHelper databaseHelper;
    private TextView textViewLogin;
    private int selectedClassId = -1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper();

        // Initialize Views
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        progressBar = findViewById(R.id.progressBar);
        editTextUsername = findViewById(R.id.editTextUsername);
        textViewLogin = findViewById(R.id.textViewLogin);
        spinnerClass = findViewById(R.id.spinnerClass);

        // Load classes into spinner
        loadClassSpinner();

        // Register Button Click Listener
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void loadClassSpinner() {
        progressBar.setVisibility(View.VISIBLE);
        databaseHelper.fetchClasses(classes -> {
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);

                ArrayAdapter<DatabaseHelper.ClassInfo> adapter = new ArrayAdapter<>(
                        RegisterActivity.this,
                        android.R.layout.simple_spinner_item,
                        classes
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerClass.setAdapter(adapter);

                spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        DatabaseHelper.ClassInfo selectedClass = (DatabaseHelper.ClassInfo) parent.getItemAtPosition(position);
                        selectedClassId = selectedClass.getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        selectedClassId = -1;
                    }
                });
            });
        });
    }

    private void registerUser() {
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        String username = editTextUsername.getText().toString().trim();

        // Validate input
        if (username.isEmpty()) {
            editTextUsername.setError("Username is required");
            editTextUsername.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            editTextPhone.setError("Phone number is required");
            editTextPhone.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Password should be at least 6 characters long");
            editTextPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Passwords don't match");
            editTextConfirmPassword.requestFocus();
            return;
        }

        if (selectedClassId == -1) {
            Toast.makeText(this, "Please select a class", Toast.LENGTH_SHORT).show();
            spinnerClass.requestFocus();
            return;
        }

        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);
        buttonRegister.setEnabled(false);

        // Attempt registration
        databaseHelper.registerUser(username, phone, email, password, selectedClassId, new DatabaseHelper.DatabaseCallback() {
            @Override
            public void onResult(boolean success, String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        buttonRegister.setEnabled(true);

                        if (success) {
                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}