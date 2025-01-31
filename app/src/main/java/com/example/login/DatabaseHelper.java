package com.example.login;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.IOException;

public class DatabaseHelper {
    protected static String db = "project"; // Replace with your database name
    protected static String user = "root";
    protected static String pass = "root";
    public static String ip = "192.168.115.137";
    private static final String TAG = "DatabaseHelper";
    private static final int PORT = 3306;
    private static final int TIMEOUT = 5000; // 5 seconds

    public interface DatabaseConnectionCallback {
        void onConnectionResult(boolean success, String message);
    }

    Connection CONN() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");

            Properties connectionProps = new Properties();
            connectionProps.put("user", user);
            connectionProps.put("password", pass);
            connectionProps.put("connectTimeout", String.valueOf(TIMEOUT));
            connectionProps.put("socketTimeout", String.valueOf(TIMEOUT));
            connectionProps.put("autoReconnect", "true");
            connectionProps.put("useSSL", "false"); // Disable SSL for testing

            @SuppressLint("DefaultLocale") String connectionString = String.format("jdbc:mysql://%s:%d/%s", ip, PORT, db);
            Log.d(TAG, "Connecting to: " + connectionString);

            conn = DriverManager.getConnection(connectionString, connectionProps);

            if (conn != null) {
                Log.d(TAG, "Database connection successful");
            }

        } catch (ClassNotFoundException e) {
            Log.e(TAG, "MySQL JDBC Driver not found", e);
            throw e;
        } catch (SQLException e) {
            Log.e(TAG, "SQL Error: " + e.getMessage());
            Log.e(TAG, "SQL State: " + e.getSQLState());
            Log.e(TAG, "Error Code: " + e.getErrorCode());
            throw e;
        }
        return conn;
    }

    @SuppressLint("StaticFieldLeak")
    public void testConnection(final DatabaseConnectionCallback callback) {
        new AsyncTask<Void, Void, ConnectionResult>() {
            @Override
            protected ConnectionResult doInBackground(Void... voids) {
                ConnectionResult result = new ConnectionResult();
                Connection connection = null;

                try {
                    if (!isHostReachable()) {
                        result.setSuccess(false);
                        result.setMessage("Cannot reach database server at " + ip + ":" + PORT);
                        return result;
                    }

                    connection = CONN();
                    if (connection != null && !connection.isClosed()) {
                        if (connection.isValid(TIMEOUT/1000)) {
                            result.setSuccess(true);
                            result.setMessage("Connection successful");
                        } else {
                            result.setSuccess(false);
                            result.setMessage("Connection established but not valid");
                        }
                    }
                } catch (SQLException e) {
                    result.setSuccess(false);
                    result.setMessage("Database Error: " + e.getMessage());
                } catch (ClassNotFoundException e) {
                    result.setSuccess(false);
                    result.setMessage("MySQL JDBC Driver not found");
                } catch (Exception e) {
                    result.setSuccess(false);
                    result.setMessage("Unexpected error: " + e.getMessage());
                } finally {
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            Log.e(TAG, "Error closing connection", e);
                        }
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(ConnectionResult result) {
                if (callback != null) {
                    callback.onConnectionResult(result.isSuccess(), result.getMessage());
                }
            }
        }.execute();
    }

    private boolean isHostReachable() {
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, PORT), TIMEOUT);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Host not reachable: " + e.getMessage());
            return false;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing socket", e);
                }
            }
        }
    }
}

class ConnectionResult {
    private boolean success;
    private String message;

    public ConnectionResult() {
        this.success = false;
        this.message = "";
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}