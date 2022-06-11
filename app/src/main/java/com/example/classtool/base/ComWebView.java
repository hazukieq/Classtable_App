package com.example.classtool.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIPackageHelper;

public class ComWebView extends WebView {

    public ComWebView(Context context) {
        this(context, null);
    }

    public ComWebView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
    }

    public ComWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void init(Context context) {
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDefaultTextEncodingName("GBK");
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setTextZoom(100);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);


        String screen = QMUIDisplayHelper.getScreenWidth(context) + "x" + QMUIDisplayHelper.getScreenHeight(context);
        String userAgent = "App/" + QMUIPackageHelper.getAppVersion(context)
                + " Android; "+ Build.VERSION.SDK_INT
                + "; Screen/\" + screen + \"; Scale/" + QMUIDisplayHelper.getDensity(context) + ")";
        String agent = getSettings().getUserAgentString();
        if (agent == null || !agent.contains(userAgent)) {
            getSettings().setUserAgentString(agent + " " + userAgent);
        }

    }


}
