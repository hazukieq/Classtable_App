package com.hazukie.scheduleviews.services;

import static android.content.ContentValues.TAG;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SctService extends Service {
    private final SctObserverBinder binder= new SctObserverBinder();



    public static class SctObserverBinder extends Binder{

        public void download(URL url, String tempPath, String path){
            new Thread(() -> {
                File file=new File(path);
                File tempfile=new File(tempPath);

                OkHttpClient client=new OkHttpClient();
                Request request=new Request.Builder()
                        .addHeader("RANGE","bytes="+"0-")
                        .url(url)
                        .build();
                InputStream is = null;
                OutputStream out=null;
                BufferedOutputStream writer=null;
                try {
                    if(!tempfile.exists()) tempfile.createNewFile();
                    if(!file.exists()) file.createNewFile();

                    Response resp=client.newCall(request).execute();
                    if(resp != null){
                        if(resp.body()!=null){
                            is=resp.body().byteStream();
                            out=new FileOutputStream(tempfile);
                            writer=new BufferedOutputStream(out);

                            byte[] bytes=new byte[1024];
                            int len;
                            while ((len=is.read(bytes))!=-1){
                                writer.write(bytes,0,len);
                            }
                        }
                        tempfile.renameTo(file);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    tempfile.deleteOnExit();
                    file.deleteOnExit();
                }finally {
                    try{
                        if(is!=null) is.close();
                        if(out!=null) out.close();
                        if(writer!=null) writer.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }).start();
        }


/*        private final Handler handler=new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==100){
                    Log.i("ScptService","download task done!");

                }
            }
        };*/



    }




    public SctService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        //throw new UnsupportedOperationException("Not yet implemented");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /*
         * 方式一：采用Handler的postDelayed(Runnable, long)方法
         */
        /*Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                // handler自带方法实现定时器
                handler.postDelayed(this, 1000*10);//每隔10s执行
                Log.d("mawl","后台服务循环执行 哈哈哈哈");
            }
        };
        handler.postDelayed(runnable, 500);//延时多长时间启动定时器*/

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}