package com.example.kidsplay;

import android.graphics.Color;
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

public class OneHundredFragment extends Fragment {
    private ViewPager2 viewPager;
    private List<letters> letterList;
    private A_ZAdapter oneHundredAdapter;  // Ensure the correct adapter is used

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_100, container, false); // Ensure this layout exists

        viewPager = view.findViewById(R.id.viewPager);

        // Initialize the letters list
        initializeLetters();

        // Set up adapter
        oneHundredAdapter = new A_ZAdapter(requireContext(), letterList);
        viewPager.setAdapter(oneHundredAdapter);

        return view;
    }

    private void initializeLetters() {
        letterList = new ArrayList<>();

        letterList.add(new letters("", R.drawable.num, true, Color.RED));

        letterList.add(new letters("One", R.drawable.one, true, Color.RED));
        letterList.add(new letters("Two", R.drawable.two, true, Color.RED));
        letterList.add(new letters("Three", R.drawable.three, true, Color.RED));
        letterList.add(new letters("Four", R.drawable.four, true, Color.RED));
        letterList.add(new letters("Five", R.drawable.five, true, Color.RED));
        letterList.add(new letters("Six", R.drawable.six, true, Color.RED));
        letterList.add(new letters("Seven", R.drawable.seven, true, Color.RED));
        letterList.add(new letters("Eight", R.drawable.eight, true, Color.RED));
        letterList.add(new letters("Nine", R.drawable.nine, true, Color.RED));
        letterList.add(new letters("Ten", R.drawable.ten, true, Color.RED));
        letterList.add(new letters("Eleven", R.drawable.eleven, true, Color.RED));
        letterList.add(new letters("Twelve", R.drawable.twelve, true, Color.RED));
        letterList.add(new letters("Thirteen", R.drawable.thirteen, true, Color.RED));
        letterList.add(new letters("Fourteen", R.drawable.fourteen, true, Color.RED));
        letterList.add(new letters("Fifteen", R.drawable.fifteen, true, Color.RED));
        letterList.add(new letters("Sixteen", R.drawable.sixteen, true, Color.RED));
        letterList.add(new letters("Seventeen", R.drawable.seventeen, true, Color.RED));
        letterList.add(new letters("Eighteen", R.drawable.eighteen, true, Color.RED));
        letterList.add(new letters("Nineteen", R.drawable.nineteen, true, Color.RED));
        letterList.add(new letters("Twenty", R.drawable.twenty, true, Color.RED));
        letterList.add(new letters("Twenty One", R.drawable.twenty_one, true, Color.RED));
        letterList.add(new letters("Twenty Two", R.drawable.twenty_two, true, Color.RED));
        letterList.add(new letters("Twenty Three", R.drawable.twenty_three, true, Color.RED));
        letterList.add(new letters("Twenty Four", R.drawable.twenty_four, true, Color.RED));
        letterList.add(new letters("Twenty Five", R.drawable.twenty_five, true, Color.RED));
        letterList.add(new letters("Twenty Six", R.drawable.twenty_six, true, Color.RED));
        letterList.add(new letters("Twenty Seven", R.drawable.twenty_seven, true, Color.RED));
        letterList.add(new letters("Twenty Eight", R.drawable.twenty_eight, true, Color.RED));
        letterList.add(new letters("Twenty Nine", R.drawable.twenty_nine, true, Color.RED));
        letterList.add(new letters("Thirty", R.drawable.thirty, true, Color.RED));
        letterList.add(new letters("Thirty One", R.drawable.thirty_one, true, Color.RED));
        letterList.add(new letters("Thirty Two", R.drawable.thirty_two, true, Color.RED));
        letterList.add(new letters("Thirty Three", R.drawable.thirty_three, true, Color.RED));
        letterList.add(new letters("Thirty Four", R.drawable.thirty_four, true, Color.RED));
        letterList.add(new letters("Thirty Five", R.drawable.thirty_five, true, Color.RED));
        letterList.add(new letters("Thirty Six", R.drawable.thirty_six, true, Color.RED));
        letterList.add(new letters("Thirty Seven", R.drawable.thirty_seven, true, Color.RED));
        letterList.add(new letters("Thirty Eight", R.drawable.thirty_eight, true, Color.RED));
        letterList.add(new letters("Thirty Nine", R.drawable.thirty_nine, true, Color.RED));
        letterList.add(new letters("Forty", R.drawable.forty, true, Color.RED));
        letterList.add(new letters("Forty One", R.drawable.forty_one, true, Color.RED));
        letterList.add(new letters("Forty Two", R.drawable.forty_two, true, Color.RED));
        letterList.add(new letters("Forty Three", R.drawable.forty_three, true, Color.RED));
        letterList.add(new letters("Forty Four", R.drawable.forty_four, true, Color.RED));
        letterList.add(new letters("Forty Five", R.drawable.forty_five, true, Color.RED));
        letterList.add(new letters("Forty Six", R.drawable.forty_six, true, Color.RED));
        letterList.add(new letters("Forty Seven", R.drawable.forty_seven, true, Color.RED));
        letterList.add(new letters("Forty Eight", R.drawable.forty_eight, true, Color.RED));
        letterList.add(new letters("Forty Nine", R.drawable.forty_nine, true, Color.RED));
        letterList.add(new letters("Fifty", R.drawable.fifty, true, Color.RED));
        letterList.add(new letters("Fifty", R.drawable.fifty, true, Color.RED));
        letterList.add(new letters("Fifty-One", R.drawable.fifty_one, true, Color.RED));
        letterList.add(new letters("Fifty-Two", R.drawable.fifty_two, true, Color.RED));
        letterList.add(new letters("Fifty-Three", R.drawable.fifty_three, true, Color.RED));
        letterList.add(new letters("Fifty-Four", R.drawable.fifty_four, true, Color.RED));
        letterList.add(new letters("Fifty-Five", R.drawable.fifty_five, true, Color.RED));
        letterList.add(new letters("Fifty-Six", R.drawable.fifty_six, true, Color.RED));
        letterList.add(new letters("Fifty-Seven", R.drawable.fifty_seven, true, Color.RED));
        letterList.add(new letters("Fifty-Eight", R.drawable.fifty_eight, true, Color.RED));
        letterList.add(new letters("Fifty-Nine", R.drawable.fifty_nine, true, Color.RED));
        letterList.add(new letters("Sixty", R.drawable.sixty, true, Color.RED));
        letterList.add(new letters("Sixty-One", R.drawable.sixty_one, true, Color.RED));
        letterList.add(new letters("Sixty-Two", R.drawable.sixty_two, true, Color.RED));
        letterList.add(new letters("Sixty-Three", R.drawable.sixty_three, true, Color.RED));
        letterList.add(new letters("Sixty-Four", R.drawable.sixty_four, true, Color.RED));
        letterList.add(new letters("Sixty-Five", R.drawable.sixty_five, true, Color.RED));
        letterList.add(new letters("Sixty-Six", R.drawable.sixty_six, true, Color.RED));
        letterList.add(new letters("Sixty-Seven", R.drawable.sixty_seven, true, Color.RED));
        letterList.add(new letters("Sixty-Eight", R.drawable.sixty_eight, true, Color.RED));
        letterList.add(new letters("Sixty-Nine", R.drawable.sixty_nine, true, Color.RED));
        letterList.add(new letters("Seventy", R.drawable.seventy, true, Color.RED));
        letterList.add(new letters("Seventy-One", R.drawable.seventy_one, true, Color.RED));
        letterList.add(new letters("Seventy-Two", R.drawable.seventy_two, true, Color.RED));
        letterList.add(new letters("Seventy-Three", R.drawable.seventy_three, true, Color.RED));
        letterList.add(new letters("Seventy-Four", R.drawable.seventy_four, true, Color.RED));
        letterList.add(new letters("Seventy-Five", R.drawable.seventy_five, true, Color.RED));
        letterList.add(new letters("Seventy-Six", R.drawable.seventy_six, true, Color.RED));
        letterList.add(new letters("Seventy-Seven", R.drawable.seventy_seven, true, Color.RED));
        letterList.add(new letters("Seventy-Eight", R.drawable.seventy_eight, true, Color.RED));
        letterList.add(new letters("Seventy-Nine", R.drawable.seventy_nine, true, Color.RED));
        letterList.add(new letters("Eighty", R.drawable.eighty, true, Color.RED));
        letterList.add(new letters("Eighty-One", R.drawable.eighty_one, true, Color.RED));
        letterList.add(new letters("Eighty-Two", R.drawable.eighty_two, true, Color.RED));
        letterList.add(new letters("Eighty-Three", R.drawable.eighty_three, true, Color.RED));
        letterList.add(new letters("Eighty-Four", R.drawable.eighty_four, true, Color.RED));
        letterList.add(new letters("Eighty-Five", R.drawable.eighty_five, true, Color.RED));
        letterList.add(new letters("Eighty-Six", R.drawable.eighty_six, true, Color.RED));
        letterList.add(new letters("Eighty-Seven", R.drawable.eighty_seven, true, Color.RED));
        letterList.add(new letters("Eighty-Eight", R.drawable.eighty_eight, true, Color.RED));
        letterList.add(new letters("Eighty-Nine", R.drawable.eighty_nine, true, Color.RED));
        letterList.add(new letters("Ninety", R.drawable.ninety, true, Color.RED));
        letterList.add(new letters("Ninety-One", R.drawable.ninety_one, true, Color.RED));
        letterList.add(new letters("Ninety-Two", R.drawable.ninety_two, true, Color.RED));
        letterList.add(new letters("Ninety-Three", R.drawable.ninety_three, true, Color.RED));
        letterList.add(new letters("Ninety-Four", R.drawable.ninety_four, true, Color.RED));
        letterList.add(new letters("Ninety-Five", R.drawable.ninety_five, true, Color.RED));
        letterList.add(new letters("Ninety-Six", R.drawable.ninety_six, true, Color.RED));
        letterList.add(new letters("Ninety-Seven", R.drawable.ninety_seven, true, Color.RED));
        letterList.add(new letters("Ninety-Eight", R.drawable.ninety_eight, true, Color.RED));
        letterList.add(new letters("Ninety-Nine", R.drawable.ninety_nine, true, Color.RED));
        letterList.add(new letters("One Hundred", R.drawable.one_hundred, true, Color.RED));



        // Continue adding up to 100
    }
}
