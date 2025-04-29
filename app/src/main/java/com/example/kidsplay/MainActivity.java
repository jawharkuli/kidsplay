package com.example.kidsplay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    @SuppressLint("StaticFieldLeak")
    public static Toolbar toolbar;
    SessionManager sessionManager;
    TextView userName, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ✅ Apply theme before layout is inflated
        SharedPreferences prefs = getSharedPreferences("UserSettings", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("darkMode", false);
        sessionManager = new SessionManager(getApplicationContext());

        // Check if session is valid
        if (!sessionManager.isValidSession()) {
            // Redirect to login if session is invalid
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        // Update last activity timestamp
        sessionManager.updateLastActivity();

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup toolbar and navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Get username and email from session manager
        String username = sessionManager.getUsername();
        String email = sessionManager.getUserEmail();

        // Get header view and set username and email
        View headerView = navigationView.getHeaderView(0);
        userName = headerView.findViewById(R.id.userName);
        userEmail = headerView.findViewById(R.id.userEmail);

        // Set text to username and email
        if (userName != null && userEmail != null) {
            userName.setText(username);
            userEmail.setText(email);
        }

        // Rest of your existing code...
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Toast.makeText(getApplicationContext(),username+email,Toast.LENGTH_SHORT).show();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_toggle);
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_settings) {
                updateToolbarTitle("Settings");
                loadFragment(new SettingsFragment(), true);
            ;
            }


            bottomNavigationView.getMenu().setGroupCheckable(0, false, true);
            drawerLayout.closeDrawers();
            return true;
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            updateToolbarTitle("Home");
            loadFragment(new HomeFragment(), false);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                updateToolbarTitle("Home");
                return loadFragment(new HomeFragment(), true);
            } else if (itemId == R.id.nav_explore) {
                updateToolbarTitle("Explore");
                Intent intent = new Intent(MainActivity.this, ClassSelectionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
                return true;
            } else if (itemId == R.id.nav_reels) {
                updateToolbarTitle("Reels");
                loadFragment(new ReelsFragment(), true);
                return true;
            } else if (itemId == R.id.nav_profile) {
                updateToolbarTitle("Profile");
                return loadFragment(new ProfileFragment(), true);
            }

            return false;
        });
    }

    // Fragment loader with animation
    private boolean loadFragment(Fragment fragment, boolean withAnimation) {
        if (fragment != null) {
            if (withAnimation) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_right,
                                R.anim.slide_out_left,
                                R.anim.slide_in_left,
                                R.anim.slide_out_right
                        )
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }
            return true;
        }
        return false;
    }

    // Toolbar title helper
    public void updateToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (!(currentFragment instanceof HomeFragment)) {
            updateToolbarTitle("Home");
            loadFragment(new HomeFragment(), true);
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        } else {
            super.onBackPressed();
        }
    }
    protected void onResume() {
        super.onResume();

        // Check session validity on resume
        if (!sessionManager.isValidSession()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            // Update last activity timestamp
            sessionManager.updateLastActivity();
        }
    }
    public void logout() {
        sessionManager.destroySession();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}
