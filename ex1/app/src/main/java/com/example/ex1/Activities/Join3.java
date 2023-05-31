package com.example.ex1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ex1.R;

public class Join3 extends AppCompatActivity {

    TextView btn_next_sign3;
    View arrow3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join3);

        arrow3 = findViewById(R.id.arrow3);
        btn_next_sign3 = findViewById(R.id.btn_next_sign3);

        btn_next_sign3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Join3.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        arrow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Join3.this,Join2.class);
                startActivity(intent);
                finish();
            }
        });
    }
}