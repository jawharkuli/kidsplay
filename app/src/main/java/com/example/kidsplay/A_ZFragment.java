package com.example.kidsplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class A_ZFragment extends Fragment {
    private ViewPager2 viewPager;
    private List<letters> letterList;
    private A_ZAdapter letterAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a__z, container, false);

        viewPager = view.findViewById(R.id.viewPager);

        // Initialize the letters list
        initializeLetters();

        // Set up adapter
        letterAdapter = new A_ZAdapter(requireContext(), letterList);
        viewPager.setAdapter(letterAdapter);

        return view;
    }

    private void initializeLetters() {
        letterList = new ArrayList<>();

        letterList.add(new letters("", R.drawable.swipe, true, android.graphics.Color.RED));

        letterList.add(new letters("a", R.drawable.a, true, android.graphics.Color.RED));
        letterList.add(new letters("b", R.drawable.b, false, android.graphics.Color.GRAY));
        letterList.add(new letters("c", R.drawable.c, true, android.graphics.Color.YELLOW));
        letterList.add(new letters("d", R.drawable.d, false, android.graphics.Color.BLUE));
        letterList.add(new letters("e", R.drawable.e, true, android.graphics.Color.GREEN));
        letterList.add(new letters("f", R.drawable.f, false, android.graphics.Color.MAGENTA));
        letterList.add(new letters("g", R.drawable.g, true, android.graphics.Color.CYAN));
        letterList.add(new letters("h", R.drawable.h, false, android.graphics.Color.LTGRAY));
        letterList.add(new letters("i", R.drawable.i, true, android.graphics.Color.DKGRAY));
        letterList.add(new letters("j", R.drawable.j, false, android.graphics.Color.WHITE));
        letterList.add(new letters("k", R.drawable.k, true, android.graphics.Color.BLACK));
        letterList.add(new letters("l", R.drawable.l, false, android.graphics.Color.RED));
        letterList.add(new letters("m", R.drawable.m, true, android.graphics.Color.GRAY));
        letterList.add(new letters("n", R.drawable.n, false, android.graphics.Color.YELLOW));
        letterList.add(new letters("o", R.drawable.o, true, android.graphics.Color.BLUE));
        letterList.add(new letters("p", R.drawable.p, false, android.graphics.Color.GREEN));
        letterList.add(new letters("q", R.drawable.q, true, android.graphics.Color.MAGENTA));
        letterList.add(new letters("r", R.drawable.r, false, android.graphics.Color.CYAN));
        letterList.add(new letters("s", R.drawable.s, true, android.graphics.Color.LTGRAY));
        letterList.add(new letters("t", R.drawable.t, false, android.graphics.Color.DKGRAY));
        letterList.add(new letters("u", R.drawable.u, true, android.graphics.Color.WHITE));
        letterList.add(new letters("v", R.drawable.v, false, android.graphics.Color.BLACK));
        letterList.add(new letters("w", R.drawable.w, true, android.graphics.Color.RED));
        letterList.add(new letters("x", R.drawable.x, false, android.graphics.Color.GRAY));
        letterList.add(new letters("y", R.drawable.y, true, android.graphics.Color.YELLOW));
        letterList.add(new letters("z", R.drawable.z, false, android.graphics.Color.BLUE));
    }
}
