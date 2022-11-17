package com.hazukie.scheduleviews.activity;

import android.annotation.SuppressLint;
import android.webkit.JavascriptInterface;

import com.hazukie.scheduleviews.custom.CnWebView;
import com.hazukie.scheduleviews.custom.TopbarLayout;
import com.hazukie.scheduleviews.utils.StatusHelper;

public class QuickMindActivity extends LocalWebActivity{
    public QuickMindActivity(){
    }

    @Override
    public void customStatus(TopbarLayout mTopBarLayout) {
        super.customStatus(mTopBarLayout);
        setLightOrDarkStatusBar(StatusHelper.Mode.Status_Dark_Text);
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void configWebView(CnWebView webView) {
        super.configWebView(webView);
        //隐藏滚动条

        hiddenStatusBar(false);
    }
}
