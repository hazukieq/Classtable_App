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

public class QuickMindActivity extends BaseWebActivity {
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
        hiddenStatusBar(true);
    }

    @Override
    protected WebViewClient getWebViewClient() {
        return new ActcomwebClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.startsWith("file:///")||url.startsWith("http://192.168.2.5:8080")){
                    return super.shouldOverrideUrlLoading(view,url);
                }
                else if(url.startsWith("app:")){
                    ThirdWebLoadAct.startActivityWithLoadUrl(QuickMindActivity.this,ThirdWebLoadAct.class,url.replace("app://","file:///android_asset/"),"","");
                    return true;
                }

                else if(url.startsWith("http://")||url.startsWith("https://")){
                    ThirdWebLoadAct.startActivityWithLoadUrl(QuickMindActivity.this,ThirdWebLoadAct.class,url,"","");
                    return true;
                }else{
                    DisplayHelper.Infost(QuickMindActivity.this,"暂不支持当前协议!");
                    return true;
                }
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
