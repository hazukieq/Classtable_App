package com.hazukie.scheduleviews.fileutil;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class WebFileOpts {
    private final Context context;
    public WebFileOpts(Context context){
        this.context=context;
    }

    private File getPublicRoot(FileRootTypes rootTypes){
        File public_docs= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        String public_pdf_root_file_name="课表助手";
        File classhelper_root=new File(public_docs,public_pdf_root_file_name);
        if(!classhelper_root.exists()) classhelper_root.mkdir();
        File file=new File(classhelper_root,rootTypes.name());
        if(!file.exists()) file.mkdir();
        return file;
    }

    /*---- WebView part START----*/
    public String[] getUnderFileLists(FileRootTypes rootMode){
        File f=context.getDir(rootMode.name(), Context.MODE_PRIVATE);
        return f.list();
    }

    public File getPublicFile(FileRootTypes rootTypes,String name){
        File root=getPublicRoot(rootTypes);
        File file=new File(root,name);
        return file;
    }

    public  String[] getPublicLists(FileRootTypes rootTypes){
        File file=getPublicRoot(rootTypes);
        return file.list();
    }
    /*---- WebView part END----*/
}
