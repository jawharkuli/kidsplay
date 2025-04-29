package com.example.kidsplay;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PrimaryContentAdapter extends RecyclerView.Adapter<PrimaryContentAdapter.ContentViewHolder> {

    private final Context context;
    private final List<DatabaseHelper.ContentInfo> contentList;
    private final OnContentClickListener listener;

    public interface OnContentClickListener {
        void onContentClick(DatabaseHelper.ContentInfo content);
    }

    public PrimaryContentAdapter(Context context, List<DatabaseHelper.ContentInfo> contentList, OnContentClickListener listener) {
        this.context = context;
        this.contentList = contentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_content, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
        DatabaseHelper.ContentInfo content = contentList.get(position);

        // Set content title (filename)
        holder.tvTitle.setText(content.getTitle());
        holder.tvLink.setText(content.getFileLink());
        // Set PDF icon
        holder.ivIcon.setImageResource(R.drawable.error_image);

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onContentClick(content);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvLink;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_content_icon);
            tvTitle = itemView.findViewById(R.id.tv_content_title);
            tvLink = itemView.findViewById(R.id.tv_content_link);
        }
    }
}