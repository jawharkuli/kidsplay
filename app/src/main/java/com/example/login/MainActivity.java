package com.example.login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) AppCompatButton login = findViewById(R.id.login);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) AppCompatButton register = findViewById(R.id.create);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginHelper loginHelper = new LoginHelper();
                loginHelper.loginUser("test@example.com", "password123", new LoginHelper.LoginCallback() {
                    @Override
                    public void onLoginResult(boolean success, String message) {
                        if (success) {
                            // Navigate to main activity
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        } else {
                            // Show error message
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrationHelper registrationHelper = new RegistrationHelper();
                registrationHelper.registerUser("6002239760", "test2@test.com", "test123",
                        new RegistrationHelper.RegistrationCallback() {
                            @Override
                            public void onRegistrationResult(boolean success, String message) {
                                if (success) {
                                    // Show success, navigate to login
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                } else {
                                    // Show error message
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Test connection when a button is clicked
        findViewById(R.id.login_button).setOnClickListener(v -> {
            // Show progress dialog
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Testing connection...");
            progressDialog.show();

            dbHelper.testConnection(new DatabaseHelper.DatabaseConnectionCallback() {
                @Override
                public void onConnectionResult(boolean success, String message) {
                    // Dismiss progress dialog
                    progressDialog.dismiss();

                    // Show result
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this,
                                message,
                                success ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG
                        ).show();
                    });
                }
            });
        });
    }
}