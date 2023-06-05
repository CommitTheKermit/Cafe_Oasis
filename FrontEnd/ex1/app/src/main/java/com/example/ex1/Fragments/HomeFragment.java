package com.example.ex1.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.ex1.R;
import com.example.ex1.Utils.ServerComm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class HomeFragment extends Fragment {

    Button rating_btn,recommendation_btn,bookmarket_btn;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);

        getParentFragmentManager().beginTransaction().add(R.id.child_frame, new RatingFragment()).commit();

        rating_btn = view.findViewById(R.id.rating_btn);
        recommendation_btn = view.findViewById(R.id.recommendation_btn);
        bookmarket_btn = view.findViewById(R.id.bookmarket_btn);

        rating_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.child_frame, new RatingFragment()).commit();
            }
        });

        recommendation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.child_frame, new RecommendationFragment()).commit();
            }
        });

        bookmarket_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.child_frame, new BookMarkerFragment()).commit();
            }
        });
//        btnRecomm.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("user_cafe_profile", new int[]{1, 3, 1, 3, 1, 3, 1, 1, 3});
//                    jsonObject.put("user_location", new double[]{35.8680733, 128.5995891});
//
//                    JSONArray jsonArray = ServerComm.getJSONArray(new URL("http://cafeoasis.xyz/cafe/recommend/keyword"),
//                            jsonObject);
//                    Log.d("debug", "debug");
//                } catch (MalformedURLException | JSONException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });



        return view;
    }
}