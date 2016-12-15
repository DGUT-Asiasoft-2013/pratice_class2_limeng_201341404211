package com.example.administrator.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.api.Server;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import fragment.pages.ShowReviewList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/8.
 */

public class FeedContentActivity extends Activity {
    boolean isLike;
    Article article;
    ShowReviewList reviewList;
    Button btnLikes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_content);
        article = (Article) getIntent().getSerializableExtra("content");

        reviewList = (ShowReviewList) getFragmentManager().findFragmentById(R.id.frag_reviews);
        reviewList.setArticle(article);

        TextView textView = (TextView) findViewById(R.id.tv_content);
        textView.setText(article.getText().toString());
        btnLikes = (Button) findViewById(R.id.add_review_like);
        btnLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goAddRiew();
            }
        });
        findViewById(R.id.add_review_like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goLike();
            }
        });


    }

    void goAddRiew() {
        Intent intent = new Intent(this, AddRivew.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("content", article);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    void goLike() {
        OkHttpClient client = Server.getSharedClient();
        MultipartBody body = new MultipartBody.Builder()
                .addFormDataPart("likes", String.valueOf(!isLike))
                // .addFormDataPart("article_id", String.valueOf(article.getId()))
                //
                .build();
        Request request = Server.requestBuilderWithApi("article/" + article.getId() + "/likes")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reload();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    final String responseBody = response.body().string();
                    Log.d("responseBody",responseBody);
                }catch(Exception e){
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reload();
                    }
                });

            }
        });
    }

    void onCheckLikedResult(boolean result) {
        isLike = result;
        btnLikes.setTextColor(result ? Color.BLUE : Color.BLACK);
    }

    void reloadLikes() {
        Request request = Server.requestBuilderWithApi("article/" + article.getId() + "/likes")
                .get().build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onReloadLikesResult(0);
                    }
                });


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    String responseString = response.body().string();
                    final Integer count = new ObjectMapper().readValue(responseString, Integer.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onReloadLikesResult(count);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onReloadLikesResult(0);
                        }
                    });

                }
            }
        });
    }

    void onReloadLikesResult(int count) {
        if (count > 0) {
            btnLikes.setText("赞(" + count + ")");
        } else {
            btnLikes.setText("赞");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        reload();
    }


    void reload() {
        reloadLikes();
        checkLiked();

    }
    void checkLiked()
    {
        Request request=Server.requestBuilderWithApi("article/"+article.getId()+"/isliked").get().build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try
                {
                    final String responseString=response.body().string();
                    final Boolean result=new ObjectMapper().readValue(responseString,Boolean.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onCheckLikedResult(result);
                        }
                    });
                }catch (final Exception e)
                {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onCheckLikedResult(false);
                        }
                    });
                }
            }
        });

    }
}
