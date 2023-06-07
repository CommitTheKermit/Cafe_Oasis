package com.example.ex1.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ex1.Fragments.RatingFragment;
import com.example.ex1.Fragments.RecommendationFragment;
import com.example.ex1.Main.MainActivity;
import com.example.ex1.Objects.DataPage;
import com.example.ex1.Objects.JsonAndStatus;
import com.example.ex1.Objects.UserInfo;
import com.example.ex1.R;
import com.example.ex1.Utils.DownloadImageTask;
import com.example.ex1.Utils.PermissionUtils;
import com.example.ex1.Utils.ServerComm;
import com.example.ex1.ViewPagerAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;
import com.navercorp.nid.NaverIdLoginSDK;
import com.navercorp.nid.oauth.OAuthLoginCallback;
import com.navercorp.nid.profile.data.NidProfile;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private TextView btn_login, btn_sign, btn_find_id, btn_find_pw;
    private EditText login_input_email, login_input_password;
    private CheckBox email_checkbox, keep_checkbox;
    private TextView remember_email, maintain_login;
    View main_image;
    ImageView btn_kakao, btn_naver;
    private static String OAUTH_CLIENT_ID = "8KKe9jwrqNw84LwVTrBY";
    private static String OAUTH_CLIENT_SECRET = "BUf5oFmqqI";
    private static String OAUTH_CLIENT_NAME = "cafe_oasis";
    public static UserInfo userInfo = new UserInfo();
    public static ViewPager2 viewPager2;
    public static ArrayList<DataPage> list = new ArrayList<>();
    Context mContext;
    NaverIdLoginSDK mOAuthLoginInstance;
    String kakaoEmail = "";
    private FusedLocationProviderClient fusedLocationClient;
    double[] doubleArr =  new double[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login = (TextView) findViewById(R.id.btn_login);
        login_input_email = (EditText) findViewById(R.id.login_input_email);
        login_input_password = (EditText) findViewById(R.id.login_input_password);
        btn_kakao = findViewById(R.id.btn_kakao);
        btn_naver = findViewById(R.id.btn_naver);
        btn_sign = findViewById(R.id.btn_sign);
        btn_find_id = findViewById(R.id.btn_find_id);
        btn_find_pw = findViewById(R.id.btn_find_pw);

        email_checkbox = findViewById(R.id.email_checkbox);
        keep_checkbox = findViewById(R.id.keep_checkbox);
        remember_email = findViewById(R.id.remember_email);
        maintain_login = findViewById(R.id.maintain_login);

        email_checkbox.setVisibility(View.GONE);
        keep_checkbox.setVisibility(View.GONE);
        remember_email.setVisibility(View.GONE);
        maintain_login.setVisibility(View.GONE);

        mContext = this;
        main_image = findViewById(R.id.main_image);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }

        JsonObject jsonObject = new JsonObject();
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            doubleArr[0] = location.getLatitude();    // 위도
            doubleArr[1] = location.getLongitude();  // 경도

            Gson gson = new Gson();
            jsonObject.add("user_location", gson.toJsonTree(doubleArr));


            JSONArray jsonArray = ServerComm.getJSONArray(
                    new URL("http://cafeoasis.xyz/cafe/recommend/rating"),
                    jsonObject);
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject json = jsonArray.getJSONObject(i);
                String name = json.get("cafe_name").toString();
                String address = json.get("address").toString();
                String phone_no = json.get("cafe_phone_no").toString();
                double latitude = json.getDouble("latitude");
                double longitude = json.getDouble("longitude");
                String url = json.getString("cafe_image");
                if(!url.startsWith("https://"))
                    url = "https://drive.google.com/open?id=1cHCOfMlA4NiGS8odHDNUI3jL0XXMM994&usp=drive_fs";

                ExecutorService executor = Executors.newSingleThreadExecutor();
                Future<Bitmap> future = executor.submit(
                        new DownloadImageTask(url));

                Bitmap bitmap = future.get();
                Resources res = getResources();
                Drawable drawable = new BitmapDrawable(res, bitmap);

                list.add(new DataPage(drawable,
                        name, address, phone_no, latitude, longitude));
            }


        } catch (ExecutionException |
                 InterruptedException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Join1.class);
                startActivity(intent);
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_input_email.getText().toString();
                String pw = login_input_password.getText().toString();

                // 번역할 텍스트와 목표 언어를 JSON 형식으로 작성
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", email);
                    jsonObject.put("password", pw);

                    JsonAndStatus resultJson = ServerComm.getOutputString(new URL("http://cafeoasis.xyz/users/login"),
                            jsonObject);

                    if(resultJson.getStatusCode() == 200){
                        Toast.makeText(LoginActivity.this, "로그인 성공",
                                Toast.LENGTH_SHORT).show();

                        JSONObject json = resultJson.getJsonObject();
                        JSONObject tempJson = json.getJSONObject("customer");

                        userInfo.setUser_email(tempJson.getString("email"));
                        userInfo.setUser_name(tempJson.getString("name"));
                        userInfo.setUser_type(tempJson.getInt("user_type"));
                        userInfo.setUser_nickname(tempJson.getString("nickname"));
                        userInfo.setUser_age(tempJson.getInt("age"));
                        userInfo.setUser_sex(tempJson.getInt("sex"));

                        int[] tempArr = new int[12];
                        if(json.get("user_keywords") != null){
                            JSONObject keywordJson = json.getJSONObject("user_keywords");
                            tempArr[0] = keywordJson.getInt("beverage");
                            tempArr[1] = keywordJson.getInt("dessert");
                            tempArr[2] = keywordJson.getInt("various_menu");
                            tempArr[3] = keywordJson.getInt("special_menu");
                            tempArr[4] = keywordJson.getInt("large_store");
                            tempArr[5] = keywordJson.getInt("background");
                            tempArr[6] = keywordJson.getInt("talking");
                            tempArr[7] = keywordJson.getInt("concentration");
                            tempArr[8] = keywordJson.getInt("trendy_store");
                            tempArr[9] = keywordJson.getBoolean("gift_packaging") ? 1 : 0;;
                            tempArr[10] = keywordJson.getBoolean("parking") ? 1 : 0;;
                            tempArr[11] = keywordJson.getBoolean("price") ? 1 : 0;;

                        }
                        userInfo.setUser_keyword(tempArr);



                        Intent intent = new Intent(LoginActivity.this, NaviActivity.class);
                        intent.putExtra("userInfo", userInfo);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 다시 확인해보세요", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | MalformedURLException e) {
                    throw new RuntimeException(e);
                }

            }
        });

