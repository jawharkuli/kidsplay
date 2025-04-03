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

public class ABCAdapter extends RecyclerView.Adapter<ABCAdapter.ViewHolder> {
    private final Context context;
    private final List<LetterItem> letterList;
    private TextToSpeech textToSpeech = null;

    public ABCAdapter(Context context, List<LetterItem> letterList) {
        this.context = context;
        this.letterList = letterList;

        // Initialize TextToSpeech
        this.textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.US);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.abc_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LetterItem letterItem = letterList.get(position);
        holder.letterText.setText(letterItem.getLetter());
        holder.wordText.setText(letterItem.getWord());
        holder.imageView.setImageResource(letterItem.getImageResId());

        // **Click Listeners for Text-to-Speech**
        holder.letterText.setOnClickListener(v -> speak(letterItem.getLetter()));
        holder.wordText.setOnClickListener(v -> speak(letterItem.getWord()));
        holder.imageView.setOnClickListener(v -> speak(letterItem.getLetter() + " for " + letterItem.getWord()));
    }

    private void speak(String text) {
        if (textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    public int getItemCount() {
        return letterList.size();
    }

    // **Release TTS to Prevent Memory Leaks**
    public void releaseTextToSpeech() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView letterText, wordText;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            letterText = itemView.findViewById(R.id.letterText);
            wordText = itemView.findViewById(R.id.wordText);
            imageView = itemView.findViewById(R.id.letterImage);
        }
    }
}
