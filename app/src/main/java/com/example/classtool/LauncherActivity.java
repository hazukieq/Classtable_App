package com.example.classtool;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.classtool.utils.FilesUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.io.File;

public class LauncherActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 10;
    private static final String[] PERMISSIONS_REQUIRED = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_launcher);

        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        int firstL=0;
        sp= PreferenceManager.getDefaultSharedPreferences(LauncherActivity.this);
        editor=sp.edit();


        int f=sp.getInt("firstL",0);
        if(f==0){
            FilesUtil.createAllFileDir();
            editor.putInt("firstL",0);
            editor.commit();
        }

        if (allPermissionsGranted()) {
            doAfterPermissionsGranted();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if(allPermissionsGranted()){
            if(Build.VERSION.SDK_INT<Build.VERSION_CODES.R||Environment.isExternalStorageManager()){
                doAfterPermissionsGranted();

            }else {
                new AlertDialog.Builder(this)
                        .setTitle("申请管理读写权限")
                        .setCancelable(true)
                        .setMessage("本程序需要您同意允许访问所有文件权限")
                        .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(LauncherActivity.this, "请用户授予关于存储权限，该权限用于存储课表数据到您手机本地文件。", Toast.LENGTH_SHORT).show();
                                Intent iq = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                iq.setData(Uri.parse("package:" + LauncherActivity.this.getPackageName()));
                                startActivity(iq);
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(LauncherActivity.this, "您已取消相关授权", Toast.LENGTH_SHORT).show();
                            finish();
                            }
                        }).show();
               // finish();

            }
            }else{
                new AlertDialog.Builder(this)
                        .setTitle("申请管理读写权限")
                        .setCancelable(true)
                        .setMessage("本程序需要您同意允许访问所有文件权限")
                        .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                allPermissionsGranted();
                               // Toast.makeText(LauncherActivity.this, "请授予读写权限及访问所有文件权限", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(LauncherActivity.this, "您已取消相关授权", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).show();

                //finish();
            }
        }
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

    private boolean allPermissionsGranted() {
        for (String permission : PERMISSIONS_REQUIRED) {
            if (ContextCompat.checkSelfPermission(getBaseContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
       // Log.i( "allPermissionsGranted: ",String.valueOf(Environment.isExternalStorageManager()));
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(com.qmuiteam.qmui.R.anim.abc_slide_in_bottom, com.qmuiteam.qmui.R.anim.abc_slide_out_bottom);
    }


}


