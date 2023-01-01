package com.hazukie.scheduleviews.net;

import android.content.Context;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.hazukie.jbridge.compiler.JBridgeApt;
import com.hazukie.jbridge.lib.IJBridgeCmd;


@JBridgeApt(cmd="infost")
public class Infost implements IJBridgeCmd {
    @Override
    public void exec(FragmentActivity context, WebView webView, String jsobj) {
        Gson gson=new Gson();
        if(jsobj.length()>0&&jsobj.startsWith("{")&&jsobj.endsWith("}")){
            Sjik st=gson.fromJson(jsobj, Sjik.class);
            Toast.makeText(context, ""+st.messege, Toast.LENGTH_SHORT).show();
        }else Toast.makeText(context, ""+jsobj, Toast.LENGTH_SHORT).show();
    }
}
