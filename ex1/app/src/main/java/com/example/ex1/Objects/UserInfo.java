package com.example.ex1.Objects;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private String user_id;
    private String user_email;
    private String user_name;
    private String user_type;

    public String getUser_id() {
        return user_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
