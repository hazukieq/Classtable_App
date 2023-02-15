package com.hazukie.scheduleviews.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.hazukie.scheduleviews.iJBridges.IJBridgeUtil;
import com.hazukie.scheduleviews.utils.DisplayHelper;
import com.permissionx.guolindev.PermissionX;

import java.util.ArrayList;
import java.util.List;


public class LauncherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyPermissions();
        //测试独立WebView链接器
        IJBridgeUtil.init();
    }


    private void applyPermissions(){

        List<String> permiss=new ArrayList<>();
        permiss.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permiss.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            permiss.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE);

        PermissionX.init(this)
                .permissions(permiss)
                .onExplainRequestReason((scope, deniedList) -> scope.showRequestReasonDialog(deniedList,"当前应用需要您同意以下授权才能正常使用","同意","拒绝"))
                .request((allGranted, grantedList, deniedList) -> {
                    if(allGranted){
                        //DisplayHelper.Infost(this,"您同意了所有权限!");
                        doAfterPermissionsGranted();
                    }else{
                        DisplayHelper.Infost(this,"您拒绝了以下权限："+deniedList);
                        finish();
                    }
                });
    }

    private void doAfterPermissionsGranted() {
        Intent in = new Intent();
        in.setClass(this,MainActivity.class);
        startActivity(in);
        finish();
    }
}


