package com.example.administrator.myapplication;

import android.app.Fragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/12/3.
 */

public  class Version_fragment1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view =inflater.inflate(R.layout.fragment_version,null);
        TextView textVersion=(TextView)view.findViewById(R.id.text);
        PackageManager pkgm=this.getActivity().getPackageManager();
        try
        {
            PackageInfo appinf=pkgm.getPackageInfo(getActivity().getPackageName(),0);
            textVersion.setText(appinf.packageName+""+appinf.versionName);
        }catch(PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();;
            textVersion.setText("ERROR");

        }
        return view;

    }
}
