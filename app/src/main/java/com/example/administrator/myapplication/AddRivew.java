package com.example.administrator.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.example.api.Server;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/14.
 */

public class AddRivew extends Activity{
    EditText editView;
    Article article;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        editView=(EditText)findViewById(R.id.text_review) ;
        article=(Article)getIntent().getSerializableExtra("content") ;
        findViewById(R.id.btn_send_review).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReview();
            }
        });
    }
    void sendReview()
    {
        String text=editView.getText().toString();
        MultipartBody body=new MultipartBody.Builder()
                .addFormDataPart("text",text)
                .build();
        Request request= Server.requestBuilderWithApi("article/"+article.getId()+"/reviews/")
                .post(body)
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final  IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AddRivew.this.onFailure(e);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AddRivew.this.onSucceed(responseBody);
                    }
                });

            }
        });
    }
    void onSucceed(String text)
    {
        new AlertDialog.Builder(this).setMessage(text)
                .setPositiveButton("oK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }
    void onFailure(Exception e)
    {
        new AlertDialog.Builder(this).setMessage(e.getMessage()).show();
    }
}
