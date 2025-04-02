package com.example.kidsplay;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Flowers extends Fragment {
    private TextToSpeech textToSpeech;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flowers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridView gridView = view.findViewById(R.id.gridView);
        textToSpeech = new TextToSpeech(requireContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.UK);
            }
        });

        List<Item> flowers = new ArrayList<>();
        flowers.add(new Item("Rose", R.drawable.rose));
        flowers.add(new Item("Lily", R.drawable.lily));
        flowers.add(new Item("Sunflower", R.drawable.sunflower));
        flowers.add(new Item("Dansy", R.drawable.dansy));

        FlowerAdapter flowerAdapter = new FlowerAdapter(requireContext(), flowers);
        gridView.setAdapter(flowerAdapter);
        gridView.setNumColumns(1); // Line by line display

        gridView.setOnItemClickListener((parent, v, position, id) -> {
            Item item = (Item) flowerAdapter.getItem(position);
            textToSpeech.speak(item.getName(), TextToSpeech.QUEUE_FLUSH, null, null);
        });
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
