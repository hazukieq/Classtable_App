package com.hazukie.scheduleviews.scheutil;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.models.ClassLabel;
import com.hazukie.scheduleviews.models.Timetable;
import com.hazukie.scheduleviews.statics.ColorSeletor;
import com.hazukie.scheduleviews.utils.CycleUtil;

import java.util.ArrayList;
import java.util.List;

public class ScheProcessor {
    private static ScheProcessor instance;
    private final Context context;
    //默认时间格子色为灰白色
    private int default_time_grid_color=0x10000;
    private int default_empty_grid_color=0x10000;

    public ScheProcessor(Context context){ this.context=context; }

    /**
     *
     * @param context 懒汉单例模式
     */
    public static ScheProcessor getInstance(Context context) {
        if(instance==null){
            synchronized (ScheProcessor.class){
                if(instance==null) instance=new ScheProcessor(context);
            }
        }
        return instance;
    }

    public ScheProcessor setDefault_empty_grid_color(int default_empty_grid_color) {
        this.default_empty_grid_color = default_empty_grid_color;
        return this;
    }

    public ScheProcessor setDefault_time_grid_color(int default_time_grid_color) {
        this.default_time_grid_color = default_time_grid_color;
        return this;
    }

    /**
     *
     * @param gridLayout 表格布局
     * @param params [宽度，长度，sideMargin，topMargin，weekNum] 所有格子的总宽度（不包括所有格子的留空值），textView的margin值，周数
     * @param lists 课程数据
     * @param timetables 作息时间数据
     * @param listener 回调接口
     */
    public void processClassDatas(GridLayout gridLayout, int[] params, @NonNull List<ClassLabel> lists, @NonNull List<Timetable> timetables, CardListener listener){

        CycleUtil.cycle(new ArrayList<>(lists), (obj, objects) -> {
            //x为横坐标，表示从第一节到最后一节课的横坐标
            //y为纵坐标，表示从周一到周七的纵坐标
            ClassLabel clzz=(ClassLabel) obj;
            int x=clzz.startCl;
            int y=clzz.week;

            //实例化textView，并设置其长宽值
            //设置textView的字体颜色、留空值、在布局中的位置
            TextView textView = new TextView(context);
            GridLayout.LayoutParams lparams = new GridLayout.LayoutParams();
            textView.setOnClickListener(v -> {
                if(listener!=null) listener.onClick(textView,clzz,timetables);
            });

            //长宽相等的正方形
            lparams.width = lparams.height = params[0]/params[4];//totalWidth/weekNum;
            lparams.setMargins(params[2],params[3],params[2],params[3]);

            textView.setTextSize(12);
            textView.setTextColor(context.getColor(R.color.text_gray));
            textView.setGravity(Gravity.CENTER);

            //判断课程卡片的clNums是否大于0，如果大于则返回true，否则返回false
            boolean checkNums= clzz.clNums > 0;
            if(checkNums)moreOneProcess(textView,lparams,clzz);
            else zeroClProcess(textView,lparams,x,y,timetables);

            gridLayout.addView(textView,lparams);
        });
    }


    //clNums>0时的处理函数
    private void moreOneProcess(TextView textView,GridLayout.LayoutParams params,ClassLabel cls){
        textView.setText(Html.fromHtml(cls.toHtml()));
        textView.setPadding(3,1,3,1);
        textView.setBackgroundColor(context.getColor(ColorSeletor.getColorByIndex(cls.color)));

        //纵坐标y，每一列
        params.columnSpec = GridLayout.spec(cls.week,1, 1f);
        //横坐标x，每一行
        params.rowSpec = GridLayout.spec(cls.startCl,cls.clNums, 1f);
    }


    //clNums总节数为0时的处理函数
    private void zeroClProcess(TextView textView, GridLayout.LayoutParams params, int x,int y,List<Timetable> timetable){
        //找到第一列，将时间数据加载进去
        String content=(x< timetable.size())?timetable.get(x).toString():"";
        if(y==0){
            textView.setText(Html.fromHtml(content));
            int colorbg=default_time_grid_color!=0x10000?default_time_grid_color: context.getColor(R.color.light_white);
            textView.setBackgroundColor(colorbg);
        }else {
            int colorv=default_empty_grid_color!=0x10000?default_empty_grid_color: context.getColor(R.color.white);
            textView.setBackgroundColor(colorv);
        }

        textView.setTextSize(13);
        params.columnSpec = GridLayout.spec(y,1, 1f);
        params.rowSpec = GridLayout.spec(x,1, 1f);
    }

    /**
     * @param mWidth 根布局总宽度
     * @param sideMargin 左右margin值
     * @param topMargin 上下margin值
     * @param weekNum 横列总数
     * @param totalClnnums 纵列总数
     * @return 返回包含长度宽度的数组 [宽度，长度,sideMargin,topMargin,weekNum,]
     */
    public static int[] calculateHeightAndWidth(int mWidth,int sideMargin,int topMargin,int weekNum,int totalClnnums){
        //4为每列 Textview 的左右留空值(margin)，所以表格布局的总宽度=LinearLayout宽度-列数×左右留白值
        int sideMargins=sideMargin*2;
        int width=mWidth-weekNum*sideMargins;

        /* Textview 的上下留空值(margin)=4且为正方形，故表格总宽度=（每个TextView的高度+上下留白值）×所有表格数 */
        int totalMargins=topMargin*2;
        int height=(width/weekNum+totalMargins)*totalClnnums;

        return new int[]{width,height,sideMargin,topMargin,weekNum};
    }
}
