package com.example.ex1.Objects;


import org.json.JSONObject;

public class JsonAndStatus {
    private JSONObject jsonObject;
    private int statusCode;


    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
