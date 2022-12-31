package com.hazukie.scheduleviews.utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.drakeet.multitype.MultiTypeAdapter;
import com.hazukie.scheduleviews.binders.UniBinder;
import com.hazukie.scheduleviews.custom.CBottomSheet;
import com.hazukie.scheduleviews.databinding.BottomsheetSchecardBinding;
import com.hazukie.scheduleviews.models.ClassLabel;
import com.hazukie.scheduleviews.models.TimeHeadModel;
import com.hazukie.scheduleviews.models.Unimodel;
import com.hazukie.scheduleviews.statics.Statics;

import java.util.ArrayList;
import java.util.List;

public class BottomialogUtil {

    private final Context context;
    public BottomialogUtil(Context context){
        this.context=context;
    }




    /**
     *
     * @param multi_all 总items列表
     * @param multi_filter 筛选后列表
     * @param multiTypeAdapter 适配器
     * @param timetable 时间头文件模型
     * @param cls 传入绑定数据模型（一般是和布局绑定邸数据模型，需要手动更新变动过过的数据）
     * @param attachedView 绑定布局
     * @param IspresetValues 是否预设有数据（即是否在原有数据卡片上修改，或者是新添卡片）
     * @param weekRID 当前是在周数筛选栏中的第几个
     */
    public void showBottomEditedSheet(@NonNull  List<Object> multi_all,@NonNull List<Object> multi_filter, MultiTypeAdapter multiTypeAdapter,@NonNull TimeHeadModel timetable,@NonNull ClassLabel cls, View attachedView, boolean IspresetValues, int weekRID){
        CBottomSheet cBottomSheet=new CBottomSheet.ScheBuilder()
                .addScheObj(cls)
                .onInterceptBinding((cb, bindin) -> {
                    BottomsheetSchecardBinding binding=(BottomsheetSchecardBinding)bindin;
                    //采用作息时间值
                    Unimodel[] starts= Statics.generateStartCls(timetable.amStart+timetable.amCl,timetable.pmStart+timetable.pmCl,timetable.mmStart+timetable.mmCl,timetable.totalClass);

                    binding.classSpinnerWeekdate.setOnClickListener(v-> showListheet(context, "星期", Statics.weekTimes,binding.classSpinnerWeekdate, null,0));
                    binding.classSpinnerStartDate.setOnClickListener(v-> showListheet(context, "开始节次", starts, binding.classSpinnerStartDate, binding.classSpinnerDate,2));
                    binding.classSpinnerDateNums.setOnClickListener(v-> showListheet(context, "上课节数", Statics.generateNums(20), binding.classSpinnerDateNums, null, 0));
                    binding.classColor.setOnClickListener(v-> showListheet(context, "颜色", ColorSeletor.colorStrings, binding.classColor, null, 1));
                    binding.classCancelBtn.setOnClickListener(v-> cb.dismiss());

                    binding.classConfirmBtn.setOnClickListener(v1->{
                        //上课节数
                        int clnums=Integer.parseInt(binding.classSpinnerDateNums.getText().toString().replace("节",""));
                        //开始节次
                        int startcl=Integer.parseInt(binding.classSpinnerStartDate.getText().toString().replaceAll("第(.{1,2})节课","$1"))-1;
                        //星期
                        int week=Statics.getWeekSortByString(binding.classSpinnerWeekdate.getText().toString());//returnArrayIndex(Statics.SearchMode.weeks,binding.classSpinnerWeekdate.getText().toString());
                        //具体时间
                        int detail_time=Statics.returnArrayIndex(Statics.SearchMode.details,binding.classSpinnerDate.getText().toString());
                        //科目
                        String subject=binding.classSubject.getText().toString();
                        //位置
                        String place=binding.classClassroom.getText().toString();
                        //备注
                        String planote=binding.classNotes.getText().toString();
                        //颜色
                        int color=ColorSeletor.getColorIndexByString(binding.classColor.getText().toString());
                        /*---------------------以上获取当前视图中所有数据---------------------------------------*/

                        int check_=0;
                        int old_week=cls.week;
                        ClassLabel cs_=new ClassLabel(clnums,startcl,week,detail_time,subject,place,planote,color);
                        //设置自定义时间段值
                        cs_.setCustomTime(binding.classCustomDate.getText().toString());

                        //如果是已有卡片上，则编辑；反之采用另一种
                        if(IspresetValues){
                            //移除旧item，避免重复判断
                            List<Object> ls=new ArrayList<>(multi_all);
                            ls.remove(cls);
                            //List<ClassLabel> same_list=CheckUtil.findSpecificItems(ls,week,detail_time);
                            check_= CheckUtil.returnIsDuplicateCheck(ls,week,detail_time,cs_,timetable);

                        }else{
                            check_= CheckUtil.returnIsDuplicateCheck(multi_all,week,detail_time,cs_,timetable);
                        }

                        //正式启动是否重叠判断方法
                        switch (check_){
                            case 1:
                                DisplayHelper.Infost(context,"和列表中卡片发生冲突！");
                                break;
                            case 2:
                                DisplayHelper.Infost(context, "当前卡片上课节数已超出"+binding.classSpinnerDate.getText().toString()+"总节数！");
                                break;
                            default:
                                if(IspresetValues){
                                    //判断item是否在当前区间内
                                    if(week==old_week|weekRID==0){
                                        //参考系
                                        int index_=multi_filter.indexOf(cls);
                                        //更新值
                                        cls.updateValues(clnums,startcl,week,detail_time,subject,place,planote,color);
                                        //设置自定义时间段值
                                        cls.setCustomTime(binding.classCustomDate.getText().toString());
                                        //更新视图
                                        multiTypeAdapter.notifyItemChanged(index_,"updating");
                                    }else{
                                        //不在当前可见视图范围内，所以需要移除相应item，并更新视图
                                        multi_all.remove(cls);
                                        multi_filter.remove(cls);
                                        multiTypeAdapter.notifyDataSetChanged();
                                        //添加数据
                                        multi_all.add(cs_);
                                    }
                                    //Log.i( "showEditPopup-preset: ","week="+week+",cls.week="+cls.week);
                                    cb.dismiss();
                                }else{
                                    multi_all.add(cs_);
                                    //判断item是否在当前区间内
                                    if(week==old_week|weekRID==0){
                                        multi_filter.add(cs_);
                                        multiTypeAdapter.notifyDataSetChanged();
                                    }
                                    cb.dismiss();
                                }
                        }
                    });

                })
                .build(context);

        cBottomSheet.show();
    }


    /**
     *
     * @param context 活动柄
     * @param textView 弹出绑定item
     * @param sub 和绑定item有关联的子item
     * @param objs 数据模型
     * @param tag 标记
     */
    public static void showListheet(Context context,String title,Object[] objs,TextView textView, TextView sub,int tag){
        CBottomSheet cbottomsh=new CBottomSheet.DefaultBuilder()
                .addTitle(title)
                .addRecyHeight(200)

                .addRegister(Unimodel.class, new UniBinder(), (cbsheet,clazz, binder) -> {
                    UniBinder uniBinder=(UniBinder) binder;
                    uniBinder.setClickListener((v, uni) -> {
                        String str=uni.title;
                        if(tag==1) textView.setBackgroundColor(context.getColor(ColorSeletor.getColorByString(str)));
                        if(tag==2){
                            if(str.startsWith("上午")) sub.setText("上午");
                            else if(str.startsWith("下午")) sub.setText("下午");
                            else if(str.startsWith("晚上")) sub.setText("晚上");
                            str=str.replaceAll("^.{2}","");
                        }
                        textView.setText(str);
                        cbsheet.dismiss();
                    });

                })
                .addDatas(objs)
                .build(context);
        cbottomsh.show();
    }
}
