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
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.File;
import java.util.List;

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
            FilesUtil.createAllFileDir();
            editor.putInt("firstL", 0);
            editor.commit();
        }
        permisions();

    }

    private void permisions(){
        PermissionX.init(this)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                .onExplainRequestReason(new ExplainReasonCallback() {
                    @Override
                    public void onExplainReason(@NonNull ExplainScope scope, @NonNull List<String> deniedList) {
                        scope.showRequestReasonDialog(deniedList,"即将申请的权限是程序必需依赖的权限，用于创建相关文件夹来管理储存课表数据","我已明白");
                    }
                })
                .onForwardToSettings(new ForwardToSettingsCallback() {
                    @Override
                    public void onForwardToSettings(@NonNull ForwardScope scope, @NonNull List<String> deniedList) {
                        scope.showForwardToSettingsDialog(deniedList,"您需要去应用程序设置当中手动开启存储权限(修改为允许管理所有文件)","我已明白");
                    }
                })
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                        if(allGranted){
                            doAfterPermissionsGranted();
                            //Toast.makeText(LauncherActivity.this, "所有申请权限都已通过", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LauncherActivity.this, "您拒绝了以下权限："+deniedList, Toast.LENGTH_SHORT).show();
                            //permisions();
                        }
                    }
                });
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


