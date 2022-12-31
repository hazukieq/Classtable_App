package com.hazukie.scheduleviews.fileutil;

import android.content.Context;

import com.hazukie.scheduleviews.models.ClassLabel;
import com.hazukie.scheduleviews.models.ScheWithTimeModel;
import com.hazukie.scheduleviews.models.TimeHeadModel;
import com.hazukie.scheduleviews.models.TimeModel;
import com.hazukie.scheduleviews.models.Unimodel;
import com.hazukie.scheduleviews.utils.CycleUtil;

import java.util.ArrayList;
import java.util.List;

public class OftenOpts extends Fileystem {
    private final Context context;
    //private Fileystem fileystem;
    private static final String default_sch="空表";
    private static final String default_time="默认作息表";

    private static final String default_sche_index_file_name="index.txt";
    private static final String default_time_index_file_name="time_index.txt";

    public OftenOpts(Context context){
        super(context);
        this.context=context;
        //if(fileystem==null) fileystem=Fileystem.getInstance(context);
    }

    /*------总表方法------*/
    //获取课表数据
    public List<ClassLabel> getClsList(String name){
        List<ClassLabel> lis=new ArrayList<>();
        List<Object> currentLis=getDataList(FileRootTypes.sches,name,ClassLabel.class);

        if(currentLis.size()>0&&currentLis.get(0)!=null)
            CycleUtil.cycle(currentLis,(obj, objects) -> lis.add((ClassLabel) obj));
        return lis;
    }

    //获取课表数据
    public List<Object> getRawClsList(String name){
        return getDataList(FileRootTypes.sches,name,ClassLabel.class);
    }

    public boolean putRawClsList(String file_name,List<ClassLabel> classLabels){
        if(classLabels.size()>0&&classLabels.get(0)!=null)
            return putDatazList(FileRootTypes.sches,file_name,new ArrayList<>(classLabels));
        else return false;
    }

    //写入索引数据
    public void putRawSctList(List<ScheWithTimeModel> scts){

        putDataList(FileRootTypes.index,default_sche_index_file_name,new ArrayList<>(scts));
    }

    //写入数据
    public boolean putRawSctzList(List<ScheWithTimeModel> scts){
        return putDatazList(FileRootTypes.index,default_sche_index_file_name,new ArrayList<>(scts));
    }

    public void putRawSct(ScheWithTimeModel sct){
        if(sct!=null) putData(FileRootTypes.index,default_sche_index_file_name,sct);
    }

    //获取已记录在案课表的所有课表名字
    public List<ScheWithTimeModel> getRecordedScts(){
        List<ScheWithTimeModel> scts=new ArrayList<>();

        List<Object> lis=getDataList(FileRootTypes.index,default_sche_index_file_name,ScheWithTimeModel.class);
        if(lis.size()>0)
            CycleUtil.cycle(lis, (obj, objects) -> scts.add((ScheWithTimeModel) obj));

        return scts;
    }

    //获取已记录在案课表的所有课表名字
    public List<Unimodel> getRecordedScheList(){
        List<Unimodel> unis_=new ArrayList<>();

        List<Object> lis=getDataList(FileRootTypes.index,default_sche_index_file_name,ScheWithTimeModel.class);
        if(lis.size()>0&&lis.get(0)!=null){
            CycleUtil.cycle(lis, (obj, objects) -> {
                ScheWithTimeModel sct=(ScheWithTimeModel)obj;
                unis_.add(new Unimodel(0,sct.getScheName()));
            });
        }
        return unis_;
    }

    //快速获取索引信息,返回课表名字和时间课表名字，并对相关文件是否存在进行确认
    public ScheWithTimeModel getSctByIndex(int index){
        ScheWithTimeModel sct=new ScheWithTimeModel(0,default_sch,default_time);
        List<Object> scts_=getDataList(FileRootTypes.index,default_sche_index_file_name,ScheWithTimeModel.class);
        if(scts_.size()>0&&scts_.get(0)!=null){
            ScheWithTimeModel sct_=(ScheWithTimeModel)scts_.get(index);
            BasicOpts basicOpts=BasicOpts.getInstance(context);
            boolean isAccess=basicOpts.exist(FileRootTypes.sches,sct_.scheName)&&basicOpts.exist(FileRootTypes.times,sct_.timeName);
            if(isAccess) sct=sct_;
        }
        return sct;
    }

