package com.hazukie.scheduleviews.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hazukie.cskheui.DisplayHelper;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.base.BaseActivity;
import com.hazukie.scheduleviews.fileutil.BasicOpts;
import com.hazukie.scheduleviews.fileutil.FileRootTypes;
import com.hazukie.scheduleviews.fileutil.OftenOpts;
import com.hazukie.scheduleviews.models.ScheWithTimeModel;
import com.hazukie.scheduleviews.statics.Statics;
import com.hazukie.scheduleviews.utils.CycleUtil;
import com.hazukie.scheduleviews.utils.SpvalueStorage;
import com.hazukie.scheduleviews.utils.StatusHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RgetActivity extends BaseActivity {
    private static String user;
    private LinearLayout root,files_lay;
    private ScrollView scroll;

    private ProgressBar progressBar;
    private TextView hint;

    private EditText edit;
    private final Handler handler=new Handler(message ->{
        String str=message.getData().getString("req");
        assert str != null;
        if(str.equals("&&file")){
            hint.setText("下载成功！");
            unzipAll("sche.zip",getDir("sches",MODE_PRIVATE));
            hint.setText("解压成功！");
            progressBar.setProgress(90);
            updateIndex();
            hint.setText("课表索引更新成功！");
            progressBar.setProgress(100);
        }else{
            if(str.startsWith("failed")) hint.setText("错误："+str);
            else{
                String uid=edit.getText().toString();
                if(uid.isEmpty()||uid.isBlank()) uid=user;
                SpvalueStorage.getInstance(this).setStringvalue("curid",uid);
                hint.setText("通过 用户码<"+uid+"> 获取远程课表文件成功...");
                String[] files=str.split(",");
                for(String f:files){
                    files_lay.addView(createCheckBox(f));
                }
            }
        }
        return true;
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rget);
        StatusHelper.controlStatusLightOrDark(this, StatusHelper.Mode.Status_Dark_Text);
        user= SpvalueStorage.getInstance(this).getStringValue("genid","123456");
        String curid=SpvalueStorage.getInstance(this).getStringValue("curid",user);
        //user="123456";

        edit=findViewById(R.id.rget_edit);
        edit.setText(curid);

        root=findViewById(R.id.rget_root);
        scroll=findViewById(R.id.rget_scroll);
        files_lay=findViewById(R.id.rget_all_files);
        hint=findViewById(R.id.rget_download_hint);
        progressBar=findViewById(R.id.rget_download_status);

        root.post(() -> scroll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,root.getHeight()/4)));

        Button down_btn = findViewById(R.id.rget_download_btn);
        Button get_btn=findViewById(R.id.rget_get_btn);
        get_btn.setOnClickListener(v->{
            files_lay.removeAllViews();
            new Thread(() ->{
                String uid=edit.getText().toString();
                if(uid.isEmpty()||uid.isBlank()) uid=user;
                getAll("", uid);
            }).start();
        });

        down_btn.setOnClickListener(v->{
            int len=files_lay.getChildCount();
            StringBuilder selecteds= new StringBuilder();
            int record=0;
            for(int i=0;i<len;i++){
                CheckBox f=(CheckBox) files_lay.getChildAt(i);
                if(f.isChecked()){
                    selecteds.append(f.getText().toString()).append(",");
                    record++;
                }
            }
            if(record>=1){
                String all_list=selecteds.toString();
                all_list=all_list.substring(0,all_list.lastIndexOf(','));
                if(selecteds.length()>0) {
                    String finalAll_list = all_list;
                    new Thread(()-> {
                        String uid=edit.getText().toString();
                        if(uid.isEmpty()||uid.isBlank()) uid=user;
                        getAll(finalAll_list, uid);
                    }).start();
                }
            }else hint.setText("请选择文件！");
        });
    }

    private CheckBox createCheckBox(String name){
        CheckBox box=new CheckBox(this);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(DisplayHelper.dp2px(this,240), ViewGroup.LayoutParams.WRAP_CONTENT);
        box.setLayoutParams(params);
        box.setTextColor(getColor(com.hazukie.cskheui.R.color.__iosblue));
        box.setText(name);
        box.setTextSize(15);
        return box;
    }

    private void getAll(String all,String user){
        OkHttpClient client = new OkHttpClient();
        // 构建请求
        String list=(all==null||all.length()==0)?"":"&list="+all;
        String type=list.length()>0?"others":"list";
        Request request = new Request.Builder()
                .get()
                .url(Statics.share_sche_url+"?type="+type+"&user="+user+list)
                .build();

        // 发送请求并处理响应
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                if(type.equals("list")){
                    String ret=response.body().string();
                    Log.i("","请求成功，响应内容为："+ret);

                    response.body().close();
                    Message msg=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("req",ret);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }else{
                    File tempfile=new File(getDir("zips",MODE_PRIVATE),"sche.zip");
                    if(tempfile.exists()) tempfile.delete();
                    tempfile.createNewFile();
                    InputStream is=response.body().byteStream();
                    FileOutputStream out=new FileOutputStream(tempfile);
                    byte[] bytes = new byte[1024];
                    int len;
                    while ((len = is.read(bytes)) != -1) {
                        out.write(bytes,0,len);
                        Log.i( "getAll: ","byte="+bytes.length);
                    }

                    out.flush();
                    out.close();
                    is.close();
                    Log.i("","getAll=>"+response.message());
                    Message msg=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("req","&&file");
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            } else {
                Log.i("","请求失败，错误信息为：" + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(response!=null) response.body().close();
        }
    }

    private boolean unzipAll(String name,File dir){
        if(name==null ||name.length()==0||dir==null) return false;
        File root=getDir("zips",MODE_PRIVATE);
        File zipf=new File(root,name);
        if(!zipf.exists()) return false;
        if(!dir.exists()) dir.delete();
        dir.mkdir();

        ZipInputStream zin;
        try{
            zin=new ZipInputStream(new FileInputStream(zipf));
            ZipEntry entry;
            while ((entry=zin.getNextEntry())!=null){
                if(!entry.isDirectory()) {
                    File f=new File(dir,entry.getName());//.substring(entry.getName().lastIndexOf('/')));
                    try{
                        Log.i("","unzipAll: "+entry.getName());
                        if(!f.exists()) f.createNewFile();
                        byte[] buffer=new byte[1024];
                        FileOutputStream fout=new FileOutputStream(f);
                        int len;
                        while((len=zin.read(buffer))!=-1){
                            fout.write(buffer,0,len);
                        }
                        fout.close();
                        zin.closeEntry();
                    }catch (Exception e){
                        e.printStackTrace();
                        return false;
                    }
                }
            }
            zin.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void updateIndex(){
        OftenOpts oftenOpts=OftenOpts.getInstance(this);
        List< ScheWithTimeModel> alls=oftenOpts.getRecordedScts();
        List<String> news= BasicOpts.getInstance(this).list(FileRootTypes.sches);
        for(String n:news){
            alls.add(new ScheWithTimeModel(0,n, Statics.default_time_file_txt));
        }
        List<ScheWithTimeModel> all_=CycleUtil.distinct(alls);
        oftenOpts.putRawSctList(all_);
    }
}