package com.hazukie.scheduleviews.models;

import androidx.annotation.NonNull;

import com.hazukie.scheduleviews.statics.Statics;
import com.hazukie.scheduleviews.utils.ColorSeletor;

import java.io.Serializable;
import java.util.Objects;

public class ClassLabel implements Serializable {
    public int clNums,startCl,week,color,detailTime;
    public String subjectName,plaNote,clRoom;
    private int startTag=0,endTag=1;
    private String customTime="";

    /**
     * @param class_nums 总课时
     * @param start_class 第一节课，从0开始计数
     * @param week 周，[0,1,2,3,4,5,6]表示一二三四五六七
     * @param detail_time 详细时间 [0,1,2]分别表示朝昼夜
     * @param subject 科目
     *
     * @param planotes 备注，限制50个字，超出则省略
     * @param color 颜色
     */
    public ClassLabel(int  class_nums,int start_class,int week,int detail_time,String subject,String clRoom,String planotes,int color){
        this.clNums=class_nums;
        this.startCl=start_class;
        this.week=week;
        this.detailTime=detail_time;
        this.subjectName=subject;
        this.plaNote=planotes;
        this.color=color;
        this.clRoom=clRoom;
    }

    public String addClNums() {
        return clNums+"节";
    }

    public String addStartCl() {
        return "第"+(startCl+1)+"节课";
    }

    public String addWeek() {
        return Statics.getWeekBySort(week);
    }

    public String addDetailTime() {
        return Statics.getDetailTimeByIndex(detailTime);
    }

    public String addColorStr() {
        return ColorSeletor.getColorStringByIndex(color);
    }

    public String addClRoom(){
        if(clRoom.isEmpty()) return "";
        return "@"+clRoom;
    }

    public void updateValues(int  class_nums, int start_class, int week, int detail_time, String subject, String clRoom, String planotes, int color){
        this.clNums=class_nums;
        this.startCl=start_class;
        this.week=week;
        this.detailTime=detail_time;
        this.subjectName=subject;
        this.plaNote=planotes;
        this.color=color;
        this.clRoom=clRoom;
    }

    public String toHtml(){
        String template="<font color=\"#495050\">%s</font><br/><small><font color=\"#C51162\">@%s</font><br/><br/>%s</small>";

        if(subjectName.length()!=0){
            return String.format(template,subjectName,clRoom,plaNote);
        }
        return "";
    }

    @NonNull
    @Override
    public String toString() {
        return "ClassLabel{" +
                "clNums=" + clNums +
                ", startCl=" + startCl +
                ", week=" + week +
                ", color=" + color +
                ", detailTime=" + detailTime +
                ", subjectName='" + subjectName + '\'' +
                ", plaNote='" + plaNote + '\'' +
                ", clRoom='" + clRoom + '\'' +
                ", startTag=" + startTag +
                ", endTag=" + endTag +
                ", customTime='" + customTime + '\'' +
                '}';
    }

    public void setCustomTime(String customTime) {
        this.customTime = customTime;
    }

    public String getCustomTime() {
        return customTime;
    }

    /* public void setStartTag(int startTag) {
        this.startTag = startTag;
    }

    public int getStartTag() {
        return startTag;
    }

    public void setEndTag(int endTag) {
        this.endTag = endTag;
    }

    public int getEndTag() {
        return endTag;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassLabel)) return false;
        ClassLabel that = (ClassLabel) o;
        return clNums == that.clNums && startCl == that.startCl && week == that.week && color == that.color && detailTime == that.detailTime && startTag == that.startTag && endTag == that.endTag && subjectName.equals(that.subjectName) && plaNote.equals(that.plaNote) && clRoom.equals(that.clRoom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clNums, startCl, week, color, detailTime, subjectName, plaNote, clRoom, startTag, endTag);
    }
}
