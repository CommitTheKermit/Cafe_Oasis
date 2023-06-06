package com.example.ex1.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.splashscreen.SplashScreen;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ex1.Main.MainActivity;
import com.example.ex1.Objects.JsonAndStatus;
import com.example.ex1.Objects.UserInfo;
import com.example.ex1.PreferenceManager;
import com.example.ex1.R;
import com.example.ex1.Utils.PermissionUtils;
import com.example.ex1.Utils.ServerComm;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;
import com.navercorp.nid.NaverIdLoginSDK;
import com.navercorp.nid.oauth.OAuthLoginCallback;
import com.navercorp.nid.profile.data.NidProfile;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private TextView btn_login, btn_sign, btn_find_id, btn_find_pw;
    private EditText login_input_email, login_input_password;
    View main_image;
    ImageView btn_kakao, btn_naver;
    CheckBox email_checkbox;
    private static String OAUTH_CLIENT_ID = "8KKe9jwrqNw84LwVTrBY";
    private static String OAUTH_CLIENT_SECRET = "BUf5oFmqqI";
    private static String OAUTH_CLIENT_NAME = "cafe_oasis";
    public static UserInfo userInfo = new UserInfo();
    Context mContext;
    NaverIdLoginSDK mOAuthLoginInstance;
    String kakaoEmail = "";
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
        mContext = this;
        main_image = findViewById(R.id.main_image);
        email_checkbox = findViewById(R.id.email_checkbox);

        boolean boo = PreferenceManager.getBoolean(mContext,"check"); //로그인 정보 기억하기 체크 유무 확인
        if(boo){ // 체크가 되어있다면 아래 코드를 수행
            //저장된 아이디와 암호를 가져와 셋팅한다.
            login_input_email.setText(PreferenceManager.getString(mContext, "id"));
            email_checkbox.setChecked(true); //체크박스는 여전히 체크 표시 하도록 셋팅
        }

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestLocationPermissions((AppCompatActivity) LoginActivity.this, 1, true);

            return;
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

                PreferenceManager.setString(mContext, "id", login_input_email.getText().toString()); //id라는 키값으로 저장


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

                        JSONObject tempJson = resultJson.getJsonObject();


                        userInfo.setUser_email(tempJson.getString("email"));
                        userInfo.setUser_name(tempJson.getString("name"));
                        userInfo.setUser_type(tempJson.getInt("user_type"));
                        userInfo.setUser_nickname(tempJson.getString("nickname"));
                        userInfo.setUser_age(tempJson.getInt("age"));
                        userInfo.setUser_sex(tempJson.getInt("sex"));

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


        //로그인 기억하기 체크박스 유무에 따른 동작 구현
        email_checkbox.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) { // 체크박스 체크 되어 있으면
                    //editText에서 아이디와 암호 가져와 PreferenceManager에 저장한다.
                    PreferenceManager.setString(mContext, "id", login_input_email.getText().toString()); //id 키값으로 저장
                    PreferenceManager.setBoolean(mContext, "check", email_checkbox.isChecked()); //현재 체크박스 상태 값 저장
                } else { //체크박스가 해제되어있으면
                    PreferenceManager.setBoolean(mContext, "check", email_checkbox.isChecked()); //현재 체크박스 상태 값 저장
                    PreferenceManager.clear(mContext); //로그인 정보를 모두 날림
                }
            }
        }) ;


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
                finish();
            }
        });

        btn_find_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,Find_pw_Activity.class);
                startActivity(intent);
                finish();
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




    // 성공 후 이동할 액티비티
//    protected void redirectSignupActivity() {
//        final Intent intent = new Intent(this, NaviActivity.class);
//        startActivity(intent);
//        finish();
//    }




}