package com.example.ex1.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ex1.Activities.CafeHistoryActivity;
import com.example.ex1.Activities.MyProfile_Modify;
import com.example.ex1.R;


public class ProfileFragment extends Fragment {

    View view, profile_modify_arrow, history_modify_arrow;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile,container,false);
        profile_modify_arrow =  view.findViewById(R.id.profile_modify_arrow);
        history_modify_arrow = view.findViewById(R.id.history_modify_arrow);


        profile_modify_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyProfile_Modify.class);
                startActivity(intent);
            }
        });

        history_modify_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CafeHistoryActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }
}