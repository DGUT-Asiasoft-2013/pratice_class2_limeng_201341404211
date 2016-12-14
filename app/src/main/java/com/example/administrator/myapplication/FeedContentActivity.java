package com.example.administrator.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import fragment.pages.ShowReviewList;

/**
 * Created by Administrator on 2016/12/8.
 */

public class FeedContentActivity extends Activity{
    Article article;
    ShowReviewList reviewList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_content);
        article=(Article)getIntent().getSerializableExtra("content") ;

        reviewList = (ShowReviewList) getFragmentManager().findFragmentById(R.id.frag_reviews);
        reviewList.setArticle(article);

        TextView textView=(TextView)findViewById(R.id.tv_content);
        textView.setText(article.getText().toString());
        findViewById(R.id.add_review).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goAddRiew();
            }
        });

    }
    void goAddRiew()
    {
        Intent intent=new Intent(this,AddRivew.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("content",article);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
