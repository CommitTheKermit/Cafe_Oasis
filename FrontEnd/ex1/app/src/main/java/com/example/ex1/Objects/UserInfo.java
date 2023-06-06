package com.example.ex1.Objects;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private int user_id = -1;
    private String user_email = "default";
    private String user_pw = "default";
    private String user_name = "default";
    private int user_type = -1;
    private String user_nickname = "default";
    private int user_age = -1;
    private int user_sex = -1;
    private String user_phone = "default";
    private int[] user_keyword = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};

    public int getUser_id() {
        return user_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_pw() {
        return user_pw;
    }

    public String getUser_name() {
        return user_name;
    }

    public int getUser_type() {
        return user_type;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public int getUser_age() {
        return user_age;
    }

    public int getUser_sex() {
        return user_sex;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public int[] getUser_keyword() {
        return user_keyword;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public void setUser_pw(String user_pw) {
        this.user_pw = user_pw;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public void setUser_age(int user_age) {
        this.user_age = user_age;
    }

    public void setUser_sex(int user_sex) {
        this.user_sex = user_sex;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public void setUser_keyword(int[] user_keyword) {
        this.user_keyword = user_keyword;
    }
}
