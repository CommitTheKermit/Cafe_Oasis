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
import com.example.ex1.Utils.ServerComm;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class Join1 extends AppCompatActivity {

    EditText editEmail, editCheckEmail;
    TextView btnEmailSend, btnCheckCode, btn_next_sign1;
    View arrow1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join1);


        btnEmailSend = findViewById(R.id.btnEmailSend);
        btnCheckCode = findViewById(R.id.btnCheckCode);
        editEmail = findViewById(R.id.edit_email);
        editCheckEmail = findViewById(R.id.edit_check_email);
        arrow1 = findViewById(R.id.arrow1);
        btn_next_sign1 = findViewById(R.id.btn_next_sign1);
        btn_next_sign1.setEnabled(false);

        UserInfo userInfo = new UserInfo();

        arrow1.setOnClickListener(new View.OnClickListener() { //로그인으로 돌아가기
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Join1.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnEmailSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputEmail = editEmail.getText().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_email", inputEmail);
                    int statusCode = ServerComm.getStatusCode(new URL("http://cafeoasis.xyz/app_oasis/mailsend"),
                            jsonObject);
                    if(statusCode == 200){
                        Toast.makeText(Join1.this, "인증 코드 발송 이메일을 확인해주세요",
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(Join1.this, "인증 코드 발송 실패",
                                Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException | MalformedURLException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        btnCheckCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputEmail = editEmail.getText().toString();
                String inputCode = editCheckEmail.getText().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_email", inputEmail);
                    jsonObject.put("user_code", inputCode);
                    int statusCode = ServerComm.getStatusCode(new URL("http://cafeoasis.xyz/app_oasis/mailverify"),
                            jsonObject);
                    if(statusCode == 200){
                        Toast.makeText(Join1.this, "인증 성공",
                                Toast.LENGTH_SHORT).show();
                        btn_next_sign1.setEnabled(true);
                    }
                    else{
                        Toast.makeText(Join1.this, "인증 실패",
                                Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException | MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btn_next_sign1.setOnClickListener(new View.OnClickListener() { //Join2로 넘어가기
            @Override
            public void onClick(View v) {
                String inputEmail = editEmail.getText().toString();
                userInfo.setUser_email(inputEmail);

                Intent intent = new Intent(Join1.this,Join2.class);
                intent.putExtra("userInfo", userInfo);
                startActivity(intent);
                finish();
            }
        });

    }
}