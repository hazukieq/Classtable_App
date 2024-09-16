package com.hazukie.scheduleviews.fileutil;

import android.annotation.SuppressLint;
import android.content.Context;

import com.hazukie.scheduleviews.models.ClassLabel;
import com.hazukie.scheduleviews.models.ScheWithTimeModel;
import com.hazukie.scheduleviews.models.TimeHeadModel;
import com.hazukie.scheduleviews.models.TimeModel;
import com.hazukie.scheduleviews.models.Unimodel;
import com.hazukie.scheduleviews.statics.Statics;
import com.hazukie.scheduleviews.utils.CycleUtil;

import java.util.ArrayList;
import java.util.List;

public class OftenOpts extends Fileystem {
    private final Context context;

    private static final String default_sch="空表";
    private static final String default_time="默认作息表";

    private static final String default_sche_index_file_name="index.txt";
    private static final String default_time_index_file_name="time_index.txt";

    @SuppressLint("StaticFieldLeak")
    private static OftenOpts instance;
    private OftenOpts(Context context){
        super(context);
        this.context=context;
    }

   public static OftenOpts getInstance(Context context) {
        if(instance==null) instance=new OftenOpts(context);
        return instance;
    }

    /*------总表方法------*/
    //获取课表数据
    public List<ClassLabel> getClsList(String name){
        List<ClassLabel> lis=new ArrayList<>();
        List<ClassLabel> currentLis=getDataList(FileRootTypes.sches,name,ClassLabel.class);
        if(currentLis.size()>0&&currentLis.get(0)!=null) return currentLis;
        return lis;
    }

    public boolean putRawClsList(String file_name,List<ClassLabel> classLabels){
        boolean check=true;
        try{
            if(classLabels.size()>0&&classLabels.get(0)!=null)
                putDataList(FileRootTypes.sches,file_name,new ArrayList<>(classLabels));
        }catch (Exception e){
            check=false;
            e.printStackTrace();
        }
        return check;
    }

    //写入索引数据
    public void putRawSctList(List<ScheWithTimeModel> scts){
        putDataList(FileRootTypes.index,default_sche_index_file_name,new ArrayList<>(scts));
    }

    //写入数据
    public boolean putRawSctzList(List<ScheWithTimeModel> scts){
        boolean check=true;
        try{
            putDataList(FileRootTypes.index,default_sche_index_file_name,new ArrayList<>(scts));
        }catch (Exception e){
            check=false;
            e.printStackTrace();
        }
        return check;
    }

    public void putRawSct(ScheWithTimeModel sct){
        if(sct!=null) putData(FileRootTypes.index,default_sche_index_file_name,sct);
    }

    //获取已记录在案课表的所有课表名字
    public List<ScheWithTimeModel> getRecordedScts(){
        List<ScheWithTimeModel> scts=new ArrayList<>();
        List<ScheWithTimeModel> lis=getDataList(FileRootTypes.index,default_sche_index_file_name,ScheWithTimeModel.class);
        if(lis.size()>0&&lis.get(0)!=null) return lis;
        return scts;
    }

    //获取已记录在案课表的所有课表名字
    public List<Unimodel> getRecordedScheList(){
        List<Unimodel> unis_=new ArrayList<>();
        List<ScheWithTimeModel> lis=getDataList(FileRootTypes.index,default_sche_index_file_name,ScheWithTimeModel.class);
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
        List<ScheWithTimeModel> scts_=getDataList(FileRootTypes.index,default_sche_index_file_name,ScheWithTimeModel.class);
        if(scts_.size()>0&&scts_.get(0)!=null){
            ScheWithTimeModel sct_=scts_.get(index);
            BasicOpts basicOpts=BasicOpts.getInstance(context);
            boolean isAccess=basicOpts.exist(FileRootTypes.sches,sct_.scheName)&&basicOpts.exist(FileRootTypes.times,sct_.timeName);
            if(isAccess) sct=sct_;
        }
        return sct;
    }

    //通过索课表名字获取数据,返回课表名字和时间课表名字，并对相关文件是否存在进行确认
    public ScheWithTimeModel getSctByName(String scheName) {
        ScheWithTimeModel sct = new ScheWithTimeModel(0, default_sch, default_time);
        List<ScheWithTimeModel> scts_ = getDataList(FileRootTypes.index, default_sche_index_file_name, ScheWithTimeModel.class);

        ScheWithTimeModel tc = null;
        if(scts_.size()>0&&scts_.get(0)!=null) {
            for (ScheWithTimeModel ct : scts_) {
                if (scheName.equals(ct.getScheName())) {
                    tc = ct;
                    break;
                }
            }
        }
        BasicOpts basicOpts = BasicOpts.getInstance(context);
        boolean isAccess=false;
        if(tc!=null){
            isAccess= basicOpts.exist(FileRootTypes.sches,tc.scheName)&&basicOpts.exist(FileRootTypes.times,tc.timeName);
        }

        if(isAccess) sct=tc;
        return sct;
    }
    /*-----总表方法结束线------*/


    /*---作息表方法---*/
    public List<TimeModel> getRecordTms(){
        List<TimeModel> tms = new ArrayList<>();
        List<TimeModel> tms_=getDataList(FileRootTypes.times,default_time_index_file_name,TimeModel.class);
        if(tms_.size()>0&&tms_.get(0)!=null) return tms_;
        return tms;
    }

    /**
     *
     * @param time_name String
     * @return Absolutely guarantee that ret will not be null object!!!
     */
    public TimeHeadModel getThm(String time_name){
        TimeHeadModel thm_=getData(FileRootTypes.times,time_name,TimeHeadModel.class);
        if(thm_ == null) return getThm(default_time+".txt");
        return thm_;
    }


    public void putThms(List<TimeModel> thms){
        if(thms==null||thms.size()==0) return;
        putDataList(FileRootTypes.times,"time_index.txt",new ArrayList<>(thms));
    }
}
