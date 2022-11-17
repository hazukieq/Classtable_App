package com.hazukie.scheduleviews.utils;

import com.hazukie.scheduleviews.models.ClassLabel;
import com.hazukie.scheduleviews.models.Timetable;

import java.util.Arrays;
import java.util.List;

public class DataInitiation {

    private final int totalNums;

    /**
     * @link 课表处理器
     * @param totalNums 课表总长度
     */
    public DataInitiation(int totalNums){
        this.totalNums=totalNums;
    }


    /**
     * @return 加载默认时间作息表数据
     */
    public static List<Timetable> initialTimeDefaults(){
        Timetable[] timetables=new Timetable[]{
                //早上
                new Timetable(0,"08:20","09:00"),
                new Timetable(1,"09:05","09:45"),
                new Timetable(2,"09:55","10:35"),
                new Timetable(3,"10:45","11:25"),
                new Timetable(4,"11:30","12:10"),

                //下午
                new Timetable(5,"14:00","14:40"),
                new Timetable(6,"14:45","15:25"),
                new Timetable(7,"15:35","16:15"),
                new Timetable(8,"16:20","17:00"),

                //晚上
                new Timetable(9,"19:00","19:40"),
                new Timetable(10,"19:45","20:25"),
                new Timetable(11,"20:30","21:10"),
        };
        return Arrays.asList(timetables);
    }

    /*
     * @return 加载默认数据
     *
    public static List<ClassLabel> initialClDefaults(int totalNum){
        ClassLabel[] classLabels=new ClassLabel[]{
                new ClassLabel(3,0,1,0,"健康评估实验","暂无","7-18周",0),
                new ClassLabel(2,3,1,0,"药理学","A1-413","1-18周",1),
                new ClassLabel(2,5,1,1,"护理学导论","A1-407","1-18周",2),
                new ClassLabel(2,9,1,2,"体育","A3-131","1-18周",11),

                new ClassLabel(3,0,2,0,"英语","A9-第八语音室","好好学习英语",3),
                new ClassLabel(2,3,2,0,"病理学","A1-213","1-18周",4),
                new ClassLabel(3,5,2,1,"预防医学","A1-415","1-18周",5),

                new ClassLabel(3,2,3,0,"健康评估","A1-217","1-18周",7),
                new ClassLabel(2,7,3,1,"药理学","A1-413","1-18周",14),

                new ClassLabel(3,2,4,0,"病理生理学","A2-245","1-18周",8),
                new ClassLabel(3,5,4,1,"病理学实验","A3-322","5-18周",0),
                new ClassLabel(3,9,4,2,"健康评估","A1-217","1-18周",10),

                new ClassLabel(4,1,5,0,"机能/健康评估实验","A2","1-9周机能，10-18周健康评估实验",6),
                new ClassLabel(3,5,5,1,"马克思主义原理","A1-404","1-14周",11),
        };

        List<ClassLabel> lists= Arrays.asList(classLabels);
        return new CalculateData(totalNum).exClasses(lists);
    }*/

    //初始化作息表，但作息表总长度必须>=课表总长度
    public  List<Timetable> initialTime(List<Timetable> timetables){
        if(timetables.size()<totalNums) throw new ArrayIndexOutOfBoundsException("Exception>>> timetables size unequal totalNums !");
        return timetables;
    }

/*
    //初始化作息表，但作息表总长度必须>=课表总长度
    public  List<Timetable> initialTime(Timetable[] timetables){
        if(timetables.length<totalNums) throw new ArrayIndexOutOfBoundsException("Exception>>> timetables lenght unequal totalNums !");
        return Arrays.asList(timetables);
    }


    //静态初始化课表
    public static List<ClassLabel> initialClass(List<ClassLabel> classLabels,int totalNums){
        return new CalculateData(totalNums).exClasses(classLabels);
    }

    //初始化课表数据
    public  List<ClassLabel> initialClass(ClassLabel[] classLabels){
        List<ClassLabel> lists= Arrays.asList(classLabels);
        return new CalculateData(totalNums).exClasses(lists);
    }
*/

    //初始化课表数据
    public List<ClassLabel> initialClass(List<ClassLabel> classLabels){
        return new CalculateData(totalNums).exClasses(classLabels);
    }

    //空加载方法
    public List<ClassLabel> initialEmpty(){
        return new CalculateData(totalNums).empty();
    }

}
