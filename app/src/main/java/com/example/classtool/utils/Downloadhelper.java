package com.example.classtool.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Downloadhelper {

    public static Integer uploadSchdfile(Context context,String uid,String fileName,String urlpha,String filename1,String urlpha1,String purl) throws IOException {
        OkHttpClient clien=new OkHttpClient().newBuilder().build();
        RequestBody cbody=new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid",uid)
                .addFormDataPart("upload_file",fileName,
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File(urlpha)))
                .addFormDataPart("time_upload",filename1,
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File(urlpha1)))
               // .addFormDataPart("timetag_upload",filename1,
                 //       RequestBody.create(MediaType.parse("application/octet-stream"),
                   //             new File(urlpha1)))
                .build();

        Request crequest=new Request.Builder()
                .url(purl)
                .method("POST",cbody)
                .build();
        Response cresponse=clien.newCall(crequest).execute();
        System.out.println(cresponse.message()+",code-->"+cresponse.code());

        return cresponse.code();
    }

    public static void downloadP(final String url,final String destFileDir,final String destFileName,final OnDownloadListener listener){
        Request request=new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client=new OkHttpClient();

        try{
            Response response=client.newCall(request)
                    .execute();

        }catch (IOException e){
            e.printStackTrace();
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                listener.onDownloadFailed(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                InputStream is=null;
                byte[] buf=new byte[2048];

                int len=0;
                FileOutputStream fos=null;
                File dir=new File(destFileDir);
                if(!dir.exists()){
                    dir.mkdirs();
                }
                File file=new File(dir,destFileName);

                try{
                    is=response.body().byteStream();
                    long total=response.body().contentLength();
                    Log.i( "onResponse: ","length-->"+total);
                    fos=new FileOutputStream(file);

                    long sum=0;
                    while ((len=is.read(buf))!=-1){
                        fos.write(buf,0,len);
                        sum+=len;
                        int progress=(int)(sum*1.0/total*100);
                        listener.onDownloadloading(progress);
                    }
                    fos.flush();
                    listener.onDownloadSuccess(file);
                }catch (Exception e){
                    listener.onDownloadFailed(e);
                }finally {
                    try {
                        if(is!=null){
                            is.close();
                        }
                        if(fos!=null) fos.close();
                    }catch (Exception e){

                    }
                }
            }
        });
    }

    public interface OnDownloadListener{
        void onDownloadSuccess(File file);
        void onDownloadloading(int progress);
        void onDownloadFailed(Exception e);
    }

    public static String generateFilepath(Context context,String firstFile,String name) throws IOException {
        File el =context.getDir(firstFile,Context.MODE_PRIVATE);
        if (!el.exists()) el.mkdir();

        File eles = new File(el, name);
        if (!eles.exists()) eles.createNewFile();


        String apk_pa = eles.getAbsolutePath();
        return  apk_pa;
    }
}
