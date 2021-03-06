package com.example.classtool.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ZoomButtonsController;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.classtool.HomeAct;
import com.example.classtool.R;
import com.example.classtool.base.BasicActivity;
import com.example.classtool.base.ComWebView;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;

public class ActcomWeb extends BasicActivity {


    private FrameLayout web_frame;
    private QMUIEmptyView web_error;
    private QMUITopBarLayout topBarLayout;
    private ProgressBar mProgressBar;
    protected ComWebView mWebView;

    public static final String EXTRA_URL="EXTRA_URL";

    private final static  int PRORESS_PROGRESS=0;
    private final static int PROGRESS_GONE=1;

    private String mUrl;
   // private String mTitle;

    private ProgressHandler mProgressHandler;
    private boolean mIsPageFinished=false;
    public boolean NEED_CLEAR=false;

    public String open_url="https://www.baidu.com";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_act_comweb);
        customStatus();

        web_frame=(FrameLayout) findViewById(R.id.web_frame);
        web_error=new QMUIEmptyView(this);
        web_error.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        topBarLayout=(QMUITopBarLayout) findViewById(R.id.web_topbar);
        Bundle returns=setArguments();
        if(returns!=null){
            String url=returns.getString(EXTRA_URL);
            if(url!=null&&url.length()>0){
                handleUrl(url);
            }
        }

        mProgressBar=(ProgressBar) findViewById(R.id.web_progress);
        mProgressHandler=new ProgressHandler();

        initTopbar();
        initWebView();

    }

    //?????????????????????????????????
    public void customStatus(){

    }

    //?????????????????????????????????????????????
    public Bundle setArguments(){
        Bundle bun=new Bundle();
        bun.putString(EXTRA_URL,open_url);
        return bun;
    }

    private void initTopbar(){
        getConfigTopbars(topBarLayout);
    }

    public configTopbars getConfigTopbars(QMUITopBarLayout topBarLayout){
        return new configTopbars(topBarLayout);
    }



    protected void initWebView(){
        mWebView=new ComWebView(getApplicationContext());
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        web_frame.addView(mWebView,params);

        mWebView.setWebChromeClient(getWebViewChromeClient());
        mWebView.setWebViewClient(getWebViewClient());
        //???????????????
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.requestFocus(View.FOCUS_DOWN);
        //setZoomControlGone(mWebView);
        configWebView(mWebView);
        mWebView.loadUrl(mUrl);

    }



    //???????????????????????????????????????
    protected void configWebView(ComWebView webView){

    }


    private void handleUrl(String url){
        String decodeUrl;
        try{
            decodeUrl= URLDecoder.decode(url,"utf-8");
        }catch (UnsupportedEncodingException e){
            decodeUrl=url;
        }
        mUrl=decodeUrl;
    }

    protected WebChromeClient getWebViewChromeClient(){
        return new ActcomwebChromeClient(this);
    }

    protected WebViewClient getWebViewClient(){
        return new ActcomwebClient();
    }

    private void sendProgressMessage(int progressType,int progress,int duration){
        Message msg=new Message();
        msg.what=progressType;
        msg.arg1=progress;
        msg.arg2=duration;
        mProgressHandler.sendMessage(msg);

    }

    @Override
    protected void onStop() {
        mWebView.getSettings().setJavaScriptEnabled(false);
        super.onStop();
    }

    @Override
    protected void onResume() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //??????WebView???????????????????????????
        if(mWebView!=null){
            mWebView.loadDataWithBaseURL(null,"","text/html","utf-8",null);
            web_frame.removeView(mWebView);
            mWebView.destroy();
            mWebView=null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {

            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void setZoomControlGone(WebView webView){
        webView.getSettings().setDisplayZoomControls(false);
        @SuppressWarnings("rawtypes")
        Class classType;
        Field field;
        try {
            classType = WebView.class;
            field = classType.getDeclaredField("mZoomButtonsController");
            field.setAccessible(true);
            ZoomButtonsController zoomButtonsController = new ZoomButtonsController(
                    webView);
            zoomButtonsController.getZoomControls().setVisibility(View.GONE);
            try {
                field.set(webView, zoomButtonsController);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static class ActcomwebChromeClient extends WebChromeClient{
        private ActcomWeb mActivity;

        public ActcomwebChromeClient(ActcomWeb activity){
            mActivity=activity;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            // ???????????????
            if (newProgress > mActivity.mProgressHandler.mDstProgressIndex) {
                mActivity.sendProgressMessage(PRORESS_PROGRESS, newProgress, 100);
            }
        }


        //??????????????????
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    }

    protected class ActcomwebClient extends  WebViewClient{
        public ActcomwebClient mWebViewClient;

        public ActcomwebClient(){ }
        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
            //???????????????????????????????????????????????????????????????
            if(NEED_CLEAR) view.clearHistory();
        }

        //????????????????????????????????????????????????????????????
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

            try{
                mWebView.stopLoading();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(error.getErrorCode()==-1){
                Log.i("onReceivedError: ","error_code-->"+error.getErrorCode()+","+error.getDescription());
            }else if(error.getDescription().equals("net::ERR_INTERNET_DISCONNECTED")){
                mWebView.loadUrl("about:blank");
                showError();
            }
            else{
                Log.i("onReceivedError: ","error_code-->"+error.getErrorCode());
            }
            super.onReceivedError(view, request, error);


        }

        //?????????????????????
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (mProgressHandler.mDstProgressIndex == 0) {
                sendProgressMessage(PRORESS_PROGRESS, 30, 500);
            }
        }

        //?????????????????????
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            sendProgressMessage(PROGRESS_GONE, 100, 0);
        }



    }


    private class ProgressHandler extends Handler {

        private int mDstProgressIndex;
        private int mDuration;
        private ObjectAnimator mAnimator;


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PRORESS_PROGRESS:
                    mIsPageFinished = false;
                    mDstProgressIndex = msg.arg1;
                    mDuration = msg.arg2;
                    mProgressBar.setVisibility(View.VISIBLE);
                    if (mAnimator != null && mAnimator.isRunning()) {
                        mAnimator.cancel();
                    }
                    mAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", mDstProgressIndex);
                    mAnimator.setDuration(mDuration);
                    mAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (mProgressBar.getProgress() == 100) {
                                sendEmptyMessageDelayed(PROGRESS_GONE, 500);
                            }
                        }
                    });
                    mAnimator.start();
                    break;
                case PROGRESS_GONE:
                    mDstProgressIndex = 0;
                    mDuration = 0;
                    mProgressBar.setProgress(0);
                    mProgressBar.setVisibility(View.GONE);
                    if (mAnimator != null && mAnimator.isRunning()) {
                        mAnimator.cancel();
                    }
                    mAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", 0);
                    mAnimator.setDuration(0);
                    mAnimator.removeAllListeners();
                    mIsPageFinished = true;
                    break;
                default:
                    break;
            }
        }
    }

    private void showError(){
        mWebView.removeAllViews();
        mWebView.addView(web_error);

        web_error.show(false, "????????????????????????~", "??????????????????????????????????????????", "????????????", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.removeAllViews();
                NEED_CLEAR=true;
                mWebView.loadUrl(mUrl);
            }
        });
    }

    protected class configTopbars{
        public boolean left_isVisible=true;
        public boolean right_isVisible=true;
        public configTopbars(QMUITopBarLayout topBarLayout){
            setTopbarTitle();
            controlVisible();
        }

        public void setTopbarTitle(){

        }



        private void controlVisible(){
            if(left_isVisible){
                topBarLayout.addLeftImageButton(R.drawable.ic_action_back,R.id.web_topbar).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mWebView.canGoBack()){
                            mWebView.goBack();
                        }
                        else finish();
                    }
                });
            }

            if(right_isVisible){
                topBarLayout.addRightImageButton(R.drawable.ic_action_more,R.id.web_topbar).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setRightClicks(view.getContext());
                    }
                });
            }
        }

        //??????????????????????????????
        public void setRightClicks(Context context){
            QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(context);
            builder.addItem(R.drawable.ic_action_webview_icon,getString(R.string.webviewAct_webviewOpen),0, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                    .addItem(R.drawable.ic_action_webview_copy,getString(R.string.webviewAct_copy),1, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                    .addItem(R.drawable.ic_action_webview_refresh,getString(R.string.webviewAct_refresh),2,QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)                        .setAddCancelBtn(true)
                    .setSkinManager(QMUISkinManager.defaultInstance(context))
                    .setOnSheetItemClickListener(new QMUIBottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
                        @Override
                        public void onClick(QMUIBottomSheet dialog, View itemView) {
                            dialog.dismiss();
                            int tag = (int) itemView.getTag();
                            switch (tag) {
                                case 0:
                                    Intent intent=new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(open_url));
                                    startActivity(intent);
                                    // Toast.makeText(getActivity(), "???????????????", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    ClipboardManager cmb=(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    cmb.setText(open_url);
                                    Toast.makeText(context, "?????????????????????", Toast.LENGTH_SHORT).show();
                                    break;
                                case 2:
                                    mWebView.reload();
                                    break;

                            }
                        }
                    }).build().show();
        }

    }
}
