package com.hazukie.scheduleviews.utils;

import android.content.Context;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.gson.Gson;
import com.hazukie.scheduleviews.models.ScheWithTimeModel;
import com.hazukie.scheduleviews.models.TimeHeadModel;
import com.hazukie.scheduleviews.models.TimeModel;
import com.hazukie.scheduleviews.models.Unimodel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.List;

public class FileHelper {
    private final Context context;
    private static FileHelper instance;
    private static final String sche_root_path="sches";
    private static final String times_root_path="times";
    private static final String index_root_path="index";
    private static final String mind_root_path="minds";
    private static final String note_root_path="notes";
    private static final String default_sch="空表",default_time="默认作息表";
    private static final String default_sche_index_file_name="index.txt",default_time_index_file_name="time_index.txt";
    private static final String public_pdf_root_file_name="课表助手";

    public FileHelper(Context context){
        this.context=context;
    }
    public enum RootMode{
        sches,times,index,mind,note
    }

    public void init() throws IOException {
        File sche_root=context.getDir(sche_root_path,Context.MODE_PRIVATE);
        File time_root=context.getDir(times_root_path,Context.MODE_PRIVATE);
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

        if(!sche_record.exists()) sche_record.createNewFile();
        if(!time_record.exists()) time_record.createNewFile();
        if(!index_record.exists()) index_record.createNewFile();
    }


    /**
     *
     * @param context 请使用ApplicationContext进行操作！！
     * @return 文件读写工具本身
     */
    public static FileHelper getInstance(Context context) {
        if(instance==null) instance=new FileHelper(context);
        return instance;
    }

    private File getDefaultRootPath(RootMode rootMode){
        return context.getDir(rootMode.name(),Context.MODE_PRIVATE);
    }


    /**
     *
     * @param rootMode shces为sches根目录，time为time根目录
     * @param file_name 文件名
     * @param data 拟写入数据
     * @return 是否写入布尔值
     */
    public boolean  write(RootMode rootMode,String file_name, List<Object> data) throws IOException {
        File file=new File(getDefaultRootPath(rootMode),file_name);
        if(!file.exists()) file.createNewFile();

        FileWriter out=new FileWriter(file);
        BufferedWriter writer=new BufferedWriter(out);

        List<String> jsons=convert2Json(data);
        if(jsons.size()!=0){
            for(String json: jsons) writer.write(json+"\n");
            writer.close();
            return true;
        }

       return false;
    }


    /**
     *
     * @param rootMode shces为sches根目录，time为time根目录
     * @param file_name 文件名
     * @param data 拟写入数据
     * @param isAppend  是否开启追加模式
     * @return 是否写入布尔值
     */
    public boolean  write(RootMode rootMode,String file_name, List<Object> data, boolean isAppend) throws IOException {
        File file=new File(getDefaultRootPath(rootMode),file_name);
        if(!file.exists()) file.createNewFile();

        FileWriter out=new FileWriter(file,isAppend);
        BufferedWriter writer=new BufferedWriter(out);

        List<String> jsons=convert2Json(data);
        if(jsons.size()!=0){
            for(String json: jsons) writer.write(json+"\n");
            writer.close();
            return true;
        }

        return false;
    }

    /**
     *
     * @param rootMode shces为sches根目录，time为time根目录
     * @param file_name 文件名
     * @param data 拟写入数据
     * @param isAppend  是否开启追加模式
     * @return 是否写入布尔值
     */
    public boolean  write(RootMode rootMode,String file_name,Object data, boolean isAppend) throws IOException {
        File f=context.getDir(rootMode.name(),Context.MODE_PRIVATE);
        File file=new File(getDefaultRootPath(rootMode),file_name);
        if(!file.exists()) file.createNewFile();

        FileWriter out=new FileWriter(file,isAppend);
        BufferedWriter writer=new BufferedWriter(out);

        String convetedStr=convertObj2Json(data);
        if(convetedStr.length()>0){
            writer.write(convetedStr+"\n");
            writer.close();
            return true;
        }
        return false;
    }

    /**
     *
     * @param rootMode 0为sches根目录，1为time根目录
     * @param path_name 文件名字
     * @return 返回读取数据
     */
    public List<Object> read(RootMode rootMode,String path_name,Type clzz_type) throws IOException {
        File file=new File(getDefaultRootPath(rootMode),path_name);
        //如果文件不存在，则创建一个
        if(!file.exists()) file.createNewFile();

        FileReader in=new FileReader(file);
        BufferedReader reader=new BufferedReader(in);

        List<Object > newlis=new ArrayList<>();
        String line;
        while ((line=reader.readLine())!=null) newlis.add(covertJson2Object(line,clzz_type));
        reader.close();

        return newlis;
    }



