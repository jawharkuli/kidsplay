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

public class Fruits extends Fragment {
    private TextToSpeech textToSpeech;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fruits, container, false);
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

        List<Item> fruits = new ArrayList<>();
        fruits.add(new Item("Apple", R.drawable.apple));
        fruits.add(new Item("Banana", R.drawable.banana));
        fruits.add(new Item("Orange", R.drawable.orange));
        fruits.add(new Item("Mango", R.drawable.mango));

        FruitAdapter fruitAdapter = new FruitAdapter(requireContext(), fruits);
        gridView.setAdapter(fruitAdapter);
        gridView.setNumColumns(1); // Line by line display

        gridView.setOnItemClickListener((parent, v, position, id) -> {
            Item item = (Item) fruitAdapter.getItem(position);
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
