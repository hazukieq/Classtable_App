package com.hazukie.scheduleviews.scheutil;

import android.content.Context;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hazukie.scheduleviews.fileutil.BasicOpts;
import com.hazukie.scheduleviews.fileutil.FileRootTypes;
import com.hazukie.scheduleviews.fileutil.OftenOpts;
import com.hazukie.scheduleviews.models.ClassLabel;
import com.hazukie.scheduleviews.models.ScheWithTimeModel;
import com.hazukie.scheduleviews.models.TimeHeadModel;
import com.hazukie.scheduleviews.models.Timetable;
import com.hazukie.scheduleviews.statics.Statics;
import com.hazukie.scheduleviews.utils.DateHelper;
import com.hazukie.scheduleviews.utils.SpvalueStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScheUIProcessor {
    private final Context context;
    //默认课表总节数
    private int totalClnnums=12;
    private final LinearLayout linearLay;
    private ScheInformation scheInformation;
    private int mWidth=600;
    private int default_time_grid_color=0x10000,default_empty_grid_color=0x10000;

    public ScheUIProcessor(Context context,LinearLayout linearLay,int mWidth,int default_totalClnnums){
        this.context=context;
        this.totalClnnums=default_totalClnnums;
        this.linearLay=linearLay;
        this.mWidth=mWidth;
    }
    public void setmWidth(int mWidth){
        this.mWidth=mWidth;
    }

    public void setDefaultColors(int time_color,int empty_color){
        if(time_color!=0x10000) this.default_time_grid_color=time_color;
        if(empty_color!=0x10000) this.default_empty_grid_color=empty_color;
    }


    public void renderUI() throws IOException {
        String seekSprecord_name=seekSpRecord();
        findFile(seekSprecord_name,mWidth);
    }

    public void changeSche(String record_name) {
        linearLay.removeAllViews();
        findFile(record_name,mWidth);
    }


    public void setScheInformation(ScheInformation scheInformation) {
        this.scheInformation = scheInformation;
    }


    private String seekSpRecord(){
        //固定查询ID字段
        String scheAndtimeTB_record=Statics.scheAndtimeTB_record;
        return SpvalueStorage.getInstance(context).getStringValue(scheAndtimeTB_record,Statics.record_name_default);
    }



    //根据上一步返回值判断是否需要查找相应文件
    private void findFile(String record_name,int mWid) {
        List<ClassLabel> clssList=new ArrayList<>();
        TimeHeadModel timetableObj=null;

        OftenOpts oftenOpts=OftenOpts.getInstance(context);
        BasicOpts basicOpts=BasicOpts.getInstance(context);

        //为避免和现有课表名字冲突，默认值使用二进制表示
        boolean isInitiate=!record_name.equals(Statics.record_name_default);

        //索引统一储存在index.txt文件
        String indexName=Statics.index_file_name;

        //判断index.txt文件是否存在
        boolean isIndexFileExist=basicOpts.exist(FileRootTypes.index,indexName);

        //索引数据
        ScheWithTimeModel mSct=oftenOpts.getSctByName(record_name);

        if(isInitiate){
            //判断记录ID是否在总索引范围内、总索引文件是否存在、总索引大小是否为0，如若为假则空加载
            if(isIndexFileExist){
                //再次判断ID对应的数据是否存在
                boolean isGetNotNull=mSct!=null;

                //判断索引文件是否为空，同时判断记录ID是否在其中！
                if(isGetNotNull){
                    String sche_name=mSct.scheName;
                    String time_name=mSct.timeName;

                    //再次判断课表文件是否存在,//判断数据大小，防止出错
                    if(basicOpts.exist(FileRootTypes.sches,sche_name))
                        clssList=oftenOpts.getClsList(sche_name);

                    //再次判断作息文件是否存在
                    if(basicOpts.exist(FileRootTypes.times,time_name))
                        timetableObj=oftenOpts.getThm(time_name);
                }
            }
        }

        //加载数据
        initDatas(clssList,timetableObj,mWid);
    }

    public void directRenderUI(List<ClassLabel> clssLst,String time){
        TimeHeadModel ime= OftenOpts.getInstance(context).getThm(time);
        initDatas(clssLst,ime,mWidth);
    }

    //核对上一步返回的数据是否为空，如空则空加载，反之跳过;核对后，执行数据加载！
    private void initDatas(List<ClassLabel> clss_list, TimeHeadModel time_model, int mWid){
        List<ClassLabel> clzz_list;
        List<Timetable> timez_list;

        //判断传进来的数据是否为空，如果为空则进行空加载！！
        if(clss_list.size()>0&&time_model!=null){
            //大小<=0，则返回true，反之false
            boolean isLoadDefaultTime=time_model.detailClass.isEmpty();

            //获取总课时长度，如果作息表数据为空，则使用默认数据 totalClnnums 来初始化DataInitiation类
            int totalLen=isLoadDefaultTime?totalClnnums:time_model.detailClass.size();
            ScheDataInitiation scheDataInitiation =new ScheDataInitiation(totalLen);

            //判断作息数据是否为空
            if(isLoadDefaultTime) timez_list= ScheDataInitiation.initialTimeDefaults();
            else timez_list= scheDataInitiation.initialTime(time_model.detailClass);

            boolean isOddOrEvenWeek=isEvenOrOddWeek();
            //加载课程数据
            //clzz_list= scheDataInitiation.initialClass(clss_list);
            clzz_list= scheDataInitiation.initialClass(clss_list,isOddOrEvenWeek);

            //渲染数据到根布局中去
            initGrids(mWid,clzz_list,timez_list,totalLen);
        }else{
            //空加载方法！！
            ScheDataInitiation initiation=new ScheDataInitiation(totalClnnums);
            clzz_list=initiation.initialEmpty();
            timez_list= ScheDataInitiation.initialTimeDefaults();
            initGrids(mWid,clzz_list,timez_list,totalClnnums);
        }
    }


    private boolean isEvenOrOddWeek(){
        SpvalueStorage sp=SpvalueStorage.getInstance(context);
        //设置月份
        //设置周数，这里需要开学日期时间！！
        int startMonth=sp.getInt("start_month",9);
        int startDay=sp.getInt("start_day",1);
        int term_totalWeek=sp.getInt("termweeks",18);
        DateHelper dateHelper=new DateHelper.Builder()
                .setStartDate(startMonth,startDay)
                .setTotalNum(term_totalWeek)
                .create();
        //判断是否是双周
        return dateHelper.getGap() % 2 == 0;
    }

    /**
     * @param width_ 布局宽度
     */
    private void initGrids(int width_,List<ClassLabel> clzz,List<Timetable> timez,int Clnums){

        //共有8列，第一列放置时间数据，剩余为课表布局列数(周一到周七)
        int weekNum=8;

        //调用课表处理器计算并返回长度宽度留空值等
        int[] widthAndHeight= ScheProcessor.calculateHeightAndWidth(width_,2,2,weekNum,Clnums);

        //初始化表格布局
        GridLayout gridLayout = new GridLayout(context);

        //调用课表处理器实现具体逻辑
        ScheProcessor.getInstance(context)
                .setDefault_empty_grid_color(default_empty_grid_color)
                .setDefault_time_grid_color(default_time_grid_color)
                .processClassDatas(gridLayout, widthAndHeight, clzz, timez, (v, cls, times) -> {
                    if(cls.clNums>0) {
                        if(scheInformation!=null) scheInformation.alertByBottomDialog(v,cls,times);
                    }
                });

        //布局高度，设置根布局参数，比如留空值、宽度、长度等，并将根布局添加到上一层布局中去显示;
        int height=widthAndHeight[1];

        LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(width_,height);
        param.setMargins(0,0,0,48);

        gridLayout.setLayoutParams(param);
        //disallow multiple click happened
        gridLayout.setMotionEventSplittingEnabled(false);
        linearLay.addView(gridLayout);
    }

    //课程卡片详细信息处理接口
    public interface ScheInformation{
        void alertByBottomDialog(TextView v,ClassLabel clz,List<Timetable> timez);
    }
}
