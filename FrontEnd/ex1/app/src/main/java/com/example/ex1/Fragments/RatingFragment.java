package com.example.ex1.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ex1.Activities.LoginActivity;
import com.example.ex1.Objects.DataPage;
import com.example.ex1.R;
import com.example.ex1.ViewPagerAdapter;

import java.util.ArrayList;


public class RatingFragment extends Fragment {
    View view;
    public static ViewPager2 viewPager2;
    public static ArrayList<DataPage> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rating,container,false);
        HomeFragment.btnRecomm.setVisibility(View.INVISIBLE);
        viewPager2 = view.findViewById(R.id.viewpager2_rating);
        HomeFragment.option = 0;

        viewPager2.setAdapter(new ViewPagerAdapter(LoginActivity.list));




        return view;
    }
}