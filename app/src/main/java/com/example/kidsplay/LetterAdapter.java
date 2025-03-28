package com.example.kidsplay;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class LetterAdapter extends RecyclerView.Adapter<LetterAdapter.LetterViewHolder> {

    private Context context;
    private List<LetterItem> letterList;
    private TextToSpeech textToSpeech;

    public LetterAdapter(Context context, List<LetterItem> letterList) {
        this.context = context;
        this.letterList = letterList;
        this.textToSpeech = new TextToSpeech(context, status -> {
            if (status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.US);
            }
        });
    }

    @NonNull
    @Override
    public LetterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_letter, parent, false);
        return new LetterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LetterViewHolder holder, int position) {
        LetterItem item = letterList.get(position);

        holder.textLetter.setText(item.getLetter());
        holder.textWord.setText(item.getWord());
        holder.imageExample.setImageResource(item.getImageResId());

        // **Dynamically set button texts**
        holder.btnSpeakLetter.setText("Pronounce " + item.getPronunciation());
        holder.btnSpeakWord.setText("Say " + item.getLetter() + " for " + item.getWord());

        // **Set click listeners for TTS**
        holder.btnSpeakLetter.setOnClickListener(v ->
                textToSpeech.speak(item.getPronunciation(), TextToSpeech.QUEUE_FLUSH, null, null)
        );

        holder.btnSpeakWord.setOnClickListener(v ->
                textToSpeech.speak(item.getLetter() + " for " + item.getWord(), TextToSpeech.QUEUE_FLUSH, null, null)
        );
    }

    @Override
    public int getItemCount() {
        return letterList.size();
    }

    public static class LetterViewHolder extends RecyclerView.ViewHolder {
        TextView textLetter, textWord;
        ImageView imageExample;
        Button btnSpeakLetter, btnSpeakWord;

        public LetterViewHolder(@NonNull View itemView) {
            super(itemView);
            textLetter = itemView.findViewById(R.id.textLetter);
            textWord = itemView.findViewById(R.id.textWord);
            imageExample = itemView.findViewById(R.id.imageExample);
            btnSpeakLetter = itemView.findViewById(R.id.btnSpeakLetter);
            btnSpeakWord = itemView.findViewById(R.id.btnSpeakWord);
        }
    }
}
