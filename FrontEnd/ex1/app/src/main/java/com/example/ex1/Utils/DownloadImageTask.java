package com.example.ex1.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class DownloadImageTask implements Callable<Bitmap> {
    private String imageUrl;

    public DownloadImageTask(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public Bitmap call() throws Exception {
        InputStream in;
        try{
            in = new URL(imageUrl).openStream();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return BitmapFactory.decodeStream(in);
    }
}

//package com.example.ex1.Utils;
//
//        import android.graphics.Bitmap;
//        import android.graphics.BitmapFactory;
//
//        import java.io.InputStream;
//        import java.net.HttpURLConnection;
//        import java.net.URL;
//        import java.util.concurrent.Callable;
//
//public class DownloadImageTask implements Callable<Bitmap> {
//    private String imageUrl;
//
//    public DownloadImageTask(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }
//
//    @Override
//    public Bitmap call() throws Exception {
//        URL url = new URL(imageUrl);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setConnectTimeout(5000); // Set the timeout
//        connection.setReadTimeout(5000); // Set the timeout
//        connection.setDoInput(true);
//        connection.connect();
//        int responseCode = connection.getResponseCode();
//
//        // Check if the response is successful
//        if (responseCode != HttpURLConnection.HTTP_OK) {
//            throw new Exception("Failed to connect");
//        }
//
//        try (InputStream in = connection.getInputStream()) {
//            return BitmapFactory.decodeStream(in);
//        } finally {
//            connection.disconnect(); // Disconnect the connection
//        }
//    }
//}
