package com.hazukie.scheduleviews.scheutil;

import androidx.annotation.NonNull;

import com.hazukie.scheduleviews.models.ClassLabel;
import com.hazukie.scheduleviews.models.TimeHeadModel;

import java.util.ArrayList;
import java.util.List;

public class CheckUtil {

    /**
     * @param ClLists 课程总列表
     * @param Day 待添加卡片的星期数
     * @param detail 待添加卡片的具体时间段
     * @return 筛选出具体时间段相同的课程卡片，并以列表形式返回
     */
    public static List<ClassLabel> findSpecificItems(@NonNull List<Object> ClLists, int Day, int detail){
        List<ClassLabel> newopy=new ArrayList<>();

        for(Object obj:ClLists){
            ClassLabel cls=(ClassLabel) obj;
            int mDay=cls.week;
            int mDetail=cls.detailTime;
            if(mDay==Day&&mDetail==detail){
                newopy.add(cls);
            }
        }
        return newopy;
    }

    /**
     * @param cls_ 即将插入课程，课程开始时间,由于startCl可能为0，所以将startCl起始数值0调整为从1开始计算,故mStartCl=startCl+1, 课程节数
     * @param totalLen 某个时间段总节数(比如上午总节数)
     * @return 返回是否重复，重复则返回true，反之返回false,默认返回不重复；
     */
    public static int CompareDuplicateOrNot(@NonNull List<ClassLabel> ClList, ClassLabel cls_, int totalLen) {
        //移除自己后才能开始检查
        //即将插入课程的结束时间,由于startCl可能为0，所以将startCl起始数值0调整为从1开始计算,故mStartCl=startCl+1
        int mStartCl = cls_.startCl + 1;
        int mEndNum = mStartCl+ cls_.clNums;

        //检查是否重叠标记
        int check = 0;//循环判断标记物
        int isDuplicate = 0;//最终判断重叠标记物


        //判断课程列表是否存在数据，如果存在数据则说明即将插入课程前已经有课程了，所以接下来需要拿即将插入课程和列表中的课程做比较
        if (ClList.size() != 0) {
            //累计当前所有class的课时
            int lastNums=0;
            for(ClassLabel cls:ClList)
                lastNums+=cls.clNums;

            //判断卡片数据集中所有item的节数总长度是否小于或等于某个时间段的总课时;小于或等于，则返回true，反之返回false
            boolean isAccordian = cls_.clNums + lastNums <= totalLen;
            //如果课程总课时超过规定课时则判定为重叠
            if(isAccordian){
                for (ClassLabel obj : ClList) {
                    //某个class的开始时间
                    int lastStartCl = obj.startCl + 1;

                    //某个class的上课课时
                    int lastClnum = obj.clNums;

                    //某节课前的一节课结束时间
                    int lastEndNum = lastClnum + lastStartCl;

                    //如果符合这种情况，则判定为重叠
                    if (lastEndNum > mStartCl && mEndNum > lastStartCl) check += 1;
                }
                //统计循环课程列表时的标记物是否大于1，大于1说明课程列表中存在重叠，则将重叠标记设为1并返回
                if (check >= 1) isDuplicate = 1;

            }else {
                isDuplicate = 1;
            }

            return isDuplicate;
        }else{
            //说明课程列表中没有数据，则只需要对即将插入课程进行条件判断
            if(cls_.clNums>totalLen)
                isDuplicate=2;
            return isDuplicate;
        }
    }

    //计算课程开始时间
    public static int calculatStartCls(int startCl){
        return startCl+1;
    }

    //计算课程结束时间
    public static int calculateEndStartCls(int startCl,int clNums){
        return startCl+clNums;
    }

    //返回课程是否存在重叠
    public static int  returnIsDuplicateCheck(List<Object> multi_all,int week,int detail_time,ClassLabel cls_, TimeHeadModel timetable) {
        int[] etails = new int[]{timetable.amCl, timetable.pmCl, timetable.mmCl};

        List<ClassLabel> same_list = CheckUtil.findSpecificItems(multi_all, week, detail_time);
        return CheckUtil.CompareDuplicateOrNot(same_list, cls_, etails[detail_time]);
    }

}
