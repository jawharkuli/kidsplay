package com.example.login;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

public class RhymesFragment extends Fragment implements RhymesAdapter.OnRhymeClickListener {
    private static final String TAG = "RhymesFragment";

    private RecyclerView recyclerView;
    private RhymesAdapter rhymesAdapter;
    private ProgressBar progressBar;
    private VideoView videoView;
    private TextView videoTitleTextView;
    private RelativeLayout videoContainer;
    private ProgressBar videoProgressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rhymes, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        videoView = view.findViewById(R.id.videoView);
        videoTitleTextView = view.findViewById(R.id.videoTitleTextView);
        videoContainer = view.findViewById(R.id.videoContainer);
        videoProgressBar = view.findViewById(R.id.videoProgressBar);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        // Initially hide the video container
        videoContainer.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        rhymesAdapter = new RhymesAdapter(this);
        recyclerView.setAdapter(rhymesAdapter);

        // Setup SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(this::fetchRhymes);
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        // Set up media controller for the video view
        MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Handle video completion
        videoView.setOnCompletionListener(mp -> {
            videoContainer.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        });

        fetchRhymes();
        return view;
    }

    private void fetchRhymes() {
        // Only show the progress bar if SwipeRefreshLayout is not refreshing
        if (!swipeRefreshLayout.isRefreshing()) {
            progressBar.setVisibility(View.VISIBLE);
        }

        DatabaseHelper databaseHelper = new DatabaseHelper();
        databaseHelper.fetchRhymes(new DatabaseHelper.RhymesCallback() {
            @Override
            public void onResult(List<RhymeInfo> rhymes) {
                if (isAdded()) { // Check if fragment is still attached to activity
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);

                    if (rhymes != null && !rhymes.isEmpty()) {
                        rhymesAdapter.setRhymes(rhymes);
                    } else {
                        Toast.makeText(getContext(), "No rhymes found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onError(String errorMessage) {
                if (isAdded()) { // Check if fragment is still attached to activity
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRhymeClick(RhymeInfo rhyme) {
        Log.d(TAG, "Rhyme clicked: " + rhyme.getTitle());
        Log.d(TAG, "Video path: " + rhyme.getFile());

        // Show the video container and hide the recycler view
        videoContainer.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        // Set the video title
        videoTitleTextView.setText(rhyme.getTitle());

        // Show the video progress bar
        videoProgressBar.setVisibility(View.VISIBLE);

        // Play the video
        playVideo(rhyme.getFile());
    }

    private void playVideo(String videoPath) {
        try {
            // Convert the path to a URI
            Uri videoUri = Uri.parse(videoPath);

            // Set error listener
            videoView.setOnErrorListener((mp, what, extra) -> {
                Log.e(TAG, "Video Error: what=" + what + ", extra=" + extra);
                videoProgressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error playing video", Toast.LENGTH_SHORT).show();
                videoContainer.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                return true;
            });

            // Set prepared listener
            videoView.setOnPreparedListener(mp -> {
                Log.d(TAG, "Video prepared successfully");
                videoProgressBar.setVisibility(View.GONE);
                mp.start();

                // Optional: Set video size to fit the screen
                adjustVideoSize(mp);
            });

            // Set the video URI and start loading
            videoView.setVideoURI(videoUri);

        } catch (Exception e) {
            Log.e(TAG, "Error playing video", e);
            videoProgressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            videoContainer.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void adjustVideoSize(MediaPlayer mp) {
        // Get the dimensions of the video
        int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();

        // Get the dimensions of the screen
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        // Calculate the scaling factor
        float scaleFactor = (float) screenWidth / (float) videoWidth;
        int scaledHeight = (int) (videoHeight * scaleFactor);

        // Set the dimensions of the VideoView
        ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
        layoutParams.width = screenWidth;
        layoutParams.height = scaledHeight;
        videoView.setLayoutParams(layoutParams);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop the video when the fragment is paused
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
        }
    }
}