package com.hazukie.scheduleviews.activity;

import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hazukie.scheduleviews.base.BaseWebActivity;
import com.hazukie.scheduleviews.custom.TopbarLayout;
import com.hazukie.scheduleviews.net.WebStacker;
import com.hazukie.scheduleviews.utils.DisplayHelper;
import com.hazukie.scheduleviews.utils.StatusHelper;

public class ThirdWebLoadAct extends BaseWebActivity {
    @Override
    public void customStatus(TopbarLayout mTopBarLayout) {
        super.customStatus(mTopBarLayout);
        setLightOrDarkStatusBar(StatusHelper.Mode.Status_Dark_Text);
        hiddenStatusBar(false);
    }

    @Override
    protected WebViewClient getWebViewClient() {
        return new ActcomwebClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                boolean isHttp=url.startsWith("http://") || url.startsWith("https://");
                //boolean isHtml=url.endsWith(".html")||url.endsWith(".htm");
                if (isHttp) {
                    return super.shouldOverrideUrlLoading(view, url);
                }
                else if(url.startsWith("pages://")){
                    ThirdWebLoadAct.startActivityWithLoadUrl(ThirdWebLoadAct.this,ThirdWebLoadAct.class,url.replace("pages://","https://"),"","");
                    return true;
                }
                else if(url.startsWith("page://")){
                    ThirdWebLoadAct.startActivityWithLoadUrl(ThirdWebLoadAct.this,ThirdWebLoadAct.class,url.replace("page://","http://"),"","");
                    return true;
                }
                else {
                    DisplayHelper.Infost(ThirdWebLoadAct.this,"暂不支持当前协议");
                    return true;
                }
            }
        };
    }

}