// 카카오톡이 설치되어 있는지 확인하는 메서드 , 카카오에서 제공함. 콜백 객체를 이용합.
        Function2<OAuthToken,Throwable, Unit> callback =new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            // 콜백 메서드 ,
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                Log.e(TAG,"CallBack Method");
                //oAuthToken != null 이라면 로그인 성공
                if(oAuthToken!=null){
                    Kakaoprofile();
                    String argUrl = "http://cafeoasis.xyz/users/signup?email=" + kakaoEmail;
                    int statusCode = -1;
                    try {
                        statusCode = ServerComm.getStatusCodeGET(new URL(argUrl));
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }

                    Intent intent;
                    if(statusCode == HttpURLConnection.HTTP_OK){
                        // 토큰이 데이터베이스에 존재하여 메인으로 넘어감
                        intent = new Intent(LoginActivity.this, NaviActivity.class);
                        intent.putExtra("option", "notFirstLogin");
                    }
                    else{
                        // 토큰이 데이터베이스에 존재하지 않아 개인정보 등록으로 넘어감

                        intent = new Intent(LoginActivity.this, MyProfile_Modify.class);
                        intent.putExtra("option", "firstLogin");
                        intent.putExtra("email", kakaoEmail);
                    }
                    startActivity(intent);
                    finish();


                }else {
                    //로그인 실패
                    Log.e(TAG, "invoke: login fail" );
                }

                return null;
            }
        };


        btn_kakao.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)){
                         UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, callback);
                         Kakaoprofile();

                     }else{
                         // 카카오톡이 설치되어 있지 않다면
                         UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, callback);
                         Kakaoprofile();
                     }
                 }
             }
        );

        btn_naver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });

        btn_find_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,Find_Id_Activity.class);
                startActivity(intent);
            }
        });

        btn_find_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,Find_pw_Activity.class);
                startActivity(intent);
            }
        });
    }

    private void Kakaoprofile() {

        // 로그인 여부에 따른 UI 설정
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {

                if (user != null) {

                    // 유저의 아이디
                    Log.d(TAG, "invoke: id =" + user.getId());
                    // 유저의 이메일
                    Log.d(TAG, "invoke: email =" + user.getKakaoAccount().getEmail());
                    // 유저의 닉네임
                    Log.d(TAG, "invoke: nickname =" + user.getKakaoAccount().getProfile().getNickname());
                    // 유저의 성별
                    Log.d(TAG, "invoke: gender =" + user.getKakaoAccount().getGender());
                    // 유저의 연령대
                    Log.d(TAG, "invoke: age=" + user.getKakaoAccount().getAgeRange());
                    kakaoEmail = user.getKakaoAccount().getEmail();

                    String email = kakaoEmail;
                    String pw = "oauth_login";

                    // 번역할 텍스트와 목표 언어를 JSON 형식으로 작성
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("email", email);
                        jsonObject.put("password", pw);

                        JsonAndStatus resultJson = ServerComm.getOutputString(new URL("http://cafeoasis.xyz/users/login"),
                                jsonObject);

                        if(resultJson.getStatusCode() == 200){
                            Toast.makeText(LoginActivity.this, "로그인 성공",
                                    Toast.LENGTH_SHORT).show();

                            JSONObject json = resultJson.getJsonObject();
                            JSONObject tempJson = json.getJSONObject("customer");

                            userInfo.setUser_email(tempJson.getString("email"));
                            userInfo.setUser_name(tempJson.getString("name"));
                            userInfo.setUser_type(tempJson.getInt("user_type"));
                            userInfo.setUser_nickname(tempJson.getString("nickname"));
                            userInfo.setUser_age(tempJson.getInt("age"));
                            userInfo.setUser_sex(tempJson.getInt("sex"));

                            int[] tempArr = new int[12];
                            if(json.get("user_keywords") != null){
                                JSONObject keywordJson = json.getJSONObject("user_keywords");
                                tempArr[0] = keywordJson.getInt("beverage");
                                tempArr[1] = keywordJson.getInt("dessert");
                                tempArr[2] = keywordJson.getInt("various_menu");
                                tempArr[3] = keywordJson.getInt("special_menu");
                                tempArr[4] = keywordJson.getInt("large_store");
                                tempArr[5] = keywordJson.getInt("background");
                                tempArr[6] = keywordJson.getInt("talking");
                                tempArr[7] = keywordJson.getInt("concentration");
                                tempArr[8] = keywordJson.getInt("trendy_store");
                                tempArr[9] = keywordJson.getBoolean("gift_packaging") ? 1 : 0;;
                                tempArr[10] = keywordJson.getBoolean("parking") ? 1 : 0;;
                                tempArr[11] = keywordJson.getBoolean("price") ? 1 : 0;;

                            }
                            userInfo.setUser_keyword(tempArr);

                            Intent intent = new Intent(LoginActivity.this, NaviActivity.class);
                            intent.putExtra("userInfo", userInfo);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 다시 확인해보세요", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException | MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
//                    id_1 = String.valueOf(user.getId());
//                    // 유저 닉네임 세팅해주기
//                    nickname.setText(user.getKakaoAccount().getProfile().getNickname());
//                    id.setText(id_1);
//                    email.setText(user.getKakaoAccount().getEmail());
//                    gen.setText(String.valueOf(user.getKakaoAccount().getGender()));
//                    age.setText(String.valueOf(user.getKakaoAccount().getAgeRange()));
//
//                    // 유저 프로필 사진 세팅해주기
//
//                    Log.d(TAG, "invoke: profile = "+user.getKakaoAccount().getProfile().getThumbnailImageUrl());


                } else {
                    // 로그인 되어있지 않으면

                }
                return null;
            }
        });
    }

    private void initData() {
        //초기화
        NaverIdLoginSDK.INSTANCE.initialize(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);
        NaverIdLoginSDK.INSTANCE.authenticate(mContext,mOAuthLoginCallback);
    }


    /**
     * OAuthLoginHandler를 startOAuthLoginActivity() 메서드 호출 시 파라미터로 전달하거나 OAuthLoginButton
     객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다.
     */

    private OAuthLoginCallback mOAuthLoginCallback = new OAuthLoginCallback() {
        @Override
        public void onSuccess() {

            String accessToken = mOAuthLoginInstance.INSTANCE.getAccessToken();
            String naverEmail = "";
            try {
                JsonAndStatus jsonAndStatus = ServerComm.getOutputString(
                        new URL("https://openapi.naver.com/v1/nid/me"),
                        accessToken);
                naverEmail = jsonAndStatus.getJsonObject().getJSONObject("response").getString("email");
            } catch (MalformedURLException | JSONException e) {
                throw new RuntimeException(e);
            }
            //String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
            //long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
            //String tokenType = mOAuthLoginInstance.getTokenType(mContext);
            //mOauthRT.setText(refreshToken);
            //mOauthExpires.setText(String.valueOf(expiresAt));
            //mOauthTokenType.setText(tokenType);
            //mOAuthState.setText(mOAuthLoginInstance.getState(mContext).toString());
//            mOAuthLogin.callProfileApi();


            String argUrl = "http://cafeoasis.xyz/users/signup?email=" + naverEmail;
            int statusCode = -1;
            try {
                statusCode = ServerComm.getStatusCodeGET(new URL(argUrl));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            Intent intent = null;
            if(statusCode == HttpURLConnection.HTTP_OK){
                // 이메일이 데이터베이스에 존재하여 메인으로 넘어감

                String email = naverEmail;
                String pw = "oauth_login";

                // 번역할 텍스트와 목표 언어를 JSON 형식으로 작성
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", email);
                    jsonObject.put("password", pw);

                    JsonAndStatus resultJson = ServerComm.getOutputString(
                            new URL("http://cafeoasis.xyz/users/login"),
                            jsonObject);

                    if(resultJson.getStatusCode() == 200){
                        Toast.makeText(LoginActivity.this, "로그인 성공",
                                Toast.LENGTH_SHORT).show();

                        JSONObject json = resultJson.getJsonObject();
                        JSONObject tempJson = json.getJSONObject("customer");

                        userInfo.setUser_email(tempJson.getString("email"));
                        userInfo.setUser_name(tempJson.getString("name"));
                        userInfo.setUser_type(tempJson.getInt("user_type"));
                        userInfo.setUser_nickname(tempJson.getString("nickname"));
                        userInfo.setUser_age(tempJson.getInt("age"));
                        userInfo.setUser_sex(tempJson.getInt("sex"));

                        int[] tempArr = new int[12];
                        if(json.get("user_keywords") != null){
                            JSONObject keywordJson = json.getJSONObject("user_keywords");
                            tempArr[0] = keywordJson.getInt("beverage");
                            tempArr[1] = keywordJson.getInt("dessert");
                            tempArr[2] = keywordJson.getInt("various_menu");
                            tempArr[3] = keywordJson.getInt("special_menu");
                            tempArr[4] = keywordJson.getInt("large_store");
                            tempArr[5] = keywordJson.getInt("background");
                            tempArr[6] = keywordJson.getInt("talking");
                            tempArr[7] = keywordJson.getInt("concentration");
                            tempArr[8] = keywordJson.getInt("trendy_store");
                            tempArr[9] = keywordJson.getBoolean("gift_packaging") ? 1 : 0;;
                            tempArr[10] = keywordJson.getBoolean("parking") ? 1 : 0;;
                            tempArr[11] = keywordJson.getBoolean("price") ? 1 : 0;;

                        }
                        userInfo.setUser_keyword(tempArr);

                        intent = new Intent(LoginActivity.this, NaviActivity.class);
                        intent.putExtra("option", "notFirstLogin");
                        intent.putExtra("userInfo", userInfo);
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 다시 확인해보세요", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | MalformedURLException e) {
                    throw new RuntimeException(e);
                }

            }
            else{
                // 이메일 데이터베이스에 존재하지 않아 개인정보 등록으로 넘어감

                intent = new Intent(LoginActivity.this, MyProfile_Modify.class);
                intent.putExtra("option", "firstLogin");
                intent.putExtra("email", naverEmail);
            }
            startActivity(intent);
            finish();

//            redirectSignupActivity();
        }

        @Override
        public void onFailure(int i, @NonNull String s) {
            String errorCode = mOAuthLoginInstance.getLastErrorCode().getCode();
            String errorDesc = mOAuthLoginInstance.getLastErrorDescription();
            Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(int i, @NonNull String s) {

        }
    };



}