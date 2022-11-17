package com.hazukie.scheduleviews.models;

public class ScheWithTimeModel {
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
}
