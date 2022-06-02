package com.example.classtool.models;

public class DowncardModel {
    private int progress;
    private String t;

    public DowncardModel(int po,String t)
    {
        this.t=t;
        this.progress=po;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }
}
