package com.example.ex1.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ex1.DataPage;
import com.example.ex1.R;
import com.example.ex1.ViewPagerAdapter;

import java.util.ArrayList;


public class RatingFragment extends Fragment {
    View view;
    ViewPager2 viewPager2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rating,container,false);

        viewPager2 = view.findViewById(R.id.viewpager2_rating);

        ArrayList<DataPage> list = new ArrayList<>();
        list.add(new DataPage(R.drawable.trost,"트로스트","대구 수성구 달구벌대로641길 4-8 2층","#디저트맛집"));
        list.add(new DataPage(R.drawable.cafe2,"2 Page","고백구","와우2"));
        list.add(new DataPage(R.drawable.cafe3,"3 Page","행복동","와우3"));

        viewPager2.setAdapter(new ViewPagerAdapter(list));




        return view;
    }
}