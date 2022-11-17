package com.hazukie.scheduleviews.activity;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hazukie.scheduleviews.custom.TopbarLayout;
import com.hazukie.scheduleviews.utils.StatusHelper;

public class QuickNoteActivity extends LocalWebActivity{

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
                return false;
            }
        };
    }
}

