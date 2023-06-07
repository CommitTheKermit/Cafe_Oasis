package com.example.ex1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ex1.Objects.UserInfo;
import com.example.ex1.R;
import com.example.ex1.Utils.ServerComm;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class MyProfile_Modify extends AppCompatActivity {

    View profile_modify_arrow1;
    TextView btn_profile_modify;
    EditText modify_name, modify_nickname, modify_age, modify_num;
    Spinner spinner_gen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_modify);

        profile_modify_arrow1 = findViewById(R.id.profile_modify_arrow1);
        btn_profile_modify = findViewById(R.id.btn_profile_modify);

        modify_name = findViewById(R.id.modify_name);
        modify_nickname = findViewById(R.id.modify_nickname);
        modify_age = findViewById(R.id.modify_age);
        modify_num = findViewById(R.id.modify_num);
        spinner_gen = findViewById(R.id.spinner_gen_mod);
        String[] items = {"남성", "여성"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gen.setAdapter(adapter);

        spinner_gen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoginActivity.userInfo.setUser_sex(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        profile_modify_arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_profile_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oauthRegister();
            }
        });


    }

    private void oauthRegister() {
        UserInfo userInfo = LoginActivity.userInfo;
        userInfo.setUser_name(modify_num.getText().toString());
        userInfo.setUser_nickname(modify_nickname.getText().toString());
        int age = -1;
        try {
            age = Integer.parseInt(modify_age.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(MyProfile_Modify.this, "입력값을 다시 확인해보세요",
                    Toast.LENGTH_SHORT).show();
        }
        userInfo.setUser_age(age);
        userInfo.setUser_phone(modify_num.getText().toString());

        JSONObject jsonObject = new JSONObject();
        Intent intent = getIntent(); //전달할 데이터를 받을 Intent
        String option = intent.getStringExtra("option");
        try {
            if(option.equals("firstLogin")){
                String email = (String) intent.getStringExtra("email");
                jsonObject.put("email", email);
                jsonObject.put("password", "oauth_login");

                jsonObject.put("name", modify_name.getText().toString());
                jsonObject.put("phone_no", modify_num.getText().toString());
                jsonObject.put("user_type", 1);
                jsonObject.put("sex", LoginActivity.userInfo.getUser_sex());
                jsonObject.put("age", Integer.parseInt(modify_age.getText().toString()));
                jsonObject.put("nickname", modify_nickname.getText().toString());

                userInfo.setUser_email(jsonObject.getString("email"));
                userInfo.setUser_name(jsonObject.getString("name"));
                userInfo.setUser_type(jsonObject.getInt("user_type"));
                userInfo.setUser_nickname(jsonObject.getString("nickname"));
                userInfo.setUser_age(jsonObject.getInt("age"));
                userInfo.setUser_sex(jsonObject.getInt("sex"));

                int statusCode = ServerComm.getStatusCode(new URL("http://cafeoasis.xyz/users/signup"),
                        jsonObject);
                if (statusCode == 200) {
                    Toast.makeText(MyProfile_Modify.this, "등록 성공",
                            Toast.LENGTH_SHORT).show();

                    intent = new Intent(MyProfile_Modify.this, Keyword.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MyProfile_Modify.this, "등록 실패",
                            Toast.LENGTH_SHORT).show();
                }
            }
            else{
                jsonObject.put("email", userInfo.getUser_email());
                jsonObject.put("password", userInfo.getUser_pw());

                jsonObject.put("name", modify_name.getText().toString());
                jsonObject.put("phone_no", modify_num.getText().toString());
                jsonObject.put("user_type", 1);
                jsonObject.put("sex", LoginActivity.userInfo.getUser_sex());
                jsonObject.put("age", Integer.parseInt(modify_age.getText().toString()));
                jsonObject.put("nickname", modify_nickname.getText().toString());

                userInfo.setUser_email(jsonObject.getString("email"));
                userInfo.setUser_name(jsonObject.getString("name"));
                userInfo.setUser_type(jsonObject.getInt("user_type"));
                userInfo.setUser_nickname(jsonObject.getString("nickname"));
                userInfo.setUser_age(jsonObject.getInt("age"));
                userInfo.setUser_sex(jsonObject.getInt("sex"));

                int statusCode = ServerComm.getStatusCode(new URL("http://cafeoasis.xyz/users/profileEdit"),
                        jsonObject);
                if (statusCode == 200) {
                    Toast.makeText(MyProfile_Modify.this, "등록 성공",
                            Toast.LENGTH_SHORT).show();

                    intent = new Intent(MyProfile_Modify.this, NaviActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MyProfile_Modify.this, "등록 실패",
                            Toast.LENGTH_SHORT).show();
                }
            }


        } catch (MalformedURLException | JSONException e) {
            throw new RuntimeException(e);
        }
    }
}