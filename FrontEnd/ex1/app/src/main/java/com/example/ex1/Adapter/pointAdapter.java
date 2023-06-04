package com.example.ex1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;

import com.example.ex1.R;
import com.naver.maps.map.overlay.InfoWindow;

public class pointAdapter extends InfoWindow.DefaultViewAdapter  {
    private final Context mContext;
    private final ViewGroup mParent;

    public pointAdapter(@NonNull Context context, ViewGroup parent)
    {
        super(context);
        mContext = context;
        mParent = parent;
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

        txtTitle.setText("트로스트");
        imagePoint.setImageResource(R.drawable.trost);
        txtAddr.setText("대구 수성구 달구벌대로641길 4-8 2층");
        txtTel.setText("0507-1304-4936");

        Button callbutton = (Button) view.findViewById(R.id.callbutton);
        callbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:0507-1304-4936"));
                mContext.startActivity(mIntent);
            }
        });
        return view;
    }
}
