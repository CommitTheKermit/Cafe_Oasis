package com.example.ex1.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ex1.Activities.CafeHistoryActivity;
import com.example.ex1.Activities.Keyword_Modify;
import com.example.ex1.Activities.LoginActivity;
import com.example.ex1.Activities.MyProfile_Modify;
import com.example.ex1.R;


public class ProfileFragment extends Fragment {

    View view, profile_modify_arrow, history_modify_arrow, keyword_modify_arrow;
    TextView textProfileNickname, textProfileEmail;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile,container,false);
        profile_modify_arrow =  view.findViewById(R.id.profile_modify_arrow);
        history_modify_arrow = view.findViewById(R.id.history_modify_arrow);
        keyword_modify_arrow = view.findViewById(R.id.keyword_modify_arrow);

        textProfileNickname = view.findViewById(R.id.textProfileNickname);
        textProfileEmail = view.findViewById(R.id.textProfileEmail);

        textProfileNickname.setText(LoginActivity.userInfo.getUser_nickname());
        textProfileEmail.setText(LoginActivity.userInfo.getUser_email());

        profile_modify_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyProfile_Modify.class);
                intent.putExtra("option", "not_first");
                startActivity(intent);

            }
        });

        keyword_modify_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Keyword_Modify.class);
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