package com.hazukie.scheduleviews.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.MultiTypeAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hazukie.cskheui.Crialoghue.Crialoghue;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.base.BaseActivity;
import com.hazukie.scheduleviews.binders.BinderClickListener;
import com.hazukie.scheduleviews.binders.SchecardBinder;
import com.hazukie.scheduleviews.binders.UniBinder;
import com.hazukie.scheduleviews.custom.TopbarLayout;
import com.hazukie.scheduleviews.fileutil.FileRootTypes;
import com.hazukie.scheduleviews.fileutil.OftenOpts;
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

public class ScheEditActivity extends BaseActivity {
    private MultiTypeAdapter mainAdp,headAdp;
    private ArrayList<Object> main_list,filtered_list,headList;
    private FloatingActionButton floatingActionBtn;
    private TimeHeadModel timeheadModel;
    private String globalTime,globalSche;
    private OftenOpts oftenOpts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sche_make);

        StatusHelper.controlStatusLightOrDark(this, StatusHelper.Mode.Status_Dark_Text);
        getDatas();
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
                new BottomialogUtil(ScheEditActivity.this).
                        showBottomEditedSheet(main_list, filtered_list, mainAdp, timeheadModel, cl, floatingActionBtn, false, weekRID);
            }catch (Exception e){
                e.printStackTrace();
            }
            });

        TopbarLayout titleLabel = findViewById(R.id.class_schedule_title);

        if(globalSche!=null) titleLabel.setTitle(globalSche.replace(".txt",""));
        titleLabel.addLftTextView("预览", v->{
            List<ClassLabel> clssLs=new ArrayList<>();
            CycleUtil.cycle(main_list, (obj, objects) -> clssLs.add((ClassLabel) obj));

            SchePreviewActivity.startActivityWithSche(this,timeheadModel.totalClass,globalTime,clssLs);
        });

        titleLabel.addRightTextView("保存", v1->showDialog());

        TextView saveLabel = findViewById(R.id.schemake_saveLabel);
        saveLabel.setVisibility(View.GONE);

        TextView scheLabel = findViewById(R.id.schemake_scheLabel);
        scheLabel.setVisibility(View.GONE);

        headAdpInits();
        mainAdpInits();
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
                    DialogUtil.showaloh(ScheEditActivity.this,mainAdp,"课程卡片",main_list,filtered_list,classLabel,filtered_index,true);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onEdit(View v, ClassLabel classLabel) {
                new BottomialogUtil(ScheEditActivity.this)
                    .showBottomEditedSheet(main_list,filtered_list,mainAdp,timeheadModel,classLabel,v,true,-1);}
        });

        mainAdp.register(ClassLabel.class,schecardBinder);

        //加载课表数据
        loadInits(globalSche,globalTime);
        filtered_list.addAll(main_list);
        mainRecy.setAdapter(mainAdp);
        mainAdp.setItems(filtered_list);
    }


    private void showDialog(){
        Crialoghue cioh=new Crialoghue.TxtBuilder()
                .addTitle("课表数据保存")
                .addHtmlMode(true)
                .addFontGravity(Gravity.START)
                .addContent("课表名称："+globalSche.replace(".txt","")+"<br/><br/>详细信息<br/><small>当前课表基于&nbsp;<b>"+globalTime.replace(".txt","")+"</b>&nbsp;制作<br/>注：作息表，指的是每日上课时间安排<br/>如需要调整或新建&nbsp;<b>作息表</b>&nbsp;,<br/> 请前往作息表制作界面</small")
                .onConfirm((cialog, rootView) -> writInits(globalSche,cialog))
                .build(this);
        cioh.show();
    }


    //加载课表文件数据
    public void loadInits(String scheName,String timeName){
        //查看上一次是否存储有数据，有则加载，无则加载默认数据
        try{
            List<ClassLabel> clssLs=oftenOpts.getClsList(scheName);
            timeheadModel=oftenOpts.getThm(timeName);
            main_list.clear();
            for(ClassLabel clss:clssLs){
                if(clss.clNums>0) main_list.add(clss);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //写入课表文件数据
    public void writInits(String sche,Crialoghue cih){
        if(main_list.size()>0&&main_list.get(0)!=null){
            oftenOpts.putDataList(FileRootTypes.sches,sche,main_list);
            Toast.makeText(ScheEditActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
            cih.dismiss();
            finish();
        }else if(main_list.size()==0){
            oftenOpts.putDataList(FileRootTypes.sches,sche,new ArrayList<>());
            Toast.makeText(ScheEditActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
            cih.dismiss();
            finish();
        }
    }

    //读取网页链接
    private void getDatas() {
        Intent in_=getIntent();
        String[] names=in_.getStringExtra("name").split(",");
        globalTime =names[1] ;
        globalSche=names[0];
    }

    public static void startActivityWithData(Context context,String scheName,String timeName){
        Intent in=new Intent();
        in.setClass(context, ScheEditActivity.class);
        in.putExtra("name",scheName+","+timeName);
        context.startActivity(in);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            DisplayHelper.showBack(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}