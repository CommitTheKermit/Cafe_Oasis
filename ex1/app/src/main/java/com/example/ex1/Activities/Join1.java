package com.example.ex1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ex1.R;

public class Join1 extends AppCompatActivity {

    TextView btn_email,btn_next_sign1;
    View arrow1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join1);

        btn_email = findViewById(R.id.btn_email);
        arrow1 = findViewById(R.id.arrow1);
        btn_next_sign1 = findViewById(R.id.btn_next_sign1);

        arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Join1.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_next_sign1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Join1.this,Join2.class);
                startActivity(intent);
            }
        });

    }
}