package com.example.ex1.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;

public class DownloadImageTask implements Callable<Bitmap> {
    private String imageUrl;

    public DownloadImageTask(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public Bitmap call() throws Exception {
        InputStream in = new URL(imageUrl).openStream();
        return BitmapFactory.decodeStream(in);
    }
}