    /**
     *
     * @param rootMode 0为sches根目录，1为time根目录
     * @param path_name 文件名字
     * @return 返回读取数据
     */
    public Object readObj(RootMode rootMode,String path_name,Type clzz_type) throws IOException {
        File file=new File(getDefaultRootPath(rootMode),path_name);
        //如果文件不存在，则创建一个
        if(!file.exists()) file.createNewFile();

        FileReader in=new FileReader(file);
        BufferedReader reader=new BufferedReader(in);

        Object obj = null;
        String line;
        while ((line=reader.readLine())!=null) obj=covertJson2Object(line,clzz_type);
        reader.close();

        return obj;
    }


    /*---- WebView part ----*/
    /**
     * @param rootMode shces为sches根目录，time为time根目录
     * @param file_name 文件名
     * @param datas 拟写入数据
     * @param isAppend  是否开启追加模式
     * @return 是否写入布尔值
     */
    public boolean  writeJsons(RootMode rootMode,String file_name, String datas, boolean isAppend) throws IOException {
        File file=new File(getDefaultRootPath(rootMode),file_name);
        if(!file.exists()) file.createNewFile();

        FileWriter out=new FileWriter(file,isAppend);
        BufferedWriter writer=new BufferedWriter(out);
        if(datas.length()!=0) {
            writer.write(datas);
            writer.close();
            return true;
        }

        return false;
    }

    public String[] getUnderFileLists(RootMode rootMode){
        File f=context.getDir(rootMode.name(),Context.MODE_PRIVATE);
        return f.list();
    }

    public static File getPdfFile(String name){
        File docs=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File classhelper=new File(docs,public_pdf_root_file_name);
        if(!classhelper.exists()) classhelper.mkdir();
        File pdf_root=new File(classhelper,"思维导图");
        if(!pdf_root.exists()) pdf_root.mkdir();
        File file=new File(pdf_root,name);
        return file;
    }

    public static File getPublicFile(String name){
        File docs=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        String public_txt_root_file_name="课表导出文件";
        File classhelper=new File(docs,public_pdf_root_file_name);
        if(!classhelper.exists()) classhelper.mkdir();
        File sche_root=new File(classhelper,"课表导出");
        if(!sche_root.exists()) sche_root.mkdir();
        File file=new File(sche_root,name);
        return file;
    }


    public static  String[] getPdfLists(){
        File docs=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File classhelper=new File(docs,public_pdf_root_file_name);
        if(!classhelper.exists()){
            boolean is=classhelper.mkdir();
            Log.i("getPdfLists>>","list has mkdir="+is);
        }

        return classhelper.list();
    }
    /*---- WebView part END----*/


    /*---- Convert Objects tool----*/
    public List<String> convert2Json(List<Object> objectList){
        List<String> stringList=new ArrayList<>();
        Gson gson=new Gson();
        if(objectList.size()!=0){
            for(Object obj:objectList) stringList.add(gson.toJson(obj));
        }
        return stringList;
    }


    public String convertObj2Json(Object obj){
        String s="";
        Gson gson=new Gson();
        if(obj!=null) s=gson.toJson(obj);
        return s;
    }

    public Object covertJson2Object(String content,Type type){
        Gson gson=new Gson();
        return gson.fromJson(content,type);
    }

    /*---- Convert Objects tool END----*/


    /*---- basic operations of file----*/
    public boolean create(RootMode rootMode,String name) throws IOException {
        File f=context.getDir(rootMode.name(),Context.MODE_PRIVATE);
        File file=new File(f,name);
        if(!file.exists()) return file.createNewFile();
        return true;
    }

    public boolean exist(RootMode root_type,String name){
        File f=context.getDir(root_type.name(),Context.MODE_PRIVATE);
        File testFille=new File(f,name);
        return testFille.exists();
    }

    public boolean delete(RootMode rootMode,String name){
        File f=context.getDir(rootMode.name(),Context.MODE_PRIVATE);
        File testFille=new File(f,name);
        boolean isDel=false;
        if(testFille.exists()) isDel=testFille.delete();
        Log.i( "FileHelper>>>","Delete_ "+name+" _status="+isDel);
        return isDel;
    }

    public boolean rename(RootMode root_type,String oldName,String name){
        File f=context.getDir(root_type.name(),Context.MODE_PRIVATE);
        File oldFille=new File(f,oldName);
        File newFille=new File(f,name);
        Log.i("FileHelper-rename>>","oldFile="+oldFille.getName()+",newFile="+newFille.getName());
        return oldFille.renameTo(newFille);
    }
    /*---- basic operations of file END----*/



