package com.example.ex1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class HomeFragment extends Fragment {

    ImageButton rating_btn,recommendation_btn,bookmarket_btn;
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


        return view;
    }
}