package com.example.classtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.drakeet.multitype.MultiTypeAdapter;
import com.example.classtool.binders.GetscheduBinder;
import com.example.classtool.models.DowncardModel;
import com.example.classtool.models.DownloadBean;
import com.example.classtool.models.QTime;
import com.example.classtool.utils.DownloadManager;
import com.example.classtool.utils.Downloadhelper;
import com.example.classtool.utils.FilesUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetscheActivity extends AppCompatActivity {

    private EditText eidt;
    private TextView down, info;
    private RecyclerView recy;
    private ArrayList<Object> alls = new ArrayList<>();
    private MultiTypeAdapter multiTypeAdapter;
    private boolean isLoad = false;
    private Handler handler;
    private Thread thread1, thread2, thread3;
    int y = 0;
    private boolean isCheck = false;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String url_path="http://43.142.122.229/";//"http://10.17.59.128:8080/";
    private String sche_path,time_path;
    private File sch,time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getsche);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        sp = PreferenceManager.getDefaultSharedPreferences(GetscheActivity.this);
        editor = sp.edit();
        try {
            sche_path=Downloadhelper.generateFilepath(GetscheActivity.this,"课表数据","云同步课表.txt");
            time_path=Downloadhelper.generateFilepath(GetscheActivity.this,"作息时间数据","云同步作息时间表.txt");
            sch=new File(sche_path);
            time=new File(time_path);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            initViews();
        } catch (IOException e) {
            e.printStackTrace();
        }


        initializeHandler();
    }


    private void initializeHandler() {
        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                    switch (msg.what) {
                        case 0:
                            try{
                            isLoad = true;
                            info.setVisibility(View.VISIBLE);
                            info.setText("用户帐号验证通过！");
                            checkInfo(1300);
                            //在这里进行文件下载
                                alls.clear();
                                String[] ses=(String[]) msg.obj;
                            alls.add(new DownloadBean("云同步课表", -1, url_path+"androiata/getd.html?uid="+eidt.getText().toString(), sche_path, (int) sch.length(),Integer.parseInt(ses[0])));
                            alls.add(new DownloadBean("云同步作息时间表",-1,url_path+"androiata/gettm.html?uid="+eidt.getText().toString(),time_path, (int) time.length(),Integer.parseInt(ses[1])));
                            multiTypeAdapter.notifyDataSetChanged();
                            editor.putInt("ScheSelected",0);
                            editor.commit();
                            //alls.add(new DownloadBean("10022", -1, "http://m.shouji.360tpcdn.com/160315/168f6b5f7e38b95f8d7dcce94076acc4/com.longtugame.jymf.qihoo_22.apk", apk_pa, (int) eles.length(), 252821785));
                            down.setClickable(false);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            break;
                        case 6:
                            isLoad = false;
                            info.setVisibility(View.VISIBLE);
                            info.setText("您输入的用户账号有误，请检查后再试！");
                            checkInfo(1600);
                            break;
                        case 7:
                            info.setVisibility(View.GONE);
                            down.setClickable(true);
                            break;
                    }

            }
        };
    }


    private void checkInfo(int tim) {
        if (info.getVisibility() == View.VISIBLE) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(tim);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = new Message();
                    msg.what = 7;
                    handler.sendMessage(msg);
                }
            }).start();

            // thread4.start();
        }

    }

    private void initViews() throws IOException {
        eidt = (EditText) findViewById(R.id.getsche_edit);
        down = (TextView) findViewById(R.id.getsche_down);
        info = (TextView) findViewById(R.id.getsche_info);
        recy=(RecyclerView)findViewById(R.id.getsche_recy);
        multiTypeAdapter=new MultiTypeAdapter();
        multiTypeAdapter.register(DownloadBean.class,new GetscheduBinder());
        recy.setAdapter(multiTypeAdapter);



       // alls.add(new DownloadBean("2003", -1, "http://m.shouji.360tpcdn.com/160318/a043152dd8789131a12c5beeb7e42e34/com.huajiao_4071059.apk", apk_paz, (int) elez.length(), 17699443));
        //alls.add(new DownloadBean("10022", -1, "http://m.shouji.360tpcdn.com/160315/168f6b5f7e38b95f8d7dcce94076acc4/com.longtugame.jymf.qihoo_22.apk", apk_pa, (int) eles.length(), 252821785));
        multiTypeAdapter.setItems(alls);


        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getEdit()) {
                    if (QMUIDisplayHelper.hasInternet(GetscheActivity.this)) {

                    } else {
                        info.setVisibility(View.VISIBLE);
                        info.setText("网络是异常,请稍后重试！");
                        checkInfo(2000);
                    }
                    checkAccess(eidt.getText().toString());
                } else {
                    info.setVisibility(View.VISIBLE);
                    info.setText("请输入用户帐号！");
                    checkInfo(1300);
                    //Toast.makeText(GetscheActivity.this, "请输入用户账号！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean getEdit() {
        if (!eidt.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }

    private void checkAccess(String url) {
        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient okHttpClient = new OkHttpClient();
                String purl = "http://43.142.122.229/androiata/check.html?uid=" + url;
                Request request = new Request.Builder()
                        .url(purl)
                        .get()
                        .build();

                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    Log.i("run: ", "-->" + response.code());
                    Message msg = new Message();
                    String[] se=response.body().string().split(",");
                    Log.i("run: ",""+se[1]);
                    if (response.code() == 200||response.code()==206) {
                        try {
                            msg.obj=se;
                            msg.what = 0;
                            handler.sendMessage(msg);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    } else {
                        msg.what = 6;
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        thread1.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DownloadManager.getInstance().RemoveObserver();
        DownloadManager.getInstance().Destory();

        if(isCheck){
            List<QTime> datags=FilesUtil.readClassTime(GetscheActivity.this,"云同步作息时间表");
            List<QTime> atas=new ArrayList<>();
            String timgas=FilesUtil.readBackupClassTimetag(GetscheActivity.this,"云同步作息时间表");
            Log.i( "onDestroy: ","time_tag-->"+timgas);
            for(int i=1;i<datags.size();i++) atas.add(datags.get(i));
            FilesUtil.AppendClassTime(GetscheActivity.this,atas,"云同步作息时间表");

            List<String> atags=FilesUtil.readTimeTag(GetscheActivity.this);
            List<String> katags=new ArrayList<>();
            katags.addAll(atags);
            for(String tag:atags){
                if(tag.split(",")[0].equals("云同步作息时间表")){
                    katags.remove(tag);
                }
            }
            List<String> hatags=new ArrayList<>();
            hatags.addAll(katags);
            hatags.add(timgas);
            FilesUtil.AppendTimeTags(GetscheActivity.this,hatags);

        }
    }
}
