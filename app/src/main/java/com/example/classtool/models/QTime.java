package com.example.classtool.models;

public class QTime {
    private int sorq;
    private String sortStr,start2end;

    public QTime(int sorq,String sortStr,String start2end){//,int class_sort){
        this.sorq=sorq;
        this.start2end=start2end;
        //this.class_sort=class_sort;
        this.sortStr=sortStr;
    }

    public int getSorq() {
        return sorq;
    }

    public String getStart2end() {
        return start2end;
    }

    public String getSortStr() {
        return sortStr;
    }
}
