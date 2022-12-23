package com.hazukie.scheduleviews.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hazukie.jbridge.compiler.JBridgeApt;
import com.hazukie.jbridge.lib.IJBridgeCmd;

@JBridgeApt(cmd="vert2Png")
public class Vert2Png implements IJBridgeCmd{

    private static final String TAG = "vert2Png";

    @Override
    public void exec(Context context, WebView webView, String raw_json) {
        Gson gson=new Gson();
        SvgObj obj=gson.fromJson(raw_json,SvgObj.class);
        Log.i(TAG, "exec: name="+obj.name+",base64="+obj.base64);
        //这里收到下载解析的base64、做相关处理
        try{
            Bitmap bitmap = Base64Util.base64ToPicture(obj.base64);
            Base64Util.savePictureToAlbum(context, bitmap,obj.name+"_思维导图");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

