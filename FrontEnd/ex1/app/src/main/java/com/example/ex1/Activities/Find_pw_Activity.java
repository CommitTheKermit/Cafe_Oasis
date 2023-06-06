package com.example.ex1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ex1.Objects.JsonAndStatus;
import com.example.ex1.R;
import com.example.ex1.Utils.ServerComm;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class Find_pw_Activity extends AppCompatActivity {

    View arrow_find_pw;
    EditText edit_find_pw_email, edit_find_pw_num;
    TextView btn_check_pw, text_userpw;
    String num,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        arrow_find_pw = findViewById(R.id.arrow_find_pw);
        edit_find_pw_email = findViewById(R.id.edit_find_pw_email);
        edit_find_pw_num = findViewById(R.id.edit_find_pw_num);
        btn_check_pw = findViewById(R.id.btn_check_pw);
        //text_userpw = findViewById(R.id.text_userpw);

        arrow_find_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_check_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = edit_find_pw_num.getText().toString();
                email = edit_find_pw_email.getText().toString();
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("email",email);
                    jsonObject.put("phone_no", num);
                    JsonAndStatus resultJson = ServerComm.getOutputString(new URL("http://cafeoasis.xyz/users/findpw"),
                            jsonObject);

                    if(resultJson.getStatusCode() == 200){
                        Toast.makeText(Find_pw_Activity.this, "인증성공",
                               Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(),Change_pw_Activity.class);
                        startActivity(intent);
                        finish();
//                        JSONObject tempJson = resultJson.getJsonObject();
//
//                        text_userpw.setText(tempJson.getString("password"));

                    }
                    else{
                        Toast.makeText(Find_pw_Activity.this, "이메일 또는 전화번호를 다시 확인해보세요", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
}