package com.example.administrator.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.helloworld.fragments.widgets.MainTabbarFragment;

import fragment.pages.FeedListFragment;

/**
 * Created by Administrator on 2016/12/6.
 */

public class HelloActivity extends Activity{
    FeedListFragment contentFeedList = new FeedListFragment();
    MainTabbarFragment tabbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_activity);
        tabbar=(MainTabbarFragment)getFragmentManager().findFragmentById(R.id.frag_tabbar);
        tabbar.setOnTabSelectedListener(new MainTabbarFragment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                changeContentFragment(index);
            }
        });
        tabbar.setOnNewClickedListener(new MainTabbarFragment.OnNewClickedListener() {
            @Override
            public void onNewClicked() {
                bringUpEditor();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tabbar.setSelectedItem(0);
    }

    void changeContentFragment(int index){
        Fragment newFrag = null;

        switch (index) {
            case 0: newFrag = contentFeedList; break;
            //case 1: newFrag = contentNoteList; break;
            //case 2: newFrag = contentSearchPage; break;
            //case 3: newFrag = contentMyProfile; break;

            default:break;
        }

        if(newFrag==null) return;

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content, newFrag)
                .commit();
    }

    void bringUpEditor(){
       // Intent itnt = new Intent(this, NewContentActivity.class);
        //startActivity(itnt);
       //overridePendingTransition(R.anim.slide_in_bottom, R.anim.none);
    }
}
