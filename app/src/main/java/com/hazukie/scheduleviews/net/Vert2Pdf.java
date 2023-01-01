package com.hazukie.scheduleviews.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.WebView;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.hazukie.jbridge.compiler.JBridgeApt;
import com.hazukie.jbridge.lib.IJBridgeCmd;

@JBridgeApt(cmd="vert2Pdf")
public class Vert2Pdf implements IJBridgeCmd {
    @Override
    public void exec(FragmentActivity context, WebView webView, String raw_json) {
        Gson gson=new Gson();
        SvgObj obj=gson.fromJson(raw_json,SvgObj.class);
        try{
            Bitmap bit=Base64Util.base64ToPicture(obj.base64);
            Base64Util.PngToPdf(context,bit,obj.name+"_思维导图");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
