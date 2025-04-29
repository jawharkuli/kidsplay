package com.example.kidsplay;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Calendar;
import java.util.Date;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_SESSION_ID = "sessionId";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_SESSION_EXPIRES = "sessionExpires";
    private static final String KEY_LAST_ACTIVITY = "lastActivity";

    // Session timeout in milliseconds (default: 30 minutes)
    private static final long SESSION_TIMEOUT = 30 * 60 * 1000;
    // Session absolute expiration (default: 24 hours)
    private static final long SESSION_ABSOLUTE_TIMEOUT = 24 * 60 * 60 * 1000;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * Create a new user session
     */
    public void createSession(String sessionId, long userId, String username, String email) {
        editor.putString(KEY_SESSION_ID, sessionId);
        editor.putLong(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);

        // Set session expiration time
        long currentTime = System.currentTimeMillis();
        editor.putLong(KEY_SESSION_EXPIRES, currentTime + SESSION_ABSOLUTE_TIMEOUT);
        editor.putLong(KEY_LAST_ACTIVITY, currentTime);

        editor.apply();
    }

    /**
     * Update the last activity timestamp to extend session timeout
     */
    public void updateLastActivity() {
        editor.putLong(KEY_LAST_ACTIVITY, System.currentTimeMillis());
        editor.apply();
    }

    /**
     * Check if the session is valid based on expiration and inactivity timeout
     */
    public boolean isValidSession() {
        if (!sharedPreferences.contains(KEY_SESSION_ID)) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        long sessionExpires = sharedPreferences.getLong(KEY_SESSION_EXPIRES, 0);
        long lastActivity = sharedPreferences.getLong(KEY_LAST_ACTIVITY, 0);

        // Check if session has absolutely expired
        if (currentTime > sessionExpires) {
            destroySession();
            return false;
        }

        // Check if session has timed out due to inactivity
        if (currentTime > (lastActivity + SESSION_TIMEOUT)) {
            destroySession();
            return false;
        }

        // Update last activity time
        updateLastActivity();
        return true;
    }

    /**
     * Get current session ID
     */
    public String getSessionId() {
        return sharedPreferences.getString(KEY_SESSION_ID, null);
    }

    /**
     * Get username from session
     */
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    /**
     * Get user email from session
     */
    public String getUserEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    /**
     * Get user ID from session
     */
    public long getUserId() {
        return sharedPreferences.getLong(KEY_USER_ID, -1);
    }

    /**
     * Destroy session and clear data
     */
    public void destroySession() {
        editor.clear();
        editor.apply();
    }

    /**
     * Get remaining session time in minutes
     */
    public int getRemainingSessionTime() {
        long currentTime = System.currentTimeMillis();
        long sessionExpires = sharedPreferences.getLong(KEY_SESSION_EXPIRES, 0);

        if (sessionExpires <= currentTime) {
            return 0;
        }

        return (int)((sessionExpires - currentTime) / (60 * 1000));
    }
}