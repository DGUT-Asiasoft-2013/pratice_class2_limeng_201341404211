package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Button bt1=(Button)findViewById(R.id.bt1);
        //bt1.setOnClickListener(new View.OnClickListener() {
        //    @Override
        ///  public void onClick(View view) {
        //      Intent intent=new Intent(MainActivity.this,fragmentFragment.class);
        //     startActivity(intent);
        //   }
        //  }

        //);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Handler handler=new Handler();
        //handler.postDelayed(new Runnable() {
        //  private  int abcd=0;
        // @Override
        //public void run() {

        //startLoginActivity();

        // }
        //},3000);


        //新建一个请求
        OkHttpClient client = new OkHttpClient();
        //请求数据的服务器的url：Http://172.27.0.29:8080/membercenter/api/hello,还有请求的方法：GET
        //然后用build创建
        Request request = new Request.Builder()
                .url("Http://172.27.0.29:8080/membercenter/api/hello")
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //toast输出必须是在主线程中的，所以用runOnUiThread
                //利用Activity.runOnUiThread(Runnable)把更新ui的代码创建在Runnable中，然后在需要更新ui时，把这个Runnable对象传给Activity.runOnUiThread(Runnable)。
                //Runnable对像就能在ui程序中被调用。如果当前线程是UI线程，那么行动是立即执行。如果当前线程不是UI线程,操作是发布到事件队列的UI线程。
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //要用string()而不用 toString()是因为服务器返回的数据可能是string也可能是bytes[]
                            Toast.makeText(MainActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        startLoginActivity();
                    }
                });

            }
        });
    }

    void startLoginActivity() {
        Intent itnt = new Intent(this, Login_Activity.class);
        startActivity(itnt);
        finish();
    }
}