    //通过索课表名字获取数据,返回课表名字和时间课表名字，并对相关文件是否存在进行确认
    public ScheWithTimeModel getSctByName(String scheName) {
        ScheWithTimeModel sct = new ScheWithTimeModel(0, default_sch, default_time);

        List<Object> scts_ = getDataList(FileRootTypes.index, default_sche_index_file_name, ScheWithTimeModel.class);
        BasicOpts basicOpts = BasicOpts.getInstance(context);

        ScheWithTimeModel tc = null;
        if(scts_.size()>0){
            for(Object obj:scts_){
                ScheWithTimeModel ct=(ScheWithTimeModel) obj;
                if(scheName.equals(ct.getScheName())){
                    tc=ct;
                    break;
                }
            }

            boolean isAccess=false;
            if(tc!=null){
                isAccess= basicOpts.exist(FileRootTypes.sches,tc.scheName)&&basicOpts.exist(FileRootTypes.times,tc.timeName);
            }

            if(isAccess) sct=tc;
        }
        return sct;
    }
    /*-----总表方法结束线------*/


    /*---作息表方法---*/
    public List<TimeModel> getRecordTms(){
        List<TimeModel> tms=new ArrayList<>();

        List<Object> tms_=getDataList(FileRootTypes.times,default_time_index_file_name,TimeModel.class);
        if(tms_.size()>0&&tms_.get(0)!=null)
            CycleUtil.cycle(tms_, (obj, objects) -> tms.add((TimeModel) obj));

        return tms;
    }

    public TimeHeadModel getThm(String time_name){
        Object thm_=getData(FileRootTypes.times,time_name,TimeHeadModel.class);
        if(thm_ instanceof String) return null;
        else {
            try{
                TimeHeadModel thm=(TimeHeadModel) thm_;
                thm=null;
            }catch (Exception e){
                thm_=null;
                e.printStackTrace();
            }
            return thm_!=null?(TimeHeadModel)thm_:null;
        }
    }

    public Object getRawThm(String time_name){
        return getData(FileRootTypes.times,time_name,TimeHeadModel.class);
    }

    /*        **
     * 写入一个文件
     * @param file_name 文件名
     * @param thm 数据
     *//*
        public void putRawThm(String file_name,TimeHeadModel thm){
            fileystem.putData(FileRootTypes.times,file_name,thm);
        }*/

    /*---作息表方法---*/
/*
    *//*-----课表作息表文件删除和重命名、索引表的添加和删除 START-----*//*
    public boolean checkSctDocIsDuplicate(String oldName,String neoName){
        List<ScheWithTimeModel> scts=getRecordedScts();
        boolean isDuplicate=false;

        if(oldName.equals(neoName)) return true;
        if(scts.size()>0&&scts.get(0)!=null){
            for (ScheWithTimeModel sct:scts) {
                if(sct.getScheName().equals(neoName)){
                    isDuplicate=true;
                    break;
                }
            }
        }

        return isDuplicate;
    }

    public boolean checkThmDocIsDuplicate(String oldName,String neoName){
        List<TimeModel> thms=getRecordTms();
        boolean isDuplicate=false;
        if(thms.size()>0&&thms.get(0)!=null){
            if(oldName.equals(neoName)) return true;

            for(TimeModel thm:thms){
                if(thm.getTimeName().equals(neoName)){
                    isDuplicate=true;
                    break;
                }
            }
        }

        return isDuplicate;
    }*/



    /*-----课表作息表文件删除和重命名、索引表的添加和删除 END-----*/

}
