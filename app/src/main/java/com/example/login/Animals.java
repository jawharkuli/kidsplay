package com.example.login;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Animals extends Fragment {
    private AnimalAdapter animalAdapter;
    private TextToSpeech textToSpeech;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_animals, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridView gridView = view.findViewById(R.id.gridView);
        textToSpeech = new TextToSpeech(requireContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.UK);
            }
        });

        List<Animal> animals = new ArrayList<>();
        animals.add(new Animal("Lion", R.drawable.lion));
        animals.add(new Animal("Elephant", R.drawable.elephant));
        animals.add(new Animal("Giraffe", R.drawable.giraffe));
        animals.add(new Animal("Zebra", R.drawable.zebra));
        animals.add(new Animal("Tiger", R.drawable.tiger));
        animals.add(new Animal("Monkey", R.drawable.monkey));

        animalAdapter = new AnimalAdapter(requireContext(), animals);
        gridView.setAdapter(animalAdapter);

        gridView.setNumColumns(1); // Line by line display

        gridView.setOnItemClickListener((parent, v, position, id) -> {
            Animal animal = (Animal) animalAdapter.getItem(position);
            textToSpeech.speak(animal.getName(), TextToSpeech.QUEUE_FLUSH, null, null);
            Toast.makeText(getContext(), "Selected: " + animal.getName(), Toast.LENGTH_SHORT).show();
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
