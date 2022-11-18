package com.hazukie.scheduleviews.models;

import android.content.Context;

import com.hazukie.scheduleviews.fileutil.FileAssist;
import com.hazukie.scheduleviews.fileutil.FileRootTypes;
import com.hazukie.scheduleviews.fileutil.Fileystem;

import java.util.ArrayList;
import java.util.List;

public class ScheWithTimeModel {
    private final static String default_index="index.txt";
    public int id;
    public String scheName;
    public String timeName;


    //ID必须是唯一且不重复的UUID
    public ScheWithTimeModel(int id, String scheName, String timeName){
        this.id=id;
        this.scheName=scheName;
        this.timeName=timeName;
    }

    public String getScheName() {
        return scheName.replace(".txt","");
    }

    public String getTimeName() {
        return timeName.replace(".txt","");
    }

    //更新时间字段值
    public void updateTimeName(String timeN){
        this.timeName=timeN;
    }

    //删除该字段名字，即将其值置换为default.txt;
    public void restoreTimeName(){
        this.timeName="默认作息表.txt";
    }

    public static void autoIncrementID(Context context,String scheName,String timeName){
        FileAssist.applyOftenOpts oftenOpts=new FileAssist.applyOftenOpts(context);

        List<ScheWithTimeModel> scts=oftenOpts.getRecordedScts();
        scts.add(new ScheWithTimeModel(scts.size(),scheName+".txt",timeName+".txt"));
        oftenOpts.putRawSctList(scts);
    }

    public static void autoDecrinmentID(Context context,String scheName,String timeName){
        FileAssist.applyOftenOpts oftenOpts=new FileAssist.applyOftenOpts(context);

        List<ScheWithTimeModel> scts=oftenOpts.getRecordedScts();
        List<ScheWithTimeModel> copy_scts=new ArrayList<>(scts);
        for (ScheWithTimeModel sct:copy_scts) {
            if(sct.getScheName().equals(scheName)&&sct.getTimeName().equals(timeName)){
                scts.remove(sct);
                break;
            }
        }

        for (int i = 0; i < scts.size(); i++) {
            ScheWithTimeModel sct=scts.get(i);
            sct.id=i;
        }

        oftenOpts.putRawSctList(scts);
    }
}
