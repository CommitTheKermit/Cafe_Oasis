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
import org.mindrot.jbcrypt.BCrypt;

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
                String pw_hash = BCrypt.hashpw(pw,BCrypt.gensalt(10));
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("password",pw_hash);

                    int statusCode = ServerComm.getStatusCode(new URL("http://cafeoasis.xyz/users/signup"),
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