package com.hazukie.scheduleviews.activity;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hazukie.scheduleviews.base.BaseWebActivity;
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
}

