package com.example.classtool;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.classtool.base.BasicActivity;
import com.example.classtool.utils.FilesUtil;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.List;


public class LauncherActivity extends BasicActivity {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int firstL = 0;
        sp = PreferenceManager.getDefaultSharedPreferences(LauncherActivity.this);
        editor = sp.edit();


        int f = sp.getInt("firstL", 0);
        if (f == 0) {
            editor.putInt("firstL", 0);
            editor.commit();
            FilesUtil.InitializeFiles(LauncherActivity.this);
        }

        permisions();
    }



        private void permisions(){
            if(Build.VERSION.SDK_INT>=30) {
                PermissionX.init(this)
                        .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                        .onExplainRequestReason(new ExplainReasonCallback() {
                            @Override
                            public void onExplainReason(@NonNull ExplainScope scope, @NonNull List<String> deniedList) {
                                scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必需依赖的权限，用于创建相关文件夹来管理储存课表数据", "我已明白");
                            }
                        })
                        .onForwardToSettings(new ForwardToSettingsCallback() {
                            @Override
                            public void onForwardToSettings(@NonNull ForwardScope scope, @NonNull List<String> deniedList) {
                                scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启存储权限(修改为允许管理所有文件)", "我已明白");
                            }
                        })
                        .request(new RequestCallback() {
                            @Override
                            public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                                if (allGranted) {
                                    doAfterPermissionsGranted();
                                    //Toast.makeText(LauncherActivity.this, "所有申请权限都已通过", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LauncherActivity.this, "您拒绝了以下权限：" + deniedList, Toast.LENGTH_SHORT).show();
                                    //permisions();
                                }
                            }
                        });
            }
            else{
                PermissionX.init(this)
                        .permissions(Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .explainReasonBeforeRequest()
                        .onExplainRequestReason(new ExplainReasonCallback() {
                            @Override
                            public void onExplainReason(@NonNull ExplainScope scope, @NonNull List<String> deniedList) {
                                scope.showRequestReasonDialog(deniedList,"APP需要申请以下权限，以便于提升用户体验。","我知道了","取消");
                            }
                        })
                        .onForwardToSettings(new ForwardToSettingsCallback() {
                            @Override
                            public void onForwardToSettings(@NonNull ForwardScope scope, @NonNull List<String> deniedList) {
                                scope.showForwardToSettingsDialog(deniedList,"您需要去应用程序设置当中手动开启权限,此权限只用于访问项目地址)","我已明白");
                            }
                        })
                        .request(new RequestCallback() {
                            @Override
                            public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                                if(allGranted){
                                    doAfterPermissionsGranted();
                                    //Toast.makeText(LauncherActivity.this, "所有申请权限都已通过", Toast.LENGTH_SHORT).show();
                                }else{
                                   QMUITipDialog ds= new QMUITipDialog.Builder(LauncherActivity.this)
                                            .setTipWord("您拒绝了以下权限："+deniedList)
                                            .create();
                                    ds.show();

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(400);
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            ds.dismiss();
                                        }
                                    }).start();
                                    //Toast.makeText(LauncherActivity.this, "您拒绝了以下权限："+deniedList, Toast.LENGTH_SHORT).show();
                                    doAfterPermissionsGranted();
                                    //permisions();
                                }
                            }
                        });
            }
        }


    private void doAfterPermissionsGranted() {

       // Handler handler=new Handler();
       // handler.postDelayed(new Runnable() {
         //   @Override
           // public void run() {
                Intent in = new Intent();
                in.setClass(LauncherActivity.this,Schedule_Activity.class);
                startActivity(in);
                finish();
         //   }
        //},800);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(com.qmuiteam.qmui.R.anim.abc_slide_in_bottom, com.qmuiteam.qmui.R.anim.abc_slide_out_bottom);
    }
}


