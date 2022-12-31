package com.hazukie.scheduleviews.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.MultiTypeAdapter;
import com.hazukie.cskheui.Crialoghue.Crialoghue;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.base.BaseActivity;
import com.hazukie.scheduleviews.binders.IOSItemBinder;
import com.hazukie.scheduleviews.binders.TimemakeBinder;
import com.hazukie.scheduleviews.custom.TopbarLayout;
import com.hazukie.scheduleviews.fileutil.FileAssist;
import com.hazukie.scheduleviews.fileutil.FileRootTypes;
import com.hazukie.scheduleviews.fileutil.Fileystem;
import com.hazukie.scheduleviews.models.ScheWithTimeModel;
import com.hazukie.scheduleviews.models.TimeHeadModel;
import com.hazukie.scheduleviews.models.TimemakeModel;
import com.hazukie.scheduleviews.models.Timetable;
import com.hazukie.scheduleviews.models.Unimodel;
import com.hazukie.scheduleviews.utils.DisplayHelper;
import com.hazukie.scheduleviews.utils.StatusHelper;

import java.util.ArrayList;
import java.util.List;

public class TimeditActivity extends BaseActivity {
    private List<Object> all;
    private List<Unimodel> amLs,pmLs,mmLs;
    private FileAssist.applyOftenOpts oftenOpts;
    
