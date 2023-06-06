package com.example.ex1.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ex1.Objects.DataPage;
import com.example.ex1.R;
import com.example.ex1.ViewPagerAdapter;

import java.util.ArrayList;


public class BookMarkerFragment extends Fragment {
    View view;
    public static ViewPager2 viewPager2;
    public static ArrayList<DataPage> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_book_marker,container,false);
        HomeFragment.btnRecomm.setVisibility(View.INVISIBLE);
        viewPager2 = view.findViewById(R.id.viewpager2_bookmarker);
        HomeFragment.option = 2;
//        list.add(new DataPage(R.drawable.cafe3,"3 Page","사랑시","와우3"));
//        list.add(new DataPage(R.drawable.cafe2,"2 Page","고백구","와우2"));
//        list.add(new DataPage(R.drawable.cafe1,"1 Page","행복동","와우1"));

        viewPager2.setAdapter(new ViewPagerAdapter(list));




        return view;
    }
}