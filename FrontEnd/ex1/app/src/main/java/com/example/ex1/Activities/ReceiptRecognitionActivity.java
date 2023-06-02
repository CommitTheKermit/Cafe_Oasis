package com.example.ex1.Activities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.Manifest;

//import com.example.ex1.Manifest;
import com.example.ex1.Objects.JsonAndStatus;
import com.example.ex1.R;
import com.example.ex1.Utils.ServerComm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class ReceiptRecognitionActivity extends AppCompatActivity {


    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_OPEN_GALLERY = 2;
    String currentPhotoPath;
    String base64Image;
    View receip_arrow1;
    String secretKey = "VXhlTkNvWFpzUGRDUkplUURxS2FoUXF4eXVVb05qakQ=";
    TextView btn_photo_shoot, btn_picure_load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receip_recognition);

        receip_arrow1 = findViewById(R.id.receip_arrow1);
        btn_photo_shoot = findViewById(R.id.btn_photo_shoot);
        btn_picure_load = findViewById(R.id.btn_picture_load);


        receip_arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_photo_shoot.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ReceiptRecognitionActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 1000);
                }
                dispatchTakePictureIntent();

            }
        });

        btn_picure_load.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ReceiptRecognitionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1002);
                }


                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_OPEN_GALLERY); //OPEN_GALLERY는 임의의 requestCode (int)
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.ex1",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            setPic();
        }
        else if (requestCode == REQUEST_OPEN_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);

            if (cursor == null || cursor.getCount() < 1) {
                return; // no cursor or no record. DO YOUR ERROR HANDLING
            }

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            if (columnIndex < 0) // no column index
                return; // DO YOUR ERROR HANDLING

            //선택한 파일 경로
            String picturePath = cursor.getString(columnIndex);
            base64Image = imageToBase64(picturePath);
            cursor.close();

            JSONObject apiInput = new JSONObject();
            try {
                apiInput.put("format", "jpg");
                apiInput.put("name", "recipt");
                apiInput.put("data", base64Image);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("images", apiInput);
                jsonObject.put("version", "V2");
                jsonObject.put("requestid", UUID.randomUUID());
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                jsonObject.put("timestamp", timeStamp);
                jsonObject.put("lang", "ko");

                JsonAndStatus outputJson = ServerComm.getOutputString(new URL("https://5yuqotffnj.apigw.ntruss.com/custom/v1/22921/5e7254c8d8fd2d1b52d8094dc099a1492233b6fe7e4222641b1586917df8e915/general"), jsonObject);
                JSONObject outputString = outputJson.getJsonObject();

                System.out.println("asd");
            } catch (JSONException | MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String imageToBase64(String imagePath) {
        String base64Image = "";
        try {
            File file = new File(imagePath);
            FileInputStream imageInputStream = new FileInputStream(file);
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = imageInputStream.read(buffer)) != -1) {
                byteOutputStream.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = byteOutputStream.toByteArray();
            base64Image = Base64.getEncoder().encodeToString(imageBytes);

            imageInputStream.close();
            byteOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64Image;
    }


//    private void setPic() {
//        ImageView imageView = (ImageView) findViewById(R.id.imageView);
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
//        imageView.setImageBitmap(bitmap);
//    }
}