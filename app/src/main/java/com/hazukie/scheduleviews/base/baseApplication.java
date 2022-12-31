package com.hazukie.scheduleviews.base;

import android.app.Application;

import com.hazukie.scheduleviews.fileutil.FileRootTypes;
import com.hazukie.scheduleviews.fileutil.Fileystem;
import com.hazukie.scheduleviews.fileutil.SctManager;
import com.hazukie.scheduleviews.models.TimeHeadModel;
import com.hazukie.scheduleviews.models.Timetable;
import com.hazukie.scheduleviews.net.WebStacker;
import com.hazukie.scheduleviews.utils.DataInitiation;
import com.hazukie.scheduleviews.utils.SpvalueStorage;

import java.util.List;

public class baseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        int ini=SpvalueStorage.getInstance(this).getInt("isInit",0);
        if(ini==0) {
            /*try {
                FileHelper.getInstance(this).init();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            Fileystem.getInstance(this).initial();
            initFiles();
            SpvalueStorage.getInstance(this).setIntValue("isInit",1);
        }
        WebStacker.getInstance(getApplicationContext()).preload();
        SctManager.getInstance(this).init();
    }

    //初始化必需文件数据;
    public void initFiles(){
/*        try {
            String time_n="默认作息表.txt";
            //固定查询ID字段，通过查找ID是否大于0来确认；如果ID>0,则说明有变动，否则就默认没有初始化；
            String scheAndtimeTB_record="record_id";
            SpvalueStorage.getInstance(this).setIntValue(scheAndtimeTB_record,0);
            Fileystem fileystem=Fileystem.getInstance(this);
           // FileHelper fileHelper=FileHelper.getInstance(getApplicationContext());


            //初始化默认时间文件数据，写入作息时间数据
            List<Timetable> isl= DataInitiation.initialTimeDefaults();
            TimeHeadModel thm=new TimeHeadModel(time_n,12,0,5,5,4,9,3,isl);
            fileystem.putData(FileRootTypes.times,time_n,thm);
            //fileHelper.write(FileHelper.RootMode.times,time_n,thm,false);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        String time_n="默认作息表.txt";
        //固定查询ID字段，通过查找ID是否大于0来确认；如果ID>0,则说明有变动，否则就默认没有初始化；
        String scheAndtimeTB_record="record_id";
        SpvalueStorage.getInstance(this).setIntValue(scheAndtimeTB_record,0);
        Fileystem fileystem=Fileystem.getInstance(this);

        //初始化默认时间文件数据，写入作息时间数据
        List<Timetable> isl= DataInitiation.initialTimeDefaults();
        TimeHeadModel thm=new TimeHeadModel(time_n,12,0,5,5,4,9,3,isl);
        fileystem.putData(FileRootTypes.times,time_n,thm);
    }
}
