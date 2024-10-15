package com.hazukie.scheduleviews.scheutil;

import com.hazukie.scheduleviews.models.ClassLabel;
import com.hazukie.scheduleviews.models.Timetable;

import java.util.Arrays;
import java.util.List;

public class ScheDataInitiation {

    private final int totalNums;

    /**
     * @link 课表处理器
     * @param totalNums 课表总长度
     */
    public ScheDataInitiation(int totalNums){
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


    //初始化作息表，但作息表总长度必须>=课表总长度
    public  List<Timetable> initialTime(List<Timetable> timetables){
        if(timetables.size()<totalNums) throw new ArrayIndexOutOfBoundsException("Exception>>> timetables size unequal totalNums !");
        return timetables;
    }


    //初始化课表数据
    public List<ClassLabel> initialClass(List<ClassLabel> classLabels){
        return new CalculateData(totalNums).exClasses(classLabels);
    }

    public List<ClassLabel> initialClass(List<ClassLabel> classLabels,boolean isOddOrEven){
        return new CalculateData(totalNums).exClasses(classLabels,isOddOrEven);
    }

    //空加载方法
    public List<ClassLabel> initialEmpty(){
        return new CalculateData(totalNums).empty();
    }

}
