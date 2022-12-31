package com.hazukie.scheduleviews.fileutil;

import android.annotation.SuppressLint;
import android.content.Context;

import com.hazukie.scheduleviews.models.ScheWithTimeModel;
import com.hazukie.scheduleviews.models.TimeModel;

import java.util.ArrayList;
import java.util.List;

public class SctManager {

    private static List<ScheWithTimeModel> sct_index=new ArrayList<>();
    private static List<TimeModel> thm_index=new ArrayList<>();

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

        OftenOpts oftenOpts=new OftenOpts(context);
        sct_index=oftenOpts.getRecordedScts();

        thm_index=oftenOpts.getRecordTms();
    }

    public List<ScheWithTimeModel> getSct_index(){
        if(sct_index==null) sct_index=new ArrayList<>();
        return sct_index;
    }

    public List<TimeModel> getThm_index(){
        if(thm_index==null) thm_index=new ArrayList<>();
        return thm_index;
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

    public void addThm2Index(TimeModel thm){
        if(thm_index==null)thm_index=new ArrayList<>();
        thm_index.add(thm);
    }

    public boolean delThm2Index(TimeModel thm){
        boolean isDel=true;
        if(thm_index.contains(thm)) isDel=thm_index.remove(thm);
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

    public boolean isDuplicate(TimeModel thm){
        boolean isDuplicate=false;
        for(TimeModel tm:thm_index){
            if(thm.getTimeName().equals(tm.getTimeName())){
                isDuplicate=true;
                break;
            }
        }
        return isDuplicate;
    }

}
