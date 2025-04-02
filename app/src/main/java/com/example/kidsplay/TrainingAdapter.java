package com.example.kidsplay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.ViewHolder> {
    private final List<TrainingItem> trainingData;

    public TrainingAdapter(List<TrainingItem> trainingData) {
        this.trainingData = trainingData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_training, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TrainingItem item = trainingData.get(position);
        holder.titleText.setText(item.getTitle());
        holder.descriptionText.setText(item.getDescription());
        holder.durationText.setText(item.getDuration());

        // Load thumbnail image
        if (item.getThumbnail() != null && !item.getThumbnail().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getThumbnail())
                    .placeholder(R.drawable.baseline_image_24)
                    .error(R.drawable.baseline_error_24)
                    .into(holder.thumbnailImage);
        }

        holder.itemView.setOnClickListener(v -> {
            // Handle training item click
            Toast.makeText(v.getContext(), "Selected: " + item.getTitle(), Toast.LENGTH_SHORT).show();
            // Start training activity or show details
        });
    }

    @Override
    public int getItemCount() {
        return trainingData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView descriptionText;
        TextView durationText;
        ImageView thumbnailImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            durationText = itemView.findViewById(R.id.durationText);
            thumbnailImage = itemView.findViewById(R.id.thumbnailImage);
        }
    }

    // Model class for training data
    public static class TrainingItem {
        private String title;
        private String description;
        private String duration;
        private String thumbnail;

        public TrainingItem(String title, String description, String duration, String thumbnail) {
            this.title = title;
            this.description = description;
            this.duration = duration;
            this.thumbnail = thumbnail;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getDuration() {
            return duration;
        }

        public String getThumbnail() {
            return thumbnail;
        }
    }
}