package com.hazukie.scheduleviews.models;

import java.util.List;

public class TimemakeModel {
    public String title;
    public int id;
    public List<Unimodel> unis;

    public TimemakeModel(int id,String title,List<Unimodel> unis){
        this.title=title;
        this.id=id;
        this.unis=unis;
    }

    public String toTotal(){
        return unis.size()!=0?"节数："+unis.size():"节数："+0;
    }
}
