package com.example.ex1;

public class DataPage {

    int image;
    String cafe_name, cafe_location, cafe_keyword;

    public DataPage(int image, String cafe_name, String cafe_location, String cafe_keyword){
        this.image = image;
        this.cafe_name = cafe_name;
        this.cafe_location = cafe_location;
        this.cafe_keyword = cafe_keyword;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
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

    public String getCafe_keyword() {
        return cafe_keyword;
    }

    public void setCafe_keyword(String cafe_keyword) {
        this.cafe_keyword = cafe_keyword;
    }




}
