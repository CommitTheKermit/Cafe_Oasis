package com.example.ex1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ex1.Objects.DataPage;
import com.example.ex1.R;
import com.naver.maps.map.overlay.InfoWindow;

public class pointAdapter extends InfoWindow.DefaultViewAdapter {
    private final Context mContext;
    private final ViewGroup mParent;
    private DataPage cafeData;

    public pointAdapter(@NonNull Context context, ViewGroup parent, DataPage data)
    {
        super(context);
        mContext = context;
        mParent = parent;
        this.cafeData = data;

    }

    @NonNull
    @Override
    protected View getContentView(@NonNull InfoWindow infoWindow)
    {

        View view = (View) LayoutInflater.from(mContext).inflate(R.layout.item_point, mParent, false);

        TextView txtTitle = (TextView) view.findViewById(R.id.txttitle);
        ImageView imagePoint = (ImageView) view.findViewById(R.id.imagepoint);
        TextView txtAddr = (TextView) view.findViewById(R.id.txtaddr);
        TextView txtTel = (TextView) view.findViewById(R.id.txttel);

        txtTitle.setText(cafeData.getCafe_name());
        imagePoint.setImageDrawable(cafeData.getImage());
        txtAddr.setText(cafeData.getCafe_location());
        txtTel.setText(cafeData.getPhone_no());

        return view;
    }
}
