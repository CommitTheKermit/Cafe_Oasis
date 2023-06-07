package com.example.ex1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ex1.R;
import com.example.ex1.Utils.ServerComm;
import com.google.android.material.slider.RangeSlider;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class Keyword_Modify extends AppCompatActivity {

    int[] keyword_mo_point_arr = {1,1,1,1,0,1,1,3,1,1,0,0};

    RangeSlider rangeSlider_beverage_mo, rangeSlider_dessert_mo,rangeSlider_various_mo,rangeSlider_special_mo,
            rangeSlider_size_mo, rangeSlider_Landscape_mo,rangeSlider_concentration_mo,rangeSlider_trendy_mo;
    CheckBox check_parking_mo, check_price_mo, check_gift_mo;

    View keyword_modify_arrow1;
    TextView btn_complete_keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_modify);

        keyword_modify_arrow1 = findViewById(R.id.keyword_modify_arrow1);
        btn_complete_keyword =findViewById(R.id.btn_complete_keyword);

        rangeSlider_beverage_mo = findViewById(R.id.rangeSlider_beverage_mo);
        rangeSlider_dessert_mo = findViewById(R.id.rangeSlider_dessert_mo);
        rangeSlider_various_mo = findViewById(R.id.rangeSlider_various_mo);
        rangeSlider_special_mo = findViewById(R.id.rangeSlider_special_mo);
        rangeSlider_size_mo = findViewById(R.id.rangeSlider_size_mo);
        rangeSlider_Landscape_mo = findViewById(R.id.rangeSlider_Landscape_mo);
        rangeSlider_concentration_mo = findViewById(R.id.rangeSlider_concentration_mo);
        rangeSlider_trendy_mo = findViewById(R.id.rangeSlider_trendy_mo);

        check_parking_mo = findViewById(R.id.check_parking_mo);
        check_gift_mo = findViewById(R.id.check_gift_mo);
        check_price_mo = findViewById(R.id.check_price_mo);

        rangeSlider_dessert_mo.addOnSliderTouchListener(rangeSliderTouchListener_dessert_mo);
        rangeSlider_various_mo.addOnSliderTouchListener(rangeSliderTouchListener_various_mo);
        rangeSlider_special_mo.addOnSliderTouchListener(rangeSliderTouchListener_special_mo);
        rangeSlider_size_mo.addOnSliderTouchListener(rangeSliderTouchListener_size_mo);
        rangeSlider_Landscape_mo.addOnSliderTouchListener(rangeSliderTouchListener_Landscape_mo);
        rangeSlider_concentration_mo.addOnSliderTouchListener(rangeSliderTouchListener_concentration_mo);
        rangeSlider_trendy_mo.addOnSliderTouchListener(rangeSliderTouchListener_trendy_mo);
        rangeSlider_beverage_mo.addOnSliderTouchListener(rangeSliderTouchListener_beverage_mo);

        keyword_modify_arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_complete_keyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(keyword_mo_point_arr[0]+keyword_mo_point_arr[1]+keyword_mo_point_arr[2]+keyword_mo_point_arr[3]<=7
                        &&keyword_mo_point_arr[5]+keyword_mo_point_arr[6]<=4){

                    if(check_parking_mo.isChecked()){
                        keyword_mo_point_arr[4]=1;
                    }
                    if(check_price_mo.isChecked()){
                        keyword_mo_point_arr[10]=1;
                    }
                    if(check_gift_mo.isChecked()){
                        keyword_mo_point_arr[11]=1;
                    }
                }
                else if(keyword_mo_point_arr[0]+keyword_mo_point_arr[1]+keyword_mo_point_arr[2]>7){
                    Toast.makeText(getApplicationContext(),"Desert, Various_menu, Special_menu의 총합이 6 이하여야 합니다.",Toast.LENGTH_SHORT).show();
                }
                else if(keyword_mo_point_arr[5]+keyword_mo_point_arr[6]>4){
                    Toast.makeText(getApplicationContext(),"Large_store, Landscape의 총합이 4 이하여야 합니다.",Toast.LENGTH_SHORT).show();
                }

                JsonObject jsonObject = new JsonObject();
                Gson gson = new Gson();
                try {
                    jsonObject.add("email", gson.toJsonTree(LoginActivity.userInfo.getUser_email()));
                    jsonObject.add("user_keyword_value", gson.toJsonTree(keyword_mo_point_arr));
                    int stuatusCOde = ServerComm.getStatusCode(new URL("http://cafeoasis.xyz/users/profile/keyword/update"),
                            jsonObject);
                    LoginActivity.userInfo.setUser_keyword(keyword_mo_point_arr);
                    finish();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    private final RangeSlider.OnSliderTouchListener rangeSliderTouchListener_beverage_mo =
            new RangeSlider.OnSliderTouchListener() {
                @Override
                public void onStartTrackingTouch(RangeSlider slider) {
//                    바가 시작하면 동작하는 부분
                }

                @Override
                public void onStopTrackingTouch(RangeSlider slider) {
//                    유저가 바에서 손을 떄었을때 동작하는 함수
//                    slider.getValues() 값이 [0.0, 5.0]처럼 배열로 값이 들어있다.
                    int miniNumber = Float.toString(slider.getValues().get(0)).indexOf(".");
                    String minVal = Float.toString(slider.getValues().get(0)).substring(0, miniNumber);
                    int keyword_point= Integer.parseInt(minVal);
                    keyword_mo_point_arr[0]=keyword_point;
//                    Log.d("DualThumbSeekbar ", "onStopTrackingTouch minPrice : " + minVal);
                }
            };

    private final RangeSlider.OnSliderTouchListener rangeSliderTouchListener_dessert_mo =
            new RangeSlider.OnSliderTouchListener() {
                @Override
                public void onStartTrackingTouch(RangeSlider slider) {
//                    바가 시작하면 동작하는 부분
                }

                @Override
                public void onStopTrackingTouch(RangeSlider slider) {
//                    유저가 바에서 손을 떄었을때 동작하는 함수
//                    slider.getValues() 값이 [0.0, 5.0]처럼 배열로 값이 들어있다.
                    int miniNumber = Float.toString(slider.getValues().get(0)).indexOf(".");
                    String minVal = Float.toString(slider.getValues().get(0)).substring(0, miniNumber);
                    int keyword_point= Integer.parseInt(minVal);
                    keyword_mo_point_arr[1]=keyword_point;
//                    Log.d("DualThumbSeekbar ", "onStopTrackingTouch minPrice : " + minVal);
                }
            };

    private final RangeSlider.OnSliderTouchListener rangeSliderTouchListener_various_mo =
            new RangeSlider.OnSliderTouchListener() {
                @Override
                public void onStartTrackingTouch(RangeSlider slider) {
//                    바가 시작하면 동작하는 부분
                }

                @Override
                public void onStopTrackingTouch(RangeSlider slider) {
//                    유저가 바에서 손을 떄었을때 동작하는 함수
//                    slider.getValues() 값이 [0.0, 5.0]처럼 배열로 값이 들어있다.
                    int miniNumber = Float.toString(slider.getValues().get(0)).indexOf(".");
                    String minVal = Float.toString(slider.getValues().get(0)).substring(0, miniNumber);
                    int keyword_point= Integer.parseInt(minVal);
                    keyword_mo_point_arr[2]=keyword_point;
//                    Log.d("DualThumbSeekbar ", "onStopTrackingTouch minPrice : " + minVal);
                }
            };

    private final RangeSlider.OnSliderTouchListener rangeSliderTouchListener_special_mo =
            new RangeSlider.OnSliderTouchListener() {
                @Override
                public void onStartTrackingTouch(RangeSlider slider) {
//                    바가 시작하면 동작하는 부분
                }

                @Override
                public void onStopTrackingTouch(RangeSlider slider) {
//                    유저가 바에서 손을 떄었을때 동작하는 함수
//                    slider.getValues() 값이 [0.0, 5.0]처럼 배열로 값이 들어있다.
                    int miniNumber = Float.toString(slider.getValues().get(0)).indexOf(".");
                    String minVal = Float.toString(slider.getValues().get(0)).substring(0, miniNumber);
                    int keyword_point= Integer.parseInt(minVal);
                    keyword_mo_point_arr[3]=keyword_point;
//                    Log.d("DualThumbSeekbar ", "onStopTrackingTouch minPrice : " + minVal);
                }
            };

    private final RangeSlider.OnSliderTouchListener rangeSliderTouchListener_size_mo =
            new RangeSlider.OnSliderTouchListener() {
                @Override
                public void onStartTrackingTouch(RangeSlider slider) {
//                    바가 시작하면 동작하는 부분
                }

                @Override
                public void onStopTrackingTouch(RangeSlider slider) {
//                    유저가 바에서 손을 떄었을때 동작하는 함수
//                    slider.getValues() 값이 [0.0, 5.0]처럼 배열로 값이 들어있다.
                    int miniNumber = Float.toString(slider.getValues().get(0)).indexOf(".");
                    String minVal = Float.toString(slider.getValues().get(0)).substring(0, miniNumber);
                    int keyword_point= Integer.parseInt(minVal);
                    keyword_mo_point_arr[5]=keyword_point;
//                    Log.d("DualThumbSeekbar ", "onStopTrackingTouch minPrice : " + minVal);
                }
            };

    private final RangeSlider.OnSliderTouchListener rangeSliderTouchListener_Landscape_mo =
            new RangeSlider.OnSliderTouchListener() {
                @Override
                public void onStartTrackingTouch(RangeSlider slider) {
//                    바가 시작하면 동작하는 부분
                }

                @Override
                public void onStopTrackingTouch(RangeSlider slider) {
//                    유저가 바에서 손을 떄었을때 동작하는 함수
//                    slider.getValues() 값이 [0.0, 5.0]처럼 배열로 값이 들어있다.
                    int miniNumber = Float.toString(slider.getValues().get(0)).indexOf(".");
                    String minVal = Float.toString(slider.getValues().get(0)).substring(0, miniNumber);
                    int keyword_point= Integer.parseInt(minVal);
                    keyword_mo_point_arr[6]=keyword_point;
//                    Log.d("DualThumbSeekbar ", "onStopTrackingTouch minPrice : " + minVal);
                }
            };

    private final RangeSlider.OnSliderTouchListener rangeSliderTouchListener_concentration_mo =
            new RangeSlider.OnSliderTouchListener() {
                @Override
                public void onStartTrackingTouch(RangeSlider slider) {
//                    바가 시작하면 동작하는 부분
                }

                @Override
                public void onStopTrackingTouch(RangeSlider slider) {
//                    유저가 바에서 손을 떄었을때 동작하는 함수
//                    slider.getValues() 값이 [0.0, 5.0]처럼 배열로 값이 들어있다.
                    int miniNumber = Float.toString(slider.getValues().get(0)).indexOf(".");
                    String minVal = Float.toString(slider.getValues().get(0)).substring(0, miniNumber);
                    int keyword_point= Integer.parseInt(minVal);
                    keyword_mo_point_arr[7]=4-keyword_point;
                    keyword_mo_point_arr[8]=keyword_point;
//                    Log.d("DualThumbSeekbar ", "onStopTrackingTouch minPrice : " + minVal);
                }
            };

    private final RangeSlider.OnSliderTouchListener rangeSliderTouchListener_trendy_mo =
            new RangeSlider.OnSliderTouchListener() {
                @Override
                public void onStartTrackingTouch(RangeSlider slider) {
//                    바가 시작하면 동작하는 부분
                }

                @Override
                public void onStopTrackingTouch(RangeSlider slider) {
//                    유저가 바에서 손을 떄었을때 동작하는 함수
//                    slider.getValues() 값이 [0.0, 5.0]처럼 배열로 값이 들어있다.
                    int miniNumber = Float.toString(slider.getValues().get(0)).indexOf(".");
                    String minVal = Float.toString(slider.getValues().get(0)).substring(0, miniNumber);
                    int keyword_point= Integer.parseInt(minVal);
                    keyword_mo_point_arr[9]=keyword_point;
//                    Log.d("DualThumbSeekbar ", "onStopTrackingTouch minPrice : " + minVal);
                }
            };

}