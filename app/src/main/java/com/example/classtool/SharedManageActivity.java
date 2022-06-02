package com.example.classtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SharedManageActivity extends AppCompatActivity {
    private EditText input;
    private TextView confirm,info;
    private QMUITopBarLayout top;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Handler handl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_manage);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        input=(EditText) findViewById(R.id.shared_edit);
        confirm=(TextView)findViewById(R.id.shared_confirm);
        top=(QMUITopBarLayout)findViewById(R.id.shared_topba);
        info=(TextView) findViewById(R.id.shared_info);
        sp= PreferenceManager.getDefaultSharedPreferences(SharedManageActivity.this);
        editor=sp.edit();

        top.setTitle("云同步验证界面");
        getPasswd();

        initViews();
        handler();
    }

    private void initViews(){
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }


    private void handler(){
        handl=new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        info.setVisibility(View.GONE);
                }
            }
        };
    }
    private void check(){
        String pwd=sp.getString("pws","20220529");

        if(input.getText().toString().equals(pwd)){
            Intent inw=new Intent();
            inw.setClass(SharedManageActivity.this,DownloadActivity.class);
            startActivity(inw);
        }else {
            /*new QMUITipDialog.Builder(SharedManageActivity.this)
                    .setTipWord("验证码错误，请重试！")
                    .create().show();*/
            info.setVisibility(View.VISIBLE);
            info.setText("验证码错误，请重新输入！");
            checkInfo(2000);
        }
    }

    private void checkInfo(int tim){
        if(info.getVisibility()==View.VISIBLE) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        Thread.sleep(tim);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg=new Message();
                    msg.what=1;
                    handl.sendMessage(msg);
                }
            }).start();

            // thread4.start();
        }

    }

    private void getPasswd(){
        String url="https://www.hazukieq.top/otherres/a.txt";

        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call=okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                editor.putString("pws",response.body().string());
                editor.commit();
            }
        });
    }
}