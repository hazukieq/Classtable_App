package com.example.classtool.models;

import java.io.Serializable;

public class ClassLabel implements Serializable {
    private int Class_nums,Start_class,Week,Colorq;
    private String Subjectplanotes;

    public ClassLabel(int  class_nums,int start_class,int week,String Subjectplanotes,int Colorq){

        this.Class_nums=class_nums;
        this.Start_class=start_class;
        this.Week=week;
        this.Subjectplanotes=Subjectplanotes;
        this.Colorq=Colorq;
    }

    public int getClass_nums() {
        return Class_nums;
    }

    public int getStart_class() {
        return Start_class;
    }

    public int getWeek() {
        return Week;
    }


    public String getSubjectplanotes() {
        return Subjectplanotes;
    }


    public int getColorq() {
        return Colorq;
    }
}
