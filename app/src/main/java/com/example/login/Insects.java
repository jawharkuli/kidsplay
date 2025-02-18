package com.example.login;

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

public class Insects extends Fragment {
    private TextToSpeech textToSpeech;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_insects, container, false);
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

        List<Item> insects = new ArrayList<>();
        insects.add(new Item("Butterfly", R.drawable.butterfly));
        insects.add(new Item("Ladybug", R.drawable.ladybug));
        insects.add(new Item("Ant", R.drawable.ant));
        insects.add(new Item("Bee", R.drawable.housefly));

        InsectAdapter insectAdapter = new InsectAdapter(requireContext(), insects);
        gridView.setAdapter(insectAdapter);
        gridView.setNumColumns(1); // Line by line display

        gridView.setOnItemClickListener((parent, v, position, id) -> {
            Item item = (Item) insectAdapter.getItem(position);
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
