package com.example.kidsplay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class StoriesFragment extends Fragment implements StoriesAdapter.OnStoryClickListener {
    private static final String TAG = "StoriesFragment";

    private String className;
    private RecyclerView recyclerView;
    private StoriesAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View emptyView;
    private DatabaseHelper dbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        className = ContentActivity.selectedClass; // Fetch className from ContentActivity
        dbHelper = new DatabaseHelper(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stories, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_stories);
        emptyView = view.findViewById(R.id.empty_view);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        int spanCount = getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE ? 3 : 2;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));

        adapter = new StoriesAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this::loadStories);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadStories();
    }

    private void loadStories() {
        if (className == null || className.isEmpty()) {
            Log.e(TAG, "Cannot load stories: className is null or empty");
            Toast.makeText(getContext(), "Error: Class name not specified", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Loading stories for class: " + className);
        swipeRefreshLayout.setRefreshing(true);

        dbHelper.fetchStories(className, new DatabaseHelper.StoriesCallback() {
            @Override
            public void onResult(List<StoryInfo> stories) {
                if (!isAdded()) return;

                swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, "Stories loaded: " + stories.size());

                if (stories.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                    adapter.updateStories(stories);
                }
            }

            @Override
            public void onError(String errorMessage) {
                if (!isAdded()) return;

                Log.e(TAG, "Error loading stories: " + errorMessage);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onStoryClick(StoryInfo story) {
        if (story == null) {
            Log.e(TAG, "Cannot open video player: story is null");
            return;
        }

        Log.d(TAG, "Story clicked: " + story.getTitle());
        Log.d(TAG, "Video path: " + story.getFile());

        // Launch the ExoPlayer-based VideoPlayerActivity
        launchVideoPlayerActivity(story);
    }

    private void launchVideoPlayerActivity(StoryInfo story) {
        Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
        Log.d("StoryUrl",story.getFile());
        intent.putExtra("filePath", story.getFile());
        intent.putExtra("title", story.getTitle());
        Log.d(TAG, "Launching VideoPlayerActivity with path: " + story.getFile());
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null; // Clean up adapter reference
    }
}