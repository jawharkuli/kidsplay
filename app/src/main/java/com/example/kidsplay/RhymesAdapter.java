package com.example.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RhymesAdapter extends RecyclerView.Adapter<RhymesAdapter.RhymeViewHolder> {
    private List<RhymeInfo> rhymes = new ArrayList<>();
    private final OnRhymeClickListener listener;

    // Interface for item click listener
    public interface OnRhymeClickListener {
        void onRhymeClick(RhymeInfo rhyme);
    }

    // Constructor accepting the listener
    public RhymesAdapter(OnRhymeClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RhymeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rhyme, parent, false);
        return new RhymeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RhymeViewHolder holder, int position) {
        RhymeInfo rhyme = rhymes.get(position);
        holder.titleTextView.setText(rhyme.getTitle());
        holder.textDescriptionView.setText(rhyme.getDescription());
        holder.itemView.setOnClickListener(v -> {
            Log.d("Url:",rhyme.getFile());
            String filePath = rhyme.getFile();
            Intent i = new Intent(v.getContext(), VideoPlayerActivity.class);
            i.putExtra("filePath",filePath);
            i.putExtra("title",rhyme.getTitle());
            v.getContext().startActivity(i);
        }); // Handle item click
    }

    @Override
    public int getItemCount() {
        return rhymes.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRhymes(List<RhymeInfo> rhymes) {
        this.rhymes = rhymes;
        notifyDataSetChanged();
    }

    static class RhymeViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView textDescriptionView;

        public RhymeViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            textDescriptionView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}