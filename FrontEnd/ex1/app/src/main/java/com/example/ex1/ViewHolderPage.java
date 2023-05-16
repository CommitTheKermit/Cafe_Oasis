package com.example.ex1;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderPage extends RecyclerView.ViewHolder{

    private ImageView viewpager_image;
    private TextView viewpager_cafe_name, viewpager_cafe_location, viewpager_cafe_keyword;


    DataPage data;

    ViewHolderPage(View itemView) {
        super(itemView);

        viewpager_image = itemView.findViewById(R.id.viewpager_image);
        viewpager_cafe_name = itemView.findViewById(R.id.viewpager_cafe_name);
        viewpager_cafe_location = itemView.findViewById(R.id.viewpager_cafe_location);
        viewpager_cafe_keyword = itemView.findViewById(R.id.viewpager_cafe_keyword);

    }

    public void onBind(DataPage data){
        this.data = data;

        viewpager_image.setImageResource(data.getImage());
        viewpager_cafe_name.setText(data.getCafe_name());
        viewpager_cafe_location.setText(data.getCafe_location());
        viewpager_cafe_keyword.setText(data.getCafe_keyword());

    }

}
