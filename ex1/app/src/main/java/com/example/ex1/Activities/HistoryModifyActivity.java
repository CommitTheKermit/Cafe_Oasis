package com.example.ex1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ex1.R;

public class HistoryModifyActivity extends AppCompatActivity {

    View history_arrow1;
    TextView btn_receip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_modify);

        history_arrow1 = findViewById(R.id.history_arrow1);
        btn_receip = findViewById(R.id.btn_receip);


        history_arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_receip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}