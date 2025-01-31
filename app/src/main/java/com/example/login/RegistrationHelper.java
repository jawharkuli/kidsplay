package com.example.login;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationHelper extends DatabaseHelper {
    private static final String TAG = "RegistrationHelper";

    public interface RegistrationCallback {
        void onRegistrationResult(boolean success, String message);
    }

    @SuppressLint("StaticFieldLeak")
    public void registerUser(final String phone, final String email,
                             final String password, final RegistrationCallback callback) {
        new AsyncTask<Void, Void, ConnectionResult>() {
            @Override
            protected ConnectionResult doInBackground(Void... voids) {
                ConnectionResult result = new ConnectionResult();
                Connection connection = null;
                PreparedStatement pstmt = null;

                try {
                    connection = CONN();
                    String query = "INSERT INTO users (phone, email, password) VALUES (?, ?, ?)";
                    pstmt = connection.prepareStatement(query);
                    pstmt.setString(1, phone);
                    pstmt.setString(2, email); // Note: In production, use password hashing
                    pstmt.setString(3, password);

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        result.setSuccess(true);
                        result.setMessage("Registration successful");
                    } else {
                        result.setSuccess(false);
                        result.setMessage("Registration failed");
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    result.setSuccess(false);
                    if (e.getMessage().contains("Duplicate entry")) {
                        result.setMessage("Username or email already exists");
                    } else {
                        result.setMessage("Registration error: " + e.getMessage());
                    }
                    Log.e(TAG, "Registration error", e);
                } finally {
                    closeResources(connection, pstmt, null);
                }
                return result;
            }

            @Override
            protected void onPostExecute(ConnectionResult result) {
                if (callback != null) {
                    callback.onRegistrationResult(result.isSuccess(), result.getMessage());
                }
            }
        }.execute();
    }

    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            Log.e(TAG, "Error closing database resources", e);
        }
    }
}
