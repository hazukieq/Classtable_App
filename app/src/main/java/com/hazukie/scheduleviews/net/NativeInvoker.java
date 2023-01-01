package com.hazukie.scheduleviews.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.fragment.app.FragmentActivity;
import com.google.gson.Gson;
import com.hazukie.jbridge.lib.JBridgeInvokeDispatcher;
import com.hazukie.jbridge.lib.JBridgeObject;
import com.hazukie.scheduleviews.custom.CnWebView;

public class NativeInvoker {
    //private FileHelper fileHelper;
    private final Gson gson;
    private final FragmentActivity context;
    private CnWebView mWebView;

    public NativeInvoker(FragmentActivity context) {
        this.context = context;
        gson = new Gson();
    }

    public NativeInvoker(FragmentActivity context, CnWebView mWebView) {
        this(context);
        this.mWebView = mWebView;
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
            Log.i("ijbridge>> ", "datas=" + str);
            JBridgeObject jBridgeObject = gson.fromJson(str, JBridgeObject.class);
            JBridgeInvokeDispatcher.getInstance().sendCmd(context, mWebView, jBridgeObject);
        }
    }

    //Native执行JS命令
    public void excecuteJs(JBridgeObject jBridgeObject) {
        context.runOnUiThread(() -> {
            String args = gson.toJson(jBridgeObject);
            Log.i("excecuteJs", "args=" + args);
            if(mWebView!=null) mWebView.loadUrl("javascript:HJBridgeCmdDispatcher().send('" + args + "')");
        });
    }

    //Native执行JS命令
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


    /*---即将废弃部分--*
    @JavascriptInterface
    public void down(String base64) {
        //这里收到下载解析的base64、做相关处理
        try {
            Bitmap bitmap = Base64Util.base64ToPicture(base64);
            Base64Util.savePictureToAlbum(context, bitmap, mind_name + "_思维导图");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void down2pdf(String base64) {
        try {
            Bitmap bit = Base64Util.base64ToPicture(base64);
            Base64Util.PngToPdf(context, bit, mind_name + "_思维导图");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void getname(String name) {
        mind_name = name;
    }

}

/*---即将废弃部分---*//*




        @JavascriptInterface
        public String invokeFileNameList(int type){
            Log.i( "web-readlist>>","type="+type);
            FileHelper.RootMode rootMode=type==0? FileHelper.RootMode.note: FileHelper.RootMode.mind;
            String[] lists=fileHelper.getUnderFileLists(rootMode);
            String lists_=gson.toJson(lists);
            Log.i( "readlist>>","file_lists_json="+lists_);
            return "{\"naimes\":"+lists_+"}";
        }

        @JavascriptInterface
        public String getFileDatas(String file_name){
            String[] parsed_file_nam=file_name.split("/");
            FileHelper.RootMode rootMode=parsed_file_nam[0].equals("note")? FileHelper.RootMode.note: FileHelper.RootMode.mind;
            String datas="";
            try{
                Object obj=fileHelper.readObj(rootMode,parsed_file_nam[1], Unimodel.class);
                Unimodel uni=(Unimodel) obj;
                datas=gson.toJson(uni.title);
                Log.i( "readDatas>>","file_contents="+datas);
            }catch (Exception e){
                e.printStackTrace();
            }
            return "{\"content\":"+datas+"}";
        }

        @JavascriptInterface
        public void putFileDatas(String file_name,String content){
            String[] parsed_file_nam=file_name.split("/");
            FileHelper.RootMode rootMode=parsed_file_nam[0].equals("note")? FileHelper.RootMode.note: FileHelper.RootMode.mind;
            try{
                fileHelper.writeJsons(rootMode,file_name,content,false);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    @JavascriptInterface
    public String deleteFile(String file_name){
            String[] parsed_file_nam=file_name.split("/");
            FileHelper.RootMode rootMode=parsed_file_nam[0].equals("note")? FileHelper.RootMode.note: FileHelper.RootMode.mind;
            boolean bs=fileHelper.delete(rootMode,parsed_file_nam[1]);
            return String.valueOf(bs);
    }

    @JavascriptInterface
    public String renameFile(String file_name,String oldName,String neoName){
        String[] parsed_file_nam=file_name.split("/");
        FileHelper.RootMode rootMode=parsed_file_nam[0].equals("note")? FileHelper.RootMode.note: FileHelper.RootMode.mind;
        boolean bs=fileHelper.rename(rootMode,oldName,neoName);
        return String.valueOf(bs);
    }


        */
