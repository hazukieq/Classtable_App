package com.hazukie.scheduleviews.models;

public class HoricardModel {
    public int id;
    public String title;
    public String description;
    public String subtitle;

    public HoricardModel(int id,String title,String subtitle,String description){
        this.id=id;
        this.title=title;
        this.subtitle=subtitle;
        this.description=description;
    }

    public String getTitle() {
        return title.replace(".txt","");
    }

    public String getSubtitle(){
        return subtitle.replace(".txt","");
    }
}
