package com.example.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ABCFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_abc, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewABC);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<LetterItem> letterList = new ArrayList<>();
        letterList.add(new LetterItem("A", "Apple", R.drawable.apple, "Aayyy"));
        letterList.add(new LetterItem("B", "Ball", R.drawable.ball, "Beeee"));
        letterList.add(new LetterItem("C", "Cat", R.drawable.cat, "Seeee"));
        letterList.add(new LetterItem("D", "Dog", R.drawable.dog, "Deeee"));
        letterList.add(new LetterItem("E", "Elephant", R.drawable.elephant, "E"));
        letterList.add(new LetterItem("F", "Fish", R.drawable.fish, "Effff"));
        letterList.add(new LetterItem("G", "Goat", R.drawable.goat, "Jeeee"));
        letterList.add(new LetterItem("H", "Hat", R.drawable.hat, "Aychhh"));
        letterList.add(new LetterItem("I", "Igloo", R.drawable.igloo, "I"));
        letterList.add(new LetterItem("J", "Jug", R.drawable.jug, "Jaaaaay"));
        letterList.add(new LetterItem("K", "Kite", R.drawable.kite, "Kaaaaay"));
        letterList.add(new LetterItem("L", "Lion", R.drawable.lion, "Elllll"));
        letterList.add(new LetterItem("M", "Monkey", R.drawable.monkey, "M"));
        letterList.add(new LetterItem("N", "Nest", R.drawable.nest, "Ennnn"));
        letterList.add(new LetterItem("O", "Orange", R.drawable.orange, "O"));
        letterList.add(new LetterItem("P", "Parrot", R.drawable.parrot, "Peeee"));
        letterList.add(new LetterItem("Q", "Queen", R.drawable.queen, "Kyu"));
        letterList.add(new LetterItem("R", "Rabbit", R.drawable.rabbit, "r"));
        letterList.add(new LetterItem("S", "Sun", R.drawable.sun, "Esssss"));
        letterList.add(new LetterItem("T", "Tiger", R.drawable.tiger, "Teeee"));
        letterList.add(new LetterItem("U", "Umbrella", R.drawable.umbrella, "You"));
        letterList.add(new LetterItem("V", "Violin", R.drawable.violin, "Veeee"));
        letterList.add(new LetterItem("W", "Watch", R.drawable.watch, "Double-you"));
        letterList.add(new LetterItem("X", "Xylophone", R.drawable.xylophone, "Ex"));
        letterList.add(new LetterItem("Y", "Yak", R.drawable.yak, "Why"));
        letterList.add(new LetterItem("z", "Zebra", R.drawable.zebra, "zet"));


        LetterAdapter adapter = new LetterAdapter(getContext(), letterList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
