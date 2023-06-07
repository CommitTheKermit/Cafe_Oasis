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

public class Find_Id_Activity extends AppCompatActivity {

    View arrow_find_id;
    TextView btn_check_id, text_userid;
    EditText edit_find_id;
    String num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        arrow_find_id = findViewById(R.id.arrow_find_id);
        btn_check_id = findViewById(R.id.btn_check_id);
        text_userid = findViewById(R.id.text_userid);
        edit_find_id = findViewById(R.id.edit_find_id);

        arrow_find_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_check_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = edit_find_id.getText().toString();
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("phone_no", num);
                    JsonAndStatus resultJson = ServerComm.getOutputString(new URL("http://cafeoasis.xyz/users/findemail"),
                            jsonObject);

                    if(resultJson.getStatusCode() == 200){
                        Toast.makeText(Find_Id_Activity.this, "아이디찾기 성공",
                                Toast.LENGTH_SHORT).show();

                        JSONObject tempJson = resultJson.getJsonObject();

                        text_userid.setText(tempJson.getString("email"));

                    }
                    else{
                        Toast.makeText(Find_Id_Activity.this, "전화번호를 다시 확인해보세요", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        });



    }
}