    /*------总表方法------*/
    //获取已记录在案课表的所有课表名字
    public static List<ScheWithTimeModel> getRecordedScts(Context context){
        List<ScheWithTimeModel> scts=new ArrayList<>();
        try {
            FileHelper fileHelper=FileHelper.getInstance(context);
            List<Object> scts_=fileHelper.read(FileHelper.RootMode.index,default_sche_index_file_name,ScheWithTimeModel.class);
            CycleUtil.cycle(scts_, (obj, objects) -> scts.add((ScheWithTimeModel) obj));
        }catch (Exception e){
            e.printStackTrace();
        }
        return scts;
    }

    //获取已记录在案课表的所有课表名字
    public static List<Unimodel> getRecordedScheList(Context context){
        List<Unimodel> unis_=new ArrayList<>();
        try {
            FileHelper fileHelper=FileHelper.getInstance(context);
            List<Object> scts_=fileHelper.read(FileHelper.RootMode.index,default_sche_index_file_name,ScheWithTimeModel.class);
            CycleUtil.cycle(scts_, (obj, objects) -> {
                ScheWithTimeModel sct=(ScheWithTimeModel)obj;
                unis_.add(new Unimodel(0,sct.getScheName()));
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return unis_;
    }

    //快速获取索引信息,返回课表名字和时间课表名字，并对相关文件是否存在进行确认
    public static ScheWithTimeModel getSctByIndex(Context context,int index){
        ScheWithTimeModel sct=new ScheWithTimeModel(0,default_sch,default_time);
        try {
            FileHelper fileHelper = FileHelper.getInstance(context);
            List<Object> scts_ = fileHelper.read(FileHelper.RootMode.index,default_sche_index_file_name, ScheWithTimeModel.class);
            ScheWithTimeModel sct_=(ScheWithTimeModel)scts_.get(index);
            boolean isAccess=fileHelper.exist(FileHelper.RootMode.sches,sct_.scheName)&&fileHelper.exist(FileHelper.RootMode.times,sct_.timeName);
            if(isAccess) sct=sct_;
        }catch (Exception e){
            e.printStackTrace();
        }
        return sct;
    }

    //通过索课表名字获取数据,返回课表名字和时间课表名字，并对相关文件是否存在进行确认
    public static ScheWithTimeModel getSctByName(Context context,String scheName){
        ScheWithTimeModel sct=new ScheWithTimeModel(0,default_sch,default_time);
        try {
            FileHelper fileHelper = FileHelper.getInstance(context);
            List<Object> scts_ = fileHelper.read(FileHelper.RootMode.index,default_sche_index_file_name, ScheWithTimeModel.class);

            ScheWithTimeModel tc=null;
            for(Object obj:scts_){
                ScheWithTimeModel ct=(ScheWithTimeModel) obj;
                if(scheName.equals(ct.getScheName())){
                    tc=ct;
                    break;
                }
            }

            boolean isAccess=false;
            if(tc!=null){
                isAccess=fileHelper.exist(FileHelper.RootMode.sches,tc.scheName)&&fileHelper.exist(FileHelper.RootMode.times,tc.timeName);
            }

            if(isAccess) sct=tc;

        }catch (Exception e){
            e.printStackTrace();
        }
        return sct;
    }
    /*-----总表方法结束线------*/



    /*-----课表方法------*/
/*    public static List<ScheModel> getRecordScms(Context context){
        List<ScheModel> scms=new ArrayList<>();
        String loadFile_index="sche_index.txt";
        try {
            FileHelper fileHelper=FileHelper.getInstance(context);
            List<Object> scms_=fileHelper.read(RootMode.sches,loadFile_index,ScheModel.class);
            CycleUtil.cycle(scms_, (obj, objects) -> scms.add((ScheModel) obj));
        }catch (Exception e){
            e.printStackTrace();
        }
        return scms;
    }*/

    /*---作息表方法---*/
    public static List<TimeModel> getRecordTms(Context context){
        List<TimeModel> tms=new ArrayList<>();
        try {
            FileHelper fileHelper=FileHelper.getInstance(context);
            List<Object> tms_=fileHelper.read(FileHelper.RootMode.times,default_time_index_file_name,TimeModel.class);
            CycleUtil.cycle(tms_, (obj, objects) -> tms.add((TimeModel) obj));
        }catch (Exception e){
            e.printStackTrace();
        }
        return tms;
    }

    public static TimeHeadModel getThm(Context context,String time_name){
        FileHelper fileHelper=FileHelper.getInstance(context);
        Object thm = null;
        try{
            thm=fileHelper.readObj(RootMode.times,time_name,TimeHeadModel.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return thm!=null?(TimeHeadModel) thm:null;
    }
    /*---作息表方法---*/


}
