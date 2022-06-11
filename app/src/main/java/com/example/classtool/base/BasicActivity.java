package com.example.classtool.base;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;

import com.hjq.toast.ToastUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;


public class BasicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        initFonts();
        ToastUtils.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);


    }



    private void initFonts(){
        Configuration configuration=getResources().getConfiguration();
        configuration.fontScale=(float) 1;
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity=configuration.fontScale*metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration,metrics);
        //configuration.setToDefaults();
    }

}