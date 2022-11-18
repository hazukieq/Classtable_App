package com.hazukie.scheduleviews.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hazukie.scheduleviews.fileutil.FileAssist;
import com.hazukie.scheduleviews.fileutil.FileRootTypes;
import com.hazukie.scheduleviews.fileutil.Fileystem;
import com.hazukie.scheduleviews.models.ClassLabel;
import com.hazukie.scheduleviews.models.ScheWithTimeModel;
import com.hazukie.scheduleviews.models.TimeHeadModel;
import com.hazukie.scheduleviews.models.TimeModel;
import com.hazukie.scheduleviews.models.Timetable;
import com.hazukie.scheduleviews.statics.Statics;
import com.hazukie.scheduleviews.utils.DataInitiation;
import com.hazukie.scheduleviews.utils.FileHelper;
import com.hazukie.scheduleviews.utils.SpvalueStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LauncherActivity extends AppCompatActivity {
    private SpvalueStorage sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fileystem fileystem=Fileystem.getInstance(getApplicationContext());
        //ClassLabel cls=new ClassLabel(10,0,1,2,"neosubject","A1-200","six weeks",0);
        //fileystem.putData(FileRootTypes.sches,"neo_sche.txt",cls);
        //ScheWithTimeModel.autoIncrementID(getApplicationContext(),"neo_sche", "默认作息表");

/*        FileHelper fileHelper=new FileHelper(this);
        List<Timetable> s=DataInitiation.initialTimeDefaults();
        TimeHeadModel thm=new TimeHeadModel("默认作息表.txt",12,0,5,5,4,9,3,s);

        List<ScheWithTimeModel> scts=FileHelper.getRecordedScts(this);
        //List<TimeModel> tms=FileHelper.getRecordTms(this);

        scts.get(0).updateTimeName("默认作息表.txt");
        //tms.get(0).timeName="武鸣校.txt";

        try{
            fileHelper.write(FileHelper.RootMode.times,"默认作息表.txt", thm,false);
            fileHelper.write(FileHelper.RootMode.index,"index.txt",new ArrayList<>(scts),false);
           // fileHelper.write(FileHelper.RootMode.times,"time_index.txt",new ArrayList<>(tms),false);
        }catch (Exception e){
            e.printStackTrace();
        }*/

        doAfterPermissionsGranted();
    }

    private void doAfterPermissionsGranted() {
        Intent in = new Intent();
        in.setClass(LauncherActivity.this,MainActivity.class);
        startActivity(in);
        finish();
    }
}


