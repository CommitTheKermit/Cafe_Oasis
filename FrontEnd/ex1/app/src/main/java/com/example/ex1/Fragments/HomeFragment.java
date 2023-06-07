package com.example.ex1.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ex1.Activities.Keyword;
import com.example.ex1.Activities.LoginActivity;
import com.example.ex1.Activities.NaviActivity;
import com.example.ex1.Objects.DataPage;
import com.example.ex1.R;
import com.example.ex1.Utils.DownloadImageTask;
import com.example.ex1.Utils.ServerComm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.ex1.ViewPagerAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.naver.maps.geometry.LatLng;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HomeFragment extends Fragment {

    Button rating_btn, recommendation_btn, bookmarket_btn, btn_map_home;
    View view;

    public static Button btnRecomm;
    public static int option;
    public static int index;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);

        getParentFragmentManager().beginTransaction().add(R.id.child_frame, new RatingFragment()).commit();

        rating_btn = view.findViewById(R.id.rating_btn);
        recommendation_btn = view.findViewById(R.id.recommendation_btn);
        bookmarket_btn = view.findViewById(R.id.bookmarket_btn);
        btnRecomm = view.findViewById(R.id.btn_recomm_home);
        btnRecomm.setVisibility(View.INVISIBLE);
        btn_map_home = view.findViewById(R.id.btn_map_home);

        rating_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.child_frame, new RatingFragment()).commit();
            }
        });

        recommendation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.child_frame, new RecommendationFragment()).commit();
            }
        });

        bookmarket_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.child_frame, new BookMarkerFragment()).commit();
            }
        });
        btnRecomm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject jsonObject = new JsonObject();
                try {


                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                    PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    double[] doubleArr =  new double[2];

                    doubleArr[0] = location.getLatitude();    // 위도
                    doubleArr[1] = location.getLongitude();  // 경도

                    Gson gson = new Gson();

                    int[] tempArr = new int[9];
                    for(int i = 0 ; i < 9; i++)
                        tempArr[i] = LoginActivity.userInfo.getUser_keyword()[i];

                    jsonObject.add("user_cafe_profile",
                            gson.toJsonTree(tempArr));
                    jsonObject.add("user_location", gson.toJsonTree(doubleArr));

                    JSONArray jsonArray = ServerComm.getJSONArray(new URL("http://cafeoasis.xyz/cafe/recommend/keyword"),
                            jsonObject);
                    RecommendationFragment.list.clear();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject json = jsonArray.getJSONObject(i);
                        String name = json.get("cafe_name").toString();
                        String address = json.get("address").toString();
                        String phone_no = json.get("cafe_phone_no").toString();
                        double latitude = json.getDouble("latitude");
                        double longitude = json.getDouble("longitude");
                        String url = json.getString("cafe_image");
                        if(!url.startsWith("https://"))
                            url = "https://drive.google.com/open?id=1cHCOfMlA4NiGS8odHDNUI3jL0XXMM994&usp=drive_fs";

                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        Future<Bitmap> future = executor.submit(
                                new DownloadImageTask(url));

                        Bitmap bitmap = future.get();
                        Resources res = getResources();
                        Drawable drawable = new BitmapDrawable(res, bitmap);

                        RecommendationFragment.list.add(new DataPage(drawable,
                                name, address, phone_no, latitude, longitude));
                    }
                    RecommendationFragment.viewPager2.setAdapter(
                            new ViewPagerAdapter(RecommendationFragment.list));

                } catch (ExecutionException |
                         InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btn_map_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<DataPage> cafeList = null;
                ViewPager2 viewPager = null;
                switch (option){
                    case 0:
                        viewPager = RatingFragment.viewPager2;
                        cafeList = RatingFragment.list;
                        break;
                    case 1:
                        viewPager = RecommendationFragment.viewPager2;
                        cafeList = RecommendationFragment.list;
                        break;
                    case 2:
                        viewPager = BookMarkerFragment.viewPager2;
                        cafeList = BookMarkerFragment.list;
                        break;

                }

                index = viewPager.getCurrentItem();
                double latitude = cafeList.get(index).getLatitude();
                double longitude = cafeList.get(index).getLongitude();

                MapFragment.latLng = new LatLng(latitude, longitude);
                NaviActivity.bottomNavigationView.setSelectedItemId(R.id.map);
//                getActivity().getSupportFragmentManager().beginTransaction().
//                        replace(R.id.main_frame, new MapFragment()).commit();

            }
        });


        return view;
    }
}