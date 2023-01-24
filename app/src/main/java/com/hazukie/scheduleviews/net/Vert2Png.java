package com.hazukie.scheduleviews.net;


import android.graphics.Bitmap;
import android.webkit.WebView;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.hazukie.scheduleviews.iJBridges.IJBridgeCmd;


public class Vert2Png implements IJBridgeCmd {
    @Override
    public void exec(FragmentActivity context, WebView webView, String raw_json) {
        Gson gson=new Gson();
        SvgObj obj=gson.fromJson(raw_json,SvgObj.class);
        //这里收到下载解析的base64、做相关处理
        try{
            Bitmap bitmap = Base64Util.base64ToPicture(obj.base64);
            Base64Util.savePictureToAlbum(context, bitmap,obj.name);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

