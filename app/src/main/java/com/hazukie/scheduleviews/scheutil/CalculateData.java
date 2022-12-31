package com.hazukie.scheduleviews.scheutil;

import android.util.Log;

import androidx.annotation.NonNull;

import com.hazukie.scheduleviews.models.ClassLabel;
import com.hazukie.scheduleviews.utils.CycleUtil;

import java.util.ArrayList;
import java.util.List;


public class CalculateData {
    List<ClassLabel> xYsList = new ArrayList<>();
    int len;
    /**
     * @param classLen 总课时
     *计算并生成课程卡片坐标
     */
    public CalculateData(int classLen) {
        this.len=classLen;
        GenerateXYZs();
    }

    //生成课表坐标
    private void GenerateXYZs() {
        xYsList.clear();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < len; j++)
                xYsList.add(new ClassLabel(0,j, i, 0,"","", "", 13));
        }
    }

    public List<ClassLabel> empty(){
        return xYsList;
    }

    /**
     * @param Classes 插入课表卡片到坐标体系，并移除原始坐标数据
     * @return 返回课程卡片坐标数据集
     */
    public List<ClassLabel> exClasses(@NonNull List<ClassLabel> Classes) {
        Log.i("exClasses: ","class="+Classes.size());
        if(Classes.size()>0&&Classes.get(0)!=null){
            List<ClassLabel> backup = new ArrayList<>(xYsList);
            CycleUtil.doubleCycle(new ArrayList<>(Classes), new ArrayList<>(xYsList), new CycleUtil.DoubleCyExecute() {
                @Override
                public void firstFunc(Object firstArg, int firstIndex) {}

                @Override
                public void secondFunc(Object firstArg, int firstIndex, Object secondArg, int secondIndex) {
                    ClassLabel mLabel=(ClassLabel)firstArg;
                    ClassLabel Label=(ClassLabel)secondArg;
                    if(mLabel.week== Label.week&&mLabel.startCl== Label.startCl){
                        backup.remove(secondIndex);
                        backup.add(secondIndex,(ClassLabel) firstArg);
                    }
                }
            });
            return returnResult(backup);
        }else{
            Log.i("exClasses: ","class is empty!");
            return empty();
        }
    }

    //处理整理好的课表坐标数据集，主要是处理跨越多少格子的坐标，移除其之后的坐标，更正坐标数据集
    private List<ClassLabel> returnResult(List<ClassLabel> all) {
        List<ClassLabel> qall = new ArrayList<>(all);
        CycleUtil.cycle(new ArrayList<>(all), (obj, objects) -> {
            ClassLabel classLabel=(ClassLabel) obj;
            int tag = classLabel.clNums;
            int index = all.indexOf(classLabel);
            if(tag>=2) for(int i=1;i<tag;i++) qall.remove(all.get(index+i));
        });
        return qall;
    }
}

