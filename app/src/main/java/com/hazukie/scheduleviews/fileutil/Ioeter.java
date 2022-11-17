package com.hazukie.scheduleviews.fileutil;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Ioeter {
    Context context;
    private static Ioeter instance;
    public Ioeter(Context context){
        this.context=context;
    }

    public static Ioeter getInstance(Context context) {
        if(instance==null) instance=new Ioeter(context);
        return instance;
    }

    private File checkPath(String file_path){
        String file_root=file_path.split("/")[0];
        File rootfile=context.getDir(file_root,Context.MODE_PRIVATE);
        if(!rootfile.exists()) rootfile.mkdir();
        File file=new File(rootfile,file_path.replace(file_root+"/",""));
        try{
            if(!file.exists()) file.createNewFile();
        }catch (Exception e){
            e.printStackTrace();
        }

        return file;
    }

    public String read(File file){
        StringBuilder stringBuilder=new StringBuilder();
        try{
            FileReader fileReader=new FileReader(file);
            BufferedReader reader=new BufferedReader(fileReader);
            String record;
            while ((record=reader.readLine())!=null){
                stringBuilder.append(record);
            }
            reader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public String read(String file_path){
        File file=checkPath(file_path);
        StringBuilder stringBuilder=new StringBuilder();
        try{
            FileReader fileReader=new FileReader(file);
            BufferedReader reader=new BufferedReader(fileReader);
            String record;
            while ((record=reader.readLine())!=null){
                stringBuilder.append(record);
            }
            reader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    public void write(File file,String content){
        write(file,content,false);
    }

    public void write(File file,String content,boolean isAppendMode){
        try{
            FileWriter fileWriter=new FileWriter(file,isAppendMode);
            BufferedWriter writer=new BufferedWriter(fileWriter);
            writer.write(content);
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void write(String file_path,String content){
        write(file_path,content,false);
    }


    public void write(String file_path,String content,boolean isAppendMode){
        File file=checkPath(file_path);
        try{
            FileWriter fileWriter=new FileWriter(file,isAppendMode);
            BufferedWriter writer=new BufferedWriter(fileWriter);
            writer.write(content);
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
