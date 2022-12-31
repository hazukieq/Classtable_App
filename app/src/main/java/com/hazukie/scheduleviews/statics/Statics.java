package com.hazukie.scheduleviews.statics;

import com.hazukie.scheduleviews.models.Unimodel;

public class Statics {
    //二进制表示 空表
    public final static String record_name_default="1111010011110101000100001101000";
    public final static Unimodel[] weekTimes=new Unimodel[]{
            new Unimodel(0,"星期一"),
            new Unimodel(1,"星期二"),
            new Unimodel(2,"星期三"),
            new Unimodel(3,"星期四"),
            new Unimodel(4,"星期五"),
            new Unimodel(5,"星期六"),
            new Unimodel(6,"星期日"),
    };

    public final static Unimodel[] headWeeks=new Unimodel[]{
            new Unimodel(1,"全部"),
            new Unimodel(0,"星期一"),
            new Unimodel(0,"星期二"),
            new Unimodel(0,"星期三"),
            new Unimodel(0,"星期四"),
            new Unimodel(0,"星期五"),
            new Unimodel(0,"星期六"),
            new Unimodel(0,"星期日"),
    };


    private final static String[] weeks=new String[]{"","一","二","三","四","五","六","日"};
    private final static String[] sorted_weeks=new String[]{"","星期一","星期二","星期三","星期四","星期五","星期六","星期日"};

    private final static String[] details=new String[]{"上午","下午","晚上"};

    public enum SearchMode {
        weeks,details
    }

    public static int returnArrayIndex(SearchMode mode,String s){
        int idn=0;
        switch (mode){
            case weeks:
                idn=search(weeks,s);
                break;
            case details:
                idn=search(details,s);
                break;
        }
        return idn;
    }

    private static int search(String[] strs,String s){
        int si=0;
        for (int i = 0; i < strs.length; i++) {
            if(strs[i].equals(s)) si=i;
        }
        return si;
    }


    public static String getWeekByIndex(int index){
        return weeks[index];
    }

    public static String getWeekBySort(int index){
        return sorted_weeks[index];
    }

    public static int getWeekSortByString(String str){
        int index_=0;
        for (int i = 0; i < weeks.length; i++) {
            if(sorted_weeks[i].equals(str)) index_=i;
        }
        return index_;
    }

    public static String getDetailTimeByIndex(int index){
        return details[index];
    }


    public static Unimodel[] generateStartCls(int amStart7amCls,int pmStart7pmCls,int mmStart7mmCls,int totalLen){
        Unimodel[] unimodels=new Unimodel[totalLen];
        for (int i = 0; i < unimodels.length; i++) {
            if(i<amStart7amCls)
                unimodels[i]=new Unimodel(i,"上午第"+(i+1)+"节课");

            if(amStart7amCls<=i&&i<pmStart7pmCls) unimodels[i]=new Unimodel(i,"下午第"+(i+1)+"节课");

            if(pmStart7pmCls<=i&&i<mmStart7mmCls) unimodels[i]=new Unimodel(i,"晚上第"+(i+1)+"节课");
        }

        return unimodels;
    }

    public static Unimodel[] generateNums(int nums){
        Unimodel[] unimodels=new Unimodel[nums];
        for (int i = 0; i < unimodels.length; i++) {
            unimodels[i]=new Unimodel(i,(i+1)+"节");
        }
        return unimodels;
    }

}
