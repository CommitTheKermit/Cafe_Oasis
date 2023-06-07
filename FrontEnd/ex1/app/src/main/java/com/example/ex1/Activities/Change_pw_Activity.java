package com.example.ex1.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ex1.Objects.UserInfo;
import com.example.ex1.R;
import com.example.ex1.Utils.ServerComm;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class Change_pw_Activity extends AppCompatActivity {

    View arrow_change_pw;
    EditText edit_change_pw_email;
    TextView btn_change_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);

        arrow_change_pw = findViewById(R.id.arrow_change_pw);
        edit_change_pw_email = findViewById(R.id.edit_change_pw_email);
        btn_change_pw = findViewById(R.id.btn_change_pw);

        Intent intent = getIntent(); //전달할 데이터를 받을 Intent
        String email =  intent.getStringExtra("email");
        String phone_no = intent.getStringExtra("phone_no");

        arrow_change_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_change_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pw = edit_change_pw_email.getText().toString();
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("email",email);
                    jsonObject.put("password",pw);
                    jsonObject.put("phone_no",phone_no);

                    int statusCode = ServerComm.getStatusCode(new URL("http://cafeoasis.xyz/users/findpw"),
                            jsonObject);


                    if(statusCode == 200){
                        Toast.makeText(Change_pw_Activity.this, "비밀번호변경 성공",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Change_pw_Activity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(Change_pw_Activity.this, "비밀번호변경 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | MalformedURLException e) {
                    throw new RuntimeException(e);
                }


            }
        });




    }
}