/*<!--即将废弃部分-->*//*

        @JavascriptInterface
        public String readlist(String type){
            Log.i( "web-readlist>>","type="+type);
            FileHelper.RootMode rootMode=type.equals("mind")? FileHelper.RootMode.mind: FileHelper.RootMode.note;
            String[] lists=fileHelper.getUnderFileLists(rootMode);
            String lists_=gson.toJson(lists);

            Log.i( "readlist>>","file_lists_json="+lists_);
            return "{\"naimes\":"+lists_+"}";
        }

        @JavascriptInterface
        public String readfile(String type,String naime){
            Log.i("web_readfile>>","naime="+naime);
            String datas="{\"id\":0,\"title\":\"\"}";
            try{
                FileHelper.RootMode rootMode=type.equals("mind")? FileHelper.RootMode.mind: FileHelper.RootMode.note;
                Object obj=fileHelper.readObj(rootMode,naime, Unimodel.class);
                Unimodel uni=(Unimodel) obj;
                datas=gson.toJson(uni);
            }catch (Exception e){
                e.printStackTrace();
            }
            return datas;
        }


        @JavascriptInterface
        public void deliloh(String type,String id,String name){
            Crialoghue crialoghue=new Crialoghue.TxtBuilder()
                    .addTitle("删除文件")
                    .addContent("是否删除该文件？")
                    .onConfirm((crialoghue1, view) -> {
                        try{
                            FileHelper.RootMode rootMode=type.equals("mind")? FileHelper.RootMode.mind: FileHelper.RootMode.note;
                            boolean bs=fileHelper.delete(rootMode,name);
                            Log.i("web-rename>>","deleteIs="+bs);
                            context.runOnUiThread(()->mWebView.loadUrl("javascript:removeC('"+id+"')"));
                            crialoghue1.dismiss();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }).build(context);
            crialoghue.show();
        }

        @JavascriptInterface
        public void saviloh(String type,String name,String contents){
            Crialoghue crialoghue=new Crialoghue.LeditBuilder()
                    .addTitle("保存数据")
                    .addLeftContent("名称")
                    .addContents(name.replace(".md",""))
                    .onConfirm((crialoghue1, view) -> {
                        EditText edit=(EditText) view;
                        String en=edit.getText().toString();
                        if(en.length()>0){
                            FileHelper.RootMode rootMode=type.equals("mind")? FileHelper.RootMode.mind: FileHelper.RootMode.note;
                            try{
                                fileHelper.writeJsons(rootMode,en+".md",contents,false);
                                DisplayHelper.Infost(context,"数据保存成功！");
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            crialoghue1.dismiss();
                        }else{
                            DisplayHelper.Infost(context,"文件名不能为空！");
                        }
                    }).build(context);
            crialoghue.show();
        }

        @JavascriptInterface
        public void redialoh(String type,String Dcid,String oldName){
            Crialoghue crialoghue=new Crialoghue.HeditBuilder()
                    .addTitle("编辑文件名")
                    .addContents("请输入新的文件名")
                    .onConfirm((crialoghue1, view) -> {
                        EditText edit=(EditText) view;
                        String neo=edit.getText().toString();
                        try{
                            boolean isDup=false;
                            FileHelper.RootMode rootMode=type.equals("mind")? FileHelper.RootMode.mind: FileHelper.RootMode.note;
                            String[] list=fileHelper.getUnderFileLists(rootMode);
                            for(String lis:list){
                                if(lis.equals(neo+".md")){
                                    isDup=true;
                                    break;
                                }
                            }

                            if((neo+".md").equals(oldName)){
                                crialoghue1.dismiss();
                            }
                            else if(!isDup&&neo.length()>0){
                                context.runOnUiThread(() -> mWebView.loadUrl("javascript:inextC('"+Dcid+"','"+neo+".md')"));
                                boolean bs=fileHelper.rename(rootMode,oldName,neo+".md");
                                Log.i("web-rename>>","renameIs="+bs);
                                crialoghue1.dismiss();
                            }else{
                                DisplayHelper.Infost(context,"文件名称已存在，请重新输入！");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }).build(context);

            crialoghue.show();
        }
    */
/*<!--即将废弃部分-->*/
