package com.example.kidsplay;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class VideoPlayerActivity extends AppCompatActivity {
    private static final String TAG = "VideoPlayerActivity";
    private static final String KEY_POSITION = "video_position";
    private static final String KEY_FULLSCREEN = "is_fullscreen";
    private static final String KEY_ORIENTATION = "is_landscape";

    private PlayerView playerView;
    private ExoPlayer player;
    private TextView titleTextView;
    private ProgressBar loadingProgress;
    private ImageButton btnShare, btnFavorite, btnBack, btnRotate;

    private long playbackPosition = 0;
    private boolean isFullScreen = true; // Start with fullscreen enabled
    private boolean isLandscape = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Apply fullscreen flags
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_video_player);

        initializeViews();

        // Restore state if available
        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(KEY_POSITION, 0);
            isFullScreen = savedInstanceState.getBoolean(KEY_FULLSCREEN, true);
            isLandscape = savedInstanceState.getBoolean(KEY_ORIENTATION, false);
        }

        handleIntentData();
        setupButtonListeners();

        // Apply fullscreen immediately on start
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            setFullScreenMode(isFullScreen);
        } else {
            setLegacyFullscreen(isFullScreen);
        }

        // Set initial orientation based on saved state
        setRequestedOrientation(isLandscape ?
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @OptIn(markerClass = UnstableApi.class)
    private void initializeViews() {
        playerView = findViewById(R.id.player_view);
        titleTextView = findViewById(R.id.video_title);
        loadingProgress = findViewById(R.id.loading_progress);
        btnShare = findViewById(R.id.btn_share);
        btnFavorite = findViewById(R.id.btn_favorite);
        btnBack = findViewById(R.id.btn_back);
        btnRotate = findViewById(R.id.btn_rotate);

        // Set up player view for better fullscreen experience
        playerView.setUseController(true);
        playerView.setControllerHideOnTouch(true);
        playerView.setControllerShowTimeoutMs(3000);

        // Handle tap to toggle UI controls
        playerView.setOnClickListener(v -> toggleControls());
    }

    private void handleIntentData() {
        String videoPath = getIntent().getStringExtra("filePath");
        String videoTitle = getIntent().getStringExtra("title");

        if (videoPath == null) {
            Toast.makeText(this, "Error: Video path is missing", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String videoUrl = videoPath.startsWith("http://") || videoPath.startsWith("https://")
                ? videoPath
                : DatabaseHelper.BASE_URL + videoPath;

        Log.d(TAG, "Playing video: " + videoUrl);
        titleTextView.setText(videoTitle != null ? videoTitle : "Video");
        Log.d("PlayerUrl",videoUrl);
        initializePlayer(videoUrl);
    }

    private void setupButtonListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnShare.setOnClickListener(v -> shareVideo());
        btnFavorite.setOnClickListener(v -> Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show());
        btnRotate.setOnClickListener(v -> toggleScreenOrientation());
    }

    private void initializePlayer(String videoUrl) {
        loadingProgress.setVisibility(View.VISIBLE);

        player = new ExoPlayer.Builder(this)
                .setHandleAudioBecomingNoisy(true)
                .build();

        // Enable hardware acceleration for better performance
        playerView.setKeepScreenOn(true);
        playerView.setPlayer(player);

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                loadingProgress.setVisibility(state == Player.STATE_BUFFERING ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPlayerError(@NonNull androidx.media3.common.PlaybackException error) {
                Log.e(TAG, "Player error: " + error.getMessage());
                Toast.makeText(VideoPlayerActivity.this, "Error playing video", Toast.LENGTH_LONG).show();
                loadingProgress.setVisibility(View.GONE);
            }
        });
        Log.d("VideoUrl",videoUrl);
        MediaItem mediaItem = MediaItem.fromUri(videoUrl);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.seekTo(playbackPosition);
        player.setPlayWhenReady(true);
    }

    private void toggleScreenOrientation() {
        isLandscape = !isLandscape;
        setRequestedOrientation(isLandscape ?
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void setFullScreenMode(boolean enable) {
        WindowInsetsController controller = getWindow().getInsetsController();
        if (controller != null) {
            if (enable) {
                controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            } else {
                controller.show(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            }
        }
        setControlsVisibility(!enable);
        isFullScreen = enable;
    }

    private void setLegacyFullscreen(boolean enable) {
        if (enable) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
        setControlsVisibility(!enable);
        isFullScreen = enable;
    }

    private void toggleControls() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            setFullScreenMode(!isFullScreen);
        } else {
            setLegacyFullscreen(!isFullScreen);
        }
    }

    private void setControlsVisibility(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        titleTextView.setVisibility(visibility);
        btnBack.setVisibility(visibility);
        btnShare.setVisibility(visibility);
        btnFavorite.setVisibility(visibility);
        btnRotate.setVisibility(visibility);
    }

    private void shareVideo() {
        String videoTitle = titleTextView.getText().toString();
        String videoUrl = getIntent().getStringExtra("filePath");

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, videoTitle);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this video: " + videoUrl);
        startActivity(Intent.createChooser(shareIntent, "Share video via"));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (player != null) {
            outState.putLong(KEY_POSITION, player.getCurrentPosition());
        }
        outState.putBoolean(KEY_FULLSCREEN, isFullScreen);
        outState.putBoolean(KEY_ORIENTATION, isLandscape);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isFullScreen) {
            // Re-apply fullscreen when returning from another app or dialog
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                setFullScreenMode(true);
            } else {
                setLegacyFullscreen(true);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player == null) {
            handleIntentData();
        } else {
            player.setPlayWhenReady(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            player.setPlayWhenReady(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}