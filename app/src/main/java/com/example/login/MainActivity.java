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
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) AppCompatButton insertLog = findViewById(R.id.insertLogs);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) AppCompatButton connectionTest = findViewById(R.id.connectionTest);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.loginUser("pass1@pass.com", "pass", (success, message) -> {
                    if (success) {
                        // Login successful
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } else {
                        // Login failed
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.registerUser("6002239768", "pass1@pass.com", "pass",
                        (success, message) -> {
                            if (success) {
                                // Registration successful
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            } else {
                                // Registration failed
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });

        insertLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.insertLog(1, "Jane", "John",
                        "00:1B:2C:3D:4E:5F", "00:5F:4E:3D:2C:1B", "file2.pdf",
                        (success, message) -> {
                            if (success) {
                                // Log inserted successfully
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            } else {
                                // Log insertion failed
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });

        // Test connection when a button is clicked
        connectionTest.setOnClickListener(v -> {
            // Create progress dialog
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Testing connection...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            dbHelper.testConnection((success, message) -> {
                // Dismiss progress dialog first
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                // Show toast based on connection result
                Toast.makeText(getApplicationContext(), message,
                                success ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG)
                        .show();
            });
        });
    }
}