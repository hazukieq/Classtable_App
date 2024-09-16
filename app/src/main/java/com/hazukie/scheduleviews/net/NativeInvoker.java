package com.hazukie.scheduleviews.net;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.fragment.app.FragmentActivity;
import com.google.gson.Gson;
import com.hazukie.scheduleviews.iJBridges.JBridgeInvokeDispatcher;
import com.hazukie.scheduleviews.iJBridges.JBridgeObject;
import com.hazukie.scheduleviews.custom.CnWebView;
import com.hazukie.scheduleviews.fileutil.FileRootTypes;
import com.hazukie.scheduleviews.fileutil.Fileystem;
import com.hazukie.scheduleviews.fileutil.NetFileOpts;
import com.hazukie.scheduleviews.utils.DisplayHelper;


public class NativeInvoker {
    private final FragmentActivity context;
    private CnWebView mWebView;

    public NativeInvoker(FragmentActivity context) {
        this.context = context;
    }

    public NativeInvoker(FragmentActivity context, CnWebView mWebView) {
        this(context);
        this.mWebView = mWebView;
    }


    /**
     * 直接返回数据给网页端通信桥
     * @param str 指令
     * @return 数据
     */
    @JavascriptInterface
    public String dipasser(String str,String type){
        String contents="";
        if ("getConfig".equals(str)) {
            NetFileOpts netFileOpts = NetFileOpts.getInstance(context);
            String s = netFileOpts.getPgConfigs(type);
            Gson gson=new Gson();
            PgConfigObj pgConfigObj;
            if(s.isEmpty()) pgConfigObj=PgConfigObj.noneState();
            else pgConfigObj=gson.fromJson(s,PgConfigObj.class);

            contents=gson.toJson(pgConfigObj);
        }

        return contents;
    }

    /**
     * 直接读取文件内容，提高效率
     */
    @JavascriptInterface
    public String direader(String str,String type){
        String contents="";
        Fileystem fileystem=Fileystem.getInstance(context);
        FileRootTypes rootType=type.equals("mind")?FileRootTypes.mind:FileRootTypes.note;
        contents+=fileystem.getDataStr(rootType,str);
        return contents;
    }

    /**
     * JS和Native通讯桥
     * 主要是JS端调用Native的方法
     *
     * @param str raw_json
     */
    @JavascriptInterface
    public void ijbridge(String str) {
        if (str.length() > 0 && str.startsWith("{") && str.endsWith("}")) {
            Gson gson=new Gson();
            JBridgeObject jBridgeObject = gson.fromJson(str, JBridgeObject.class);
            JBridgeInvokeDispatcher.getInstance().sendCmd(context, mWebView, jBridgeObject);
        }
    }

    /**
     *
     * @param jBridgeObject Native执行JS命令
     */
    public static void excecuteJs(FragmentActivity context,WebView webView,Gson gson, JBridgeObject jBridgeObject) {
        String args = gson.toJson(jBridgeObject);
        Log.i("excecuteJs", "args=" + args);
        context.runOnUiThread(() -> {
            try{
                if(webView!=null) webView.loadUrl("javascript:HJBridgeCmdDispatcher().send('" + args + "')");
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}