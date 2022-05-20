package com.example.classtool.utils;

import android.util.Log;

import com.example.classtool.models.Class_cardmodel;
import com.example.classtool.models.Time_sets;

import java.util.ArrayList;

public  class CompareIsDuplication {

    private static String[] startClasses=new String[]{
            "第1节课", "第2节课", "第3节课", "第4节课", "第5节课",
            "第6节课", "第7节课", "第8节课", "第9节课",
            "第10节课", "第11节课", "第12节课", "第13节课"
    };

    private static String[] Class_nums=new String[]{
           "0节","1节","2节","3节","4节","5节","6节"
    };


    private ArrayList<Class_cardmodel> findLast(ArrayList<Object> alls, String DayAndTime){
        ArrayList<Class_cardmodel> new_alls=new ArrayList<>();
        for(int i=0;i<alls.size();i++){

            Class_cardmodel clazz= (Class_cardmodel) alls.get(i);
            Log.i("findClzz-->",clazz.getClass_date()+clazz.getClass_startClass()+clazz.getClass_course()+clazz.getClass_classPlace());
            if(clazz.getClass_date().equals(DayAndTime)){
                new_alls.add(clazz);
                Log.i("findSameDayAndTime's Clazz-->",clazz.getClass_date()+clazz.getClass_startClass()+clazz.getClass_course()+clazz.getClass_classPlace());
            }
        }
        return new_alls;
    }


    public int returnResult(ArrayList<Object> mAlls, Class_cardmodel data, String class_num,String DayAndTime){

        int classStart=returnStartClass_index(startClasses,data.getClass_startClass());
        Log.i("上课时间-->",String.valueOf(classStart));
        int checkNums=0;//卡片不存在重复，但现在卡片日期与之前的重复，checknum=2；其他，checkNum=checkNum+5；
        int current_real_time=0;
        ArrayList<Class_cardmodel> Allq=findLast(mAlls,DayAndTime);

        int error_check=0;
        if(classStart==5|classStart==10|classStart==15){
        }//else if(returnStartClass_index(Class_nums,class_num)==0){
           // current_real_time=1;
            //Log.i("num-->", String.valueOf(returnStartClass_index(Class_nums,class_num)));
        //}
        else{
            current_real_time=returnTime_real_index(Time_sets.detail_real_num,classStart);
            Log.i("真实现在时间数值-->",String.valueOf(current_real_time));
        }

        if(Allq.size()==0){
            checkNums=6;
        }else if(Allq.size()>0){

           // int error_check=0;
            int check=0;
            for(Class_cardmodel clss:Allq){
               // int check=0;
                int mLastTime=returnStartClass_index(startClasses,clss.getClass_startClass())+returnStartClass_index(Class_nums,clss.getClass_totalClass());
                int lastTime=returnTime_real_index(Time_sets.detail_real_num,mLastTime);
                Log.i("之前时间真实数值-->",String.valueOf(lastTime));

                if(returnStartClass_index(Class_nums,clss.getClass_totalClass())==4){
                    error_check+=1;
                    break;
                }


                if(lastTime>current_real_time){
                    int ycurrentindex=returnStartClass_index(startClasses,data.getClass_startClass())+returnStartClass_index(Class_nums,class_num);
                    int ycurrent=returnTime_real_index(Time_sets.detail_real_num,ycurrentindex);

                    int yindex=returnStartClass_index(startClasses,clss.getClass_startClass());
                    int ylast=returnTime_real_index(Time_sets.detail_real_num,yindex);

                    Log.i("", "ycurrent-->"+ycurrent+"  ylast-->"+ylast);
                    if(ylast>=ycurrent){
                        check+=2;
                    }else if(ycurrent>ylast){
                        error_check+=1;
                        //check+=2+error_check;//+Allq.indexOf(clss);
                    }
                   // else if(ycurrent!=ylast){
                    //}
                   // check=5+ Allq.indexOf(clss);
                   // Log.i("current_real_time-->",String.valueOf(current_real_time));
                    //Log.i("with last view duplicate,view-->",clss.getClass_date()+clss.getClass_startClass());
                }else if(current_real_time>lastTime|(current_real_time==lastTime&&data.getClass_totalClass().equals(clss.getClass_totalClass())&&(!data.getClass_startClass().equals(clss.getClass_startClass())))){
                    Log.i("current_real_time-->",String.valueOf(current_real_time));
                    check+=2;
                }else if(current_real_time==lastTime&&data.getClass_startClass().equals(clss.getClass_startClass())){
                    error_check+=1;//+Allq.indexOf(clss);
                }


                checkNums=check;
                Log.i("循环后标记数值-->",String.valueOf(check));
            }
        }

        if(error_check>=1) checkNums=3;
        Log.i("返回标记数值-->",String.valueOf(checkNums));
        return checkNums;
    }

    private int returnStartClass_index(String[] mDta,String startClass){
        int index=0;
        for(int g=0;g< mDta.length;g++){
           // int f=0;
            if(startClass.equals(mDta[g])){
                index=g;
            }
           // index=f;
        }
        return index;
    }

    private int returnTime_real_index(int[] mDta,int startClass){
        int index=0;
        for(int g=0;g< mDta.length;g++){
            if(startClass==mDta[g]){
                index=g;
            }
        }
        return index;
    }

    public int returnSameTimeTotal(ArrayList<Object> alls,String dayAndTime) {
        ArrayList<Class_cardmodel> allp = findLast(alls, dayAndTime);
        int totals = 0;
        //int total = returnStartClass_index(Class_nums, numsStr);
        for (Class_cardmodel qlss : allp) {

            int wotal = 0;
            wotal += returnStartClass_index(Class_nums, qlss.getClass_totalClass());
            totals = wotal;
        }
        return totals;
    }
}
