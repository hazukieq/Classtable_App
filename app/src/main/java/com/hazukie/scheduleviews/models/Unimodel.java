package com.hazukie.scheduleviews.models;

public class Unimodel {
    public String title;
    public int id;
    public Unimodel(int id,String title){
        this.id=id;
        this.title=title;
    }

    public String getTitle() {
        return title.replace(".txt","");
    }
}
