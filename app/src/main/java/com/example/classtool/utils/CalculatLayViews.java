package com.example.classtool.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.classtool.Schedule_Activity;
import com.example.classtool.models.ClassLabel;

import java.util.ArrayList;
import java.util.List;

public class CalculatLayViews {

    List<ClassLabel> xYsList = new ArrayList<>();

    int len;
    public CalculatLayViews(int classLen) {
        this.len=classLen;

        GenerateXYZs();
    }

    private void GenerateXYZs() {
        xYsList.clear();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < len; j++) {
                xYsList.add(new ClassLabel(0, j, i, "", 0));
            }
        }
    }


    public List<ClassLabel> Excheng_Classes(List<ClassLabel> Classes) {
        List<ClassLabel> new_alls = new ArrayList<>();
        List<ClassLabel> backup=new ArrayList<>();
        backup.addAll(xYsList);
        for (int g=0;g<Classes.size();g++) {
            int y=0;
            int c=0;
            ClassLabel mLabel=Classes.get(g);
            for (int h=0;h< xYsList.size();h++) {
                ClassLabel Label=xYsList.get(h);
                if (mLabel.getWeek()== Label.getWeek() && mLabel.getStart_class() == Label.getStart_class()) {
                    y=1;
                    c=h;
                    continue;

                }
            }
            if(y==1){
                backup.remove(c);
                backup.add(c,mLabel);
            }
        }

        new_alls.addAll(backup);
        return new_alls;

    }


    public List<ClassLabel> ReturnHtmlindex(List<ClassLabel> all) {
        List<ClassLabel> qall=new ArrayList<>();
        qall.addAll(all);
        for (ClassLabel classLabel : all) {
            int tag = classLabel.getClass_nums();
            int index = all.indexOf(classLabel);

            int sec=0,thir=0;
            if (tag ==2) {
                qall.remove(all.get(index+1));
            }else if(tag==3){
                qall.remove(all.get(index+1));
                qall.remove(all.get(index+2));
            }else if(tag==4){
                qall.remove(all.get(index+1));
                qall.remove(all.get(index+2));
                qall.remove(all.get(index+3));
            }else if(tag==5){
                qall.remove(all.get(index+1));
                qall.remove(all.get(index+2));
                qall.remove(all.get(index+3));
                qall.remove(all.get(index+4));
            }
        }
        return qall;
    }
}