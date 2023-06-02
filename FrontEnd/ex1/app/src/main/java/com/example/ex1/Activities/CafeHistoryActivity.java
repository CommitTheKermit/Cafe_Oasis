package com.example.ex1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ex1.R;

public class CafeHistoryActivity extends AppCompatActivity {

    View cafe_history_arrow1, receipt_arrow, cafe_history_modify_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_history);

        cafe_history_arrow1 = findViewById(R.id.cafe_history_arrow1);
        receipt_arrow = findViewById(R.id.receipt_arrow);
        cafe_history_modify_arrow = findViewById(R.id.cafe_history_modify_arrow);


        cafe_history_arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        receipt_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReceiptRecognitionActivity.class);
                startActivity(intent);
            }
        });

        cafe_history_modify_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistoryModifyActivity.class);
                startActivity(intent);
            }
        });


    }
}