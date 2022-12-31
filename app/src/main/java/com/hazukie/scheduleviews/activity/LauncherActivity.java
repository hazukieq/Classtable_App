package com.hazukie.scheduleviews.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.hazukie.jbridge.lib.IJBridgeUtil;
import com.hazukie.jbridge.lib.JBridgeInvokeDispatcher;
import com.hazukie.jbridge.lib.JBridgeObject;

import com.hazukie.scheduleviews.utils.DisplayHelper;
import com.hazukie.scheduleviews.utils.SpvalueStorage;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;

import java.util.ArrayList;
import java.util.List;


public class LauncherActivity extends AppCompatActivity {
    //private SpvalueStorage sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //测试独立WebView链接器
        IJBridgeUtil.init();
        //Fileystem fileystem=Fileystem.getInstance(getApplicationContext());
        //ClassLabel cls=new ClassLabel(10,0,1,2,"neosubject","A1-200","six weeks",0);
        //fileystem.putData(FileRootTypes.sches,"neo_sche.txt",cls);
        //ScheWithTimeModel.autoIncrementID(getApplicationContext(),"neo_sche", "默认作息表");

/*        FileHelper fileHelper=new FileHelper(this);
        List<Timetable> s=DataInitiation.initialTimeDefaults();
        TimeHeadModel thm=new TimeHeadModel("默认作息表.txt",12,0,5,5,4,9,3,s);

        List<ScheWithTimeModel> scts=FileHelper.getRecordedScts(this);
        //List<TimeModel> tms=FileHelper.getRecordTms(this);

        scts.get(0).updateTimeName("默认作息表.txt");
        //tms.get(0).timeName="武鸣校.txt";

        try{
            fileHelper.write(FileHelper.RootMode.times,"默认作息表.txt", thm,false);
            fileHelper.write(FileHelper.RootMode.index,"index.txt",new ArrayList<>(scts),false);
           // fileHelper.write(FileHelper.RootMode.times,"time_index.txt",new ArrayList<>(tms),false);
        }catch (Exception e){
            e.printStackTrace();
        }*/
        doAfterPermissionsGranted();
        //applyPermissions();
    }

    private void applyPermissions(){
        PermissionX.init(this)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplainRequestReason((scope, deniedList) -> {
                    scope.showRequestReasonDialog(deniedList,"当前应用需要您同意以下授权才能正常使用","同意","拒绝");
                })
                .request((allGranted, grantedList, deniedList) -> {
                    if(allGranted){
                        DisplayHelper.Infost(this,"您同意了所有权限!");
                        doAfterPermissionsGranted();
                    }else{
                        DisplayHelper.Infost(this,"您拒绝了以下权限："+deniedList);
                        finish();
                    }
                });
    }
    private void doAfterPermissionsGranted() {
        Intent in = new Intent();
        in.setClass(LauncherActivity.this,MainActivity.class);
        startActivity(in);
        finish();
    }
}


