package com.example.ex1.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ex1.Main.MainActivity;
import com.example.ex1.R;
import com.example.ex1.Utils.PermissionUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private TextView btn_login;
    private EditText login_input_email, login_input_password;
    View main_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login = (TextView) findViewById(R.id.btn_login);
        login_input_email = (EditText) findViewById(R.id.login_input_email);
        login_input_password = (EditText) findViewById(R.id.login_input_password);


        main_image = findViewById(R.id.main_image);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestLocationPermissions((AppCompatActivity) LoginActivity.this, 1, true);

            return;
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_input_email.getText().toString();
                String pw = login_input_password.getText().toString();


                new Thread(){
                    @Override
                    public void run() {
                        try{
                            // API 요청을 보내기 위한 URL 생성
                            URL url = new URL("http://52.79.247.229:8000/pics_users/login");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            int status = conn.getResponseCode();
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Type", "application/json");


                            // 번역할 텍스트와 목표 언어를 JSON 형식으로 작성
                            JSONObject data = new JSONObject();
                            data.put("user_id", email);
                            data.put("user_pw", pw);

                            conn.setDoOutput(true);
                            conn.getOutputStream().write(data.toString().getBytes());

                            String output = null;
                            int status = conn.getResponseCode();
                            if(status == HttpURLConnection.HTTP_OK){
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 다시 확인해보세요", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            // 연결 종료
                            conn.disconnect();
                        }
                        catch (JSONException e)
                        {
                            throw new RuntimeException(e);
                        } catch (ProtocolException e) {
                            throw new RuntimeException(e);
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }.start();

            }
        });



    }
}