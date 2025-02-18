package com.example.login;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ContentActivity extends AppCompatActivity {

    private TextView contentTextView;
    private String selectedClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        contentTextView = findViewById(R.id.contentTextView);

        // Get the selected class from Intent
        selectedClass = getIntent().getStringExtra("selectedClass");

        if (selectedClass != null && !selectedClass.trim().isEmpty()) {
            contentTextView.setText("Loading content...");
            fetchDataFromDatabase(selectedClass);
        } else {
            Toast.makeText(this, "No class selected!", Toast.LENGTH_SHORT).show();
            finish(); // Close activity to prevent a blank screen
        }
    }

    private void fetchDataFromDatabase(String selectedClass) {
        try {
            // Encode class to handle spaces and special characters
            String encodedClass = URLEncoder.encode(selectedClass, "UTF-8");

            // Construct URL
            String url = "http://YOUR_IP_ADDRESS/project/fetch_content.php?class=" + encodedClass;

            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.trim().isEmpty()) {
                                contentTextView.setText(response);
                            } else {
                                contentTextView.setText("No content available for " + selectedClass);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            contentTextView.setText("Error loading content. Please try again.");
                            Toast.makeText(ContentActivity.this, "Network error! Please check your connection.", Toast.LENGTH_SHORT).show();
                        }
                    });

            queue.add(stringRequest);
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(this, "Encoding error!", Toast.LENGTH_SHORT).show();
        }
    }
}
