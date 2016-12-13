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
 * Created by Administrator on 2016/12/12.
 */

public class NewContentActivity extends Activity{
    EditText editTitle,editText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_content);
        editTitle=(EditText)findViewById(R.id.title);
        editText=(EditText)findViewById(R.id.text);
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendContent();
            }
        });


    }
    void sendContent() {
        String text = editText.getText().toString();
        String title = editTitle.getText().toString();
        if (text != null && title != null) {
            MultipartBody body = new MultipartBody.Builder()
                    .addFormDataPart("title", title)
                    .addFormDataPart("text", text)
                    .build();
            Request request = Server.requestBuilderWithApi("article")
                    .post(body)
                    .build();
            Server.getSharedClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            NewContentActivity.this.onFailure(e);
                        }
                    });

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseBody = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            NewContentActivity.this.onSucceed(responseBody);
                        }
                    });

                }
            });

        }
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






