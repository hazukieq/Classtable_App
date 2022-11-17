package com.hazukie.scheduleviews.utils;

import android.util.Log;

import com.hazukie.scheduleviews.statics.Statics;

import java.util.Calendar;
import java.util.Date;

public class DateHelper{
    private final Calendar instance;
    private int startWeekth,endWeekth;
    private String gapStr="";
    public DateHelper(){
        instance=Calendar.getInstance();
        instance.setFirstDayOfWeek(Calendar.MONDAY);
        instance.setMinimalDaysInFirstWeek(1);
    }

    public DateHelper(Builder builder){
        this();
        startWeekth=getStartWeek(builder.start_year, builder.start_mont, builder.start_dait);
        endWeekth=getEndWeek(startWeekth, builder.totalNum);
        gapStr=getWeekGap(startWeekth,getCurrentWeek(),builder.totalNum);
    }

    //获取开始日期所属周目
    private int getStartWeek(int jear,int mont,int dait){
        instance.set(Calendar.YEAR,jear);
        instance.set(Calendar.DAY_OF_MONTH,mont-1,dait);
        //如果当前日期属于星期六、星期日，则视为此周已过去，则开学周目数需要+1以修正实际周目数
        int currentWeekOfDait=instance.get(Calendar.DAY_OF_WEEK);
        boolean isNotNeedPlusOne=currentWeekOfDait<5;
        Log.i( "getStartWeek>>","currentWeekOfDait="+currentWeekOfDait);

        if(isNotNeedPlusOne) return instance.get(Calendar.WEEK_OF_YEAR);
        return instance.get(Calendar.WEEK_OF_YEAR)+1;
    }

    //获取当前时间属于全年中的第几周
    private int getCurrentWeek(){
        instance.setTimeInMillis(System.currentTimeMillis());
        return instance.get(Calendar.WEEK_OF_YEAR);
    }


    //获取结束日期所属周目
    private int getEndWeek(int startWeekth,int totalNum){
        return startWeekth+totalNum;
    }

    //开始周目和当前周目之差
    private String getWeekGap(int startWeek,int currentWeek,int total){
        boolean isOver=startWeek>currentWeek;
        //因为一般开学日期当周目也包含其中，故需要+1以修正周目数
        int gap=isOver?startWeek-currentWeek+1:currentWeek-startWeek+1;
        if(isOver) return "第"+gap+"(总周数"+total+")周";
        return "第"+gap+"周";
    }

    public String getCurrentWeekStr(){
        instance.setTimeInMillis(System.currentTimeMillis());
        return "第"+instance.get(Calendar.WEEK_OF_YEAR)+"周";
    }

    public int getStartWeekth() {
        return startWeekth;
    }

    public int getEndWeekth() {
        return endWeekth;
    }

    public String getGapStr() {
        return gapStr;
    }

    //获取当前周的所有日期
    public String[] getCurrentWeekDays(){
        instance.setTimeInMillis(System.currentTimeMillis());
        instance.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        String temp="<font color=\"gray\"><small>%1$s日</font></small>";
        String[] allweeks=new String[8];
        allweeks[0]="";
        allweeks[1]=String.format(temp,instance.get(Calendar.DAY_OF_MONTH));

        for (int i = 2;i<allweeks.length; i++) {
            instance.add(Calendar.DAY_OF_YEAR, 1);
            allweeks[i]=String.format(temp, instance.get(Calendar.DAY_OF_MONTH));
        }
        return allweeks;
    }

    public String[] returnWeekDayByDaitSetz(){
        String[] daits=getCurrentWeekDays();
        String[] neodayz=new String[daits.length];
        for (int i = 0; i < daits.length; i++) {
            neodayz[i]= Statics.getWeekByIndex(i)+"<br/>"+daits[i];
        }
        return neodayz;
    }


    public int getCurrentMont(){
        instance.setTimeInMillis(System.currentTimeMillis());
        return instance.get(Calendar.MONTH)+1;
    }
    public String getDai(){
        instance.setTimeInMillis(System.currentTimeMillis());
        return instance.get(Calendar.MONTH)+1+"月"+instance.get(Calendar.DAY_OF_MONTH)+"日";
    }
    public static class Builder{
        private int start_dait=1;
        private int start_mont=9;
        private int start_year=2022;
        private int totalNum=18;

        public Builder setStartDate(int mont,int dait){
            this.start_mont=mont;
            this.start_dait=dait;
            return this;
        }

        public Builder setTotalNum(int totalNum){
            this.totalNum=totalNum;
            return this;
        }

        public DateHelper create(){
            Calendar calendar=Calendar.getInstance();
            this.start_year= calendar.get(Calendar.YEAR);
            return new DateHelper(this);
        }
    }
}