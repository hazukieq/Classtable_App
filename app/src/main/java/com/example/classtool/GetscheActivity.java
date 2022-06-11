package com.example.classtool;

import androidx.annotation.NonNull;
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
import android.widget.TextView;

import com.drakeet.multitype.MultiTypeAdapter;
import com.example.classtool.base.BasicActivity;
import com.example.classtool.binders.GetscheduBinder;
import com.example.classtool.models.DownloadBean;
import com.example.classtool.models.QTime;
import com.example.classtool.models.Static_sets;
import com.example.classtool.utils.DownloadManager;
import com.example.classtool.utils.Downloadhelper;
import com.example.classtool.utils.FilesUtil;
import com.example.classtool.utils.FindSort;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetscheActivity extends BasicActivity {

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
    private GetscheduBinder binder;
    private File sch,time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getsche);
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
                               // alls.clear();
                                String[] ses=(String[]) msg.obj;


                                File fs=new File(Environment.getExternalStorageDirectory(),"课表助手");
                                if(!fs.exists()) fs.mkdir();
                                File fd=new File(fs,"时间.txt");
                                fd.createNewFile();
                            alls.add(new DownloadBean("云同步课表", -1, url_path+"androiata/getd.html?uid="+eidt.getText().toString(), sche_path, 0,Integer.parseInt(ses[1])));
                            alls.add(new DownloadBean("云同步作息时间表",-1,url_path+"androiata/gettm.html?uid="+eidt.getText().toString(),fd.getAbsolutePath(), 0,Integer.parseInt(ses[2])));
                            multiTypeAdapter.notifyDataSetChanged();
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
                        case 8:
                            isLoad = false;
                            info.setVisibility(View.VISIBLE);
                            info.setText("哎呀~服务端出现故障了...");
                            checkInfo(1600);
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
        binder=new GetscheduBinder();
        binder.setOnSetClicks(new GetscheduBinder.onSetClicks() {
            @Override
            public void onUpdateProgress(int postition, int value) {

            }

            @Override
            public void onDelete(int position) {
                   alls.remove(position);
                   multiTypeAdapter.notifyItemRemoved(position);
            }
        });
        multiTypeAdapter.register(DownloadBean.class,binder);
        recy.setAdapter(multiTypeAdapter);



        try{
            List<DownloadBean> dalls=FilesUtil.readDownload_datas(GetscheActivity.this);
            if(dalls.size()>0){
                alls.addAll(dalls);//.setProgress();)
                multiTypeAdapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

       // alls.add(new DownloadBean("2003", -1, "http://m.shouji.360tpcdn.com/160318/a043152dd8789131a12c5beeb7e42e34/com.huajiao_4071059.apk", apk_paz, (int) elez.length(), 17699443));
        //alls.add(new DownloadBean("10022", -1, "http://m.shouji.360tpcdn.com/160315/168f6b5f7e38b95f8d7dcce94076acc4/com.longtugame.jymf.qihoo_22.apk", apk_pa, (int) eles.length(), 252821785));
        multiTypeAdapter.setItems(alls);


        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getEdit()) {
                    if (QMUIDisplayHelper.hasInternet(GetscheActivity.this)) {
                        try {
                            alls.clear();
                            checkAccess(eidt.getText().toString());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    } else {
                        info.setVisibility(View.VISIBLE);
                        info.setText("网络异常,请稍后重试！");
                        checkInfo(2000);
                    }

                } else {
                    info.setVisibility(View.VISIBLE);
                    info.setText("请输入用户帐号！");
                    checkInfo(1300);
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
        new Thread(new Runnable() {
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
                    String s=response.body().string();
                    if(s.startsWith("#all,")) {
                            Log.i( "run: ",""+s);
                            String[] se = s.split(",");
                            msg.obj = se;
                        }
                    if (response.code() == 200||response.code()==206) {
                            msg.what = 0;
                    } else {
                        msg.what = 6;
                    }
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg=new Message();
                    msg.what=8;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }


    private void checkData(String name) {

        try {
            //List<String> copq=FilesUtil.readBackupClassTimetag();
            List<String> schtag = FilesUtil.readSchedulAndTimeTag(GetscheActivity.this);
            List<String> chtag = new ArrayList<>();

            chtag.addAll(schtag);

                //添加课表索引
                for (String str : schtag) {
                    if (str.equals("云同步课表,云同步作息时间表") | str.equals("云同步课表,武鸣校区作息时间")) {
                        chtag.remove(str);
                    }
                }
                chtag.add("云同步课表,"+name);
                FilesUtil.RemoveScheDulAndTimeTag(GetscheActivity.this, chtag);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void checkTimetag(){

            List<String> copq = FilesUtil.readBackupClassTimetag();
            List<QTime> ds = new ArrayList<>();
            for (int i = 1; i < copq.size(); i++) {
                String[] strs = copq.get(i).split(",");
                QTime q = new QTime(FindSort.returnColorSort(Static_sets.detail_real_numStrs, strs[0]), strs[1], strs[2].replace("<br/>", "-"));
                ds.add(q);
            }
            FilesUtil.AppendClassTime(GetscheActivity.this, ds, "云同步作息时间表");


            String timgas = copq.get(0).replace("-1,", "");
            List<String> atags = FilesUtil.readTimeTag(GetscheActivity.this);
            List<QTime> atas = new ArrayList<>();

            //添加时间索引标记
            List<String> katags = new ArrayList<>();
            katags.addAll(atags);
            for (String tag : atags) {
                if (tag.split(",")[0].equals("云同步作息时间表")) {
                    katags.remove(tag);
                }
            }
            List<String> hatags = new ArrayList<>();
            hatags.addAll(katags);
            hatags.add(timgas);
            FilesUtil.AppendTimeTags(GetscheActivity.this, hatags);
        }


    @Override
    protected void onDestroy() {
        super.onDestroy();


        try {
            List<DownloadBean> qalls = new ArrayList<>();
            if(alls.size()>0) {
                switch (alls.size()) {
                    case 0:
                        FilesUtil.writeDownload_datas(GetscheActivity.this, qalls, false);
                        break;
                    case 1:
                        DownloadBean bean=(DownloadBean) alls.get(0);
                        if(bean.name.equals("云同步作息时间表")&&bean.download_state==4){
                            checkTimetag();
                        }
                        if(bean.name.equals("云同步课表")&&bean.download_state==4){
                                checkData("武鸣校区作息时间");
                        }
                        break;
                    case 2:
                        for (int i = 0; i < alls.size(); i++) {
                            DownloadBean b = (DownloadBean) alls.get(i);
                            qalls.add(b);
                        }

                        FilesUtil.writeDownload_datas(GetscheActivity.this, qalls, false);

                        DownloadBean sc = (DownloadBean) alls.get(0);
                        DownloadBean ti = (DownloadBean) alls.get(1);

                        checkTimetag();
                        if (sc.download_state == 4 && ti.download_state == 4) {
                            checkData("云同步作息时间表");
                        }
                        if (sc.download_state == 4 && ti.download_state != 4) {
                            checkData("武鸣校区作息时间");
                        }
                        break;
                    default:
                        break;
                }
            }else{
                FilesUtil.writeDownload_datas(GetscheActivity.this, qalls, false);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }




        editor.putInt("ScheSelected",0);
        editor.commit();
        DownloadManager.getInstance().RemoveObserver();
        DownloadManager.getInstance().Destory();
        // if(SisDown==1&&TisDown==1) isCheck=true;


    }
}
