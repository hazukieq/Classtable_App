package com.hazukie.scheduleviews.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hazukie.cskheui.DisplayHelper;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.base.BaseActivity;
import com.hazukie.scheduleviews.fileutil.BasicOpts;
import com.hazukie.scheduleviews.fileutil.FileRootTypes;
import com.hazukie.scheduleviews.statics.Statics;
import com.hazukie.scheduleviews.utils.SpvalueStorage;
import com.hazukie.scheduleviews.utils.StatusHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import kotlin.text.Charsets;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SyncActivity extends BaseActivity {
    private LinearLayout files_lay;
    private ScrollView scroll;
    private Button upload_btn,download_btn;
    private ProgressBar progressBar;
    private String user;
    private TextView genid,hint;

    private final Handler handler=new Handler(message ->{
        String str=message.getData().getString("req");
        if(str != null&&str.equals("success")){
            hint.setText("文件上传成功！");
            progressBar.setProgress(100);
        }else{
            hint.setText("错误："+str);
            progressBar.setProgress(0);
        }
        //Toast.makeText(this,"请求结果："+str,Toast.LENGTH_SHORT).show();
        return true;
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        StatusHelper.controlStatusLightOrDark(this, StatusHelper.Mode.Status_Dark_Text);
        SpvalueStorage sp = SpvalueStorage.getInstance(this);
        genid=findViewById(R.id.sync_genid);

        if(!sp.getBooleanValue("geninit", false)){
            String uid= UUID.randomUUID().toString().replace("-","").substring(0,8);
            sp.setStringvalue("genid",uid);
            sp.setBooleanValue("geninit",true);
        }
        genid.setText(Html.fromHtml("<b>你的用户码: " + sp.getStringValue("genid", "123456") + "</b><br/>请选择上传课表文件： "));
        user= SpvalueStorage.getInstance(this).getStringValue("genid","123456");
        hint=findViewById(R.id.sync_upload_hint);
        files_lay=findViewById(R.id.sync_all_files);
        scroll=findViewById(R.id.sync_scroll);
        upload_btn=findViewById(R.id.sync_upload_btn);
        download_btn=findViewById(R.id.sync_donwload_btn);
        progressBar=findViewById(R.id.sync_upload_status);
        BasicOpts basicOpts=BasicOpts.getInstance(this);
        List<String> files = basicOpts.list(FileRootTypes.sches);

        LinearLayout root=findViewById(R.id.sync_root);

        root.post(() -> scroll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,root.getHeight()/4)));
        download_btn.setOnClickListener(v->startAct2Act(this,RgetActivity.class));

        if(files.size()>0&&files.get(0)!=null){
            for(String f:files){
                files_lay.addView(createCheckBox(f));
            }
        }

        upload_btn.setOnClickListener(v->{
            progressBar.setProgress(0);
            List<String> selecteds=new ArrayList<>();
            int len=files_lay.getChildCount();
            for(int i=0;i<len;i++){
                CheckBox f=(CheckBox) files_lay.getChildAt(i);
                if(f.isChecked()) selecteds.add(f.getText().toString());
            }
            Log.i( "onCreate: ","selected="+selecteds.size());
            if(zipALl(selecteds)){
                progressBar.setProgress(80);
                new Thread(this::uploadAll).start();
            }else hint.setText("尚未选择上传文件！");
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

    private boolean zipALl(List<String> all){
        if(all==null||all.size()==0) return false;

        File dir=getDir("zips",MODE_APPEND);
        if(!dir.exists()) dir.mkdir();
        File dirf=new File(dir,"sche.zip");
        hint.setText("正在压缩文件...");
        int progress=30;
        progressBar.setProgress(progress);
        try{
            if(!dirf.exists()) dirf.createNewFile();
            FileOutputStream fout=new FileOutputStream(dirf);
            ZipOutputStream zos=new ZipOutputStream(fout);
            for(String s:all){
                if(progress<100) progress+=10;
                progressBar.setProgress(progress);

                ZipEntry entry=new ZipEntry(s);
                zos.putNextEntry(entry);
                File sf=new File(getDir("sches",MODE_PRIVATE),s);
                try {
                    byte[] buffer = new byte[1024];
                    FileInputStream fis = new FileInputStream(sf);
                    int len = fis.read(buffer);
                    while (len>0){
                        zos.write(buffer,0,len);
                        len=fis.read(buffer);
                    }
                    zos.flush();
                    zos.closeEntry();
                    fis.close();
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
            }
            zos.close();
            hint.setText("压缩成功,准备上传...");
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void uploadAll() {
        OkHttpClient client = new OkHttpClient();
        // 构建请求体
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type", "sche")
                .addFormDataPart("fmt", "zip")
                .addFormDataPart("sche", "sche.zip",
                        RequestBody.create(MediaType.parse("application/text"), new File(getDir("zips", MODE_PRIVATE), "sche.zip")))
                .addFormDataPart("user", user)
                .build();

        // 构建请求
        Request request = new Request.Builder()
                .url(Statics.share_sche_url)
                .post(requestBody)
                .build();

        // 发送请求并处理响应
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String ret = response.body().string();
                Log.i("", "请求成功，响应内容为：" + ret);//Toast.LENGTH_SHORT).show();
                response.body().close();
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("req", ret);
                msg.setData(bundle);
                handler.sendMessage(msg);
            } else {
                Log.i("", "请求失败，错误信息为：" + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) response.body().close();
        }
    }
}