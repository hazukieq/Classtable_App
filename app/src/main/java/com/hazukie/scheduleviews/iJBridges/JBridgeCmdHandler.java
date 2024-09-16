package com.hazukie.scheduleviews.iJBridges;

import android.webkit.WebView;

import androidx.fragment.app.FragmentActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JBridgeCmdHandler {
    private Map<String, IJBridgeCmd> mCmdMap;
    public JBridgeCmdHandler(){
        mCmdMap=new HashMap<>();
    }

    private static final class InstanceHolder {
        static final JBridgeCmdHandler instance = new JBridgeCmdHandler();
    }

    public static  synchronized JBridgeCmdHandler getInstance() {
        return InstanceHolder.instance;
    }

    private void putCmd(String cmd, IJBridgeCmd cmethod) {
        if(mCmdMap.isEmpty()) mCmdMap=new HashMap<>();
        if(!mCmdMap.containsKey(cmd)) mCmdMap.put(cmd,cmethod);
    }

    public void registerCmd(String cmd, IJBridgeCmd cmethod){
        putCmd(cmd,cmethod);
    }

    public void handleBridgeInvoke(FragmentActivity context, WebView webView, String cmd, String params){
        if(mCmdMap.containsKey(cmd)) Objects.requireNonNull(mCmdMap.get(cmd)).exec(context,webView,params);
        else System.out.println("Exception: failed to invoke a function exec() of Class#"+cmd+"#, please check if apt was bound to top of Class#"+cmd+"# or not !");
    }
}
