package com.hazukie.scheduleviews.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hazukie.scheduleviews.fileutil.FileRootTypes;
import com.hazukie.scheduleviews.fileutil.Fileystem;
import com.hazukie.scheduleviews.iJBridges.IJBridgeUtil;
import com.hazukie.scheduleviews.models.ClassLabel;
import com.hazukie.scheduleviews.models.OldClassLabel;
import com.hazukie.scheduleviews.utils.DisplayHelper;
import com.hazukie.scheduleviews.utils.SpvalueStorage;
import com.permissionx.guolindev.PermissionX;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class LauncherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateSchesFmt();
        applyPermissions();
        //测试独立WebView链接器
        IJBridgeUtil.init();
    }


    private void applyPermissions(){
        List<String> permiss=new ArrayList<>();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            permiss.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        else{
            permiss.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permiss.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

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

    //处理重大更新后的兼容问题
    private void updateSchesFmt() {
        int oldversionCode=SpvalueStorage.getInstance(this).getInt("isUpdatedOn",1);
        int versionCode = 0;
        try{versionCode=getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;}
        catch (PackageManager.NameNotFoundException e){e.printStackTrace();}

        if(oldversionCode<versionCode){
            String[] all_sches=this.getDir(FileRootTypes.sches.name(), Context.MODE_PRIVATE).list();
            Fileystem filemgr=Fileystem.getInstance(this);
            if(all_sches != null&&all_sches.length>0){
                for(String f :all_sches){
                    OldClassLabel old=filemgr.getData(FileRootTypes.sches,f, OldClassLabel.class);
                    filemgr.putData(FileRootTypes.sches,f,ClassLabel.updateOld2Neo(old));
                }
                SpvalueStorage.getInstance(this).setIntValue("isUpdatedOn",versionCode);
            }
        }
    }
}


