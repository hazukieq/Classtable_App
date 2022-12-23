package com.hazukie.scheduleviews.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebHistoryItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.hazukie.scheduleviews.R;

import java.io.File;

public class    CnWebView extends WebView {

    private static final String TAG ="CnWebView" ;

    public CnWebView(Context context) {
        this(context, null);
        init(context);
    }

    public CnWebView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
        init(context);
    }

    public CnWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private String getWebCacheDir(Context context){
        return context.getDir("webache",Context.MODE_PRIVATE).getAbsolutePath();
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void init(Context context) {
        setBackgroundColor(Color.TRANSPARENT);
        setBackgroundResource(R.color.white);

        //隐藏滚动条
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        setNestedScrollingEnabled(false);

        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);

        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setUseWideViewPort(true);


        webSettings.setAllowFileAccess(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setDomStorageEnabled(true);

        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);


        webSettings.setTextZoom(100);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);

        //set web cache strategy
        boolean isDebug=true;
        int loadMode=isDebug?WebSettings.LOAD_NO_CACHE:WebSettings.LOAD_DEFAULT;
        webSettings.setCacheMode(loadMode);
        String appCache_path=getWebCacheDir(context);
        File cacheDir=new File(appCache_path);
        if(!cacheDir.exists()&&!cacheDir.isDirectory()) cacheDir.mkdirs();
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(appCache_path);

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onResume() {
        super.onResume();
        getSettings().setJavaScriptEnabled(true);
    }


    public void releaseAll(){
        removeAllViews();
        stopLoading();
        setWebViewClient(null);
        setWebChromeClient(null);
        loadUrl("about:blank");
        clearHistory();
    }

    @Override
    public boolean canGoBack() {
        WebBackForwardList back_list=copyBackForwardList();
        int currentIndex=back_list.getCurrentIndex()-1;

        if(currentIndex>=0){
            WebHistoryItem item=back_list.getItemAtIndex(currentIndex);
            Log.i(TAG, "canGoBack: "+item.getUrl());
            if(item.getUrl().equals("about:blank")) return false;
        }
        return super.canGoBack();
    }
}
