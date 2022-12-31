package com.hazukie.scheduleviews.fileutil;

import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Converter {
    private  Gson gson;
    public Converter(){
        if(gson==null) gson=new Gson();
    }
    public List<String> convertObj2Jsn(List<Object> objectList){
        List<String> stringList=new ArrayList<>();

        if(objectList.size()!=0){
            for(Object obj:objectList) stringList.add(gson.toJson(obj));
        }
        return stringList;
    }

    public String convertObj2Jsn(Object obj){
        String s="";
        if(obj!=null) s=gson.toJson(obj);
        return s;
    }

    public Object convertJsn2Obj(String content, Type type){
        if(content.length()>0&&type!=null)
        return gson.fromJson(content,type);
        return "{}";
    }
}
