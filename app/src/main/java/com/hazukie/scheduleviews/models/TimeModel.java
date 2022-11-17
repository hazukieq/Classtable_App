package com.hazukie.scheduleviews.models;

public class TimeModel {
    public int id;
    public String timeName;
    public TimeModel(int id, String timeName){
        this.id=id;
        this.timeName=timeName;
    }

    public String getTimeName() {
        return timeName.replace(".txt","");
    }
}
