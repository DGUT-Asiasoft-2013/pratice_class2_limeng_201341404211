package com.example.administrator.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;

import fragment.SimpleTextInputCellFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login_Activity extends Activity {
    //实例化三个SimpleTextInputCellFragment对象
    SimpleTextInputCellFragment fragInputCellUsername;
    SimpleTextInputCellFragment fragInputCellUserPassword;
    SimpleTextInputCellFragment fragInputCellUserPasswordRepeat;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        fragInputCellUsername = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.user_name);
        fragInputCellUserPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.user_password);


        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goRegister();
            }
        });
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLogin();
            }
        });
        findViewById(R.id.btn_forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                goRecoverPassword();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        fragInputCellUsername.setLabelText("用户名");
        fragInputCellUsername.setHintText("请输入用户名");
        fragInputCellUserPassword.setLabelText("密码");
        fragInputCellUserPassword.setHintText("请输入密码");
        fragInputCellUserPassword.setIsPassword(true);
    }

    void goRegister() {
        Intent itnt = new Intent(this, RegisterActivity.class);
        startActivity(itnt);
    }

    void goLogin() {
        Log.e("hidshfaiohidsfhishf","hudfsihiu");
        //获取组件的输入数据
        String account = fragInputCellUsername.getText();
        String password = fragInputCellUserPassword.getText();
        //用户密码加密
        password = MD5.getMD5(password);
        //创建okHttpClient对象
        OkHttpClient client = new OkHttpClient();
        //创建一个Form数据组
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("account", account)
                .addFormDataPart("passwordHash", password);
        //创建连接数据的url,以及数据
        MultipartBody postBody = bodyBuilder.build();
        //创建一个客户端请求：需要输入请求服务器的url,以及请求的方法
        Request request = new Request.Builder()
                .url("Http://172.27.0.29:8080/membercenter/api/login")
                .method("post", null)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        onFailure(call, e);
                    }
                });

            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {

                //必须处理
                try{
                    final String s = response.body().string();
                    ObjectMapper mapper = new ObjectMapper();
                    final User user = mapper.readValue(s, User.class);
                   //把操作返回主线程之中
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(Login_Activity.this)
                                    .setTitle("登陆成功")
                                    .setMessage("Hello"+user.getName())
                                    .setPositiveButton("好", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            goHelloActivity();
                                        }
                                    }).show();
                        }
                    });
                }catch (final Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Login_Activity.this.onFailure(call,e);
                        }
                    });
                }
            }
        });


    }
/*
    void onResponse(Call call, String responseBody) {
        new AlertDialog.Builder(this)
                .setTitle("登陆成功")
                .setMessage(responseBody)
                .setPositiveButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //finish();
                    }
                }).show();
               goHelloActivity();
    }
    */


    void goHelloActivity() {
        Intent intent = new Intent(this, HelloActivity.class);
        startActivity(intent);
    }

    void goRecoverPassword() {
        Intent itnt = new Intent(this, PasswordRecoverActivity.class);
        startActivity(itnt);
    }

    void onFailure(Call call, final Exception e) {
        new AlertDialog.Builder(this)
                .setTitle("登陆失败")
                .setMessage(e.getLocalizedMessage())
                .setNegativeButton("好", null)
                .show();

    }


}

