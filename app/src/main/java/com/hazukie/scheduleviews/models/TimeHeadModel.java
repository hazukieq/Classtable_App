package com.hazukie.scheduleviews.models;
import java.util.List;

public class TimeHeadModel {
    public int amCl;
    public int amStart;
    public List<Timetable> detailClass;
    public int  mmCl;
    public int  mmStart;
    public int  pmCl;
    public int pmStart;
    public String timeName;
    public int totalClass;

    public enum Lperiod{
        am,pm,mm
    }
    //如果下午、晚上字段不存在的话，则将其值置为0，以表示该字段不存在！！！
    public TimeHeadModel(String timeName,int totalClass,int amStart,int amCl,int pmStart,int pmCl,int mmStart,int mmCl,List<Timetable> detailClass){
        this.timeName=timeName;
        this.totalClass=totalClass;
        this.amStart=amStart;
        this.amCl=amCl;
        this.pmStart=pmStart;
        this.pmCl=pmCl;
        this.mmStart=mmStart;
        this.mmCl=mmCl;
        this.detailClass=detailClass;
    }

    public void updateTimeList(List<Timetable> tbs){
        this.detailClass=tbs;
    }

    public void restoreWholePeriod(Lperiod lperiod){
        switch (lperiod){
            case am:
                this.amCl=0;
                this.amStart=0;
                break;
            case pm:
                this.pmCl=0;
                this.pmStart=0;
                break;
            case mm:
                this.mmCl=0;
                this.mmStart=0;
                break;
        }
    }

    public String outputBasics(){
        String basics_temp="文件名：<b>%s</b><br/><br/>%s<br/>总节数：%d";
        String details="";
        if (amCl!=0) details+="上午："+amCl+"节<br/>";
        if(pmCl!=0) details+="下午："+pmCl+"节<br/>";
        if(mmCl!=0) details+="晚上："+mmCl+"节<br/>";

    return String.format(basics_temp,timeName,details,totalClass);//timeName
    }
}