package com.hazukie.scheduleviews.models;

import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(!(obj instanceof TimeModel)) return false;
        return timeName.equals(((TimeModel)obj).timeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeName);
    }
}
