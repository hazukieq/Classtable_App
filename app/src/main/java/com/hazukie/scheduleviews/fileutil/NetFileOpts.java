package com.hazukie.scheduleviews.fileutil;

import android.content.Context;
import android.os.Environment;

import com.hazukie.scheduleviews.statics.Statics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class NetFileOpts {
    private final Context context;
    private static NetFileOpts instance;
    private NetFileOpts(Context context){
        this.context=context;
    }

    public static NetFileOpts getInstance(Context context) {
        if(instance==null) instance=new NetFileOpts(context);
        return instance;
    }

/*    private File getPublicRoot(FileRootTypes rootTypes){
        File public_docs= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        String public_export_root_file_name="课表助手";
        File classhelper_root=new File(public_docs,public_export_root_file_name);
        if(!classhelper_root.exists()) classhelper_root.mkdir();
        File file=new File(classhelper_root,rootTypes.name().equals("mind")?"思维导图":"便签笔记");
        if(!file.exists()) file.mkdir();
        return file;
    }*/
    private File getPublicRoot(String file_name){
        File public_docs= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        String public_export_root_file_name= Statics.public_dir;
        File classhelper_root=new File(public_docs,public_export_root_file_name);
        if(!classhelper_root.exists()) classhelper_root.mkdir();
        File file=new File(classhelper_root,file_name);
        if(!file.exists()) file.mkdir();
        return file;
    }

    /*---- WebView part START----*/
    //获取网页配置
    public String getPgConfigs(String rootMode) {
        String contents;
        try{
            FileRootTypes type=rootMode.equals("mind")?FileRootTypes.mind:FileRootTypes.note;
            BasicOpts basicOpts=BasicOpts.getInstance(context);
            basicOpts.create(type,".configs");

            File root=context.getDir(type.name(), Context.MODE_PRIVATE);
            File f=new File(root,".configs");

            FileReader in=new FileReader(f);
            BufferedReader reader=new BufferedReader(in);
            String line;
            StringBuilder stringBuilder=new StringBuilder();
            while ((line=reader.readLine())!=null){
                stringBuilder.append(line);
            }

            contents=stringBuilder.toString();
        }catch (Exception e){
            contents="";
            e.printStackTrace();
        }

        return contents;
    }


    public String[] getUnderFileLists(FileRootTypes rootMode){
        File f=context.getDir(rootMode.name(), Context.MODE_PRIVATE);
        return f.list();
    }

    public String[] getUnderFileLists(String rootMode){
        File f=context.getDir(rootMode, Context.MODE_PRIVATE);
        return f.list();
    }

    public String[] getPublicFileList(String name){
        File f=getPublicRoot(name);
        if(f.isDirectory()) return f.list();
        return null;
    }

/*    public File getPublicFile(FileRootTypes rootTypes,String name){
        File root=getPublicRoot(rootTypes);
        return new File(root,name);
    }*/

    public File getPublicFile(String rootTypes,String name){
        File root=getPublicRoot(rootTypes);
        return new File(root,name);
    }

    public File getPublicDir(String dirName){
        return getPublicRoot(dirName);
    }

    /*---- WebView part END----*/
}
