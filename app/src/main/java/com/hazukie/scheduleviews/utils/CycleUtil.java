package com.hazukie.scheduleviews.utils;

import java.util.List;
import java.util.Map;

public class CycleUtil {
    public static void cycle(List<Object> objectList,CyExecute cyExecute){
        for(Object obj:objectList) cyExecute.function(obj);
    }

    public interface CyExecute{
        void function(Object obj,Object...objects);
    }

    public static void cycle(List<Object> objectList,Object[] objects,CyExecute cyExecute){
        for(Object obj:objectList) cyExecute.function(obj,objects);
    }

    public static void cycleByMap(Map<Object,Object> objectList, CyExecute cyExecute){
        for(Object obj:objectList.keySet()) cyExecute.function(obj,objectList.get(obj));
    }

    public static void doubleCycle(List<Object> firstList,List<Object> secondList,DoubleCyExecute doubleCyExecute){
        for(Object item:firstList){
            for (int i = 0; i < secondList.size(); i++)
                doubleCyExecute.secondFunc(item,firstList.indexOf(item),secondList.get(i),i);
            doubleCyExecute.firstFunc(item,firstList.indexOf(item));
        }
    }

    public interface DoubleCyExecute{
        void firstFunc(Object firstArg,int firstIndex);
        void secondFunc(Object firstArg,int firstIndex,Object secondArg,int secondIndex);
    }

}
