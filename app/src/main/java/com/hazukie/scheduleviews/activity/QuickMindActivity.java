package com.hazukie.scheduleviews.activity;

import android.annotation.SuppressLint;
import android.view.KeyEvent;

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(mWebView.canGoBack()) mWebView.goBack();
            else DisplayHelper.showBack(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
