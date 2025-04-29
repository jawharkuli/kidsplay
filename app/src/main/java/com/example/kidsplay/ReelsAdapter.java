package com.example.kidsplay;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReelsAdapter extends RecyclerView.Adapter<ReelsAdapter.ReelViewHolder> {

    private final Context context;
    private final List<ReelModel> list;
    private final Map<Integer, ExoPlayer> playersMap = new HashMap<>();
    private int currentlyPlayingPosition = -1;

    public ReelsAdapter(Context context, List<ReelModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ReelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reel, parent, false);
        return new ReelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReelViewHolder holder, int position) {
        ReelModel reel = list.get(position);
        holder.title.setText(reel.getTitle());

        String videoUrl = reel.getVideoUrl()
                .replace("localhost", "192.168.179.216")
                .replace("127.0.0.1", "192.168.67.216");

        Log.d("ReelsAdapter", "Original URL: " + reel.getVideoUrl());
        Log.d("ReelsAdapter", "Modified URL: " + videoUrl);

        ExoPlayer player;

        if (!playersMap.containsKey(position)) {
            player = new ExoPlayer.Builder(context).build();
            playersMap.put(position, player);

            MediaItem mediaItem = MediaItem.fromUri(videoUrl);
            player.setMediaItem(mediaItem);
            player.setRepeatMode(ExoPlayer.REPEAT_MODE_ONE);
            player.prepare();
        } else {
            player = playersMap.get(position);
        }

        holder.playerView.setPlayer(player);

        holder.playerView.setOnClickListener(v -> {
            if (player != null) {
                if (player.isPlaying()) player.pause();
                else player.play();
            }
        });
    }

    public void playVideoAt(int position) {
        if (currentlyPlayingPosition == position) return;

        // Pause previous video
        if (playersMap.containsKey(currentlyPlayingPosition)) {
            ExoPlayer previousPlayer = playersMap.get(currentlyPlayingPosition);
            if (previousPlayer != null && previousPlayer.isPlaying()) {
                previousPlayer.setPlayWhenReady(false);
                previousPlayer.pause();
            }
        }

        // Play current video
        if (playersMap.containsKey(position)) {
            ExoPlayer currentPlayer = playersMap.get(position);
            if (currentPlayer != null) {
                currentPlayer.setPlayWhenReady(true);
                currentPlayer.play();
                currentlyPlayingPosition = position;
            }
        }
    }

    public void releaseAllPlayers() {
        for (Map.Entry<Integer, ExoPlayer> entry : playersMap.entrySet()) {
            ExoPlayer player = entry.getValue();
            if (player != null) {
                player.release();
            }
        }
        playersMap.clear();
        currentlyPlayingPosition = -1;
    }

    @Override
    public void onViewRecycled(@NonNull ReelViewHolder holder) {
        int position = holder.getAdapterPosition();
        if (position != RecyclerView.NO_POSITION && playersMap.containsKey(position)) {
            ExoPlayer player = playersMap.get(position);
            if (player != null && player.isPlaying()) {
                player.setPlayWhenReady(false);
                player.pause();
            }
        }
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ReelViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        PlayerView playerView;

        public ReelViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.reelTitle);
            playerView = itemView.findViewById(R.id.reelPlayer);
        }
    }
}
