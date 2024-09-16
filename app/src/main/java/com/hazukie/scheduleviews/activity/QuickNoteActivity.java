package com.hazukie.scheduleviews.activity;

import android.annotation.SuppressLint;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hazukie.scheduleviews.base.BaseWebActivity;
import com.hazukie.scheduleviews.custom.CnWebView;
import com.hazukie.scheduleviews.custom.TopbarLayout;
import com.hazukie.scheduleviews.utils.DisplayHelper;
import com.hazukie.scheduleviews.utils.StatusHelper;

public class QuickNoteActivity extends BaseWebActivity {

    @Override
    public void customStatus(TopbarLayout mTopBarLayout) {
        super.customStatus(mTopBarLayout);
        setLightOrDarkStatusBar(StatusHelper.Mode.Status_Dark_Text);
        hiddenStatusBar(true);
    }


    @SuppressLint("JavascriptInterface")
    @Override
    protected void configWebView(CnWebView webView) {
        super.configWebView(webView);
    }

    @Override
    protected WebViewClient getWebViewClient() {
        return new ActcomwebClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.startsWith("file:///")){
                    return super.shouldOverrideUrlLoading(view,url);
                }
                else if(url.startsWith("http://")||url.startsWith("https://")){
                    ThirdWebLoadAct.startActivityWithLoadUrl(QuickNoteActivity.this,ThirdWebLoadAct.class,url,"","");
                    return true;
                }else{
                    DisplayHelper.Infost(QuickNoteActivity.this,"暂不支持当前协议!");
                    return true;
                }
                //return false;//super.shouldOverrideUrlLoading(view, url);
            }
        };
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(mWebView.canGoBack()) mWebView.goBack();
            else DisplayHelper.showBack(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

