package com.example.ex1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ex1.R;

public class MyProfile_Modify extends AppCompatActivity {

    View profile_modify_arrow1;
    TextView btn_profile_modify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_modify);

        profile_modify_arrow1 = findViewById(R.id.profile_modify_arrow1);
        btn_profile_modify = findViewById(R.id.btn_profile_modify);

        profile_modify_arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_profile_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}