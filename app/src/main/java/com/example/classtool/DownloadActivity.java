package com.example.classtool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classtool.base.PopupWindows;
import com.example.classtool.models.QTime;
import com.example.classtool.utils.Downloadhelper;
import com.example.classtool.utils.FilesUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.util.QMUIToastHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DownloadActivity extends AppCompatActivity {
    private TextView upload,uids,choose;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private List<String> fileindexs=new ArrayList<>();
    private List<String> filnames=new ArrayList<>();
    private QMUIPopup popup;
    private String[] news=new String[]{};
    private String[] time_news=new String[]{};
    private int Rcod=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        choose=(TextView) findViewById(R.id.download_choose);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        uids=(TextView) findViewById(R.id.download_random_uid);
        upload=(TextView) findViewById(R.id.download_upload);
        sp= PreferenceManager.getDefaultSharedPreferences(DownloadActivity.this);
        editor=sp.edit();

        int first=sp.getInt("first",0);

        if(first==0){
            FirstInitialize();
            editor.putInt("first",1);
            editor.commit();
        }else if(first>0){
           laterSetuid();
        }
        iniViews();
    }

    private void laterSetuid(){
        String ui=sp.getString("scheuid","20220529");
        uids.setText(ui);
    }

    private void iniViews(){
        fileindexs= FilesUtil.readSchedulAndTimeTag(DownloadActivity.this);
        if(fileindexs.size()>0){
            String[] fileSets=new String[fileindexs.size()];
            String[] timeSets=new String[fileindexs.size()];
            for(int y=0;y<fileindexs.size();y++){
                String[] strSet=fileindexs.get(y).split(",");
                filnames.add(strSet[0]);
                timeSets[y]=strSet[1];
                fileSets[y]=strSet[0];
            }
            news=fileSets.clone();
            time_news=timeSets.clone();
            choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    QMUPopus qpopus=new QMUPopus(DownloadActivity.this,popup);
                    qpopus.showPopup(choose,fileSets,300,120,0,0);
                }
            });

            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!choose.getText().toString().equals("选择文件")){
                        if(QMUIDisplayHelper.hasInternet(DownloadActivity.this)){
                            fileUpload();
                        }else{
                            new QMUITipDialog.Builder(DownloadActivity.this)
                                    .setTipWord("请检查网络是否异常！")
                                    .create().show();

                        }

                    }else{
                        new QMUITipDialog.Builder(DownloadActivity.this)
                                .setTipWord("请选择您需要同步的课表！")
                                .create().show();
                       // Toast.makeText(DownloadActivity.this, "请选择您需要同步的课表！", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            new QMUITipDialog.Builder(DownloadActivity.this)
                    .setTipWord("似乎没有发现任何课表的存在！")
                    .create().show();
            //Toast.makeText(this, "似乎没有发现任何课表的存在！", Toast.LENGTH_SHORT).show();
        }
    }

    private void  FirstInitialize(){
        String uid=getUUID();
        uids.setText(uid);
        editor.putString("scheuid",uid);
        editor.commit();
    }
    private String getUUID(){
        UUID uuid=UUID.randomUUID();
        String str=uuid.toString();
        String uuidStr=str.replace("-","").substring(16);
        return uuidStr;
    }

    private void fileUpload(){
        File firstp=getDir("课表数据",MODE_PRIVATE);
        File firsttimep=getDir("作息时间数据",MODE_PRIVATE);


        File schedup=new File(firstp,choose.getText().toString()+".txt");
        String filepath=schedup.getAbsolutePath();



        List<QTime> clazzes=FilesUtil.readClassTime(DownloadActivity.this,time_news[filnames.indexOf(choose.getText().toString())]);
        List<String> timetags=FilesUtil.readTimeTag(DownloadActivity.this);
        String tgs="";
        for(String tag:timetags){
            if(tag.split(",")[0].equals(time_news[filnames.indexOf(choose.getText().toString())])){
                tgs=tag;
            }
        }

        List<QTime> lassses=new ArrayList<>();
        lassses.add(new QTime(-1,tgs.replace("武鸣校区作息时间","云同步作息时间表"),""));
        lassses.addAll(clazzes);

        FilesUtil.AppendBackupTimeTags(DownloadActivity.this,lassses);

        File timep=new File(firsttimep,"云备份索引.txt");
        String timepath=timep.getAbsolutePath();

        String uid=sp.getString("scheuid","20220529");
        String uploadurl="http://43.142.122.229/androiata/upload?uid="+uid;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                  int Rcode= Downloadhelper.uploadSchdfile(DownloadActivity.this,uid,choose.getText().toString()+".txt",filepath,choose.getText().toString()+"_time.txt",timepath,uploadurl);
                   Rcod=Rcode;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        if(Rcod==200|Rcod==0){
            new QMUITipDialog.Builder(DownloadActivity.this)
                    .setTipWord("成功上传文件到远端服务器！")
                    .create().show();

            //Toast.makeText(this, "成功上传文件到远端服务器！", Toast.LENGTH_SHORT).show();
        }else{
            new QMUITipDialog.Builder(DownloadActivity.this)
                    .setTipWord("上传失败，网络失败代码为"+Rcod+",请及时与技术人员反馈")
                    .create().show();
            //Toast.makeText(this, "上传失败，网络失败代码为"+Rcod+",请及时与技术人员反馈", Toast.LENGTH_LONG).show();
        }
    }


    public  class  QMUPopus extends PopupWindows{

        public QMUPopus(Context context, QMUIPopup popup) {
            super(context, popup);
        }

        @Override
        public void Clickpopup(int i) {
            super.Clickpopup(i);
            choose.setText(news[i]);
        }
    }
}