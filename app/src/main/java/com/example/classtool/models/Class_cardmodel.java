package com.example.classtool.models;

import java.io.Serializable;


public class Class_cardmodel implements Serializable {


    public int id;

    public String class_date;


    public String class_course;

    public String class_startClass;

    public String class_totalClass;

    public String class_classPlace;

    public String lassColor;

    public String otherNotes;

    public int ClassColor;


    public String TableSchdule_sort_tag;


    public Class_cardmodel(String class_date, String class_course, String class_startClass, String class_totalClass, String class_classPlace, String lassColor, int ClassColor, String otherNotes, String TableSchdule_sort_tag, int id){
        this.id=id;
        this.class_date=class_date;
        this.class_course=class_course;
        this.class_startClass=class_startClass;
        this.class_totalClass=class_totalClass;
        this.class_classPlace=class_classPlace;
        this.ClassColor=ClassColor;
        this.lassColor=lassColor;
        this.otherNotes=otherNotes;
        this.TableSchdule_sort_tag=TableSchdule_sort_tag;
      //  this.viewposition=viewposition;
    }




    public String getClass_date() {
        return class_date;
    }

    public void setClass_date(String class_date) {
        this.class_date = class_date;
    }

    public String getClass_course() {
        return class_course;
    }

    public void setClass_course(String class_course) {
        this.class_course = class_course;
    }

    public String getClass_startClass() {
        return class_startClass;
    }

    public void setClass_startClass(String class_startClass) {
        this.class_startClass = class_startClass;
    }

    public String getClass_totalClass() {
        return class_totalClass;
    }

    public void setClass_totalClass(String class_totalClass) {
        this.class_totalClass = class_totalClass;
    }

    public String getClass_classPlace() {
        return class_classPlace;
    }

    public void setClass_classPlace(String class_classPlace) {
        this.class_classPlace = class_classPlace;
    }

    public int getClassColor() {
        return ClassColor;
    }

    public void setClassColor(int classColor) {
        ClassColor = classColor;
    }

    public String getLassColor() {
        return lassColor;
    }

    public void setLassColor(String lassColor) {
        this.lassColor = lassColor;
    }

    public String getOtherNotes() {
        return otherNotes;
    }

    public void setOtherNotes(String otherNotes) {
        this.otherNotes = otherNotes;
    }

    public String getTableschdulSort(){
        return TableSchdule_sort_tag;
    }

    public int getId() {
        return id;
    }
}
