package com.example.ex1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ex1.R;

public class Keyword_Modify extends AppCompatActivity {

    View keyword_modify_arrow1;
    TextView btn_complete_keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_modify);

        keyword_modify_arrow1 = findViewById(R.id.keyword_modify_arrow1);
        btn_complete_keyword =findViewById(R.id.btn_complete_keyword);

        keyword_modify_arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_complete_keyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}