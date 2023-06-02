package com.example.ex1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ex1.Objects.UserInfo;
import com.example.ex1.R;

public class Join2 extends AppCompatActivity {

    TextView btn_next_sign2;
    View arrow2;
    EditText edit_password, edit_check_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join2);

        arrow2 = findViewById(R.id.arrow2);
        btn_next_sign2 = findViewById(R.id.btn_next_sign2);
        edit_password = findViewById(R.id.edit_password);
        edit_check_password = findViewById(R.id.edit_check_password);

        Intent intent = getIntent(); //전달할 데이터를 받을 Intent
        UserInfo userInfo = (UserInfo) intent.getSerializableExtra("userInfo");


        btn_next_sign2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_password.getText().toString().equals(edit_check_password.getText().toString())){

                    userInfo.setUser_pw(edit_password.getText().toString());

                    Intent intent = new Intent(Join2.this,Join3.class);
                    intent.putExtra("userInfo", userInfo);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(Join2.this, "비밀번호를 다시 확인해보세요",
                            Toast.LENGTH_SHORT).show();
                }



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