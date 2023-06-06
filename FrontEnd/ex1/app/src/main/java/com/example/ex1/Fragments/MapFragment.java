package com.example.ex1.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ex1.Adapter.pointAdapter;
import com.example.ex1.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private MapView mapView;
    private static NaverMap naverMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000; //2
    private FusedLocationSource locationSource; //2
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    //Infowindow 변수 선언 및 초기화
    private InfoWindow infoWindow1 = new InfoWindow();

    //마커
    private Marker marker1 = new Marker();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE); //2
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location initialLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);;
        double latitude = initialLocation.getLatitude();    // 위도
        double longitude = initialLocation.getLongitude();  // 경도

        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(latitude, longitude),  // 위치 지정
                15                           // 줌 레벨
        );

        ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE); //권한확인
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        naverMap.setLocationSource(locationSource);  //현재 위치
        naverMap.setLayerGroupEnabled(naverMap.LAYER_GROUP_BUILDING, true);

        naverMap.setCameraPosition(cameraPosition);
        // 정보 창이 열려있는 경우, 지도를 누르면 닫기
        naverMap.setOnMapClickListener((point, coord) -> {
            if (infoWindow1.getMarker() != null) {
                infoWindow1.close();
            }
        });

        //2. Json 시도
       /*
       try {
            String json = loadJSONFromAsset(); // assets 폴더의 JSON 파일을 어떻게 읽어오지..?
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                double lat = jsonObject.getDouble("latitude");
                double lng = jsonObject.getDouble("longitude");
                String name = jsonObject.getString("cafe_name");
                String address = jsonObject.getString("address");
                String phone = jsonObject.getString("cafe_phone");

                // 마커 생성
                Marker[] marker;
                setMarker(marker[i], lat, lng, R.drawable.ic_baseline_place_24, name);

                marker1.setOnClickListener(overlay -> {
                    if (marker1.getInfoWindow() == null) {
                        ViewGroup rootView = (ViewGroup) mapView.findViewById(R.id.fragment_container);
                        pointAdapter adapter = new pointAdapter(requireActivity(), rootView);
                        // 정보 창을 엽니다.
                        infoWindow1.setAdapter(adapter);
                        infoWindow1.setZIndex(10); //인포창의 우선순위
                        infoWindow1.setAlpha(0.9f); //투명도 조정
                        infoWindow1.open(marker1); //인포창 표시
                    } else {
                        // 정보 창이 이미 열려있는 경우 닫습니다.
                        infoWindow1.close();
                    }
                    return true;
                });
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }*/

        setMarker(marker1, 35.84049650463925, 128.7003034345919, R.drawable.ic_baseline_place_24, "트로스트");

        //1. 기존 되는 코드
        marker1.setOnClickListener(overlay -> {
            if (marker1.getInfoWindow() == null) {
                ViewGroup rootView = (ViewGroup) mapView.findViewById(R.id.fragment_container);
                pointAdapter adapter = new pointAdapter(requireActivity(), rootView);
                // 정보 창을 엽니다.
                infoWindow1.setAdapter(adapter);
                infoWindow1.setZIndex(10); //인포창의 우선순위
                infoWindow1.setAlpha(0.9f); //투명도 조정
                infoWindow1.open(marker1); //인포창 표시
            } else {
                // 정보 창이 이미 열려있는 경우 닫습니다.
                infoWindow1.close();
            }
            return true;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void setMarker(Marker marker, double lat, double lng, int resourceID, String name){
        //원근감 표시
        marker.setIconPerspectiveEnabled(true);
        //아이콘 지정
        marker.setIcon(OverlayImage.fromResource(resourceID));
        //마커 위치
        marker.setPosition(new LatLng(lat, lng));
        //마커 표시
        marker.setMap(naverMap);
        //marker.setCaptionText(name);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