    private String globalTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timedit);
        StatusHelper.controlStatusLightOrDark(this, StatusHelper.Mode.Status_Dark_Text);
        getDatas();
        oftenOpts=new FileAssist.applyOftenOpts(getApplicationContext());
        initViews();
    }

    private void initViews(){
        MultiTypeAdapter mudp=new MultiTypeAdapter();
        all=new ArrayList<>();

        Button creatDocs=findViewById(R.id.activity_timedit_confirm);
        creatDocs.setOnClickListener(v->assembleDatas(all));

        RecyclerView recy=findViewById(R.id.activity_timedit_recy);
        recy.setHasFixedSize(true);

        TopbarLayout top=findViewById(R.id.activity_timedit_topbar);
        top.setTitle(globalTime.replace(".txt",""));

        amLs = new ArrayList<>();
        pmLs=new ArrayList<>();
        mmLs=new ArrayList<>();
        
        TimeHeadModel thm= oftenOpts.getThm(globalTime);//FileHelper.getThm(this,globalTime);
        if(thm!=null) parseThmData(thm);
        
        all.add(new ScheWithTimeModel(0,"作息表文件名",globalTime.replace(".txt","")));
        all.add(new TimemakeModel(0,"上午时间段",amLs));
        all.add(new TimemakeModel(0,"下午时间段",pmLs));
        all.add(new TimemakeModel(0,"晚上时间段",mmLs));


        IOSItemBinder iosItemBinder=new IOSItemBinder();
        mudp.register(ScheWithTimeModel.class,iosItemBinder);
        TimemakeBinder timemakeBinder=new TimemakeBinder();
        timemakeBinder.setAddCalls(new TimemakeBinder.AddCalls() {
            @Override
            public void addSy(TimemakeModel timemakeModel) {
                Crialoghue coh=new Crialoghue.HeditBuilder()
                        .addTitle("编辑时间")
                        .addHint("请按 0800-0900 格式输入")
                        .onConfirm((crialoghue, view) -> {
                            EditText eidt=(EditText) view;
                            String contents=eidt.getText().toString();
                            if(contents.length()>=9){
                                int sizes=timemakeModel.unis.size();
                                int index_=all.indexOf(timemakeModel);
                                timemakeModel.unis.add(new Unimodel(sizes,contents));
                                mudp.notifyItemChanged(index_,"updating");
                                crialoghue.dismiss();
                            }else{
                                //eidt.setHint("输入内容太短了，请重新输入！");
                                DisplayHelper.Infost(TimeditActivity.this,"输入内容太短了，请重新输入！");
                            }
                        })
                        .build(TimeditActivity.this);

                coh.show();
            }

            @Override
            public void quickAddSy(TimemakeModel timemakeModel) {
                StringBuilder stringBuilder=new StringBuilder();
                for (int i = 0; i < timemakeModel.unis.size(); i++) {
                    stringBuilder.append(timemakeModel.unis.get(i).title).append("\n");
                }
                Crialoghue criloh=new Crialoghue.HeditBuilder()
                        .addTitle("快捷输入")
                        .addHint("请输入每节课具体时间")
                        .addIsMultiEdit(true)
                        .addContents(stringBuilder.toString())
                        .addIsBottomVisible(true)
                        .addIsHtmlMode(true)
                        .addBottomContents("<b><font color=\"#1b88ee\">说明</font></b><br/>每节课的开始、结束时间，每行只能输入一节课时间。<br/>示例：<br/>0930-1045<br/>1145-1230<br/><br/><br/>时间输入格式为：<b>小时分钟-小时分钟</b><br/>例:0200-0420<br/>")
                        .onConfirm((crialoghue, view) -> {
                            EditText eidt=(EditText) view;
                            String contents=eidt.getText().toString();
                            String[] parsedContents=contents.split("\n");
                            List<Unimodel> _unis=new ArrayList<>();
                            for (int i = 0; i < parsedContents.length; i++) {
                                String tr=parsedContents[i].length()>=9?parsedContents[i].substring(0,9):parsedContents[i];
                                if(!tr.isEmpty()){
                                    // tr=tr.replaceAll("(\\d{2})(\\d{2})","$1:$2");
                                    _unis.add(new Unimodel(i,tr));
                                }
                            }

                            timemakeModel.unis.addAll(_unis);
                            int index_=all.indexOf(timemakeModel);
                            mudp.notifyItemChanged(index_,"updating");
                            crialoghue.dismiss();
                        })
                        .build(TimeditActivity.this);
                criloh.setCanceledOnTouchOutside(false);
                criloh.show();
            }
        });
        mudp.register(TimemakeModel.class,timemakeBinder);
        recy.setAdapter(mudp);
        mudp.setItems(all);
    }
    
    private void parseThmData(TimeHeadModel thm){
        int amEnd=thm.amCl;
        int pmEnd=amEnd+thm.pmCl;
        int mmEnd=pmEnd+ thm.mmCl;
        
        for (int i = 0; i < thm.detailClass.size(); i++) {
            Timetable tm=thm.detailClass.get(i);
            if(i<amEnd) amLs.add(new Unimodel(i,tm.getOriginStr()));
            if(i>=amEnd&&i<pmEnd) pmLs.add(new Unimodel(i,tm.getOriginStr()));
            if(i>=pmEnd&&i<mmEnd) mmLs.add(new Unimodel(i,tm.getOriginStr()));
        }
    }


    private void assembleDatas(List<Object> dasa){
        List<Timetable> tms=new ArrayList<>();
        List<Unimodel> mergeLs=new ArrayList<>();
        String docName=globalTime;

        int amStart=0,amCl=0,pmCl=0,mmStart=0,mmCl=0,totalNum=0;
        for (int i = 1; i < dasa.size(); i++) {
            TimemakeModel tim=(TimemakeModel) dasa.get(i);
            if(tim.title.equals("上午时间段")&&tim.unis.size()>0)amCl=tim.unis.size();
            if(tim.title.equals("下午时间段")&&tim.unis.size()>0)pmCl=tim.unis.size();

            if(tim.title.equals("晚上时间段")&&tim.unis.size()>0){
                mmStart=amCl+pmCl;
                mmCl=tim.unis.size();
            }
            mergeLs.addAll(tim.unis);
        }

        for (int j = 0; j < mergeLs.size(); j++) {
            Unimodel uni_=mergeLs.get(j);
            if(!uni_.title.isEmpty()){
                String[] timez= uni_.title.split("-");
                String start=timez[0].replaceAll("(\\d{2})(\\d{2})","$1:$2");
                String end=timez[1].replaceAll("(\\d{2})(\\d{2})","$1:$2");
                Timetable tma=new Timetable(j,start,end);
                tms.add(tma);
            }
        }
        totalNum=amCl+pmCl+mmCl;
        TimeHeadModel timeHeadM=new TimeHeadModel(docName,totalNum,amStart,amCl,amCl,pmCl,mmStart,mmCl,tms);

        //FileHelper fileHelper=new FileHelper(TimeditActivity.this);
        try{
            if(tms.size()>0){
                boolean isWirte2TimeFile= Fileystem.getInstance(getApplicationContext()).putDataz(FileRootTypes.times,docName,timeHeadM);//fileHelper.write(FileHelper.RootMode.times,docName,timeHeadM,false);

                if(isWirte2TimeFile){
                    DisplayHelper.Infost(TimeditActivity.this,"修改成功！");
                    finish();
                }else{
                    DisplayHelper.Infost(TimeditActivity.this,"保存失败！");
                }

            }
            else if(tms.size()==0){
                DisplayHelper.Infost(TimeditActivity.this,"您似乎还没有进行添加操作！");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            DisplayHelper.showBack(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //读取网页链接
    private void getDatas() {
        Intent in_=getIntent();
        String name=in_.getStringExtra("name");
        if(name!=null&&name.length()>0){
            globalTime=name;
        }else{
            globalTime="";
        }
    }

    public static void startActivityWithData(Context context, String timeName){
        Intent in=new Intent();
        in.setClass(context, TimeditActivity.class);
        in.putExtra("name",timeName);
        context.startActivity(in);
    }
}
