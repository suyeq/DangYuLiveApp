package com.example.suyeq.dangyuliveapp.loginregister;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.suyeq.dangyuliveapp.AppApplication;
import com.example.suyeq.dangyuliveapp.MainActivity;
import com.example.suyeq.dangyuliveapp.R;
import com.example.suyeq.dangyuliveapp.editprofile.EditProFileActivity;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveLoginManager;


public class LoginActivity extends AppCompatActivity {

    private EditText mAccountEdt;
    private EditText mPasswordEdt;
    private Button mLoginBtn;
    private Button mRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findAllViews();
        setListeners();
    }

    private void findAllViews() {
        mAccountEdt = (EditText) findViewById(R.id.account);
        mPasswordEdt = (EditText) findViewById(R.id.password);
        mLoginBtn = (Button) findViewById(R.id.login);
        mRegisterBtn = (Button) findViewById(R.id.register);
    }

    private void setListeners() {
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//登录操作
                login();
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//注册的操作
                register();
            }
        });
    }

    private void register() {
        //注册新用户，跳转到注册页面。
        Intent intent = new Intent();
        intent.setClass(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void login() {
        final String accountStr = mAccountEdt.getText().toString();
        String passwordStr = mPasswordEdt.getText().toString();
        Log.e("qwe","name="+accountStr+"password="+passwordStr);
        if(TextUtils.isEmpty(accountStr) ||
                TextUtils.isEmpty(passwordStr) ){
            Toast.makeText(this, "用户名或密码为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        loginActurally(accountStr,passwordStr);


    }

    private void loginActurally(final String accountStr,String passwordStr) {
        ILiveLoginManager.getInstance().tlsLogin(accountStr, passwordStr, new ILiveCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                //登陆成功。
                loginLive(accountStr, data);
                //Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                //登录失败
                Toast.makeText(LoginActivity.this,  errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loginLive(String accountStr, String data) {
        ILiveLoginManager.getInstance().iLiveLogin(accountStr, data, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                //最终登录成功
                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                //判断第几次登陆
              //  SharedPreferences sp=getSharedPreferences("FirstLogin",MODE_PRIVATE);
              //  boolean isfirstlogin=sp.getBoolean("firstlogin",true);
             //   if(isfirstlogin){
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
              //  }else{
               //     Intent intent = new Intent();
               //     intent.setClass(LoginActivity.this, MainActivity.class);
              //      startActivity(intent);
             //   }
                getSelfInfo();

                //finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                //登录失败
                Toast.makeText(LoginActivity.this, "iLive登录失败：" + errMsg, Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void getSelfInfo() {
        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int i, String s) {
                Toast.makeText(LoginActivity.this, "获取信息失败：" + s, Toast.LENGTH_SHORT).show();
                Log.e("cuowu",s);
            }

            @Override
            public void onSuccess(TIMUserProfile timUserProfile) {
                //获取自己信息成功
                Toast.makeText(LoginActivity.this, "获取信息成功", Toast.LENGTH_SHORT).show();
                AppApplication.getApplication().setSelfProfile(timUserProfile);
            }
        });
    }
}





