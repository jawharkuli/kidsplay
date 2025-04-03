package com.example.kidsplay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoryViewHolder> {
    private List<StoryInfo> stories;
    private final OnStoryClickListener listener;

    public interface OnStoryClickListener {
        void onStoryClick(StoryInfo story);
    }

    public StoriesAdapter(List<StoryInfo> stories, OnStoryClickListener listener) {
        this.stories = stories;
        this.listener = listener;
    }

    public void updateStories(List<StoryInfo> newStories) {
        this.stories = newStories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_story, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        StoryInfo story = stories.get(position);
        holder.bind(story, listener);
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    static class StoryViewHolder extends RecyclerView.ViewHolder {
        private final ImageView thumbnailView;
        private final TextView titleView;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailView = itemView.findViewById(R.id.image_story_thumbnail);
            titleView = itemView.findViewById(R.id.text_story_title);
        }

        public void bind(final StoryInfo story, final OnStoryClickListener listener) {
            titleView.setText(story.getTitle());

            // Load thumbnail using Glide
            if (story.getThumbnail() != null && !story.getThumbnail().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(story.getThumbnail())
                        .placeholder(R.drawable.baseline_video_file_24)
                        .error(R.drawable.baseline_error_24)
                        .centerCrop()
                        .into(thumbnailView);
            } else {
                thumbnailView.setImageResource(R.drawable.baseline_video_file_24);
            }

            // Set click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onStoryClick(story);
                }
            });
        }
    }
}