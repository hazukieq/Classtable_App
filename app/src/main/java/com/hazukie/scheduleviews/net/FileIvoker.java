package com.hazukie.scheduleviews.net;

import android.webkit.WebView;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.hazukie.scheduleviews.iJBridges.IJBridgeCmd;
import com.hazukie.scheduleviews.fileutil.FileRootTypes;
import com.hazukie.scheduleviews.fileutil.OftenOpts;
import com.hazukie.scheduleviews.utils.DisplayHelper;

public class FileIvoker implements IJBridgeCmd {

    @Override
    public void exec(FragmentActivity context, WebView webView, String jsons) {
        Gson gson=new Gson();
        //NetFileOpts netFileOpts=NetFileOpts.getInstance(context);
        MsgPasser msgPasser= gson.fromJson(jsons,MsgPasser.class);
        if(msgPasser!=null&&msgPasser.args.size()==3){
            FileRootTypes type=msgPasser.args.get(0).equals("mind")?FileRootTypes.mind:FileRootTypes.note;
            String filename=msgPasser.args.get(1)!=null?msgPasser.args.get(1):"";
            String content=msgPasser.args.get(2)!=null?msgPasser.args.get(2):"";

            switch (msgPasser.cmd){
                //获取当前网页配置信息
                case "putConfigData":
                    OftenOpts.getInstance(context).putDataStr(type,filename,content);
                    break;

                //对文件进行写入操作
                case "putData":
                    OftenOpts.getInstance(context).putDataStr(type,filename,content);
                    DisplayHelper.Infost(context,"数据已保存");
                    break;
                default:
                    break;

                /*
                //列出某个文件夹下所有文件
                case "askFlist":
                    String[] lists=netFileOpts.getUnderFileLists(type);
                    Log.i(TAG, "exec: lists="+ Arrays.toString(lists));
                    NativeInvoker.excecuteJs(context,webView,new Gson(),new JBridgeObject("getConfig",Arrays.toString(lists)));
                    break;

                //删除某个文件
                case "delData":
                    BasicOpts.getInstance(context).delete(type,filename);

                    break;
                //获取某个文件内容
                case "askData":
                    String s= Fileystem.getInstance(context).getDataStr(type,filename);
                    Log.i(TAG, "exec: read_data="+s);
                    NativeInvoker.excecuteJs(context,webView,new Gson(),new JBridgeObject("getData",s));
                    break;*/
            }
        }
    }

}

