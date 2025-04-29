package com.example.kidsplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReelsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReelsAdapter adapter;
    private final List<ReelModel> reelsList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reels, container, false);

        // Hide only ActionBar
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }

        recyclerView = view.findViewById(R.id.reelsRecycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        adapter = new ReelsAdapter(getContext(), reelsList);
        recyclerView.setAdapter(adapter);

        // Auto play when scrolling stops
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (lm != null) {
                        int visiblePosition = lm.findFirstCompletelyVisibleItemPosition();
                        if (visiblePosition != RecyclerView.NO_POSITION) {
                            adapter.playVideoAt(visiblePosition);
                        }
                    }
                }
            }
        });

        loadReels();

        return view;
    }

    private void loadReels() {
        reelsList.clear();
        String URL = DatabaseHelper.BASE_URL + "reels_videos/fetch_reels_android.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject reel = array.getJSONObject(i);
                            reelsList.add(new ReelModel(
                                    reel.getInt("id"),
                                    reel.getString("title"),
                                    reel.getString("video_url")
                            ));
                        }

                        adapter.notifyDataSetChanged();

                        recyclerView.post(() -> adapter.playVideoAt(0));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Failed to load reels", Toast.LENGTH_SHORT).show());

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(stringRequest);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adapter != null) {
            adapter.releaseAllPlayers(); // 🔥 Stops playback when leaving fragment
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        }

        if (adapter != null) {
            adapter.releaseAllPlayers(); // Extra safety
        }
    }
}
