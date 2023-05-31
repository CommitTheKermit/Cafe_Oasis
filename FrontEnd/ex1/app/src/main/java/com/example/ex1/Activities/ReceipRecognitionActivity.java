package com.example.ex1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.ex1.R;

public class ReceipRecognitionActivity extends AppCompatActivity {

    View receip_arrow1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receip_recognition);

        receip_arrow1 = findViewById(R.id.receip_arrow1);

        receip_arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}