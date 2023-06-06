package com.example.ex1.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ex1.DataPage;
import com.example.ex1.R;
import com.example.ex1.Utils.ServerComm;
import com.example.ex1.ViewPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class RecommendationFragment extends Fragment {
    View view;
    ViewPager2 viewPager2;
    Button btnRecomm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recommendation,container,false);

        viewPager2 = view.findViewById(R.id.viewpager2_recommendation);
        btnRecomm = (Button) view.findViewById(R.id.viewpager_recomm_btn);

        ArrayList<DataPage> list = new ArrayList<>();
        list.add(new DataPage(R.drawable.cafe2,"2 Page","사랑시","와우2"));
        list.add(new DataPage(R.drawable.cafe3,"3 Page","고백구","와우3"));
        list.add(new DataPage(R.drawable.cafe1,"1 Page","행복동","와우1"));

        viewPager2.setAdapter(new ViewPagerAdapter(list));

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}