package com.hazukie.scheduleviews.net;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.hazukie.jbridge.compiler.JBridgeApt;
import com.hazukie.jbridge.lib.IJBridgeCmd;
import com.hazukie.jbridge.lib.JBridgeObject;
import com.hazukie.scheduleviews.fileutil.BasicOpts;
import com.hazukie.scheduleviews.fileutil.FileRootTypes;
import com.hazukie.scheduleviews.fileutil.Fileystem;
import com.hazukie.scheduleviews.fileutil.NetFileOpts;
import com.hazukie.scheduleviews.fileutil.OftenOpts;

import java.util.Arrays;

@JBridgeApt(cmd = "fileInvoker")
public class FileIvoker implements IJBridgeCmd {

    @Override
    public void exec(FragmentActivity context, WebView webView, String jsons) {
        Gson gson=new Gson();
        NetFileOpts netFileOpts=NetFileOpts.getInstance(context);
        MsgPasser msgPasser= gson.fromJson(jsons,MsgPasser.class);
        if(msgPasser!=null&&msgPasser.args.size()==3){
            FileRootTypes type=msgPasser.args.get(0).equals("mind")?FileRootTypes.mind:FileRootTypes.note;
            String filename=msgPasser.args.get(1)!=null?msgPasser.args.get(1):"";
            String content=msgPasser.args.get(2)!=null?msgPasser.args.get(2):"";

            switch (msgPasser.cmd){
                case "askFlist":
                    String[] lists=netFileOpts.getUnderFileLists(type);
                    Log.i(TAG, "exec: lists="+ Arrays.toString(lists));
                    NativeInvoker.excecuteJs(context,webView,new Gson(),new JBridgeObject("getData",Arrays.toString(lists)));
                    break;
                case "delData":
                    BasicOpts.getInstance(context).delete(type,filename);
                    break;
                case "putData":
                    OftenOpts.getInstance(context).putDataStr(type,filename,content);
                    break;
                case "askData":
                    String s= Fileystem.getInstance(context).getDataStr(type,filename);
                    Log.i(TAG, "exec: read_data="+s);
                    NativeInvoker.excecuteJs(context,webView,new Gson(),new JBridgeObject("getData",s));
                    break;
                default:
                    break;
            }
        }
    }

}

