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
                    public void run() {//HttpURLConnection 통신하기 위해서는 반드시 쓰레드가 필요함
                        try{
                            // API 요청을 보내기 위한 URL 생성
                            URL url = new URL("http://52.79.247.229:8000/pics_users/login");//서버 IP + 서버 메소드
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            int status = conn.getResponseCode();
                            conn.setRequestMethod("POST");//대부분 POST 방식으로 처리함
                            conn.setRequestProperty("Content-Type", "application/json");


                            // 번역할 텍스트와 목표 언어를 JSON 형식으로 작성
                            JSONObject data = new JSONObject();//JSON 형식 생성
                            data.put("user_id", email);//JSON에 키 : 밸류 저장
                            data.put("user_pw", pw);//JSON에 키 : 밸류 저장

                            conn.setDoOutput(true);//POST 방식 요청/응답에 꼭 필요
                            conn.getOutputStream().write(data.toString().getBytes());//JSON을 커넥션에 써줌

                            String output = null;
                            int status = conn.getResponseCode();//메소드 처리 아님. 상태코드만 받는다
                            if(status == HttpURLConnection.HTTP_OK){//상태코드가 200이라면 정상처리됐음을 의미
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 다시 확인해보세요", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            // 연결 종료
                            conn.disconnect();//커낵션 종료 꼭 필요함
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