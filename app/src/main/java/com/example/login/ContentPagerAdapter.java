package com.example.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContentPagerAdapter extends RecyclerView.Adapter<ContentPagerAdapter.ContentViewHolder> {
    private List<ContentItem> items;

    public ContentPagerAdapter(List<ContentItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.page_item, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
        ContentItem item = items.get(position);
        holder.letterText.setText(item.getLetter());
        holder.exampleText.setText(item.getExample());
        holder.imageView.setImageResource(item.getImageResource());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView letterText;
        TextView exampleText;
        ImageView imageView;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            letterText = itemView.findViewById(R.id.letterText);
            exampleText = itemView.findViewById(R.id.exampleText);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}