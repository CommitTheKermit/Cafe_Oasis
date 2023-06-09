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

public class Join3 extends AppCompatActivity {

    TextView btn_next_sign3;
    View arrow3;
    EditText edit_name, edit_nickname, edit_age, edit_num;
    Spinner spinner_gen;
    UserInfo userInfo = new UserInfo();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join3);

        arrow3 = findViewById(R.id.arrow3);
        btn_next_sign3 = findViewById(R.id.btn_next_sign3);
        edit_name = findViewById(R.id.edit_name);
        edit_nickname = findViewById(R.id.edit_nickname);
        edit_age = findViewById(R.id.edit_age);
        edit_num = findViewById(R.id.edit_num);
        spinner_gen = findViewById(R.id.spinner_gen);

        Intent intent = getIntent(); //전달할 데이터를 받을 Intent
        userInfo = (UserInfo) intent.getSerializableExtra("userInfo");

        String[] items = {"남성", "여성"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gen.setAdapter(adapter);

        spinner_gen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userInfo.setUser_sex(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_next_sign3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfo.setUser_name(edit_name.getText().toString());
                userInfo.setUser_nickname(edit_nickname.getText().toString());
                int age = -1;
                try {
                    age = Integer.parseInt(edit_age.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(Join3.this, "입력값을 다시 확인해보세요",
                            Toast.LENGTH_SHORT).show();
                }
                userInfo.setUser_age(age);
                userInfo.setUser_phone(edit_num.getText().toString());

                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put("email", userInfo.getUser_email());
                    jsonObject.put("password", userInfo.getUser_pw());
                    jsonObject.put("name", userInfo.getUser_name());
                    jsonObject.put("phone_no", userInfo.getUser_phone());
                    jsonObject.put("user_type", 1);
                    jsonObject.put("sex", userInfo.getUser_sex());
                    jsonObject.put("age", userInfo.getUser_age());
                    jsonObject.put("nickname", userInfo.getUser_nickname());

                    int statusCode = ServerComm.getStatusCode(new URL("http://cafeoasis.xyz/users/signup"),
                            jsonObject);
                    if(statusCode == 200){
                        Toast.makeText(Join3.this, "가입 성공",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Join3.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(Join3.this, "가입 실패",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | MalformedURLException e) {
                    throw new RuntimeException(e);
                }
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