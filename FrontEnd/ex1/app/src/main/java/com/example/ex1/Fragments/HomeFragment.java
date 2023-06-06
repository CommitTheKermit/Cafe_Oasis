package com.example.ex1.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ex1.Activities.NaviActivity;
import com.example.ex1.DataPage;
import com.example.ex1.R;
import com.example.ex1.Utils.ServerComm;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


import java.net.MalformedURLException;
import java.net.URL;

public class HomeFragment extends Fragment {

    Button rating_btn,recommendation_btn,bookmarket_btn, btnRecomm;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);

        getParentFragmentManager().beginTransaction().add(R.id.child_frame, new RatingFragment()).commit();

        rating_btn = view.findViewById(R.id.rating_btn);
        recommendation_btn = view.findViewById(R.id.recommendation_btn);
        bookmarket_btn = view.findViewById(R.id.bookmarket_btn);
        btnRecomm = view.findViewById(R.id.btn_recomm_home);

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
        btnRecomm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"shit", Toast.LENGTH_SHORT).show();
                JsonObject jsonObject = new JsonObject();
                try {
                    int[] intArr = new int[]{1, 3, 1, 3, 1, 3, 1, 1, 3};
                    double[] doubleArr =  new double[]{35.8680733, 128.5995891};

                    Gson gson = new Gson();
                    jsonObject.add("user_cafe_profile", gson.toJsonTree(intArr));
                    jsonObject.add("user_location", gson.toJsonTree(doubleArr));


                    JSONArray jsonArray = ServerComm.getJSONArray(new URL("http://cafeoasis.xyz/cafe/recommend/keyword"),
                            jsonObject);
                    Log.d("debug", "debug");

//                    for(int i = 0; i < 3; i++)
//                    {
//                        JsonObject json = jsonArray.getJSONObject(i);
//                        String name = json.get("카페이름").toString();
//                        String address = json.get("카페이름").toString();
//                        String phone_no = json.get("카페이름").toString();
//                        RecommendationFragment.list.add(new DataPage())
//                    }


                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        });



        return view;
    }
}