package com.hazukie.scheduleviews.services;

import static android.content.ContentValues.TAG;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class SctService extends Service {
    private final SctObserverBinder binder= new SctObserverBinder();


    public static class SctObserverBinder extends Binder{
        int isUpdate=0;
        public void updateMsg(int value){
            isUpdate=value;
            Log.d(TAG, "onBinder: value is updated to "+value+" now !");
        }

        public int getIsUpdateMsg(){
            Log.d(TAG,"onBinder: get value="+isUpdate);
            return isUpdate;
        }

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
        Log.i(TAG, "onStartCommand: service executed!");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: service executed!");
        super.onDestroy();
    }

}