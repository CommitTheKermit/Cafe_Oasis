package com.example.ex1.Activities;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonDownloader {
    int cafe_id = 1;
    String cafe_name;
    public void downloadJson() {
        new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                String cafe_name;
                try {
                    // API 요청을 보내기 위한 URL 생성
                    URL url = new URL("http://cafeoasis.xyz/app_oasis/cafeinfo");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");

                    // 번역할 텍스트와 목표 언어를 JSON 형식으로 작성
                    JSONObject data = new JSONObject();
                    data.put("cafe_id", cafe_id);

                    conn.setDoOutput(true);
                    conn.getOutputStream().write(data.toString().getBytes());

                    String output = null;
                    int status = conn.getResponseCode();
                    if (status == HttpURLConnection.HTTP_OK) {
                        InputStream temp = conn.getInputStream();
                        output = new BufferedReader(new InputStreamReader(temp)).lines()
                                .reduce((a, b) -> a + b).get();
                    } else {
                        //do something else
                    }
                    cafe_name = new JSONObject(output).getString("cafe_name");
                    data.getString(cafe_name);
                    // 연결 종료
                    conn.disconnect();
                } catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }
}

