package com.example.ex1.Main;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ex1.Activities.LoginActivity;
import com.example.ex1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int cafe_id = 0;


        new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                String returnStr;
                try{
                    // API 요청을 보내기 위한 URL 생성
                    URL url = new URL("http://cafeoasis.xyz/app_oasis/cafeinfo");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            int status = conn.getResponseCode();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");


                    // 번역할 텍스트와 목표 언어를 JSON 형식으로 작성
                    JSONObject data = new JSONObject();
                    data.put("user_id", cafe_id);

                    conn.setDoOutput(true);
                    conn.getOutputStream().write(data.toString().getBytes());

                    String output = null;
                    int status = conn.getResponseCode();
                    if(status == HttpURLConnection.HTTP_OK){
                        InputStream temp = conn.getInputStream();

                        output = new BufferedReader(new InputStreamReader(temp)).lines()
                                .reduce((a, b) -> a + b).get();
                    }
                    else{
                        //do something else
                    }

                    returnStr = new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text");
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
}