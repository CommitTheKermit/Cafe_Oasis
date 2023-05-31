package com.example.ex1.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
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
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;


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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE); //2
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);  //현재 위치
        //naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        naverMap.setLayerGroupEnabled(naverMap.LAYER_GROUP_BUILDING, true);

        ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE); //권한확인


        /*CameraPosition cameraPosition = new CameraPosition(
                new LatLng(35.83822810000016, 128.75294189999966),  // 위치 지정
                15                           // 줌 레벨
        );
        naverMap.setCameraPosition(cameraPosition);*/

        //마커
        setMarker(marker1, 35.84049650463925, 128.7003034345919, R.drawable.ic_baseline_place_24, "트로스트");

        marker1.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay)
            {
                ViewGroup rootView = (ViewGroup) mapView.findViewById(R.id.fragment_container);
                pointAdapter adapter = new pointAdapter(requireActivity(), rootView);

                infoWindow1.setAdapter(adapter);
                infoWindow1.setZIndex(10); //인포창의 우선순위
                infoWindow1.setAlpha(0.9f); //투명도 조정
                infoWindow1.open(marker1); //인포창 표시
                return false;
                /* Toast.makeText(getActivity().getApplicationContext(), "마커1 클릭", Toast.LENGTH_SHORT).show();
                return false;*/
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