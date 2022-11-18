package com.hazukie.scheduleviews.net;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.hazukie.cskheui.Crialoghue.Clicks;
import com.hazukie.cskheui.Crialoghue.Crialoghue;
import com.hazukie.scheduleviews.custom.CnWebView;
import com.hazukie.scheduleviews.models.Unimodel;
import com.hazukie.scheduleviews.utils.DisplayHelper;
import com.hazukie.scheduleviews.utils.FileHelper;

public class NativeInvoker {
        private FileHelper fileHelper;
        private Gson gson;
        private FragmentActivity context;
        private CnWebView mWebView;
        private String mind_name="";

        public NativeInvoker(FragmentActivity context){
            this.context=context;
            fileHelper=new FileHelper(context);
            gson=new Gson();
        }

        public NativeInvoker(FragmentActivity context, CnWebView mWebView){
            this(context);
            this.mWebView=mWebView;
        }
        

        @JavascriptInterface
        public void down(String base64) {
            //这里收到下载解析的base64、做相关处理
            try{
                Bitmap bitmap = Base64Util.base64ToPicture(base64);
                Base64Util.savePictureToAlbum(context, bitmap,mind_name+"_思维导图");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        @JavascriptInterface
        public void down2pdf(String base64){
            try{
                Bitmap bit=Base64Util.base64ToPicture(base64);
                Base64Util.PngToPdf(context,bit,mind_name+"_思维导图");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

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
        public String sendDatas(String file_name,int type){
            FileHelper.RootMode rootMode=type==0? FileHelper.RootMode.note: FileHelper.RootMode.mind;

            String datas="";
            try{
                Object obj=fileHelper.readObj(rootMode,file_name, Unimodel.class);
                Unimodel uni=(Unimodel) obj;
                datas=gson.toJson(uni.title);
                Log.i( "readDatas>>","file_contents="+datas);
            }catch (Exception e){
                e.printStackTrace();
            }
            return "{\"content\":"+datas+"}";
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
        public void getname(String name){
            mind_name=name;
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
/*        @JavascriptInterface
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
                    .addLeftContent("文件名称")
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
        }*/
}


