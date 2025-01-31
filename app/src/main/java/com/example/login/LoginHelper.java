package com.example.login;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginHelper extends DatabaseHelper {
    private static final String TAG = "LoginHelper";

    public interface LoginCallback {
        void onLoginResult(boolean success, String message);
    }

    @SuppressLint("StaticFieldLeak")
    public void loginUser(final String username, final String password, final LoginCallback callback) {
        new AsyncTask<Void, Void, ConnectionResult>() {
            @Override
            protected ConnectionResult doInBackground(Void... voids) {
                ConnectionResult result = new ConnectionResult();
                Connection connection = null;
                PreparedStatement pstmt = null;
                ResultSet rs = null;

                try {
                    connection = CONN();
                    String query = "SELECT * FROM users WHERE email = ? AND password = ?";
                    pstmt = connection.prepareStatement(query);
                    pstmt.setString(1, username);
                    pstmt.setString(2, password); // Note: In production, use password hashing

                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        result.setSuccess(true);
                        result.setMessage("Login successful");
                    } else {
                        result.setSuccess(false);
                        result.setMessage("Invalid username or password");
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    result.setSuccess(false);
                    result.setMessage("Login error: " + e.getMessage());
                    Log.e(TAG, "Login error", e);
                } finally {
                    closeResources(connection, pstmt, rs);
                }
                return result;
            }

            @Override
            protected void onPostExecute(ConnectionResult result) {
                if (callback != null) {
                    callback.onLoginResult(result.isSuccess(), result.getMessage());
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