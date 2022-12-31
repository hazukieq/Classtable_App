package com.hazukie.scheduleviews.fileutil;

import android.annotation.SuppressLint;
import android.content.Context;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Fileystem {
    private final Context context;

    @SuppressLint("StaticFieldLeak")
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


    private void createRootFile(String name){
        File f=context.getDir(name,Context.MODE_PRIVATE);
        if(!f.exists()) f.mkdir();
    }

    private File createRoot(String name){
        File f=context.getDir(name,Context.MODE_PRIVATE);
        if(!f.exists()) f.mkdir();
        return f;
    }

    private void createFile(String root,String name){
        File f=new File(createRoot(root),name);
        try{
            if(!f.exists()) f.createNewFile();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void initial(){
        for(FileRootTypes fileRootType:FileRootTypes.values()){
            createRootFile(fileRootType.name());
        }

        //作息表索引
        createFile(FileRootTypes.times.name(),"time_index.txt");

        //总表索引
        createFile(FileRootTypes.index.name(), "index.txt");

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

    private File selectFile(FileRootTypes rootPathType,String path){
        return getDefaultRootPath(rootPathType,path);
    }

    private String selectFile2Str(FileRootTypes rootPathType,String path){
        return getDefaultRootPath(rootPathType,path).getAbsolutePath();
    }


    public List<Object> getDataList(FileRootTypes rootPathType,String file_name, Type type){
        List<Object> newlis=new ArrayList<>();
        File currentFile=selectFile(rootPathType,file_name);
        String rawData= ioeter.read(currentFile);
        if(rawData.length()!=0) {
            String[] rawData_splitN=rawData.split("\n");
            boolean isStartup=rawData_splitN.length>0;
            if(isStartup){
                for (String s : rawData_splitN) {
                    if(s==null||s.isEmpty()){}
                    else newlis.add(converter.convertJsn2Obj(s,type));
                }
            }
        }
        return newlis;
    }

    public Object getData(FileRootTypes rootPathType,String file_name, Type type){
        File currentFile=selectFile(rootPathType,file_name);
        String rawData= ioeter.read(currentFile);
        return converter.convertJsn2Obj(rawData,type);
    }

    public String getDataStr(FileRootTypes rootPathType,String file_name){
        File currentFile=selectFile(rootPathType,file_name);
        return ioeter.read(currentFile);
    }

    public void putDataList(FileRootTypes rootPathType,String file_name,List<Object> objs){
        File currentFile=selectFile(rootPathType,file_name);
        StringBuilder stringBuilder=new StringBuilder();
        if(objs.size()>0){
            for (Object obj:objs) {
                String str= converter.convertObj2Jsn(obj)+"\n";
                stringBuilder.append(str);
            }
        }else{
            stringBuilder.append(" ");
        }
        ioeter.write(currentFile,stringBuilder.toString());
    }

    public void putData(FileRootTypes rootPathType,String file_name,Object obj){
        File currentFile=selectFile(rootPathType,file_name);
        String content="";
        if(obj!=null) content=converter.convertObj2Jsn(obj);
        ioeter.write(currentFile,content);
    }




    public boolean putDatazList(FileRootTypes rootPathType,String file_name,List<Object> objs){
        File currentFile=selectFile(rootPathType,file_name);
        StringBuilder stringBuilder=new StringBuilder();
        if(objs.size()>0){
            for (Object obj:objs) {
                String str= converter.convertObj2Jsn(obj)+"\n";
                stringBuilder.append(str);
            }
        }else{
            stringBuilder.append(" ");
        }
        return ioeter.writeObj(currentFile,stringBuilder.toString());
    }

    public boolean putDataz(FileRootTypes rootPathType,String file_name,Object obj){
        File currentFile=selectFile(rootPathType,file_name);
        String content="";
        if(obj!=null) content=converter.convertObj2Jsn(obj);
        return ioeter.writeObj(currentFile,content);
    }

    public boolean putDataz(FileRootTypes rootPathType,String file_name,Object obj,boolean isAppend){
        File currentFile=selectFile(rootPathType,file_name);
        String content="";
        if(obj!=null) content=converter.convertObj2Jsn(obj);
        return ioeter.writeObj(currentFile,content,isAppend);
    }



    public void putDataStr(FileRootTypes rootPathType,String file_name,String strs){
        File currentFile=selectFile(rootPathType,file_name);
        ioeter.write(currentFile,strs);
    }

    public void putDataStr(File file,String strs){
        ioeter.write(file,strs);
    }
}
