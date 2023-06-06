package com.example.ex1.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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

import com.example.ex1.Activities.LoginActivity;
import com.example.ex1.Adapter.pointAdapter;
import com.example.ex1.Objects.DataPage;
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

import java.util.ArrayList;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private MapView mapView;
    public static NaverMap naverMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000; //2
    private FusedLocationSource locationSource; //2
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    public static LatLng latLng;

    //Infowindow 변수 선언 및 초기화
    private InfoWindow[] infoWindows = new InfoWindow[10];
    int i = 0;
    ArrayList<DataPage> cafeList = null;

    //마커
//    private Marker marker1 = new Marker();
    private Marker[] markerArr = new Marker[10];

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
        Location initialLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double latitude = initialLocation.getLatitude();    // 위도
        double longitude = initialLocation.getLongitude();  // 경도
        CameraPosition cameraPosition = null;

        if(latLng != null){
            cameraPosition = new CameraPosition(
                    latLng,  // 위치 지정
                    15                           // 줌 레벨
            );
            latLng = null;
        }
        else{
            cameraPosition = new CameraPosition(
                    new LatLng(latitude, longitude),  // 위치 지정
                    15                           // 줌 레벨
            );
        }
        int drawble = R.drawable.ic_baseline_place_24;
        if(LoginActivity.list.size() > 0){
            cafeList = LoginActivity.list;
            if(RecommendationFragment.list.size() > 0)
                cafeList.addAll(RecommendationFragment.list);
            for(i = 0; i < cafeList.size(); i++){
                markerArr[i] = new Marker();
                infoWindows[i] = new InfoWindow();

                if(i > 2)
                    drawble = R.drawable.baseline_place_24_pink;

                setMarker(markerArr[i],
                        cafeList.get(i).getLatitude(),
                        cafeList.get(i).getLongitude(),
                        drawble,
                        cafeList.get(i).getCafe_name());

                final int index = i;
                markerArr[i].setOnClickListener(new Overlay.OnClickListener() {
                    @Override
                    public boolean onClick(@NonNull Overlay overlay) {
                        if (markerArr[index].getInfoWindow() == null) {
                            ViewGroup rootView = (ViewGroup) mapView.findViewById(R.id.fragment_container);
                            pointAdapter adapter = new pointAdapter(requireActivity(), rootView, cafeList.get(index));
                            // 정보 창을 엽니다.
                            infoWindows[index].setAdapter(adapter);
                            infoWindows[index].setZIndex(10); //인포창의 우선순위
                            infoWindows[index].setAlpha(0.9f); //투명도 조정
                            infoWindows[index].open(markerArr[index]); //인포창 표시
                        } else {
                            // 정보 창이 이미 열려있는 경우 닫습니다.
                            infoWindows[index].close();
                        }
                        return true;
                    }
                });

            }
        }

        ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE); //권한확인
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        naverMap.setLocationSource(locationSource);  //현재 위치
        naverMap.setLayerGroupEnabled(naverMap.LAYER_GROUP_BUILDING, true);

        naverMap.setCameraPosition(cameraPosition);
        // 정보 창이 열려있는 경우, 지도를 누르면 닫기
        naverMap.setOnMapClickListener((point, coord) -> {
            for(i = 0; i < infoWindows.length; i++){
                if (infoWindows[i].getMarker() != null) {
                    infoWindows[i].close();
                }
            }

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
