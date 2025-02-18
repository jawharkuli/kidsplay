package com.example.login;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";

    // Database Configuration
    private static final String DB_NAME = "project";
    private static final String DB_USER = "admin";
    private static final String DB_PASS = "password";
    private static final String DB_IP = "192.168.43.216";
    private static final String DB_PORT = "3306";
    private static final int TIMEOUT = 5000;
    private static String username = "";
    private static String userEmail = "";
    private static int userCid = 0;

    // Getters and Setters
    public String getUserEmail() { return userEmail; }
    public static void setUserEmail(String userEmail) { DatabaseHelper.userEmail = userEmail; }
    public String getUsername() { return username; }
    public static void setUsername(String username) { DatabaseHelper.username = username; }
    public int getUserCid() { return userCid; }
    public static void setUserCid(int cid) { DatabaseHelper.userCid = cid; }

    // Class data model
    public static class ClassInfo {
        private int id;
        private String name;

        public ClassInfo(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() { return id; }
        public String getName() { return name; }

        @Override
        public String toString() {
            return name;
        }
    }

    // Callbacks
    public interface DatabaseCallback {
        void onResult(boolean success, String message);
    }

    public interface ClassesCallback {
        void onResult(List<ClassInfo> classes);
    }

    // Password Hashing Method
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
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            Properties props = new Properties();
            props.put("user", DB_USER);
            props.put("password", DB_PASS);
            props.put("connectTimeout", String.valueOf(TIMEOUT));
            props.put("socketTimeout", String.valueOf(TIMEOUT));
            props.put("autoReconnect", "true");
            props.put("useSSL", "false");

            String connectionString = String.format("jdbc:mysql://%s:%s/%s", DB_IP, DB_PORT, DB_NAME);
            return DriverManager.getConnection(connectionString, props);
        } catch (ClassNotFoundException | SQLException e) {
            Log.e(TAG, "Connection error", e);
            throw new SQLException("Database connection failed: " + e.getMessage());
        }
    }

    // Fetch Classes Method
    @SuppressLint("StaticFieldLeak")
    public void fetchClasses(final ClassesCallback callback) {
        new AsyncTask<Void, Void, List<ClassInfo>>() {
            @Override
            protected List<ClassInfo> doInBackground(Void... voids) {
                List<ClassInfo> classes = new ArrayList<>();
                try (Connection conn = getConnection();
                     PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM class ORDER BY id");
                     ResultSet rs = pstmt.executeQuery()) {

                    while (rs.next()) {
                        classes.add(new ClassInfo(
                                rs.getInt("id"),
                                rs.getString("classname")
                        ));
                    }
                } catch (SQLException e) {
                    Log.e(TAG, "Error fetching classes", e);
                }
                return classes;
            }

            @Override
            protected void onPostExecute(List<ClassInfo> classes) {
                callback.onResult(classes);
            }
        }.execute();
    }

    // User Registration Method
    @SuppressLint("StaticFieldLeak")
    public void registerUser(final String username, final String phone, final String email,
                             final String password, final int cid, final DatabaseCallback callback) {
        new AsyncTask<Void, Void, ConnectionResult>() {
            @Override
            protected ConnectionResult doInBackground(Void... voids) {
                ConnectionResult result = new ConnectionResult();
                try (Connection conn = getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(
                             "INSERT INTO users (username, phone, email, password, cid) VALUES (?, ?, ?, ?, ?)")) {

                    String hashedPassword = hashPassword(password);
                    pstmt.setString(1, username);
                    pstmt.setString(2, phone);
                    pstmt.setString(3, email);
                    pstmt.setString(4, hashedPassword);
                    pstmt.setInt(5, cid);

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
                    if (e.getMessage().contains("Duplicate")) {
                        if (e.getMessage().contains("username")) {
                            result.setMessage("Username already exists");
                        } else if (e.getMessage().contains("email")) {
                            result.setMessage("Email already exists");
                        } else if (e.getMessage().contains("phone")) {
                            result.setMessage("Phone number already exists");
                        } else {
                            result.setMessage("User already exists");
                        }
                    } else {
                        result.setMessage("Registration error: " + e.getMessage());
                    }
                    Log.e(TAG, "Registration error", e);
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
    public void loginUser(final String email, final String password, final DatabaseCallback callback) {
        new AsyncTask<Void, Void, ConnectionResult>() {
            @Override
            protected ConnectionResult doInBackground(Void... voids) {
                ConnectionResult result = new ConnectionResult();
                try (Connection conn = getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(
                             "SELECT * FROM users WHERE email = ? AND password = ?")) {

                    String hashedPassword = hashPassword(password);
                    pstmt.setString(1, email);
                    pstmt.setString(2, hashedPassword);

                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            username = rs.getString("username");
                            userEmail = rs.getString("email");
                            userCid = rs.getInt("cid");
                            result.setSuccess(true);
                            result.setMessage("Login successful");
                        } else {
                            result.setSuccess(false);
                            result.setMessage("Invalid email or password");
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

    // Internal Result Handling Class
    static class ConnectionResult {
        private boolean success;
        private String message;

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}