package com.hazukie.scheduleviews.utils;

import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.models.Unimodel;

public class ColorSeletor {
    private static final int[] Colors=new int[]{
            R.color.class_red,//0
            R.color.class_yellow,//1
            R.color.class_green,//2
            R.color.class_purple,//3
            R.color.class_white,//4
            R.color.class_pink,//5
            R.color.class_blue,//6
            R.color.class_orange,//7
            R.color.class_light_yellow,//8
            R.color.class_light_purple,//9
            R.color.class_light_blue,//10
            R.color.class_mint,//11,
            R.color.class_sunflower,//12
            R.color.light_white,//13默认背景色
            R.color.class_qingblue,//14
    };

    public static final Unimodel[] colorStrings=new Unimodel[]{
            new Unimodel(0,"红色"),
            new Unimodel(1,"黄色"),
            new Unimodel(2,"绿色"),
            new Unimodel(3,"紫色"),
            new Unimodel(4,"白色"),
            new Unimodel(5,"粉色"),
            new Unimodel(6,"蓝色"),
            new Unimodel(7,"橙色"),
            new Unimodel(8,"淡黄色"),
            new Unimodel(9,"淡紫色"),
            new Unimodel(10,"淡蓝色"),
            new Unimodel(11,"薄荷绿"),
            new Unimodel(12,"向日葵色"),
            new Unimodel(13,"粉白色"),
            new Unimodel(14,"靛蓝色"),
    };

    public static int getColorByIndex(int index){
        if(index<=Colors.length) return Colors[index];
        else return Colors[13];
    }

    public static String getColorStringByIndex(int index){
        if(index<=Colors.length) return colorStrings[index].title;
        else return colorStrings[13].title;
    }

    public static int getColorByString(String index){
        int ex=0;
        for (int i = 0; i < colorStrings.length; i++) {
            Unimodel unimodel = colorStrings[i];
            if (unimodel.title.equals(index)) ex=i;
        }
        return Colors[ex];
    }

    public static int getColorIndexByString(String index){
        int ex=0;
        for (int i = 0; i < colorStrings.length; i++) {
            Unimodel unimodel=colorStrings[i];
            if(unimodel.title.equals(index)) ex=i;
        }
        return ex;
    }
}