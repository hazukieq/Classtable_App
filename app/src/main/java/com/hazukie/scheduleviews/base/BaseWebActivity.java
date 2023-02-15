package com.hazukie.scheduleviews.base;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.MimeTypeMap;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.hazukie.cskheui.Crialoghue.Crialoghue;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.custom.CnWebView;
import com.hazukie.scheduleviews.custom.TopbarLayout;
import com.hazukie.scheduleviews.net.NativeInvoker;
import com.hazukie.scheduleviews.net.WebStacker;
import com.hazukie.scheduleviews.utils.ProgressHandler;
import com.hazukie.scheduleviews.utils.StatusHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BaseWebActivity extends BaseActivity {
    private static final String TAG = "BaseWebActivity";
    private FrameLayout web_frame;
    private TopbarLayout topBarLayout;
    protected CnWebView mWebView;

    private final static int PRORESS_PROGRESS = 0;
    private final static int PROGRESS_GONE = 1;

    private ProgressHandler mProgressHandler;
    public boolean NEED_CLEAR = false;

    //默认链接
    public String open_url = "https://www.baidu.com";
    //传递参数
    public String open_param="";
    private String mUrl;

    //android 5.0 之后需要开启浏览器的整体缓存才能截取整个Web
    //public boolean isEnableImgCache=false;

    /*
    private  SctService.SctObserverBinder binder;
    private final ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = (SctService.SctObserverBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localweb);
        web_frame = findViewById(R.id.web_frame);
        topBarLayout = findViewById(R.id.web_topbar);
        initTopbar();

        getArguments();
        if (open_url != null && open_url.length() > 0) handleUrl(open_url);

        ProgressBar mProgressBar = findViewById(R.id.web_progress);
        mProgressHandler = new ProgressHandler(mProgressBar);
        initWebView();
    }

    /**
     * @param TYPE 0代表白色文字状态栏，1代表黑色文字状态栏
     */
    public void setLightOrDarkStatusBar(StatusHelper.Mode TYPE){
        StatusHelper.controlStatusLightOrDark(this, TYPE);
    }

    //控制活动界面状态栏颜色
    public void customStatus(TopbarLayout mTopBarLayout) { }


    //读取网页链接
    private void getArguments() {
        Intent in_=getIntent();
        open_url = in_.getStringExtra("url");
        String title = in_.getStringExtra("title");
        open_param=in_.getStringExtra("appendParam");
        topBarLayout.setTitle(title);
    }

    /**
     * @param context 当前活动 Context
     * @param secondeClass 目标活动名字
     * @param url 加载网页链接
     * @param title 网页标题
     * @param appendParam 附加参数
     */
    public static void startActivityWithLoadUrl(Context context,Class<?> secondeClass, String url,String title,String appendParam) {
        Intent intent = new Intent(context, secondeClass);
        intent.putExtra("url", url);
        intent.putExtra("title",title);
        intent.putExtra("appendParam",appendParam);
        context.startActivity(intent);
    }


    //初始化状态栏
    private void initTopbar() {
        customStatus(topBarLayout);
    }


    //是否隐藏状态栏
    public  void hiddenStatusBar(boolean isLoad){
        if(isLoad)topBarLayout.setVisibility(View.GONE);
        else topBarLayout.setVisibility(View.VISIBLE);
    }


    //处理网页链接编码
    private void handleUrl(String url) {
        String decodeUrl;
        try {
            decodeUrl = URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            decodeUrl = url;
        }
        mUrl = decodeUrl;
    }

    protected void initWebView() {
        mWebView = WebStacker.getInstance(getApplicationContext()).getCnWebView(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        web_frame.addView(mWebView, params);

        mWebView.setWebChromeClient(getWebViewChromeClient());
        mWebView.setWebViewClient(getWebViewClient());
        mWebView.addJavascriptInterface(new NativeInvoker(BaseWebActivity.this,mWebView), "java");

        configWebView(mWebView);
        mWebView.loadUrl(mUrl);
    }


    //可以通过改方法配置网页设置
    protected void configWebView(CnWebView webView) {}

    protected WebChromeClient getWebViewChromeClient() {
        return new ActcomwebChromeClient(this);
    }

    protected WebViewClient getWebViewClient() {
        return new ActcomwebClient();
    }

    private void sendProgressMessage(int progressType, int progress, int duration) {
        Message msg = new Message();
        msg.what = progressType;
        msg.arg1 = progress;
        msg.arg2 = duration;
        mProgressHandler.sendMessage(msg);

    }

    public static class ActcomwebChromeClient extends WebChromeClient {
        private final BaseWebActivity mActivity;

        public ActcomwebChromeClient(BaseWebActivity activity) {
            mActivity = activity;
        }

       /* @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            CnWebView cnWebView=WebStacker.getInstance(mActivity.getApplicationContext()).getCnWebView(mActivity);
            view.addView(cnWebView);
            WebView.WebViewTransport transport=(WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(cnWebView);
            resultMsg.sendToTarget();

            cnWebView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Intent in=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    mActivity.startActivity(in);
                    return true;//super.shouldOverrideUrlLoading(view, url);
                }
            });
            return true;//super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }*/

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            //Log.i("LocalWeb>>", consoleMessage.message());
            return super.onConsoleMessage(consoleMessage);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            // 修改进度条
            if (newProgress > mActivity.mProgressHandler.mDstProgressIndex) {
                mActivity.sendProgressMessage(PRORESS_PROGRESS, newProgress, 100);
            }
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Crialoghue crialoghue=new Crialoghue.TxtBuilder()
                    .addTitle("警告")
                    .addContent(message)
                    .onConfirm((crialoghue1, view1) -> {
                        crialoghue1.dismiss();
                        result.confirm();
                    })
                    .onCancel((crigh,v)->{
                        crigh.dismiss();
                        result.cancel();
                    }).build(view.getContext());
            crialoghue.show();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            Crialoghue crialoghue=new Crialoghue.TxtBuilder()
                    .addTitle("警告")
                    .addContent(message)
                    .onConfirm((crialoghue1, view1) -> {
                        crialoghue1.dismiss();
                        result.confirm();
                    })
                    .onCancel((crigh,v)->{
                        crigh.dismiss();
                        result.cancel();
                    }).build(view.getContext());
            crialoghue.show();
            return true;
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            Crialoghue crialoghue=new Crialoghue.LeditBuilder()
                    .addTitle(message)
                    .addHint(defaultValue)
                    .onConfirm((crialoghue1, view1) -> {
                        EditText editText=(EditText) view1;
                        crialoghue1.dismiss();
                        result.confirm(editText.getText().toString());
                    })
                    .onCancel((crialoghue12, view12) -> {
                        crialoghue12.dismiss();
                        result.cancel();
                    })
                    .build(view.getContext());
            crialoghue.show();
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
    }

    protected class ActcomwebClient extends WebViewClient {
        public ActcomwebClient() {}

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
            //清空错误代码加载成功后之前保留的后台栈历史
            if (NEED_CLEAR) view.clearHistory();
        }

        //网页开始加载时
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (mProgressHandler.mDstProgressIndex == 0) {
                sendProgressMessage(PRORESS_PROGRESS, 30, 500);
            }
        }

        //网页加载结束时
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            sendProgressMessage(PROGRESS_GONE, 100, 0);
        }

        @SuppressLint("WebViewClientOnReceivedSslError")
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();//接受所有证书
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            WebResourceResponse resp;
            if(isAssetRes(request)) resp=assetResRequest(view.getContext(),request);
            else if(isCacheRes(request)) resp=cacheResRequest(view.getContext(),request);
            else resp=super.shouldInterceptRequest(view, request);

            return resp;
        }
    }

    private boolean isAssetRes(WebResourceRequest request){
        String url=request.getUrl().toString();
        return url.startsWith("file:///android_asset/");
    }

    private WebResourceResponse assetResRequest(Context context,WebResourceRequest request){
        String url=request.getUrl().toString();
        WebResourceResponse resourceResponse = null;
        try{
            //资源文件名
            String formattedFileName=url.replace("file:///android_asset/","");
            Log.i("InterceptedRsp", "assetResRequest="+formattedFileName);
            /*int fileIndex=formattedUrl.lastIndexOf("/")+1;
            String fileName=url.substring(fileIndex);*/

/*
            //资源文件后缀
            int suffixIndex=url.lastIndexOf(".")+1;
            String suffix=url.substring(suffixIndex);

            String whole_res_path_head;

            boolean checkImgType=
                    suffix.equals("png")||suffix.equals("jpg")||
                    suffix.equals("jpeg")||suffix.equals("icon")||
                    suffix.equals("gif")||suffix.equals("svg")||suffix.equals("img");

            //boolean checkOthers=suffix.equals("js")||suffix.equals("css");
            //boolean checkVideos=suffix.equals("mp3")||suffix.equals("mp4")||suffix.equals("m4a")||;

            if(checkImgType) whole_res_path_head="img/";
            else whole_res_path_head=suffix+"/";
            Log.i(TAG, "assetResRequest="+formattedFileName);*/

            resourceResponse=new WebResourceResponse(getMimeTypeFromUrl(url),"UTF-8",context.getAssets().open(formattedFileName));
            Map<String,String> headers=new HashMap<>();
            headers.put("access-control-allow-origin","*");
            resourceResponse.setResponseHeaders(headers);
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return resourceResponse;
    }


    private boolean isCacheRes(WebResourceRequest request){
        String url=request.getUrl().toString();
        String extension=MimeTypeMap.getFileExtensionFromUrl(url);
        return extension.equals("icon")||extension.equals("bmp")||extension.equals("png")||
                extension.equals("jpg")||extension.equals("gif")||extension.equals("jpeg")||
                extension.equals("svg")||extension.equals("webp")||
                extension.equals("json")||extension.equals("eot")||
                extension.equals("otf")||extension.equals("ttf")||extension.equals("woff")||
                extension.equals("css")||extension.equals("js");
    }

    private static String getMD5(String url) {
        String result="";
        try {
            MessageDigest md=MessageDigest.getInstance("md5");
            md.update(url.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb=new StringBuilder();
            for(byte b:bytes){
                String str=Integer.toHexString(b&0xFF);
                if(str.length()==1){
                    sb.append("0");
                }
                sb.append(str);
            }
            result=sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    private WebResourceResponse cacheResRequest(Context context,WebResourceRequest request){
        String url=request.getUrl().toString();
        String mimeType=getMimeTypeFromUrl(url);

        if(isImgRes(request)){
            try{
                Log.i( "cacheResRequest","img_url="+url);
                File file= Glide.with(this).download(url).submit().get();
                WebResourceResponse resp=new WebResourceResponse(mimeType,"utf-8",new FileInputStream(file));
                Map<String,String> headers=new HashMap<>();
                headers.put("access-control-allow-origin","*");
                resp.setResponseHeaders(headers);
                return resp;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }


        if(isJsRes(request)){
            String cachePath=context.getDir("webache",Context.MODE_PRIVATE).getAbsolutePath();
            String cacheFilePath=cachePath+File.separator+"success-"+getMD5(url);
            String temp_cacheFilePath=cachePath+File.separator+getMD5(url);

            File cacheFile=new File(cacheFilePath);
            Log.i( "cacheResRequest","jscss_url="+cacheFilePath);
            if(!cacheFile.exists()||!cacheFile.isFile()){
                downloadAsset(url,temp_cacheFilePath,cacheFilePath);
            }

            if(cacheFile.exists()&&cacheFile.isFile()){
                WebResourceResponse resp=null;
                try{
                    resp=new WebResourceResponse(mimeType,"utf-8",new FileInputStream(cacheFile));
                    HashMap<String,String> map=new HashMap<>();
                    map.put("access-control-allow-origin","*");
                    resp.setResponseHeaders(map);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return resp;
            }
        }

        return null;
    }

    private boolean isJsRes(WebResourceRequest request){
        String url=request.getUrl().toString();
        String extension=MimeTypeMap.getFileExtensionFromUrl(url);
        return extension.equals("js")||extension.equals("css");
    }

    private boolean isImgRes(WebResourceRequest request){
        String url=request.getUrl().toString();
        String extension=MimeTypeMap.getFileExtensionFromUrl(url);
        return extension.equals("jpg")||extension.equals("jpeg")||extension.equals("png")||
                extension.equals("ico")||extension.equals("bmp")||extension.equals("gif")||
                extension.equals("svg")||extension.equals("webp");
    }

    private String getMimeTypeFromUrl(String url) {
        try{
            String extension= MimeTypeMap.getFileExtensionFromUrl(url);
            if(!extension.isEmpty()&&!extension.equals("null")){
                if(extension.equals("json")){
                    return "application/json";
                }
                return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)==null?"*/*":MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "*/*";
    }


    private void downloadAsset(String url, String tempPath, String path){
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(40,TimeUnit.SECONDS)
                .build();

        Request request=new Request.Builder()
                //.addHeader("RANGE","bytes="+"0-")
                .url(url)
                .build();
        InputStream is = null;
        File file=new File(tempPath);
        try {
            RandomAccessFile tempfile=new RandomAccessFile(tempPath,"rw");

            Response resp=client.newCall(request).execute();
            if(resp != null){
                if(resp.body()!=null){
                    is=resp.body().byteStream();
                    //byte[] isytes=resp.body().byteStream().readAllBytes();
                    byte[] bytes=new byte[1024*6];
                    int len;

                    while ((len=is.read(bytes))!=-1){
                        tempfile.write(bytes,0,len);
                    }
                }
                resp.body().close();

                if(file.exists()&&file.isFile()) file.renameTo(new File(path));
            }
        } catch (IOException e) {
            e.printStackTrace();
            file.deleteOnExit();
        }finally {
            try{
                if(is!=null) is.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁WebView组件，防止内存泄露
        if (mWebView != null) {
            web_frame.removeView(mWebView);
            WebStacker.getInstance(getApplicationContext()).recycleCnWebView(mWebView);
            mWebView = null;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK&&mWebView.canGoBack()){
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
