package com.hazukie.scheduleviews.fileutil;

import com.google.gson.Gson;

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

    public <T> String convertObj2Jsn(T  t,String appendStr){
        String s="";
        if(t!=null) s=gson.toJson(t)+appendStr;
        return s;
    }

    public <T> String convertObj2Jsn(T  t){
        return convertObj2Jsn(t,"");
    }

    /**
     *
     * @param content 解释字符
     * @param type 反射类型
     * @return @null 返回值可能为空
     * @param <T> 泛型
     */
    public <T> T convertJsn2Any(String content, Class<T> type){
        if(content.length()>0&&type!=null) return gson.fromJson(content,type);
        return null;
    }
}
