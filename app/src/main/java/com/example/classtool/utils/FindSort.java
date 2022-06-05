package com.example.classtool.utils;

public class FindSort {
    public static int returnColorSort(String[] qolors,String value){
        int lenth=qolors.length;
        int color_sort=0;
        for(int i=0;i<lenth;i++){
            if(qolors[i].equals(value)){
                //return i;
                color_sort=i;
            }
        }
        return color_sort;
    }
}
