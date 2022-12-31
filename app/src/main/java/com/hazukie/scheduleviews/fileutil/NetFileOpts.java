package com.hazukie.scheduleviews.fileutil;

import android.content.Context;
import android.os.Environment;

import java.io.File;

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

    private File getPublicRoot(FileRootTypes rootTypes){
        File public_docs= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        String public_export_root_file_name="课表助手";
        File classhelper_root=new File(public_docs,public_export_root_file_name);
        if(!classhelper_root.exists()) classhelper_root.mkdir();
        File file=new File(classhelper_root,rootTypes.name());
        if(!file.exists()) file.mkdir();
        return file;
    }
    private File getPublicRoot(String file_name){
        File public_docs= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        String public_export_root_file_name="课表助手";
        File classhelper_root=new File(public_docs,public_export_root_file_name);
        if(!classhelper_root.exists()) classhelper_root.mkdir();
        File file=new File(classhelper_root,file_name);
        if(!file.exists()) file.mkdir();
        return file;
    }

    /*---- WebView part START----*/
    public String[] getUnderFileLists(FileRootTypes rootMode){
        File f=context.getDir(rootMode.name(), Context.MODE_PRIVATE);
        return f.list();
    }


    public String[] getPublicFileList(String name){
        File f=getPublicRoot(name);
        if(f.isDirectory()) return f.list();
        return null;
    }

    public File getPublicFile(FileRootTypes rootTypes,String name){
        File root=getPublicRoot(rootTypes);
        return new File(root,name);
    }

    /*---- WebView part END----*/
}
