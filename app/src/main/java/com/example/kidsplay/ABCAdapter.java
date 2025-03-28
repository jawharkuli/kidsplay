package com.example.login;

import static com.example.login.R.id.btnSpeakLetter;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ABCAdapter extends RecyclerView.Adapter<ABCAdapter.ViewHolder> {
    private final List<ABCModel> abcList;
    private final Context context;
    private int selectedPosition = RecyclerView.NO_POSITION; // Track selected item

    public ABCAdapter(Context context, List<ABCModel> abcList) {
        this.context = context;
        this.abcList = abcList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.abc_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ABCModel abcModel = abcList.get(position);

        holder.letterText.setText(abcModel.getLetter());
        holder.letterImage.setImageResource(abcModel.getImageResId());

        // Background Change on Click
        holder.itemView.setBackgroundColor(
                position == selectedPosition ? Color.parseColor("#FFD54F") : Color.TRANSPARENT
        );

        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = position;

            // Update only the changed items for performance
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);
        });

        // Handle Pronunciation Button Click
        holder.pronounceButton.setOnClickListener(v -> {
            int soundResId = abcModel.getSoundResId();

            // Ensure sound resource is valid
            if (soundResId != 0) {
                MediaPlayer mediaPlayer = MediaPlayer.create(context, soundResId);

                if (mediaPlayer != null) {
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mp -> {
                        mp.release();
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return abcList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView letterText;
        ImageView letterImage;
        ImageButton pronounceButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            letterText = itemView.findViewById(R.id.letterText);
            letterImage = itemView.findViewById(R.id.letter_image);
            pronounceButton = itemView.findViewById(btnSpeakLetter);

            // Check if pronounceButton exists in layout
            if (pronounceButton == null) {
                throw new RuntimeException("pronounceButton is missing in abc_item.xml!");
            }
        }
    }
}
