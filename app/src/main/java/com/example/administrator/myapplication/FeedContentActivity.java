package com.example.administrator.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/12/8.
 */

public class FeedContentActivity extends Activity{
    Article article;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_content);
        article=(Article)getIntent().getSerializableExtra("content") ;
        TextView textView=(TextView)findViewById(R.id.tv_content);
        textView.setText(article.getText().toString());

    }
}
