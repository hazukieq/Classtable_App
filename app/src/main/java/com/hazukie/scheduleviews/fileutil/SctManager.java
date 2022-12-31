package com.hazukie.scheduleviews.fileutil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.hazukie.scheduleviews.models.ScheWithTimeModel;
import com.hazukie.scheduleviews.models.TimeModel;

import java.util.ArrayList;
import java.util.List;

public class SctManager {

    private static List<ScheWithTimeModel> sct_index=new ArrayList<>();
    private static List<TimeModel> tm_index =new ArrayList<>();

    private final Context context;

    @SuppressLint("StaticFieldLeak")
    private static SctManager instance;

    private SctManager(Context context){
        this.context=context;
    }

    public static SctManager getInstance(Context context) {
        if(instance==null) instance=new SctManager(context);
        return instance;
    }

    public void init(){
/*        sct_index=new ArrayList<>();
        thm_index=new ArrayList<>();*/
        Looper.myQueue().addIdleHandler(() -> {
            OftenOpts oftenOpts=OftenOpts.getInstance(context);
            sct_index=oftenOpts.getRecordedScts();

            tm_index =oftenOpts.getRecordTms();
            Log.i("SctMgr:","initialized, sct_index_buffer="+sct_index.size()+",thm_index_buffer="+ tm_index.size());
            return false;
        });

    }

    public List<ScheWithTimeModel> getSct_index(){
        if(sct_index==null) sct_index=new ArrayList<>();
        return sct_index;
    }

    public List<TimeModel> getTm_index(){
        if(tm_index ==null) tm_index =new ArrayList<>();
        return tm_index;
    }

    public boolean addScts2Index(List<ScheWithTimeModel> scts){
        return sct_index.addAll(scts);
    }

    public boolean addTms2Index(List<TimeModel> tms){
        return tm_index.addAll(tms);
    }

    public void addSct2Index(ScheWithTimeModel sct){
        if(sct_index==null) sct_index=new ArrayList<>();
        sct_index.add(sct);
    }

    public boolean delSct2Index(ScheWithTimeModel sct){
        boolean isDel=true;
        if(sct_index.contains(sct)) isDel=sct_index.remove(sct);
        return isDel;
    }

    public void addTm2Index(TimeModel tm){
        if(tm_index ==null) tm_index =new ArrayList<>();
        tm_index.add(tm);
    }

    public boolean delTm2Index(TimeModel tm){
        boolean isDel=true;
        if(tm_index.contains(tm)) isDel= tm_index.remove(tm);
        return isDel;
    }

    public boolean isDuplicate(ScheWithTimeModel sct){
        boolean isDuplicate=false;
        for(ScheWithTimeModel sctz:sct_index){
            if(sct.getScheName().equals(sctz.getScheName())){
                isDuplicate=true;
                break;
            }
        }
        return isDuplicate;
    }

    public boolean isDuplicate(TimeModel tm){
        boolean isDuplicate=false;
        for(TimeModel tm_: tm_index){
            if(tm.getTimeName().equals(tm_.getTimeName())){
                isDuplicate=true;
                break;
            }
        }
        return isDuplicate;
    }

}
