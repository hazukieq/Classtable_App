package com.hazukie.scheduleviews.models;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.util.List;

public class Timetable {
    public int timeSort;
    public String startTime;
    public String endTime;

    public Timetable(int timeSort,String startTime,String endTime){
        this.startTime=startTime;
        this.endTime=endTime;
        this.timeSort=timeSort;
    }


    //获取原来字符，去除「：」符号
    public String getOriginStr(){
        startTime=startTime.replace(":","");
        endTime=endTime.replace(":","");
        return startTime+"-"+endTime;
    }

    @NonNull
    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        String template="<font color=\"#333333\">%d</font><br/><small><font color=\"gray\">%s<br/>%s</font></small>";
        return  String.format(template,(timeSort+1),startTime,endTime);
    }

    /**
     * 导出时间函数
     * @param startIndex 课程开始时间索引
     * @param clNum 节数
     * @return 返回时间
     */
    public static String exportTimes(List<Timetable> times, int startIndex, int clNum){
        //重新计算开始下标索引和结束下标索引,防止出现下标索引大于作息表索引的情况
        int start_index=startIndex>times.size()?0:startIndex;
        int end_index=startIndex+clNum>times.size()?times.size()-1:startIndex+clNum-1;

        return times.get(start_index).startTime+"-"+times.get(end_index).endTime;
    }
}
