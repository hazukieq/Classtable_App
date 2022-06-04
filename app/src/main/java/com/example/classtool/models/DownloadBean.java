package com.example.classtool.models;

import java.io.Serializable;

public class DownloadBean implements Serializable {
    public int id;
    public String url;
    public String name;
    public int progress;
    public int download_state;
    public String save_path;
    public long appSize;

    public DownloadBean(String name,int download_state,String url,String save_path,int progress,long appSize){
       // this.id=id;
        this.name=name;
        this.url=url;
        this.progress=progress;
        this.download_state=download_state;
        this.save_path=save_path;
        this.appSize=appSize;
    }

    public int getProgress() {
        return progress;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDownload_state() {
        return download_state;
    }

    public void setDownload_state(int download_state) {
        this.download_state = download_state;
    }

    public String getSave_path() {
        return save_path;
    }

    public void setSave_path(String save_path) {
        this.save_path = save_path;
    }
}
