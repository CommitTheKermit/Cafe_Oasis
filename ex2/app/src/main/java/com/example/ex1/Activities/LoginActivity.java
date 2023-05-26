package com.example.ex1.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ex1.Main.MainActivity;
import com.example.ex1.R;
import com.example.ex1.Utils.PermissionUtils;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;
import com.navercorp.nid.NaverIdLoginSDK;
import com.navercorp.nid.oauth.OAuthLoginCallback;

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
    private TextView btn_login;
    private EditText login_input_email, login_input_password;
    View main_image;
    ImageView btn_kakao, btn_naver;
    private static String OAUTH_CLIENT_ID = "8KKe9jwrqNw84LwVTrBY";
    private static String OAUTH_CLIENT_SECRET = "BUf5oFmqqI";
    private static String OAUTH_CLIENT_NAME = "cafe_oasis";
    Context mContext;
    NaverIdLoginSDK mOAuthLoginInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login = (TextView) findViewById(R.id.btn_login);
        login_input_email = (EditText) findViewById(R.id.login_input_email);
        login_input_password = (EditText) findViewById(R.id.login_input_password);
        btn_kakao = findViewById(R.id.btn_kakao);
        btn_naver = findViewById(R.id.btn_naver);

        mContext = this;
        main_image = findViewById(R.id.main_image);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestLocationPermissions((AppCompatActivity) LoginActivity.this, 1, true);

            return;
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_input_email.getText().toString();
                String pw = login_input_password.getText().toString();


                new Thread(){
                    @Override
                    public void run() {
                        try{
                            // API 요청을 보내기 위한 URL 생성
                            URL url = new URL("http://52.79.247.229:8000/pics_users/login");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            int status = conn.getResponseCode();
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Type", "application/json");


                            // 번역할 텍스트와 목표 언어를 JSON 형식으로 작성
                            JSONObject data = new JSONObject();
                            data.put("user_id", email);
                            data.put("user_pw", pw);

                            conn.setDoOutput(true);
                            conn.getOutputStream().write(data.toString().getBytes());

                            String output = null;
                            int status = conn.getResponseCode();
                            if(status == HttpURLConnection.HTTP_OK){
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 다시 확인해보세요", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            // 연결 종료
                            conn.disconnect();
                        }
                        catch (JSONException e)
                        {
                            throw new RuntimeException(e);
                        } catch (ProtocolException e) {
                            throw new RuntimeException(e);
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }.start();

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
                    // 토큰이 전달된다면 로그인이 성공한 것이고 토큰이 전달되지 않으면 로그인 실패한다.
                    Intent intent = new Intent(LoginActivity.this,NaviActivity.class);
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

                     }else{
                         // 카카오톡이 설치되어 있지 않다면
                         UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, callback);
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

    }

//    private void Kakaoprofile() {
//
//        // 로그인 여부에 따른 UI 설정
//        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
//            @Override
//            public Unit invoke(User user, Throwable throwable) {
//
//                if (user != null) {
//
//                    // 유저의 아이디
//                    Log.d(TAG, "invoke: id =" + user.getId());
//                    // 유저의 이메일
//                    Log.d(TAG, "invoke: email =" + user.getKakaoAccount().getEmail());
//                    // 유저의 닉네임
//                    Log.d(TAG, "invoke: nickname =" + user.getKakaoAccount().getProfile().getNickname());
//                    // 유저의 성별
//                    Log.d(TAG, "invoke: gender =" + user.getKakaoAccount().getGender());
//                    // 유저의 연령대
//                    Log.d(TAG, "invoke: age=" + user.getKakaoAccount().getAgeRange());
//
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
//
//
//                } else {
//                    // 로그인 되어있지 않으면
//
//                }
//                return null;
//            }
//        });
//    }

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

            //String accessToken = mOAuthLoginInstance.getAccessToken();
            //String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
            //long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
            //String tokenType = mOAuthLoginInstance.getTokenType(mContext);
            //mOauthRT.setText(refreshToken);
            //mOauthExpires.setText(String.valueOf(expiresAt));
            //mOauthTokenType.setText(tokenType);
            //mOAuthState.setText(mOAuthLoginInstance.getState(mContext).toString());
//            mOAuthLogin.callProfileApi();
            redirectSignupActivity();
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
    protected void redirectSignupActivity() {
        final Intent intent = new Intent(this, NaviActivity.class);
        startActivity(intent);
        finish();
    }

}