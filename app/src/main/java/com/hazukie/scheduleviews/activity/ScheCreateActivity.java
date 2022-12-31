package com.hazukie.scheduleviews.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.MultiTypeAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.base.BaseActivity;
import com.hazukie.scheduleviews.base.FragmentContainerAct;
import com.hazukie.scheduleviews.binders.BinderClickListener;
import com.hazukie.scheduleviews.binders.SchecardBinder;
import com.hazukie.scheduleviews.binders.UniBinder;
import com.hazukie.scheduleviews.custom.TopbarLayout;
import com.hazukie.scheduleviews.fileutil.OftenOpts;
import com.hazukie.scheduleviews.fragments.ScheFileCreateFrag;
import com.hazukie.scheduleviews.models.ClassLabel;
import com.hazukie.scheduleviews.models.TimeHeadModel;
import com.hazukie.scheduleviews.models.Unimodel;
import com.hazukie.scheduleviews.statics.Statics;
import com.hazukie.scheduleviews.utils.BottomialogUtil;
import com.hazukie.scheduleviews.utils.CycleUtil;
import com.hazukie.scheduleviews.scheutil.ScheDataInitiation;
import com.hazukie.scheduleviews.utils.DialogUtil;
import com.hazukie.scheduleviews.utils.DisplayHelper;
import com.hazukie.scheduleviews.utils.StatusHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ScheCreateActivity extends BaseActivity {
    public static ScheCreateActivity instance;
    private MultiTypeAdapter mainAdp,headAdp;
    private ArrayList<Object> main_list,filtered_list,headList;
    private FloatingActionButton floatingActionBtn;
    private TimeHeadModel timeheadModel;
    private String globalTime="默认课表.txt";
    private OftenOpts oftenOpts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sche_make);
        StatusHelper.controlStatusLightOrDark(this, StatusHelper.Mode.Status_Dark_Text);
        getDatas();
        instance=this;
        Log.i("SchecreateAct>>","globalTimeName="+globalTime);
        oftenOpts=OftenOpts.getInstance(getApplicationContext());
        initViews();
    }

    private void initViews(){
        filtered_list=new ArrayList<>();

        floatingActionBtn=findViewById(R.id.schemake_addLabel);
        floatingActionBtn.setOnClickListener(v->{
            //找到已选中Item的序号，传递其给classlabel模型
            int weekRID=0;
            for(int y=0;y<headList.size();y++){
                Unimodel unimo=(Unimodel)headList.get(y);
                if(unimo.id==1) weekRID=y;
            }

            if(weekRID==0)weekRID=1;
            ClassLabel cl=new ClassLabel(1,0,weekRID,0,"","","",11);
            try {
                if(timeheadModel==null) timeheadModel= new TimeHeadModel("默认作息表",12,0,5,5,4,9,3, ScheDataInitiation.initialTimeDefaults());
                new BottomialogUtil(ScheCreateActivity.this).
                        showBottomEditedSheet(main_list, filtered_list, mainAdp, timeheadModel, cl, floatingActionBtn, false, weekRID);
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        TopbarLayout titleLabel = findViewById(R.id.class_schedule_title);
        titleLabel.setVisibility(View.GONE);


        LinearLayout linear=findViewById(R.id.act_sche_make_linear);
        linear.setVisibility(View.VISIBLE);

        TextView scheLabel = findViewById(R.id.schemake_scheLabel);
        scheLabel.setText("课表预览");

        //跳转预览界面
        scheLabel.setOnClickListener(v->{
            List<ClassLabel> clssLs=new ArrayList<>();
            for(Object obj:main_list) clssLs.add((ClassLabel) obj);
            SchePreviewActivity.startActivityWithSche(this,timeheadModel.totalClass,globalTime,clssLs);
/*            for (int i = 0; i < main_list.size(); i++) {
                ClassLabel cs=(ClassLabel) main_list.get(i);
                clssLs.add(cs);
            }*/
        });

        //跳转保存界面
        TextView saveLabel = findViewById(R.id.schemake_saveLabel);
        saveLabel.setOnClickListener(v->{
                    List<ClassLabel> cxx=new ArrayList<>();
                    CycleUtil.cycle(main_list, (obj, objects) -> cxx.add((ClassLabel) obj));
                    FragmentContainerAct.startActivityWithLoadUrl(this, ScheFileCreateFrag.class,globalTime,cxx);
                });


        headAdpInits();
        mainAdpInits();

        //关闭上一个活动！
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                FragmentContainerAct.instance.finish();
                Log.i("fragAct>>","closed!");
            }
        };
        Timer timer=new Timer();
        timer.schedule(task,380);
    }


    private void headAdpInits(){
        //加载静态数据进去
        List<Unimodel> headWeeks=Arrays.asList(Statics.headWeeks);

        headList=new ArrayList<>();
        headAdp=new MultiTypeAdapter();
        RecyclerView headRecy = findViewById(R.id.recy_horizontalay_recy);
        LinearLayoutManager lm=new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);

        UniBinder uniBinder=new UniBinder();
        //调整item布局
        uniBinder.setJustify(view -> {
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int paddingV= DisplayHelper.dp2px(this,4);
            params.setMargins(paddingV,0,paddingV,0);
            view.setLayoutParams(params);
            view.setPadding(paddingV,paddingV,paddingV,paddingV);
        });

        //控制筛选
        uniBinder.setClickListener((v, uni) -> {
            for(int i=0;i<headList.size();i++){
                Unimodel unimodel=(Unimodel) headList.get(i);
                if(unimodel.id==1) unimodel.id=0;
            }
            uni.id=1;
            headAdp.notifyDataSetChanged();


            //如果选中「全部」则清空筛选列表，并重新加入数据
            boolean isClearAll=uni.title.equals("全部");
            List<ClassLabel> filteredLs=new ArrayList<>();
            CycleUtil.cycle(main_list,(obj, objects) -> {
                ClassLabel cls_=(ClassLabel) obj;
                if(Statics.getWeekBySort(cls_.week).equals(uni.title)) filteredLs.add(cls_);
            });

            filtered_list.clear();
            filtered_list.addAll(filteredLs);
            if(isClearAll){
                //清空过滤列表数据！
                filtered_list.clear();
                filtered_list.addAll(main_list);
            }
            mainAdp.notifyDataSetChanged();
        });

        uniBinder.setIsSelectedMode(true);
        headAdp.register(Unimodel.class,uniBinder);
        headRecy.setLayoutManager(lm);
        headRecy.setAdapter(headAdp);

        headList.addAll(headWeeks);
        headAdp.setItems(headList);

    }


    private void mainAdpInits(){
        RecyclerView mainRecy = findViewById(R.id.schemake_recy);
        mainAdp=new MultiTypeAdapter();
        main_list=new ArrayList<>();

        SchecardBinder schecardBinder=new SchecardBinder();
        schecardBinder.setBinderClickListener(new BinderClickListener() {
            @Override
            public void onDelete(View v, ClassLabel classLabel) {
                //获取索引值,传递给对话框静态方法
                int filtered_index=filtered_list.indexOf(classLabel);
                try{
                    DialogUtil.showaloh(ScheCreateActivity.this,mainAdp,"课程卡片",main_list,filtered_list,classLabel,filtered_index,true);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onEdit(View v, ClassLabel classLabel) {
                new BottomialogUtil(ScheCreateActivity.this)
                        .showBottomEditedSheet(main_list,filtered_list,mainAdp,timeheadModel,classLabel,v,true,-1);

            }
        });

        mainAdp.register(ClassLabel.class,schecardBinder);

        //加载作息表数据
        loadInits(globalTime);

        filtered_list.addAll(main_list);
        mainRecy.setAdapter(mainAdp);
        mainAdp.setItems(filtered_list);
    }



    //加载作息表文件数据
    private void loadInits(String loadFile_time){
        timeheadModel=oftenOpts.getThm(loadFile_time);//FileHelper.getThm(getApplicationContext(),loadFile_time);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            DisplayHelper.showBack(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    protected void onPause() {
        //返回上一个界面时,重置选项到全部,修正筛选器显示错误BUG
        for(int y=0;y<headList.size();y++){
            Unimodel unimo=(Unimodel)headList.get(y);
            if(unimo.id==1) unimo.id=0;
        }
        ((Unimodel)headList.get(0)).id=1;
        super.onPause();
    }

    //读取网页链接
    private void getDatas() {
        Intent in_=getIntent();
        globalTime = in_.getStringExtra("timeName");
    }

    public static void startActivityWithData(Context context, String timeName){
        Intent in=new Intent();
        in.setClass(context,ScheCreateActivity.class);
        in.putExtra("timeName",timeName);
        context.startActivity(in);
    }

}