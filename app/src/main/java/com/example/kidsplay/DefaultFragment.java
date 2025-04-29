package com.example.kidsplay;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DefaultFragment extends Fragment {

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_default, container, false);

        TextView textView = view.findViewById(R.id.text_default);

        Bundle args = getArguments();
        if (args != null) {
            String subjectName = args.getString("subjectName", "Subject");
            textView.setText("Content for " + subjectName + " is not available yet");
        }

        return view;
    }
}