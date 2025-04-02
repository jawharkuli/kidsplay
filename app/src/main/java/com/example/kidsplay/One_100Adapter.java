package com.example.kidsplay;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class One_100Adapter extends RecyclerView.Adapter<One_100Adapter.FruitViewHolder> {
    private final List<letters> fruits;
    private final Context context;
    private TextToSpeech textToSpeech = null;

    public One_100Adapter(Context context, List<letters> fruits) {
        this.context = context;
        this.fruits = fruits;

        // Initialize TextToSpeech
        textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.US);
            }
        });
    }

    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_a_z, parent, false);
        return new FruitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FruitViewHolder holder, int position) {
        letters fruit = fruits.get(position);
        holder.fruitName.setText(fruit.getName());
        holder.fruitImage.setImageResource(fruit.getImageResId());
    }

    @Override
    public void onViewAttachedToWindow(@NonNull FruitViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        speakFruitName(holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return fruits.size();
    }

    private void speakFruitName(int position) {
        if (position >= 0 && position < fruits.size()) {
            String name = fruits.get(position).getName();
            textToSpeech.speak(name, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    static class FruitViewHolder extends RecyclerView.ViewHolder {
        TextView fruitName;
        ImageView fruitImage;

        public FruitViewHolder(@NonNull View itemView) {
            super(itemView);
            fruitName = itemView.findViewById(R.id.fruitName);
            fruitImage = itemView.findViewById(R.id.fruitImage);
        }
    }
}