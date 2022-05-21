package com.example.classtool;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.example.classtool.utils.FilesUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

public class LauncherActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        int firstL = 0;
        sp = PreferenceManager.getDefaultSharedPreferences(LauncherActivity.this);
        editor = sp.edit();


        int f = sp.getInt("firstL", 0);
        if (f == 0) {
            editor.putInt("firstL", 0);
            editor.commit();
            FilesUtil.InitializeFiles(LauncherActivity.this);
        }
        doAfterPermissionsGranted();
    }

    private void doAfterPermissionsGranted() {

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent();
                in.setClass(LauncherActivity.this,Schedule_Activity.class);
                startActivity(in);
                finish();
            }
        },800);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(com.qmuiteam.qmui.R.anim.abc_slide_in_bottom, com.qmuiteam.qmui.R.anim.abc_slide_out_bottom);
    }
}


