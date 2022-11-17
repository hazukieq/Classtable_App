package com.hazukie.scheduleviews.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.base.BaseActivity;
import com.hazukie.scheduleviews.custom.CBottomSheet;
import com.hazukie.scheduleviews.databinding.BottomialogBinding;
import com.hazukie.scheduleviews.models.ClassLabel;
import com.hazukie.scheduleviews.models.Timetable;
import com.hazukie.scheduleviews.statics.Statics;
import com.hazukie.scheduleviews.utils.CheckUtil;
import com.hazukie.scheduleviews.utils.ColorSeletor;
import com.hazukie.scheduleviews.utils.DateHelper;
import com.hazukie.scheduleviews.utils.ScheUIProcessor;
import com.hazukie.scheduleviews.utils.StatusHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PreviewActivity extends BaseActivity {
    private List<ClassLabel> clssLs;
    private LinearLayout scheContainer;
    private int totalClass=12;
    private int mwidth=400;
    private String default_time="默认作息表.txt";

    private ScheUIProcessor uiProcessor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        StatusHelper.controlStatusLightOrDark(this, StatusHelper.Mode.Status_Light_Text);
        clssLs=new ArrayList<>();

        LinearLayout lay=findViewById(R.id.topweeks);
        lay.setBackgroundColor(getColor(R.color.tri_transparency));
        int childs=lay.getChildCount();
        TextView head=(TextView) lay.getChildAt(0);
        String html_="<b><font color=\"gray\">%1$s月</font></b>";
        DateHelper dateHelper=new DateHelper();
        head.setText(Html.fromHtml(String.format(html_,dateHelper.getCurrentMont())));
        String[] weekz=dateHelper.returnWeekDayByDaitSetz();
        for (int i = 1; i < childs; i++) {
            TextView tx=(TextView) lay.getChildAt(i);
            tx.setText(Html.fromHtml(weekz[i]));
        }

        getDatas();

        scheContainer=findViewById(R.id.preview);
        scheContainer.post(() -> {
            //初始化课表布局
            mwidth=scheContainer.getWidth();
            uiProcessor=new ScheUIProcessor(PreviewActivity.this,scheContainer,mwidth,totalClass);
            uiProcessor.setDefaultColors(getColor(R.color.tri_transparency),getColor(android.R.color.transparent));
            uiProcessor.setScheInformation(this::showDetails);
            try {
                //课表数据按流程进行加载
                uiProcessor.directRenderUI(clssLs,default_time);
            } catch (Exception e) { e.printStackTrace(); }
        });

    }

    //初始化对话框
    private void showDetails(TextView v,ClassLabel cls,List<Timetable> times){
        v.setBackgroundColor(getColor(R.color.white));
        CBottomSheet cbottomsh=new CBottomSheet.Builder()
                .addView(R.layout.bottomialog, this, (CBottomSheet.InterceptBinding) (cBottomSheet, binding) -> {
                    //初始化对话框布局
                    BottomialogBinding pinding=(BottomialogBinding) binding;
                    pinding.classSubject.setText(cls.subjectName);

                    //日期
                    String temp_ = getString(R.string.dialog_cls_time_template);
                    pinding.classCalendar.setText(String.format(temp_, Statics.getWeekBySort(cls.week),
                            CheckUtil.calculatStartCls(cls.startCl),
                            CheckUtil.calculateEndStartCls(cls.startCl,cls.clNums),cls.clNums));
                    //自定义时间段
                    String cls_l=cls.getCustomTime();
                    boolean cls_lq= TextUtils.isEmpty(cls_l);
                    Log.i( "initDialogContent>>",""+cls_lq+","+cls_l);
                    if(cls_lq) pinding.classTime.setText(Timetable.exportTimes(times,cls.startCl,cls.clNums));
                    else pinding.classTime.setText(cls_l);

                    //地点
                    pinding.classPlace.setText(cls.clRoom);
                    //备注
                    pinding.classPlanote.setText(cls.plaNote);
                })
                .addScheObj(cls)
                .build(this);
        cbottomsh.show();
        cbottomsh.setOnCancelListener(dialog -> v.setBackgroundColor(getColor(ColorSeletor.getColorByIndex(cls.color))));
    }

    private void getDatas(){
        Intent in_=getIntent();
        Bundle bun=in_.getExtras();
        List<ClassLabel> objs=(List<ClassLabel>) bun.getSerializable("lis");
        if(objs.size()>0) clssLs.addAll(objs);
        String time=in_.getStringExtra("time");
        if(time.length()>0) default_time=time;
        totalClass= in_.getIntExtra("total",12);
        Log.i("PreviewActivity-getDatas>>","objs="+objs.size()+", time="+time+", totalClass="+totalClass);
    }

    /**
     *
     * @param context 当前活动柄
     * @param clss 数据列表
     */
    public static void startActivityWithSche(Context context, int total,String timeName,List<ClassLabel> clss) {
        Intent intent = new Intent(context, PreviewActivity.class);
        Bundle bun=new Bundle();
        bun.putSerializable("lis", (Serializable) clss);
        intent.putExtra("time",timeName);
        intent.putExtra("total",total);
        intent.putExtras(bun);
        context.startActivity(intent);
    }
}
