package com.example.administrator.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import fragment.PasswordRecoverStep1Fragment;
import fragment.PasswordRecoverStep2Fragment;

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
        getFragmentManager().beginTransaction().replace(R.id.container, step1).commit();

    }

    void goStep2() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, step2)
                .addToBackStack(null)
                .commit();
    }

}


