package com.hazukie.scheduleviews.fileutil;

import android.content.Context;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Fileystem {
    private final String sche_root_path="sche";
    private final String time_root_path="time";
    private final String index_root_path="index";
    private final String note_root_path="note";
    private final String mind_root_path="mind";



    private Context context;
    private static Fileystem instance;
    private Converter converter;
    private Ioeter ioeter;

    public Fileystem(Context context){
        this.context=context;
        if(converter==null) converter=new Converter();
        if(ioeter==null) ioeter=Ioeter.getInstance(context);
    }

    public static Fileystem getInstance(Context context) {
        if(instance==null) instance=new Fileystem(context);
        return instance;
    }


    private void initial(){
        File sche_root=context.getDir(sche_root_path,Context.MODE_PRIVATE);
        File time_root=context.getDir(time_root_path,Context.MODE_PRIVATE);
        File index_root=context.getDir(index_root_path,Context.MODE_PRIVATE);

        File mind_root=context.getDir(mind_root_path,Context.MODE_PRIVATE);
        File note_root=context.getDir(note_root_path,Context.MODE_PRIVATE);

        if(!sche_root.exists()) sche_root.mkdir();
        if(!time_root.exists()) time_root.mkdir();
        if(!index_root.exists()) index_root.mkdir();

        if(!mind_root.exists()) mind_root.mkdir();
        if(!note_root.exists()) note_root.mkdir();

        //课表索引
        File sche_record=new File(sche_root,"sche_index.txt");
        //作息表索引
        File time_record=new File(time_root,"time_index.txt");
        //总表索引
        File index_record=new File(index_root,"index.txt");

        try{
            if(!sche_record.exists()) sche_record.createNewFile();
            if(!time_record.exists()) time_record.createNewFile();
            if(!index_record.exists()) index_record.createNewFile();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private File getDefaultRootPath(FileRootTypes rootPathType,String path){
        File root=context.getDir(rootPathType.name(), Context.MODE_PRIVATE);
        File file=new File(root,path);
        try{
            if(!file.exists()) file.createNewFile();
        }catch (Exception e){
            e.printStackTrace();
        }
        return file;
    }

    public File  selectFile(FileRootTypes rootPathType,String path){
        return getDefaultRootPath(rootPathType,path);
    }
    public String selectFile2Str(FileRootTypes rootPathType,String path){
        return getDefaultRootPath(rootPathType,path).getAbsolutePath();
    }


    public List<Object> getDataList(FileRootTypes rootPathType,String file_name, Type type){
        List<Object> newlis=new ArrayList<>();
        File currentFile=selectFile(rootPathType,file_name);
        String rawData= ioeter.read(currentFile);
        String[] rawData_splitN=rawData.split("\n");
        boolean isStartup=rawData_splitN.length>0;
        if(isStartup){
            for (String s : rawData_splitN) {
                newlis.add(converter.convertObj2Jsn(s));
            }
        }
        return newlis;
    }

    public Object getData(FileRootTypes rootPathType,String file_name, Type type){
        File currentFile=selectFile(rootPathType,file_name);
        String rawData= ioeter.read(currentFile);
        return converter.convertObj2Jsn(rawData);
    }

    public String getDataStr(FileRootTypes rootPathType,String file_name, Type type){
        File currentFile=selectFile(rootPathType,file_name);
        return ioeter.read(currentFile);
    }

    public void putData(FileRootTypes rootPathType,String file_name,Object obj){
        File currentFile=selectFile(rootPathType,file_name);
        String content=converter.convertObj2Jsn(obj);
        ioeter.write(currentFile,content);
    }

    public void putDataStr(FileRootTypes rootPathType,String file_name,String strs){
        File currentFile=selectFile(rootPathType,file_name);
        ioeter.write(currentFile,strs);
    }
}
