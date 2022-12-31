package com.hazukie.scheduleviews.fileutil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class BasicOpts  {
    private final Context context;

    @SuppressLint("StaticFieldLeak")
    private static BasicOpts instance;

    private BasicOpts(Context context){
        this.context=context;
    }

    public static BasicOpts getInstance(Context context) {
        if(instance==null) instance=new BasicOpts(context);
        return instance;
    }

    /*---- basic operations of file----*/
    public boolean create(FileRootTypes rootMode, String name) throws IOException {
        File f=context.getDir(rootMode.name(),Context.MODE_PRIVATE);
        File file=new File(f,name);
        if(!file.exists()) return file.createNewFile();
        return true;
    }

    public boolean exist(FileRootTypes root_type, String name){
        File f=context.getDir(root_type.name(),Context.MODE_PRIVATE);
        File testFille=new File(f,name);
        return testFille.exists();
    }

    public boolean delete(FileRootTypes rootMode, String name){
        File f=context.getDir(rootMode.name(),Context.MODE_PRIVATE);
        File testFille=new File(f,name);
        boolean isDel=false;
        if(testFille.exists()) isDel=testFille.delete();
        Log.i( "FileHelper>>>","Delete_ "+name+" _status="+isDel);
        return isDel;
    }

    public boolean rename(FileRootTypes root_type, String oldName, String name){
        File f=context.getDir(root_type.name(),Context.MODE_PRIVATE);
        File oldFille=new File(f,oldName);
        File newFille=new File(f,name);
        Log.i("FileHelper-rename>>","oldFile="+oldFille.getName()+",newFile="+newFille.getName());
        return oldFille.renameTo(newFille);
    }
    /*---- basic operations of file END----*/
}
