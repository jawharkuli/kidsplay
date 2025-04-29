package com.example.kidsplay;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class PrimaryContentActivity extends AppCompatActivity {

    private static final String TAG = "PrimaryContentActivity";
    public static final String EXTRA_CONTENT_ID = "com.example.kidsplay.CONTENT_ID";
    public static final String EXTRA_CONTENT_TITLE = "com.example.kidsplay.CONTENT_TITLE";
    public static final String EXTRA_CONTENT_TYPE = "com.example.kidsplay.CONTENT_TYPE";
    public static final String EXTRA_CONTENT_PATH = "com.example.kidsplay.CONTENT_PATH";
    public static final String EXTRA_CONTENT_LINK = "com.example.kidsplay.CONTENT_LINK";

    private String selectedClass;
    private String subjectName;
    private DatabaseHelper dbHelper;
    private ListView listViewContent;
    private ArrayAdapter<String> fileAdapter;
    private final List<String> fileTitles = new ArrayList<>();
    private final List<DatabaseHelper.ContentInfo> contentList = new ArrayList<>();
    private TextView tvEmptyView;
    Button openLinkButton;

    @SuppressLint({"MissingInflatedId", "SetTextI18s"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_content);

        // Get intent data from PrimaryActivity
        Intent intent = getIntent();
        selectedClass = intent.getStringExtra("selectedClass");
        subjectName = intent.getStringExtra(PrimaryActivity.EXTRA_SUBJECT_NAME);

        // Set up the subject title
        TextView tvSubjectTitle = findViewById(R.id.tv_subject_title);
        tvSubjectTitle.setText(subjectName + " - " + selectedClass);

        // Initialize empty view
        tvEmptyView = findViewById(R.id.tv_empty_view);

        // Initialize ListView
        listViewContent = findViewById(R.id.list_view_content);
        fileAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileTitles);
        listViewContent.setAdapter(fileAdapter);

        // Set click listener for ListView items
        listViewContent.setOnItemClickListener((parent, view, position, id) -> {
            if (position >= 0 && position < contentList.size()) {
                DatabaseHelper.ContentInfo content = contentList.get(position);
                openPdfFile(content);
            }
        });

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Load content for the selected subject
        loadContent();
    }

    @SuppressLint("SetTextI18n")
    private void loadContent() {
        if (subjectName == null || subjectName.isEmpty() || selectedClass == null || selectedClass.isEmpty()) {
            Toast.makeText(this, "Invalid subject or class selection", Toast.LENGTH_SHORT).show();
            tvEmptyView.setText("Invalid selection");
            tvEmptyView.setVisibility(View.VISIBLE);
            listViewContent.setVisibility(View.GONE);
            return;
        }

        // Show loading state
        tvEmptyView.setText("Loading content...");
        tvEmptyView.setVisibility(View.VISIBLE);
        listViewContent.setVisibility(View.GONE);

        // Now using subjectName instead of subjectId
        dbHelper.fetchContentForSubject(subjectName, selectedClass, new DatabaseHelper.ContentCallback() {
            @Override
            public void onResult(List<DatabaseHelper.ContentInfo> content) {
                // Filter for PDF content only
                contentList.clear();
                fileTitles.clear();

                for (DatabaseHelper.ContentInfo item : content) {
                    if ("pdf".equalsIgnoreCase(item.getFileType())) {
                        contentList.add(item);
                        fileTitles.add(item.getTitle());
                    }
                }

                fileAdapter.notifyDataSetChanged();

                if (contentList.isEmpty()) {
                    tvEmptyView.setText("No PDF content available for " + subjectName);
                    tvEmptyView.setVisibility(View.VISIBLE);
                    listViewContent.setVisibility(View.GONE);
                    Timber.tag(TAG).i("No PDF content found for subject: %s in class: %s", subjectName, selectedClass);
                } else {
                    tvEmptyView.setVisibility(View.GONE);
                    listViewContent.setVisibility(View.VISIBLE);
                    Timber.tag(TAG).i("Found %d PDF files for subject: %s in class: %s",
                            contentList.size(), subjectName, selectedClass);
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(PrimaryContentActivity.this, "Error loading content: " + errorMessage, Toast.LENGTH_SHORT).show();
                tvEmptyView.setText("Failed to load content");
                tvEmptyView.setVisibility(View.VISIBLE);
                listViewContent.setVisibility(View.GONE);
                Timber.tag(TAG).e("Error loading content: %s", errorMessage);
            }
        });
    }

    private void openPdfFile(DatabaseHelper.ContentInfo content) {
        Toast.makeText(getApplicationContext(), "Opening: " + content.getTitle(), Toast.LENGTH_SHORT).show();
        String filePath = DatabaseHelper.BASE_URL+content.getFilePath();
        openPdfWithExternalApp(filePath);
    }
    private void openPdfWithExternalApp(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // No application to view PDFs
            Toast.makeText(this, "No PDF viewer found", Toast.LENGTH_SHORT).show();
            Timber.tag(TAG).e("No PDF viewer application: " + e.getMessage());
        }
    }

    public void onBackButtonClick(View view) {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.cleanup();
        }
    }
}