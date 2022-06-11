package com.example.classtool.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.classtool.models.DownloadBean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DownloadManager {

    public static final int STATE_NONE = -1;
    /** 开始下载 */
    public static final int STATE_START = 0;
    /** 等待中 */
    public static final int STATE_WAITING = 1;
    /** 下载中 */
    public static final int STATE_DOWNLOADING = 2;
    /** 暂停 */
    public static final int STATE_PAUSED = 3;
    /** 下载完毕 */
    public static final int STATE_DOWNLOADED = 4;
    /** 下载失败 */
    public static final int STATE_ERROR = 5;
    /** 删除下载成功 */
    public static final int STATE_DELETE = 6;

    /** 用于记录观察者，当信息发送了改变，需要通知他们 */
    private Map<String, DownloadObserver> mObservers = new ConcurrentHashMap<>();
    /** 用于记录所有下载的任务，方便在取消下载时，通过id能找到该任务进行删除 */
    private Map<String, DownLoadTask> mTaskMap = new ConcurrentHashMap<>();
    /** 全局记录当前正在下载的bean */
    private DownloadBean down_bean;

    private static DownloadManager instance;



    public static DownloadManager getInstance() {
        if (instance == null) {
            instance = new DownloadManager();
        }
        return instance;
    }

    /** 注册观察者 */
    public void registerObserver(String name, DownloadObserver observer) {
        if (!mObservers.containsKey(name)) {
            mObservers.put(name, observer);
        }
    }

    /** 移除观察者 */
    public void RemoveObserver() {
        mObservers.clear();
    }

    /** 删除当前正在下载的任务 */
    public void DeleteDownTask(DownloadBean bean) throws IOException {
        File file = new File(bean.getSave_path());
        if (mTaskMap.containsKey(bean.name)) {
            // 拿到当前任务
            DownLoadTask task = mTaskMap.get(bean.name);
            // 暂停下载任务(等于取消了该线程)
            task.bean.download_state= STATE_DELETE;

            // 再更改删除界面状态(这是也调一次是怕在没下载的时候删除)
            bean.download_state = STATE_DELETE;
            notifyDownloadStateChanged(bean);

            // 最后删除索引
            //DataBaseUtil.DeleteDownLoadById(bean.id);
            //FilesUtil.writeDownload_datas(new DownloadBean("",0,"","",0,0),false);
            // 删除文件


            if (file.exists()) {
                file.delete();
            }
            file = null;
        }else {
            bean.download_state = STATE_DELETE;
            notifyDownloadStateChanged(bean);
            if (file.exists()) {
                file.delete();
            }
            file = null;
        }
    }


    /** 销毁的时候关闭线程池以及当前执行的线程，并清空所有数据和把当前下载状态存进数据库 */
    public void Destory()  {
        DownloadExecutor.stop();
        mObservers.clear();
        mTaskMap.clear();
        if (down_bean != null) {
            down_bean.download_state = STATE_PAUSED;
            //添加索引
            //DataBaseUtil.UpdateDownLoadById(down_bean);
            //FilesUtil.writeDownload_datas(down_bean,true);
        }
    }

    /** 当下载状态发送改变的时候回调 */
    private ExecuteHandler handler = new ExecuteHandler();

    /** 拿到主线程Looper */
    @SuppressLint("HandlerLeak")
    private class ExecuteHandler extends Handler {
        private ExecuteHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            DownloadBean bean = (DownloadBean) msg.obj;
            if (mObservers.containsKey(bean.name)) {
                DownloadObserver observer = mObservers.get(bean.name);
                switch (bean.download_state) {
                    case STATE_START:// 开始下载
                        observer.onStart(bean);
                        break;
                    case STATE_WAITING:// 准备下载
                        observer.onPrepare(bean);
                        break;
                    case STATE_DOWNLOADING:// 下载中
                        observer.onProgress(bean);
                        break;
                    case STATE_PAUSED:// 暂停
                        observer.onStop(bean);
                        break;
                    case STATE_DOWNLOADED:// 下载完毕
                        try {
                            observer.onFinish(bean);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case STATE_ERROR:// 下载失败
                        observer.onError(bean);
                        break;
                    case STATE_DELETE:// 删除成功
                        observer.onDelete(bean);
                        break;
                }
            }
        }
    }


    /** 当下载状态发送改变的时候调用 */
    private void notifyDownloadStateChanged(DownloadBean bean) {
        Message message = handler.obtainMessage();
        message.obj = bean;
        handler.sendMessage(message);
    }

    /** 开启下载，需要传入一个DownAppBean对象 */
    public void download(DownloadBean bean)  {
        // 先判断是否有这个app的下载信息
        //DownloadBean bean = FilesUtil.readDownload_datas(loadbean.name);
        /*if (bean == null) {// 如果没有，则根据loadBean创建一个新的下载信息
            bean = loadbean;
            //DataBaseUtil.insertDown(bean);
        }*/

        // 判断状态是否为STATE_NONE、STATE_PAUSED、STATE_ERROR、STATE_DELETE。只有这4种状态才能进行下载，其他状态不予处理
        if (bean.download_state == STATE_NONE
                || bean.download_state == STATE_PAUSED
                || bean.download_state == STATE_DELETE
                || bean.download_state == STATE_ERROR) {
            // 下载之前，把状态设置为STATE_WAITING，因为此时并没有产开始下载，只是把任务放入了线程池中，当任务真正开始执行时，才会改为STATE_DOWNLOADING
            bean.download_state = STATE_WAITING;
           // DataBaseUtil.UpdateDownLoadById(bean);
            // 每次状态发生改变，都需要回调该方法通知所有观察者
            notifyDownloadStateChanged(bean);

            DownLoadTask task = new DownLoadTask(bean);// 创建一个下载任务，放入线程池
            // 线程放入map里面方便管理
            mTaskMap.put(bean.name, task);

            DownloadExecutor.execute(task);
        } else if (bean.download_state == STATE_START
                || bean.download_state == STATE_DOWNLOADING
                || bean.download_state == STATE_WAITING) {// 如果正在下载则暂停

            if (mTaskMap.containsKey(bean.name)) {
                DownLoadTask task = mTaskMap.get(bean.name);
                task.bean.download_state = STATE_PAUSED;
               // DataBaseUtil.UpdateDownLoadById(task.bean);

                // 取消还在排队中的线程
                if (DownloadExecutor.cancel(task)) {
                    mObservers.get(bean.name).onStop(task.bean);
                }
            }
        }
    }

    public class DownLoadTask implements Runnable {

        private DownloadBean bean;

        public DownLoadTask(DownloadBean bean) {
            this.bean = bean;
        }

        @Override
        public void run() {
            // 等待中就暂停了
            if (bean.download_state== STATE_PAUSED) {
                bean.download_state = STATE_PAUSED;
                //DataBaseUtil.UpdateDownLoadById(bean);

                return;
            } else if (bean.download_state == STATE_DELETE) {// 等待中就删除直接回调界面，然后直接返回
                bean.download_state = STATE_DELETE;
                notifyDownloadStateChanged(bean);
                mTaskMap.remove(bean.name);
                return;
            }

            bean.download_state = DownloadManager.STATE_START;// 开始下载
            //DataBaseUtil.UpdateDownLoadById(bean);
            notifyDownloadStateChanged(bean);

            // 当前下载的进度
            long compeleteSize = 0;
            File file = new File(bean.getSave_path());// 获取下载文件
            if (!file.exists()) {
                // 如果文件不存在
                bean.progress = 0;
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // 如果存在就拿当前文件的长度，设置当前下载长度
                // (这样的好处就是不用每次在下载文件的时候都需要写入数据库才能记录当前下载的长度，一直操作数据库是很费资源的)
                compeleteSize = file.length();
            }

            try{
               /* URL url = new URL(bean.url);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setConnectTimeout(30 * 1000);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Range", "bytes=" + compeleteSize
                        + "-" + bean.appSize);*/
                    OutputStream outputStream=new FileOutputStream(file);
                    Request request=new Request.Builder()
                            .url(bean.url)
                            .addHeader("Range", "bytes=" + compeleteSize)
                                    //+ "-" + bean.appSize)
                            .build();
                    OkHttpClient client=new OkHttpClient();

                    try{
                        Response response=client.newCall(request)
                                .execute();

                        // 获取的状态码
                        int code = response.code();
                        // 判断是否能够断点下载
                        if (code == 206) {
                            @SuppressWarnings("resource")
                            ResponseBody body = response.body();
                            // 将要下载的文件写到保存在保存路径下的文件中
                            InputStream is = body.byteStream();
                            byte[] buffer = new byte[512];
                            int length = 0;
                            // 进入下载中状态
                            bean.download_state = STATE_DOWNLOADING;
                            //DataBaseUtil.UpdateDownLoadById(bean);

                            while ((length = is.read(buffer)) != -1) {
                                // 暂停就回调，然后直接返回
                                if (bean.download_state == STATE_PAUSED) {
                                    bean.download_state = STATE_PAUSED;
                                    //DataBaseUtil.UpdateDownLoadById(bean);
                                    //FilesUtil.writeDownload_datas(bean);
                                    outputStream.close();
                                    is.close();
                                    notifyDownloadStateChanged(bean);
                                    return;
                                } else if (bean.download_state == STATE_DELETE) {// 下载的时候删除直接回调界面，然后直接返回
                                    bean.download_state = STATE_DELETE;
                                    notifyDownloadStateChanged(bean);
                                    outputStream.close();
                                    is.close();
                                    mTaskMap.remove(bean.name);
                                    return;
                                }
                                // 把当前下载的bean给全局记录的bean
                                down_bean = bean;
                                outputStream.write(buffer, 0, length);
                                compeleteSize += length;
                                // 更新数据库中的下载信息
                                // 用消息将下载信息传给进度条，对进度条进行更新
                                bean.progress = (int) compeleteSize;
                                notifyDownloadStateChanged(bean);
                            }

                            if (bean.appSize == bean.progress) {
                                bean.download_state = STATE_DOWNLOADED;
                                //DataBaseUtil.UpdateDownLoadById(bean);
                                // FilesUtil.writeDownload_datas(bean);
                                notifyDownloadStateChanged(bean);
                            } else {
                                bean.download_state = STATE_ERROR;
                                //DataBaseUtil.UpdateDownLoadById(bean);
                                //FilesUtil.writeDownload_datas(bean);
                                notifyDownloadStateChanged(bean);
                                bean.progress = 0;// 错误状态需要删除文件
                                file.delete();
                            }
                            outputStream.close();
                            is.close();
                        } else {
                            Log.e("123456", "不支持断点下载");
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                        bean.download_state = STATE_ERROR;
                        //DataBaseUtil.UpdateDownLoadById(bean);
                        notifyDownloadStateChanged(bean);
                        bean.progress = 0;// 错误状态需要删除文件
                        file.delete();
                    }
            } catch (IOException e) {
            }
                 mTaskMap.remove(bean.name);
        }
    }
}
