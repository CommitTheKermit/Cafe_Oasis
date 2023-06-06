package com.example.ex1.Objects;

import android.graphics.drawable.Drawable;

public class DataPage {

//    int image;
    Drawable image;
    String cafe_name, cafe_location, phone_no;
    double latitude, longitude;
    public DataPage(Drawable image, String cafe_name, String cafe_location, String phone_no,
                    double latitude, double longitude){
        this.image = image;
        this.cafe_name = cafe_name;
        this.cafe_location = cafe_location;
        this.phone_no = phone_no;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getCafe_name() {
        return cafe_name;
    }

    public void setCafe_name(String cafe_name) {
        this.cafe_name = cafe_name;
    }

    public String getCafe_location() {
        return cafe_location;
    }

    public void setCafe_location(String cafe_location) {
        this.cafe_location = cafe_location;
    }


    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
