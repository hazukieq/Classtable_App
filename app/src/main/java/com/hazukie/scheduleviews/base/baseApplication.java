package com.hazukie.scheduleviews.base;

import android.app.Application;

import com.hazukie.scheduleviews.models.TimeHeadModel;
import com.hazukie.scheduleviews.models.Timetable;
import com.hazukie.scheduleviews.utils.DataInitiation;
import com.hazukie.scheduleviews.utils.FileHelper;
import com.hazukie.scheduleviews.utils.SpvalueStorage;

import java.io.IOException;
import java.util.List;

public class baseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        int ini=SpvalueStorage.getInstance(this).getInt("isInit",0);
        if(ini==0) {
            try {
                FileHelper.getInstance(this).init();
            } catch (IOException e) {
                e.printStackTrace();
            }
            initFiles();
            SpvalueStorage.getInstance(this).setIntValue("isInit",1);
        }

    }

    //初始化必需文件数据;
    public void initFiles(){
        try {
           // String sche_n="默认课表.txt";
            String time_n="默认作息表.txt";
            //固定查询ID字段，通过查找ID是否大于0来确认；如果ID>0,则说明有变动，否则就默认没有初始化；
            String scheAndtimeTB_record="record_id";
            SpvalueStorage.getInstance(this).setIntValue(scheAndtimeTB_record,0);
            FileHelper fileHelper=FileHelper.getInstance(getApplicationContext());


            //初始化默认时间文件数据，写入作息时间数据
            List<Timetable> isl= DataInitiation.initialTimeDefaults();
            TimeHeadModel thm=new TimeHeadModel(time_n,12,0,5,5,4,9,3,isl);
            //List<TimeHeadModel> tis= Arrays.asList(new TimeHeadModel(time_n,12,0,5,5,4,9,3,isl));
            //List<Object> sl = new ArrayList<>(tis);
            fileHelper.write(FileHelper.RootMode.times,time_n,thm,false);

            //初始化课表文件数据，并写入
            /*List<ClassLabel> clzz=DataInitiation.initialClDefaults(12);
            List<Object> obj_lzz=new ArrayList<>(clzz);
            fileHelper.write(FileHelper.RootMode.sches,sche_n,obj_lzz);*/

           /* //插入时间和课表文件记录到索引文件中去
            //List<ScheWithTimeModel> scheWithTimeModels=new ArrayList<>();
            ScheWithTimeModel sct=new ScheWithTimeModel(0,sche_n,time_n);
            //scheWithTimeModels.add(new ScheWithTimeModel(0,sche_n,time_n));
            fileHelper.write(FileHelper.RootMode.index,"index.txt",sct,false);*/

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
