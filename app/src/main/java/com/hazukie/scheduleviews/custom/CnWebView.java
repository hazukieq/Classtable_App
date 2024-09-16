package com.hazukie.scheduleviews.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebHistoryItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.hazukie.scheduleviews.R;


public class    CnWebView extends WebView {

    private static final String TAG ="CnWebView" ;

    public CnWebView(Context context) {
        this(context, null);
        init();
    }

    public CnWebView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
        init();
    }

    public CnWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private String getWebCacheDir(Context context){
        return context.getDir("app_webache",Context.MODE_PRIVATE).getAbsolutePath();
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void init() {
        setBackgroundColor(Color.TRANSPARENT);
        setBackgroundResource(R.color.white);

        //隐藏滚动条
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);

        setOverScrollMode(View.OVER_SCROLL_NEVER);//取消WebView中混动或拖动到顶部、底部时的阴影
        setNestedScrollingEnabled(false);//

        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        webSettings.setSupportMultipleWindows(false);

        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setUseWideViewPort(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setAllowFileAccess(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setTextZoom(100);

        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);

        //webSettings.setBlockNetworkLoads(false);
        webSettings.setDomStorageEnabled(true);


        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);

        //set web cache strategy
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        /*
        boolean isCacheMode=false;
        int loadMode=isCacheMode?WebSettings.LOAD_DEFAULT:WebSettings.LOAD_NO_CACHE;
        if(isCacheMode){
            String appCache_path=getWebCacheDir(context);
            File cacheDir=new File(appCache_path);
            if(!cacheDir.exists()&&!cacheDir.isDirectory()) cacheDir.mkdirs();
            //webSettings.setAppCacheEnabled(true);
            //webSettings.setAppCachePath(appCache_path);
        }*/

        //启动硬件加速
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onResume() {
        super.onResume();
        getSettings().setJavaScriptEnabled(true);
    }


    public void releaseAll(){
        stopLoading();
        loadUrl("about:blank");
        clearHistory();
        setWebViewClient(null);
        setWebChromeClient(null);
        removeAllViews();
    }

    @Override
    public boolean canGoBack() {
        WebBackForwardList back_list=copyBackForwardList();
        int currentIndex=back_list.getCurrentIndex()-1;

        if(currentIndex>=0){
            WebHistoryItem item=back_list.getItemAtIndex(currentIndex);
            //Log.i(TAG, "canGoBack: "+item.getUrl());
            if(item.getUrl().equals("about:blank")) return false;
        }
        return super.canGoBack();
    }
}
