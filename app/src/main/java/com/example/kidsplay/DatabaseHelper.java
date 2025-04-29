package com.example.kidsplay;

import android.content.Context;
import android.os.Build;
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
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import timber.log.Timber;

public class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";

    // Database Configuration - Should be moved to a secure configuration file or BuildConfig
    private static final String DB_NAME = "project";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";
    static final String DB_IP ="192.168.115.216";
    private static final String DB_PORT ="3306";
    private static final int TIMEOUT = 5000;
    public static final String BASE_URL = "http://"+DB_IP+"/project/";
    private static final String RHYMES_API_URL = BASE_URL + "get_rhymes.php";

    // User session data
    private static String username = "";
    private static String userEmail = "";
    private static int userCid = 0;
    private static long userId = -1; // Added user ID field to match SessionManager needs

    // Context
    private final Context context;
    private SessionManager sessionManager; // Added SessionManager field

    // Constructor
    public DatabaseHelper(Context context) {
        this.context = context;
        this.sessionManager = new SessionManager(context); // Initialize SessionManager
    }

    // Getters and Setters
    public String getUserEmail() { return userEmail; }
    public static void setUserEmail(String userEmail) { DatabaseHelper.userEmail = userEmail; }
    public String getUsername() { return username; }
    public static void setUsername(String username) { DatabaseHelper.username = username; }
    public int getUserCid() { return userCid; }
    public static void setUserCid(int cid) { DatabaseHelper.userCid = cid; }
    public long getUserId() { return userId; } // Added getter for user ID
    public static void setUserId(long id) { DatabaseHelper.userId = id; } // Added setter for user ID

    // New method to get SessionManager instance
    public SessionManager getSessionManager() {
        return sessionManager;
    }

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
    public static class SubjectInfo {
        private final int id;
        private final String name;
        private final String imageUrl;

        public SubjectInfo(int id, String name, String imageUrl) {
            this.id = id;
            this.name = name;
            this.imageUrl = imageUrl;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getImageUrl() { return imageUrl; }

        @NonNull
        @Override
        public String toString() {
            return name;
        }
    }
    public static class ContentInfo {
        private final int id;
        private final int cid;
        private final int sid;
        private final String title;
        private final String fileType;
        private final String filePath;
        private final String fileLink;
        private long fileSize;

        public ContentInfo(int id, int cid, int sid, String title, String fileType,
                           String filePath, String fileLink, long fileSize) {
            this.id = id;
            this.cid = cid;
            this.sid = sid;
            this.title = title;
            this.fileType = fileType;
            this.filePath = filePath;
            this.fileLink = fileLink;
            this.fileSize = fileSize;
        }

        public int getId() { return id; }
        public int getCid() { return cid; }
        public int getSid() { return sid; }
        public String getTitle() { return title; }
        public String getFileType() { return fileType; }
        public String getFilePath() { return filePath; }
        public String getFileLink() { return fileLink; }
        public long getFileSize() { return fileSize; }

        @NonNull
        @Override
        public String toString() {
            return title;
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

    public interface StoriesCallback {
        void onResult(List<StoryInfo> stories);
        void onError(String errorMessage);
    }

    public interface RhymesCallback {
        void onResult(List<RhymeInfo> rhymes);
        void onError(String errorMessage);
    }
    public interface SubjectsCallback {
        void onResult(List<SubjectInfo> subjects);
        void onError(String errorMessage);
    }
    public interface ContentCallback {
        void onResult(List<ContentInfo> content);
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
    public void fetchSubjects(final SubjectsCallback callback) {
        executor.execute(() -> {
            List<SubjectInfo> subjects = new ArrayList<>();
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                conn = getConnection();
                if (conn == null) {
                    Log.e(TAG, "Database connection failed");
                    mainHandler.post(() -> callback.onError("Database connection failed"));
                    return;
                }

                String query = "SELECT * FROM subjects ORDER BY id";
                pstmt = conn.prepareStatement(query);

                Log.d(TAG, "Executing query to fetch subjects");
                rs = pstmt.executeQuery();

                int count = 0;
                while (rs.next()) {
                    count++;
                    subjects.add(new SubjectInfo(
                            rs.getInt("id"),
                            rs.getString("sname"),
                            BASE_URL + rs.getString("image")
                    ));

                    Log.d(TAG, "Found subject: ID=" + rs.getInt("id") + ", Name=" + rs.getString("sname"));
                }

                Log.d(TAG, "Query complete. Found " + count + " subjects");

                mainHandler.post(() -> {
                    if (subjects.isEmpty()) {
                        Log.d(TAG, "No subjects found in database");
                    }
                    callback.onResult(subjects);
                });

            } catch (SQLException e) {
                Log.e(TAG, "Error fetching subjects", e);
                mainHandler.post(() -> callback.onError("Error fetching subjects: " + e.getMessage()));
            } finally {
                // Close resources properly
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

    // User Registration Method
    public void registerUser(final String username, final String phone, final String email,
                             final String password, final int cid, final DatabaseCallback callback) {
        executor.execute(() -> {
            boolean success = false;
            String message = "";
            long newUserId = -1;

            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(
                         "INSERT INTO users (username, phone, email, password, cid) VALUES (?, ?, ?, ?, ?)",
                         PreparedStatement.RETURN_GENERATED_KEYS)) {

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
                    // Get the generated user ID
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            newUserId = generatedKeys.getLong(1);
                            // Set static user data
                            DatabaseHelper.username = username;
                            DatabaseHelper.userEmail = email;
                            DatabaseHelper.userCid = cid;
                            DatabaseHelper.userId = newUserId;

                            // Create a session for the new user
                            String sessionId = UUID.randomUUID().toString();
                            sessionManager.createSession(sessionId, newUserId, username, email);
                        }
                    }

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
                        // Retrieve user data
                        long retrievedUserId = rs.getLong("id"); // Get the user ID
                        username = rs.getString("username");
                        userEmail = rs.getString("email");
                        userCid = rs.getInt("cid");
                        userId = retrievedUserId; // Store the user ID

                        // Create session for the logged in user
                        String sessionId = UUID.randomUUID().toString();
                        sessionManager.createSession(sessionId, retrievedUserId, username, email);

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

    // Check if user session is valid
    public boolean isUserLoggedIn() {
        return sessionManager.isValidSession();
    }

    // Log out user
    public void logoutUser() {
        sessionManager.destroySession();
        username = "";
        userEmail = "";
        userCid = 0;
        userId = -1;
    }

    // Fetch Files
    public void fetchContentForSubject(String subjectName, String className, final ContentCallback callback) {
        Log.d(TAG, "Starting to fetch content for class: " + className + " and subject name: " + subjectName);

        executor.execute(() -> {
            List<ContentInfo> contentList = new ArrayList<>();
            String query = "SELECT f.* FROM files f " +
                    "JOIN class c ON f.cid = c.id " +
                    "JOIN subjects s ON f.sid = s.id " +
                    "WHERE c.classname = ? AND s.sname = ? " +
                    "ORDER BY f.uploaded_at DESC";

            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                conn = getConnection();
                if (conn == null) {
                    Log.e(TAG, "Database connection failed");
                    mainHandler.post(() -> callback.onError("Database connection failed"));
                    return;
                }

                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, className);
                pstmt.setString(2, subjectName);

                Log.d(TAG, "Executing query with className = " + className + " and subjectName = " + subjectName);
                rs = pstmt.executeQuery();

                int count = 0;
                while (rs.next()) {
                    count++;
                    contentList.add(new ContentInfo(
                            rs.getInt("id"),
                            rs.getInt("cid"),
                            rs.getInt("sid"),
                            rs.getString("filename"),
                            rs.getString("filetype"),
                            rs.getString("file_path"),
                            rs.getString("file_link"),
                            rs.getLong("file_size")
                    ));

                    Timber.tag(TAG).d("Found content: ID=" + rs.getInt("id") +
                            ", Filename=" + rs.getString("filename") +
                            ", Type=" + rs.getString("filetype"));
                }

                Timber.tag(TAG).d("Query complete. Found " + count + " content items for class: " +
                        className + " and subject name: " + subjectName);

                mainHandler.post(() -> {
                    if (contentList.isEmpty()) {
                        Timber.tag(TAG).d("No content found for class: " + className + " and subject name: " + subjectName);
                    }
                    callback.onResult(contentList);
                });

            } catch (SQLException e) {
                Timber.tag(TAG).e(e, "Error fetching content for class: " + className + " and subject name: " + subjectName);
                mainHandler.post(() -> callback.onError("Error fetching content: " + e.getMessage()));
            } finally {
                // Close resources properly
                try {
                    if (rs != null) rs.close();
                    if (pstmt != null) pstmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    Timber.tag(TAG).e(e, "Error closing database resources");
                }
            }
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

    // Modified fetchStories method to better align with StoriesFragment
    public void fetchStories(String className, final StoriesCallback callback) {
        Timber.tag(TAG).d("Starting to fetch stories for class: %s", className);

        executor.execute(() -> {
            List<StoryInfo> stories = new ArrayList<>();
            String query = "SELECT s.* FROM stories s " +
                    "JOIN class c ON s.cid = c.id " +
                    "WHERE c.classname = ? " +
                    "ORDER BY s.created_at DESC";

            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                conn = getConnection();
                if (conn == null) {
                    Timber.tag(TAG).e("Database connection failed");
                    mainHandler.post(() -> callback.onError("Database connection failed"));
                    return;
                }

                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, className); // Bind class name parameter

                Timber.tag(TAG).d("Executing query with className = %s", className);
                rs = pstmt.executeQuery();

                int count = 0;
                while (rs.next()) {
                    count++;
                    stories.add(new StoryInfo(
                            rs.getInt("id"),
                            rs.getInt("cid"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("filetype"),
                            rs.getString("file"),
                            rs.getString("thumbnail"),
                            rs.getString("created_at")
                    ));
                }

                Timber.tag(TAG).d("Query complete. Found " + count + " stories for class: " + className);

                mainHandler.post(() -> {
                    if (stories.isEmpty()) {
                        Timber.tag(TAG).d("No stories found for class: %s", className);
                    }
                    callback.onResult(stories);
                });

            } catch (SQLException e) {
                Timber.tag(TAG).e(e, "Error fetching stories by class name");
                mainHandler.post(() -> callback.onError("Error fetching stories: " + e.getMessage()));
            } finally {
                // Close resources properly
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
                    char correctOption = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                        correctOption = (correctOptionStr != null && !correctOptionStr.isEmpty()) ?
                                correctOptionStr.charAt(0) : 'A';
                    }

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

    // Clean up resources
    public void cleanup() {
        executor.shutdown();
    }
}