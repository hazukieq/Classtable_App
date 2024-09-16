package com.hazukie.scheduleviews.net;

import android.webkit.WebView;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.hazukie.scheduleviews.iJBridges.IJBridgeCmd;
import com.hazukie.scheduleviews.fileutil.FileRootTypes;
import com.hazukie.scheduleviews.fileutil.OftenOpts;

public class FileIvoker implements IJBridgeCmd {

    @Override
    public void exec(FragmentActivity context, WebView webView, String jsons) {
        Gson gson=new Gson();
        MsgPasser msgPasser= gson.fromJson(jsons,MsgPasser.class);
        if(msgPasser!=null&&msgPasser.args.size()==3){
            FileRootTypes type=msgPasser.args.get(0).equals("mind")?FileRootTypes.mind:FileRootTypes.note;
            String filename=msgPasser.args.get(1)!=null?msgPasser.args.get(1):"";
            String content=msgPasser.args.get(2)!=null?msgPasser.args.get(2):"";

            if(msgPasser.cmd.equals("putData")){
                OftenOpts.getInstance(context).putDataStr(type,filename,content);
            }
        }
    }

}

