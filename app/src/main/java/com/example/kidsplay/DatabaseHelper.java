package com.example.kidsplay;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";

    // Database Configuration - Should be moved to a secure configuration file or BuildConfig
    private static final String DB_NAME = "project";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";
    static final String DB_IP ="192.168.220.216";
    private static final String DB_PORT ="3306";
    private static final int TIMEOUT = 5000;
    public static final String BASE_URL = "http://"+DB_IP+"/project/";
    private static final String RHYMES_API_URL = BASE_URL + "get_rhymes.php";
    // User session data
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

    // Executor service for background operations
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    // Class data model
    public static class ClassInfo {
        private final int id;
        private final String name;

        public ClassInfo(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() { return id; }
        public String getName() { return name; }

        @NonNull
        @Override
        public String toString() {
            return name;
        }
    }

    // Callbacks
    public interface DatabaseCallback {
        void onResult(boolean success, String message);
    }

    public interface FilesCallback {
        void onResult(List<FileInfo> files);
    }

    public interface ClassesCallback {
        void onResult(List<ClassInfo> classes);
    }

    public interface TrainingCallback {
        void onResult(List<TrainingInfo> trainingData);

        void onError(String error);
    }

    public interface QuizCallback {
        void onResult(List<QuizInfo> quizzes);
    }
    public interface RhymesCallback {
        void onResult(List<RhymeInfo> rhymes);

        void onError(String errorMessage);
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

    // Fetch Classes Method - Using ExecutorService
    public void fetchClasses(final ClassesCallback callback) {
        executor.execute(() -> {
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

            mainHandler.post(() -> callback.onResult(classes));
        });
    }

    // User Registration Method
    public void registerUser(final String username, final String phone, final String email,
                             final String password, final int cid, final DatabaseCallback callback) {
        executor.execute(() -> {
            boolean success = false;
            String message = "";

            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(
                         "INSERT INTO users (username, phone, email, password, cid) VALUES (?, ?, ?, ?, ?)")) {

                String hashedPassword = hashPassword(password);
                if (hashedPassword == null) {
                    message = "Password hashing failed";
                    String finalMessage1 = message;
                    mainHandler.post(() -> callback.onResult(false, finalMessage1));
                    return;
                }

                pstmt.setString(1, username);
                pstmt.setString(2, phone);
                pstmt.setString(3, email);
                pstmt.setString(4, hashedPassword);
                pstmt.setInt(5, cid);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    success = true;
                    message = "Registration successful";
                } else {
                    message = "Registration failed";
                }
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate")) {
                    if (e.getMessage().contains("username")) {
                        message = "Username already exists";
                    } else if (e.getMessage().contains("email")) {
                        message = "Email already exists";
                    } else if (e.getMessage().contains("phone")) {
                        message = "Phone number already exists";
                    } else {
                        message = "User already exists";
                    }
                } else {
                    message = "Registration error: " + e.getMessage();
                }
                Log.e(TAG, "Registration error", e);
            }

            final boolean finalSuccess = success;
            final String finalMessage = message;
            mainHandler.post(() -> callback.onResult(finalSuccess, finalMessage));
        });
    }

    // User Login Method
    public void loginUser(final String email, final String password, final DatabaseCallback callback) {
        executor.execute(() -> {
            boolean success = false;
            String message = "";

            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(
                         "SELECT * FROM users WHERE email = ? AND password = ?")) {

                String hashedPassword = hashPassword(password);
                if (hashedPassword == null) {
                    message = "Password hashing failed";
                    String finalMessage1 = message;
                    mainHandler.post(() -> callback.onResult(false, finalMessage1));
                    return;
                }

                pstmt.setString(1, email);
                pstmt.setString(2, hashedPassword);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        username = rs.getString("username");
                        userEmail = rs.getString("email");
                        userCid = rs.getInt("cid");
                        success = true;
                        message = "Login successful";
                    } else {
                        message = "Invalid email or password";
                    }
                }
            } catch (Exception e) {
                message = "Login error: " + e.getMessage();
                Log.e(TAG, "Login error", e);
            }

            final boolean finalSuccess = success;
            final String finalMessage = message;
            mainHandler.post(() -> callback.onResult(finalSuccess, finalMessage));
        });
    }

    // Fetch Files
    public void fetchFiles(final FilesCallback callback) {
        executor.execute(() -> {
            List<FileInfo> files = new ArrayList<>();
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM files ORDER BY uploaded_at DESC");
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    files.add(new FileInfo(
                            rs.getInt("id"),
                            rs.getInt("cid"),
                            rs.getInt("sid"),
                            rs.getString("filetype"),
                            rs.getString("file_path"),
                            rs.getLong("file_size"),
                            rs.getString("file_link"),
                            rs.getString("filename"),
                            rs.getString("link_text"),
                            rs.getString("uploaded_at")
                    ));
                }
            } catch (SQLException e) {
                Log.e(TAG, "Error fetching files", e);
            }

            mainHandler.post(() -> callback.onResult(files));
        });
    }
    public void fetchRhymes(String className, final RhymesCallback callback) {
        executor.execute(() -> {
            List<RhymeInfo> rhymes = new ArrayList<>();
            String query = "SELECT r.* FROM rhymes r " +
                    "JOIN class c ON r.cid = c.id " +
                    "WHERE c.classname = ? " +
                    "ORDER BY r.upload_date DESC";

            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, className); // Bind class name parameter

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        rhymes.add(new RhymeInfo(
                                rs.getInt("id"),
                                rs.getInt("cid"),
                                rs.getString("title"),
                                rs.getString("description"),
                                rs.getString("filetype"),
                                rs.getString("file"),
                                rs.getString("thumbnail"),
                                rs.getString("duration"),
                                rs.getString("upload_date")
                        ));
                    }
                }
            } catch (SQLException e) {
                Log.e(TAG, "Error fetching rhymes by class name", e);
                mainHandler.post(() -> callback.onError("Error fetching rhymes: " + e.getMessage()));
                return;
            }

            mainHandler.post(() -> callback.onResult(rhymes));
        });
    }

    public void fetchQuizzesByClassName(String className, final QuizCallback callback) {
        Log.d(TAG, "Starting to fetch quizzes for class: " + className);

        // Show loading state in UI (if needed)
        executor.execute(() -> {
            List<QuizInfo> quizzes = new ArrayList<>();
            String query = "SELECT q.* FROM quize q " +
                    "INNER JOIN class c ON q.cid = c.id " +
                    "WHERE c.classname = ? " +
                    "ORDER BY q.id";

            Log.d(TAG, "Preparing SQL query: " + query + " with parameter className = " + className);

            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                conn = getConnection();
                if (conn == null) {
                    Log.e(TAG, "Database connection failed");
                    mainHandler.post(() -> callback.onResult(new ArrayList<>()));
                    return;
                }

                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, className); // Bind the class name to the query

                Log.d(TAG, "Executing query with className = " + className);
                rs = pstmt.executeQuery();

                int count = 0;
                while (rs.next()) {
                    count++;
                    int id = rs.getInt("id");
                    int cid = rs.getInt("cid");
                    String question = rs.getString("question"); // Fetching the question
                    String image = rs.getString("image");
                    String optionA = rs.getString("option_a");
                    String optionB = rs.getString("option_b");
                    String optionC = rs.getString("option_c");
                    String optionD = rs.getString("option_d");
                    String correctOptionStr = rs.getString("correct_option");
                    char correctOption = (correctOptionStr != null && !correctOptionStr.isEmpty()) ?
                            correctOptionStr.charAt(0) : 'A';

                    Log.d(TAG, "Found quiz: ID=" + id + ", CID=" + cid + ", Question=" + question);

                    quizzes.add(new QuizInfo(
                            id,
                            cid,
                            question, // Added question
                            image,
                            optionA,
                            optionB,
                            optionC,
                            optionD,
                            correctOption
                    ));
                }

                Log.d(TAG, "Query complete. Found " + count + " quizzes for class: " + className);

                mainHandler.post(() -> {
                    if (quizzes.isEmpty()) {
                        Log.d(TAG, "No quizzes found for class: " + className);
                    }
                    callback.onResult(quizzes);
                });

            } catch (SQLException e) {
                Log.e(TAG, "Error fetching quizzes for class: " + className, e);
                mainHandler.post(() -> callback.onResult(new ArrayList<>()));
            } finally {
                // Close resources
                try {
                    if (rs != null) rs.close();
                    if (pstmt != null) pstmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    Log.e(TAG, "Error closing database resources", e);
                }
            }
        });
    }

    // Fetch Training Data from Database
    public void fetchTraining(final TrainingCallback callback) {
        executor.execute(() -> {
            List<TrainingInfo> trainingData = new ArrayList<>();
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM training ORDER BY id");
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    trainingData.add(new TrainingInfo(
                            rs.getInt("id"),
                            rs.getInt("cid"),
                            rs.getString("sentences")
                    ));
                }
            } catch (SQLException e) {
                Log.e(TAG, "Error fetching training data", e);
            }

            mainHandler.post(() -> callback.onResult(trainingData));
        });
    }

    // Clean up resources
    public void cleanup() {
        executor.shutdown();
    }
}