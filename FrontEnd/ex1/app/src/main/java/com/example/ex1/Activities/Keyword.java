package com.example.ex1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

public class Keyword extends AppCompatActivity {
    public static int[] intArr = new int[9];
    int[] keyword_point_arr = {1,1,1,1,0,1,1,3,1,1,0,0};

    RangeSlider rangeSlider_beverage, rangeSlider_dessert,rangeSlider_various,rangeSlider_special,rangeSlider_size,
            rangeSlider_Landscape,rangeSlider_concentration,rangeSlider_trendy,rangeSlider_price;
    CheckBox check_parking, check_price,check_gift;

    TextView btn_next_keyword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword);

        rangeSlider_beverage = findViewById(R.id.rangeSlider_beverage);
        rangeSlider_dessert = findViewById(R.id.rangeSlider_dessert);
        rangeSlider_various = findViewById(R.id.rangeSlider_various);
        rangeSlider_special = findViewById(R.id.rangeSlider_special);
        rangeSlider_size = findViewById(R.id.rangeSlider_size);
        rangeSlider_Landscape = findViewById(R.id.rangeSlider_Landscape);
        rangeSlider_concentration = findViewById(R.id.rangeSlider_concentration);
        rangeSlider_trendy = findViewById(R.id.rangeSlider_trendy);

        check_parking = findViewById(R.id.check_parking);
        check_gift = findViewById(R.id.check_gift);
        check_price = findViewById(R.id.check_price);
        btn_next_keyword = findViewById(R.id.btn_next_keyword);

        rangeSlider_dessert.addOnSliderTouchListener(rangeSliderTouchListener_dessert);
        rangeSlider_various.addOnSliderTouchListener(rangeSliderTouchListener_various);
        rangeSlider_special.addOnSliderTouchListener(rangeSliderTouchListener_special);
        rangeSlider_size.addOnSliderTouchListener(rangeSliderTouchListener_size);
        rangeSlider_Landscape.addOnSliderTouchListener(rangeSliderTouchListener_Landscape);
        rangeSlider_concentration.addOnSliderTouchListener(rangeSliderTouchListener_concentration);
        rangeSlider_trendy.addOnSliderTouchListener(rangeSliderTouchListener_trendy);
        rangeSlider_beverage.addOnSliderTouchListener(rangeSliderTouchListener_beverage);


        btn_next_keyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(keyword_point_arr[0]+keyword_point_arr[1]+keyword_point_arr[2]+keyword_point_arr[3]<=7
                        &&keyword_point_arr[5]+keyword_point_arr[6]<=4){

                    if(check_parking.isChecked()){
                        keyword_point_arr[4]=1;
                    }
                    if(check_price.isChecked()){
                        keyword_point_arr[10]=1;
                    }
                    if(check_gift.isChecked()){
                        keyword_point_arr[11]=1;
                    }

                }
                else if(keyword_point_arr[0]+keyword_point_arr[1]+keyword_point_arr[2]>7){
                    Toast.makeText(getApplicationContext(),"Desert, Various_menu, Special_menu의 총합이 6 이하여야 합니다.",Toast.LENGTH_SHORT).show();
                }
                else if(keyword_point_arr[5]+keyword_point_arr[6]>4){
                    Toast.makeText(getApplicationContext(),"Large_store, Landscape의 총합이 4 이하여야 합니다.",Toast.LENGTH_SHORT).show();
                }
                JsonObject jsonObject = new JsonObject();
                Gson gson = new Gson();
                try {
                    jsonObject.add("email", gson.toJsonTree(LoginActivity.userInfo.getUser_email()));
                    jsonObject.add("user_keyword_value", gson.toJsonTree(keyword_point_arr));
                    ServerComm.getJSONArray(new URL("http://cafeoasis.xyz/users/profile/keyword/create"),
                            jsonObject);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }


            }
        });


    }

    private final RangeSlider.OnSliderTouchListener rangeSliderTouchListener_beverage =
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
                    keyword_point_arr[0]=keyword_point;
//                    Log.d("DualThumbSeekbar ", "onStopTrackingTouch minPrice : " + minVal);
                }
            };

    private final RangeSlider.OnSliderTouchListener rangeSliderTouchListener_dessert =
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
                    keyword_point_arr[1]=keyword_point;
//                    Log.d("DualThumbSeekbar ", "onStopTrackingTouch minPrice : " + minVal);
                }
            };

    private final RangeSlider.OnSliderTouchListener rangeSliderTouchListener_various =
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
                    keyword_point_arr[2]=keyword_point;
//                    Log.d("DualThumbSeekbar ", "onStopTrackingTouch minPrice : " + minVal);
                }
            };

    private final RangeSlider.OnSliderTouchListener rangeSliderTouchListener_special =
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
                    keyword_point_arr[3]=keyword_point;
//                    Log.d("DualThumbSeekbar ", "onStopTrackingTouch minPrice : " + minVal);
                }
            };

    private final RangeSlider.OnSliderTouchListener rangeSliderTouchListener_size =
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
                    keyword_point_arr[5]=keyword_point;
//                    Log.d("DualThumbSeekbar ", "onStopTrackingTouch minPrice : " + minVal);
                }
            };

    private final RangeSlider.OnSliderTouchListener rangeSliderTouchListener_Landscape =
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
                    keyword_point_arr[6]=keyword_point;
//                    Log.d("DualThumbSeekbar ", "onStopTrackingTouch minPrice : " + minVal);
                }
            };

    private final RangeSlider.OnSliderTouchListener rangeSliderTouchListener_concentration =
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
                    keyword_point_arr[7]=4-keyword_point;
                    keyword_point_arr[8]=keyword_point;
//                    Log.d("DualThumbSeekbar ", "onStopTrackingTouch minPrice : " + minVal);
                }
            };

    private final RangeSlider.OnSliderTouchListener rangeSliderTouchListener_trendy =
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
                    keyword_point_arr[9]=keyword_point;
//                    Log.d("DualThumbSeekbar ", "onStopTrackingTouch minPrice : " + minVal);
                }
            };


}