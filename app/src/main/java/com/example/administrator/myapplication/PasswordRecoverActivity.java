package com.example.administrator.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.api.Server;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import fragment.PasswordRecoverStep1Fragment;
import fragment.PasswordRecoverStep2Fragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/6.
 */

public class PasswordRecoverActivity extends Activity {
    PasswordRecoverStep1Fragment step1 = new PasswordRecoverStep1Fragment();
    PasswordRecoverStep2Fragment step2 = new PasswordRecoverStep2Fragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recover);


        step1.setOnGoNextListener(new PasswordRecoverStep1Fragment.OnGoNextListener() {
            @Override
            public void onGoNext() {
                goStep2();
            }
        });

        step2.setOngosubmitListener(new PasswordRecoverStep2Fragment.OngosubmitListener() {
            public void submit() {
                goSubmit();
            }
        });

        getFragmentManager().beginTransaction().replace(R.id.container, step1).commit();
    }

    void goStep2() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, step2)
                .addToBackStack(null)
                .commit();
    }

    void goSubmit() {
        OkHttpClient client = Server.getSharedClient();
        MultipartBody body = new MultipartBody.Builder()
                .addFormDataPart("email", step1.getText())
                .addFormDataPart("passwordHash", MD5.getMD5(step2.getText1())).build();
        Request request = Server.requestBuilderWithApi("forgot_password")
                .method("post", null)
                .post(body)
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
            public void onResponse(final Call call, Response response) throws IOException {
                try {
                    final Boolean succed = new ObjectMapper().readValue(response.body().bytes(), Boolean.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (succed) {
                                new AlertDialog.Builder(PasswordRecoverActivity.this)
                                        .setMessage("修改成功")
                                        .show();


                            }
                        }
                    });

                } catch (final Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //要放回主线程中执行
                            PasswordRecoverActivity.this.onFailure(call,e);
                        }
                    });

                }


            }
        });

    }
    void onFailure(final Call call, final Exception e) {
        new AlertDialog.Builder(this)
                .setTitle("修改失败")
                .setMessage(e.getLocalizedMessage())
                .setNegativeButton("好", null)
                .show();

    }


}


