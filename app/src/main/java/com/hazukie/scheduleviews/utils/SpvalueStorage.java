package com.hazukie.scheduleviews.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SpvalueStorage {
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;
    private static SpvalueStorage instance;

    private SpvalueStorage(Context context){
        sp= PreferenceManager.getDefaultSharedPreferences(context);
        editor=sp.edit();
    }
    public static SpvalueStorage getInstance(Context context) {
        if(instance==null){
           instance=new SpvalueStorage(context);
        }
        return instance;
    }

    public  String  getStringValue(String key,String defaultValue){
        return sp.getString(key,defaultValue);
    }

    public boolean getBooleanValue(String key,boolean bool){
        return sp.getBoolean(key,bool);
    }

    public  void setStringvalue(String key, String value){
        editor.putString(key,value);
        editor.commit();
    }

    public  int getInt(String key,int defaultValue){
        return sp.getInt(key,defaultValue);
    }

    public  void  setIntValue(String key,int value){
        editor.putInt(key,value);
        editor.commit();
    }

    public void setBooleanValue(String key,boolean bool){
        editor.putBoolean(key,bool);
        editor.commit();
    }
}
