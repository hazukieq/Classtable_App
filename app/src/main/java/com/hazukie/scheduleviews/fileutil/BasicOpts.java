package com.hazukie.scheduleviews.fileutil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BasicOpts {
    private final Context context;

    @SuppressLint("StaticFieldLeak")
    private static BasicOpts instance;

    private BasicOpts(Context context) {
        this.context = context;
    }

    public static BasicOpts getInstance(Context context) {
        if (instance == null) instance = new BasicOpts(context);
        return instance;
    }

    /*---- basic operations of file----*/
    public boolean create(FileRootTypes rootMode, String name) throws IOException {
        File f = context.getDir(rootMode.name(), Context.MODE_PRIVATE);
        File file = new File(f, name);
        if (!file.exists()) return file.createNewFile();
        return true;
    }

    public boolean exist(FileRootTypes root_type, String name) {
        File f = context.getDir(root_type.name(), Context.MODE_PRIVATE);
        File testFille = new File(f, name);
        return testFille.exists();
    }

    public boolean delete(FileRootTypes rootMode, String name) {
        File f = context.getDir(rootMode.name(), Context.MODE_PRIVATE);
        File testFille = new File(f, name);
        boolean isDel = false;
        if (testFille.exists()) isDel = testFille.delete();
        Log.i("FileHelper>>>", "Delete_ " + name + " _status=" + isDel);
        return isDel;
    }

    public boolean rename(FileRootTypes root_type, String oldName, String name) {
        File f = context.getDir(root_type.name(), Context.MODE_PRIVATE);
        File oldFille = new File(f, oldName);
        File newFille = new File(f, name);
        Log.i("FileHelper-rename>>", "oldFile=" + oldFille.getName() + ",newFile=" + newFille.getName());
        return oldFille.renameTo(newFille);
    }

    public void copy(FileRootTypes root_type, File src, String dest) {
        File root = context.getDir(root_type.name(), Context.MODE_PRIVATE);
        File f = new File(root, dest);
        if (src == null || src.isDirectory()) return;
        try {
            if (!f.exists()) f.createNewFile();
            FileWriter writer = new FileWriter(f, false);
            FileReader reader = new FileReader(src);
            int data;
            while ((data = reader.read()) != -1) {
                writer.write((char) data);
            }

            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> list(FileRootTypes root_type,String end_prifix){
        File root = context.getDir(root_type.name(), Context.MODE_PRIVATE);
        List<String> res=new ArrayList<>();
        if(root.exists()){
            String[] strs=root.list();
            if(strs == null) return res;
            for (String str : strs) {
                if (str.endsWith(end_prifix)) res.add(str);
            }
        }
        return res;
    }

    public List<String> list(FileRootTypes root_type){
        File root = context.getDir(root_type.name(), Context.MODE_PRIVATE);
        List<String> res=new ArrayList<>();
        if(root.exists()){
            String[] strs=root.list();
            if(strs == null) return res;
            res.addAll(Arrays.asList(strs));
        }
        return res;
    }
    /*---- basic operations of file END----*/
}
