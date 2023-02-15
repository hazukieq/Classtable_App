package com.hazukie.scheduleviews.net;

import android.webkit.WebView;

import androidx.fragment.app.FragmentActivity;

import com.hazukie.scheduleviews.iJBridges.IJBridgeCmd;
import com.hazukie.scheduleviews.utils.DisplayHelper;

public class Infost implements IJBridgeCmd {

    @Override
    public void exec(FragmentActivity context, WebView webView, String jsobj) {
        DisplayHelper.Infost(context,jsobj);
    }
}
