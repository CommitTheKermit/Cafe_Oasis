package com.example.ex1.Utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.ex1.Objects.JsonAndStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerComm {
    private static int statusCode = 0;
    private static JsonAndStatus outputJson = null;

    public static int getStatusCode(URL argUrl, JSONObject argJson){
        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                    // API 요청을 보내기 위한 URL 생성
                    HttpURLConnection conn = (HttpURLConnection) argUrl.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");

                    conn.setDoOutput(true);
                    conn.getOutputStream().write(argJson.toString().getBytes());

                    int status = conn.getResponseCode();

                    // 연결 종료
                    conn.disconnect();
                    statusCode = status;

                } catch (Exception e) {
                    return;
                }
            }
        };
        thread.start();

        try{
            thread.join();
            return statusCode;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public static JsonAndStatus getOutputString(URL argUrl, JSONObject argJson) {
        Thread thread = new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    String output = null;
                    JSONObject returnJson = null;
                    // API 요청을 보내기 위한 URL 생성
                    HttpURLConnection conn = (HttpURLConnection) argUrl.openConnection();
                    conn.setRequestMethod("POST");
                    if(argUrl.toString().equals("https://5yuqotffnj.apigw.ntruss.com/custom/v1/22921/5e7254c8d8fd2d1b52d8094dc099a1492233b6fe7e4222641b1586917df8e915/general")){
                        conn.setRequestProperty("X-OCR-SECRET",
                                "VXhlTkNvWFpzUGRDUkplUURxS2FoUXF4eXVVb05qakQ=");
                    }

                    conn.setRequestProperty("Content-Type", "application/json");

                    conn.setDoOutput(true);
                    conn.getOutputStream().write(argJson.toString().getBytes());

                    int status = conn.getResponseCode();

                    if (status == HttpURLConnection.HTTP_OK) {
                        InputStream temp = conn.getInputStream();

                        output = new BufferedReader(new InputStreamReader(temp)).lines()
                                .reduce((a, b) -> a + b).get();
                    } else {
//                        output = "FAIL TO RECEIVE FROM SERVER";
                    }

                    returnJson = new JSONObject(output);
                    outputJson = new JsonAndStatus();
                    // 연결 종료
                    conn.disconnect();
                    outputJson.setJsonObject(returnJson);
                    outputJson.setStatusCode(status);

                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        thread.start();

        try {
            thread.join();
            return outputJson;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
