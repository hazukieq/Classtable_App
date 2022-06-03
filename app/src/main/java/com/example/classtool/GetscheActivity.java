package com.example.classtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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

import com.drakeet.multitype.MultiTypeAdapter;
import com.example.classtool.binders.GetscheduBinder;
import com.example.classtool.models.DowncardModel;
import com.example.classtool.models.QTime;
import com.example.classtool.utils.Downloadhelper;
import com.example.classtool.utils.FilesUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetscheActivity extends AppCompatActivity {

    private EditText eidt;
    private TextView down,info;
    private RecyclerView recy;
    private ArrayList<Object> alls=new ArrayList<>();
    private MultiTypeAdapter multiTypeAdapter;
    private boolean isLoad=false;
    private Handler handler;
    private Thread thread1,thread2,thread3;
    int y=0;
    private boolean isCheck=false;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getsche);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        sp= PreferenceManager.getDefaultSharedPreferences(GetscheActivity.this);
        editor=sp.edit();

        initViews();
        initializeHandler();
    }


    private void initializeHandler(){
        handler=new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        isLoad=true;
                        //info.setVisibility(View.VISIBLE);
                        //info.setText("用户帐号验证通过！");
                        //checkInfo(1300);
                        //在这里进行文件下载操作
                        down.setClickable(false);
                        alls.clear();
                        multiTypeAdapter.notifyDataSetChanged();
                        String downloadSchepath=getDir("课表数据",MODE_PRIVATE).getAbsolutePath();
                        String purl="http://43.142.122.229/androiata/getd.html?uid="+eidt.getText().toString();
                        alls.add(new DowncardModel(0,"云同步课表"));
                        multiTypeAdapter.notifyDataSetChanged();

                        getF(purl,downloadSchepath);
                        startSecondTask();

                        break;
                    case 1:
                        DowncardModel downcardModel=(DowncardModel) alls.get(0);
                        downcardModel.setProgress((int) msg.obj);
                        multiTypeAdapter.notifyItemChanged(0,"updating");
                        break;
                    case 3:
                        DowncardModel downcrdModel=(DowncardModel) alls.get(0);
                        downcrdModel.setProgress(100);
                        multiTypeAdapter.notifyItemChanged(0,"updating");
                        info.setVisibility(View.VISIBLE);
                        info.setText("云同步课表文件下载完成！");
                        checkInfo(2000);
                        List<String> tags=FilesUtil.readSchedulAndTimeTag(GetscheActivity.this);
                        List<String> retags=new ArrayList<>();
                        retags.addAll(tags);
                        boolean isApp=false;
                        for(String tag:tags){
                            if(tag.equals("云同步课表,云同步作息时间表")|tag.equals("云同步课表,武鸣校区作息时间")) {
                                isApp=true;
                                retags.remove(tag);
                            }
                        }
                        if(isApp==false){
                            FilesUtil.AppendScheDulAndTimeTag(GetscheActivity.this,"云同步课表,云同步作息时间表");
                        }
                        else if(isApp==true){
                            FilesUtil.RemoveScheDulAndTimeTag(GetscheActivity.this,retags);
                        }

                        break;
                    case 4:
                        DowncardModel f=(DowncardModel)alls.get(1);
                        f.setProgress((int)msg.obj);
                        multiTypeAdapter.notifyItemChanged(1,"updating");
                        break;
                    case 5:
                        DowncardModel h=(DowncardModel) alls.get(1);
                        h.setProgress(100);
                        multiTypeAdapter.notifyItemChanged(1,"updating");
                        info.setVisibility(View.VISIBLE);
                        info.setText("云同步作息时间表文件下载完成！");
                        checkInfo(2000);
                        isCheck=true;
                        editor.putInt("ScheSelected",0);
                        editor.commit();
                        break;
                    case 6:
                        isLoad=false;
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
                        msg.what=7;
                        handler.sendMessage(msg);
                    }
                }).start();

               // thread4.start();
            }

    }

    private void initViews(){
        recy=(RecyclerView) findViewById(R.id.getsche_recy);
        eidt=(EditText)findViewById(R.id.getsche_edit);
        down=(TextView) findViewById(R.id.getsche_down);
        info=(TextView)findViewById(R.id.getsche_info);
        multiTypeAdapter=new MultiTypeAdapter();
        GetscheduBinder binder=new GetscheduBinder();
        multiTypeAdapter.register(DowncardModel.class,binder);


        try {
            int s = 0;
            List<String> tags = FilesUtil.readSchedulAndTimeTag(GetscheActivity.this);
            for (String ta : tags) {
                if (ta.equals("云同步课表,云同步作息时间表")) {
                    s = 1;
                }
            }
            if (s == 1) {
                alls.add(new DowncardModel(100,"云同步课表"));
                alls.add(new DowncardModel(100,"云同步作息时间表"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        recy.setAdapter(multiTypeAdapter);

        multiTypeAdapter.setItems(alls);

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getEdit()){
                    checkAccess(eidt.getText().toString());
                }else{
                    info.setVisibility(View.VISIBLE);
                    info.setText("请输入用户帐号！");
                    checkInfo(1300);
                    //Toast.makeText(GetscheActivity.this, "请输入用户账号！", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean getEdit(){
        if(!eidt.getText().toString().isEmpty()){
            return  true;
        }
        return false;
    }

    private void checkAccess(String url) {
        thread1=new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient okHttpClient=new OkHttpClient();
                String purl="http://43.142.122.229/androiata/getd.html?uid="+url;
                Request request=new Request.Builder()
                        .url(purl)
                        .get()
                        .build();

                Response response= null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    Log.i( "run: ","-->"+response.code());
                    Message msg=new Message();
                    if(response.code()==200){

                        msg.what=0;
                        handler.sendMessage(msg);

                    }else{
                        msg.what=6;
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        thread1.start();
    }


    private void startSecondTask()  {
        String downloadTimepath=getDir("作息时间数据",MODE_PRIVATE).getAbsolutePath();
        String pur="http://43.142.122.229/androiata/gettim.html?uid="+eidt.getText().toString();
        alls.add(new DowncardModel(0,"云同步作息时间表"));
        multiTypeAdapter.notifyDataSetChanged();
        getT(pur,downloadTimepath);
    }


    private void getT(String purl,String downloadSchepath) {
      thread3=new Thread(new Runnable() {

              @Override
              public void run () {
                  try {
                      thread2.join();
                  }catch (Exception e){

                  }

              Message msg = new Message().obtain();
              //Message msg1=new Message();
              Downloadhelper.downloadP(purl, downloadSchepath, "云同步作息时间表.txt", new Downloadhelper.OnDownloadListener() {
                  @Override
                  public void onDownloadSuccess(File file) {
                      msg.what = 5;
                      handler.sendMessage(msg);
                  }

                  @Override
                  public void onDownloadloading(int progress) {


                      msg.obj = progress;
                      msg.what = 4;
                      handler.sendMessage(msg);
                  }

                  @Override
                  public void onDownloadFailed(Exception e) {

                  }
              });

          }

        });
        thread3.start();
        //thread3.join();

    }


    private void getF(String purl,String downloadSchepath)  {
        thread2=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                   // thread1.join();
                }catch (Exception e){

                }
                Message msg=new Message().obtain();
                Downloadhelper.downloadP(purl, downloadSchepath, "云同步课表.txt", new Downloadhelper.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(File file) {
                       msg.what=3;
                       handler.sendMessage(msg);
                    }

                    @Override
                    public void onDownloadloading(int progress) {
                        Log.i( "onDownloadloading: ","-->"+progress);
                        msg.obj=progress;
                        msg.what=1;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onDownloadFailed(Exception e) {

                    }
                });
            }
        });
                thread2.start();
                //thread2.join();
            }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
