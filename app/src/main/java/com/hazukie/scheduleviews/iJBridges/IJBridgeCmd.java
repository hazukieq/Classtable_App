package com.hazukie.scheduleviews.iJBridges;


import android.webkit.WebView;

import androidx.fragment.app.FragmentActivity;

public interface IJBridgeCmd {
    void exec(FragmentActivity context, WebView webView, String jsobj);
}
