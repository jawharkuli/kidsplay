package com.example.login;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.io.IOException;

public class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";

    // Database Configuration
    private static final String DB_NAME = "project";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";
    private static final String DB_IP = "192.168.115.137";
    private static final int DB_PORT = 3306;
    private static final int TIMEOUT = 5000;

    // Callback Interfaces
    public interface DatabaseCallback {
        void onResult(boolean success, String message);
    }

    // Hashing Method for Password
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Password hashing error", e);
            return null;
        }
    }

    // Database Connection Method
    private Connection getConnection() throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            Properties props = new Properties();
            props.put("user", DB_USER);
            props.put("password", DB_PASS);
            props.put("connectTimeout", String.valueOf(TIMEOUT));
            props.put("socketTimeout", String.valueOf(TIMEOUT));
            props.put("autoReconnect", "true");
            props.put("useSSL", "false");

            @SuppressLint("DefaultLocale") String connectionString = String.format("jdbc:mysql://%s:%d/%s", DB_IP, DB_PORT, DB_NAME);
            return DriverManager.getConnection(connectionString, props);
        } catch (ClassNotFoundException | SQLException e) {
            Log.e(TAG, "Connection error", e);
            throw e;
        }
    }

    // Test Connection Method
    @SuppressLint("StaticFieldLeak")
    public void testConnection(final DatabaseCallback callback) {
        new AsyncTask<Void, Void, ConnectionResult>() {
            @Override
            protected ConnectionResult doInBackground(Void... voids) {
                ConnectionResult result = new ConnectionResult();
                try (Connection conn = getConnection()) {
                    result.setSuccess(true);
                    result.setMessage("Database connection successful");
                } catch (Exception e) {
                    result.setSuccess(false);
                    result.setMessage("Connection failed: " + e.getMessage());
                }
                return result;
            }

            @Override
            protected void onPostExecute(ConnectionResult result) {
                callback.onResult(result.isSuccess(), result.getMessage());
            }
        }.execute();
    }

    // User Login Method
    @SuppressLint("StaticFieldLeak")
    public void loginUser(final String username, final String password, final DatabaseCallback callback) {
        new AsyncTask<Void, Void, ConnectionResult>() {
            @Override
            protected ConnectionResult doInBackground(Void... voids) {
                ConnectionResult result = new ConnectionResult();
                try (Connection conn = getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(
                             "SELECT * FROM users WHERE email = ? AND password = ?")) {

                    String hashedPassword = hashPassword(password);
                    pstmt.setString(1, username);
                    pstmt.setString(2, hashedPassword);

                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            result.setSuccess(true);
                            result.setMessage("Login successful");
                        } else {
                            result.setSuccess(false);
                            result.setMessage("Invalid username or password");
                        }
                    }
                } catch (Exception e) {
                    result.setSuccess(false);
                    result.setMessage("Login error: " + e.getMessage());
                    Log.e(TAG, "Login error", e);
                }
                return result;
            }

            @Override
            protected void onPostExecute(ConnectionResult result) {
                callback.onResult(result.isSuccess(), result.getMessage());
            }
        }.execute();
    }

    // User Registration Method
    @SuppressLint("StaticFieldLeak")
    public void registerUser(final String phone, final String email,
                             final String password, final DatabaseCallback callback) {
        new AsyncTask<Void, Void, ConnectionResult>() {
            @Override
            protected ConnectionResult doInBackground(Void... voids) {
                ConnectionResult result = new ConnectionResult();
                try (Connection conn = getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(
                             "INSERT INTO users (phone, email, password) VALUES (?, ?, ?)")) {

                    String hashedPassword = hashPassword(password);
                    pstmt.setString(1, phone);
                    pstmt.setString(2, email);
                    pstmt.setString(3, hashedPassword);

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        result.setSuccess(true);
                        result.setMessage("Registration successful");
                    } else {
                        result.setSuccess(false);
                        result.setMessage("Registration failed");
                    }
                } catch (SQLException e) {
                    result.setSuccess(false);
                    result.setMessage(e.getMessage().contains("Duplicate")
                            ? "Username or email already exists"
                            : "Registration error: " + e.getMessage());
                    Log.e(TAG, "Registration error", e);
                } catch (Exception e) {
                    result.setSuccess(false);
                    result.setMessage("Unexpected error: " + e.getMessage());
                    Log.e(TAG, "Unexpected error", e);
                }
                return result;
            }

            @Override
            protected void onPostExecute(ConnectionResult result) {
                callback.onResult(result.isSuccess(), result.getMessage());
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void insertLog(final int userId, final String sender, final String receiver,
                          final String sourceMac, final String destinationMac,
                          final String filename, final DatabaseCallback callback) {
        new AsyncTask<Void, Void, ConnectionResult>() {
            @Override
            protected ConnectionResult doInBackground(Void... voids) {
                ConnectionResult result = new ConnectionResult();
                try (Connection conn = getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(
                             "INSERT INTO logs (id, sender, receiver, source_mac, destination_mac, filename) VALUES (?, ?, ?, ?, ?, ?)")) {

                    pstmt.setInt(1, userId);
                    pstmt.setString(2, sender);
                    pstmt.setString(3, receiver);
                    pstmt.setString(4, sourceMac);
                    pstmt.setString(5, destinationMac);
                    pstmt.setString(6, filename);

                    int rowsAffected = pstmt.executeUpdate();
                    result.setSuccess(rowsAffected > 0);
                    result.setMessage(rowsAffected > 0 ? "Log inserted successfully" : "Failed to insert log");
                } catch (Exception e) {
                    result.setSuccess(false);
                    result.setMessage("Log insertion error: " + e.getMessage());
                    Log.e(TAG, "Log insertion error", e);
                }
                return result;
            }

            @Override
            protected void onPostExecute(ConnectionResult result) {
                callback.onResult(result.isSuccess(), result.getMessage());
            }
        }.execute();
    }

    // Internal Result Handling Class
    static class ConnectionResult {
        private boolean success;
        private String message;

        public void setSuccess(boolean success) { this.success = success; }
        public boolean isSuccess() { return success; }

        public void setMessage(String message) { this.message = message; }
        public String getMessage() { return message; }
    }
}