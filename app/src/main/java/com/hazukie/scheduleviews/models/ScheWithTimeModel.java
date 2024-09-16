package com.hazukie.scheduleviews.models;

import androidx.annotation.NonNull;
import java.util.Objects;

public class ScheWithTimeModel {
    private final static String default_index="index.txt";
    private final static String default_timetable_name="默认作息表.txt";
    public int id;
    public String scheName;
    public String timeName;


    //ID必须是唯一且不重复的UUID
    public ScheWithTimeModel(int id, String scheName, String timeName){
        this.id=id;
        this.scheName=scheName;
        this.timeName=timeName;
    }

    public String getScheName() {
        if(timeName==null||timeName.isEmpty()) timeName=default_timetable_name;

        return scheName.replace(".txt","");
    }

    public String getTimeName() {
        if(timeName==null||timeName.isEmpty()) timeName=default_timetable_name;
        return timeName.replace(".txt","");
    }

    //更新时间字段值
    public void updateTimeName(String timeN){
        this.timeName=timeN;
    }

    //删除该字段名字，即将其值置换为default.txt;
    public void restoreTimeName(){
        this.timeName=default_timetable_name;
    }

    /**---新增方法---**/
    /*public static void autoIncrementID(Context context,String scheName,String timeName){
        FileAssist.applyOftenOpts oftenOpts=new FileAssist.applyOftenOpts(context);

        List<ScheWithTimeModel> scts=oftenOpts.getRecordedScts();
        scts.add(new ScheWithTimeModel(scts.size(),scheName+".txt",timeName+".txt"));
        oftenOpts.putRawSctList(scts);
    }

    public static void autoDecrinmentID(Context context,String scheName,String timeName){
        FileAssist.applyOftenOpts oftenOpts=new FileAssist.applyOftenOpts(context);

        List<ScheWithTimeModel> scts=oftenOpts.getRecordedScts();
        List<ScheWithTimeModel> copy_scts=new ArrayList<>(scts);
        for (ScheWithTimeModel sct:copy_scts) {
            if(sct.getScheName().equals(scheName)&&sct.getTimeName().equals(timeName)){
                scts.remove(sct);
                break;
            }
        }

        for (int i = 0; i < scts.size(); i++) {
            ScheWithTimeModel sct=scts.get(i);
            sct.id=i;
        }

        oftenOpts.putRawSctList(scts);
    }*/

    @NonNull
    @Override
    public String toString() {
        return "ScheWithTimeModel{" +
                "id=" + id +
                ", scheName='" + scheName + '\'' +
                ", timeName='" + timeName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheWithTimeModel)) return false;
        ScheWithTimeModel that = (ScheWithTimeModel) o;
        return scheName.equals(that.scheName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheName);
    }
}
