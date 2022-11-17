package com.hazukie.scheduleviews.fileutil;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.hazukie.scheduleviews.models.ClassLabel;
import com.hazukie.scheduleviews.models.ScheWithTimeModel;
import com.hazukie.scheduleviews.models.TimeHeadModel;
import com.hazukie.scheduleviews.models.TimeModel;
import com.hazukie.scheduleviews.models.Unimodel;
import com.hazukie.scheduleviews.utils.CycleUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileAssist {
    private static final String public_pdf_root_file_name="课表助手";
    private static final String default_sch="空表";
    private static final String default_time="默认作息表";

    private static final String default_sche_index_file_name="index.txt";
    private static final String default_time_index_file_name="time_index.txt";
    
    public static class applyBasicFileOpts{
        private final Context context;
        public applyBasicFileOpts(Context context){
            this.context=context;
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
    
    
    public static class applyOftenOpts{
        private final Context context;
        private Fileystem fileystem;
        public applyOftenOpts(Context context){
            this.context=context;
            if(fileystem==null) fileystem=Fileystem.getInstance(context);
        }

        /*------总表方法------*/
        //获取课表数据
        public List<ClassLabel> getClsList(String name){
            List<ClassLabel> lis=new ArrayList<>();

            List<Object> currentLis=fileystem.getDataList(FileRootTypes.sches,name,ClassLabel.class);
            CycleUtil.cycle(currentLis,(obj, objects) -> lis.add((ClassLabel) obj));
            return lis;
        }

        //获取课表数据
        public List<Object> getRawClsList(String name){
            return fileystem.getDataList(FileRootTypes.sches,name,ClassLabel.class);
        }

        //获取已记录在案课表的所有课表名字
        public List<ScheWithTimeModel> getRecordedScts(){
            List<ScheWithTimeModel> scts=new ArrayList<>();

            List<Object> lis=fileystem.getDataList(FileRootTypes.index,default_sche_index_file_name,ScheWithTimeModel.class);
            CycleUtil.cycle(lis, (obj, objects) -> scts.add((ScheWithTimeModel) obj));

            return scts;
        }

        //获取已记录在案课表的所有课表名字
        public List<Unimodel> getRecordedScheList(){
            List<Unimodel> unis_=new ArrayList<>();

            List<Object> lis=fileystem.getDataList(FileRootTypes.index,default_sche_index_file_name,ScheWithTimeModel.class);
            CycleUtil.cycle(lis, (obj, objects) -> {
                ScheWithTimeModel sct=(ScheWithTimeModel)obj;
                unis_.add(new Unimodel(0,sct.getScheName()));
            });

            return unis_;
        }

        //快速获取索引信息,返回课表名字和时间课表名字，并对相关文件是否存在进行确认
        public ScheWithTimeModel getSctByIndex(int index){
            ScheWithTimeModel sct=new ScheWithTimeModel(0,default_sch,default_time);
            List<Object> scts_=fileystem.getDataList(FileRootTypes.index,default_sche_index_file_name,ScheWithTimeModel.class);
            ScheWithTimeModel sct_=(ScheWithTimeModel)scts_.get(index);
            applyBasicFileOpts applybasicFileOpts=new applyBasicFileOpts(context);

            boolean isAccess=applybasicFileOpts.exist(FileRootTypes.sches,sct_.scheName)&&applybasicFileOpts.exist(FileRootTypes.times,sct_.timeName);
            if(isAccess) sct=sct_;
            return sct;
        }

        //通过索课表名字获取数据,返回课表名字和时间课表名字，并对相关文件是否存在进行确认
        public ScheWithTimeModel getSctByName(String scheName){
            ScheWithTimeModel sct=new ScheWithTimeModel(0,default_sch,default_time);

            List<Object> scts_=fileystem.getDataList(FileRootTypes.index,default_sche_index_file_name,ScheWithTimeModel.class);
            applyBasicFileOpts applybasicFileOpts=new applyBasicFileOpts(context);

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
                isAccess=applybasicFileOpts.exist(FileRootTypes.sches,tc.scheName)&&applybasicFileOpts.exist(FileRootTypes.times,tc.timeName);
            }

            if(isAccess) sct=tc;
            return sct;
        }
        /*-----总表方法结束线------*/


        /*---作息表方法---*/
        public List<TimeModel> getRecordTms(){
            List<TimeModel> tms=new ArrayList<>();

            List<Object> tms_=fileystem.getDataList(FileRootTypes.times,default_time_index_file_name,TimeModel.class);
            CycleUtil.cycle(tms_, (obj, objects) -> tms.add((TimeModel) obj));

            return tms;
        }

        public TimeHeadModel getThm(String time_name){
            Object thm = null;
            thm=fileystem.getData(FileRootTypes.times,time_name,TimeHeadModel.class);

            return thm!=null?(TimeHeadModel) thm:null;
        }

        public Object getRawThm(String time_name){
            return fileystem.getData(FileRootTypes.times,time_name,TimeHeadModel.class);
        }
        /*---作息表方法---*/
    }



    public static class applyWebFileOpts{
        private final Context context;
        public applyWebFileOpts(Context context){
            this.context=context;
        }
        
        /*---- WebView part START----*/
        public String[] getUnderFileLists(FileRootTypes rootMode){
            File f=context.getDir(rootMode.name(), Context.MODE_PRIVATE);
            return f.list();
        }

        public static File getPdfFile(String name){
            File docs= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            File classhelper=new File(docs,public_pdf_root_file_name);
            if(!classhelper.exists()) classhelper.mkdir();
            File file=new File(classhelper,name);
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
    }
    
}
