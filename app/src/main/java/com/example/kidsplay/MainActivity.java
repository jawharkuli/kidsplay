package com.example.kidsplay;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    public static Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Setup Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set hamburger menu icon
        toolbar.setNavigationIcon(R.drawable.ic_toggle); // Ensure you have a menu icon in res/drawable
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Handle Navigation Drawer item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_settings) {
                loadFragment(new SettingsFragment()); // Load SettingsFragment when clicked
            } else if (itemId == R.id.nav_logs) {
                // Add the logic for logs if needed
            } else if (itemId == R.id.nav_abc) {
                loadFragment(new ABCFragment()); // Load ABC Fragment when clicked
            }

            // Close drawer after selection
            drawerLayout.closeDrawers();
            return true;
        });

        // Initialize Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load HomeFragment initially
        if (savedInstanceState == null) {
            updateToolbarTitle("Home");
            loadFragment(new HomeFragment()); // Default fragment on app launch
        }

        // Bottom Navigation Item Selected Listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                updateToolbarTitle("Home");
                return loadFragment(new HomeFragment());
            } else if (itemId == R.id.nav_explore) {
                updateToolbarTitle("Explore");
                // Start ClassSelectionActivity on Explore click
                startActivity(new Intent(MainActivity.this, ClassSelectionActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                updateToolbarTitle("Profile");
                return loadFragment(new ProfileFragment());
            }
            return false;
        });
    }

    // Method to load a fragment dynamically
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    // Method to update the toolbar title based on the selected fragment
    public void updateToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        // Handle back press inside the drawer
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
