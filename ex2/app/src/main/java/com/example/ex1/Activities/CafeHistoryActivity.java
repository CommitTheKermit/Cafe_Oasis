package com.example.ex1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.ex1.R;

public class CafeHistoryActivity extends AppCompatActivity {

    View cafe_history_arrow1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_history);

        cafe_history_arrow1 = findViewById(R.id.cafe_history_arrow1);

        cafe_history_arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}