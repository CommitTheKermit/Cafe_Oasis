package com.example.ex1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ex1.R;

public class Join2 extends AppCompatActivity {

    TextView btn_next_sign2;
    View arrow2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join2);

        arrow2 = findViewById(R.id.arrow2);
        btn_next_sign2 = findViewById(R.id.btn_next_sign2);

        btn_next_sign2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Join2.this,Join3.class);
                startActivity(intent);
            }
        });

        arrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Join2.this,Join1.class);
                startActivity(intent);
                finish();
            }
        });
    }
}