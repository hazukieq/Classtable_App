package com.example.classtool.models;

public class SchedulModel {
    private String sche,time;
    private int sort;

    public SchedulModel(int sort,String sche,String time){
        this.sche=sche;
        this.time=time;
        this.sort=sort;
    }

    public int getSort() {
        return sort;
    }

    public String getSche() {
        return sche;
    }

    public String getTime() {
        return time;
    }
}
