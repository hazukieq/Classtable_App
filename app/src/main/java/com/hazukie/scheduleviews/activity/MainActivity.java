package com.hazukie.scheduleviews.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.MultiTypeAdapter;
import com.hazukie.cskheui.Crialoghue.Crialoghue;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.base.BaseActivity;
import com.hazukie.scheduleviews.binders.UniBinder;
import com.hazukie.scheduleviews.custom.CBottomSheet;
import com.hazukie.scheduleviews.custom.CPoWin;
import com.hazukie.scheduleviews.custom.CRecyclerView;
import com.hazukie.scheduleviews.custom.TopbarLayout;
import com.hazukie.scheduleviews.databinding.BottomialogBinding;
import com.hazukie.scheduleviews.fragments.Mindmap;
import com.hazukie.scheduleviews.fragments.SchePrevFrag;
import com.hazukie.scheduleviews.models.ClassLabel;
import com.hazukie.scheduleviews.models.ScheWithTimeModel;
import com.hazukie.scheduleviews.models.Timetable;
import com.hazukie.scheduleviews.models.Unimodel;
import com.hazukie.scheduleviews.statics.Statics;
import com.hazukie.scheduleviews.utils.CheckUtil;
import com.hazukie.scheduleviews.utils.ColorSeletor;
import com.hazukie.scheduleviews.utils.DateHelper;
import com.hazukie.scheduleviews.utils.DisplayHelper;
import com.hazukie.scheduleviews.utils.FileHelper;
import com.hazukie.scheduleviews.utils.ScheUIProcessor;
import com.hazukie.scheduleviews.utils.ScreenShotHelper;
import com.hazukie.scheduleviews.utils.SpvalueStorage;
import com.hazukie.scheduleviews.utils.StatusHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private LinearLayout linearLayout,topweeks;
    private TopbarLayout topbarLayout;
    private DrawerLayout drawerLayout;
    private TextView scheName,scheTime;
    private int width__;
    private static final int totalClnnums=12;
    private ScheUIProcessor uiProcessor;
    private List<ScheWithTimeModel> scts;
    private SpvalueStorage sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        StatusHelper.controlStatusLightOrDark(MainActivity.this, StatusHelper.Mode.Status_Dark_Text);
        initViews();
    }

    private void initViews(){
        scts=new ArrayList<>();
        scts=FileHelper.getRecordedScts(getApplicationContext());
        sp=SpvalueStorage.getInstance(getApplicationContext());
        InitWithHeadViews();
        InitWithMainViews();
        InitWithSideViews();
    }

    //处理头部UI逻辑
    private void InitWithHeadViews(){
        topbarLayout = findViewById(R.id.topbar);
        topweeks=findViewById(R.id.topweeks);

        //获取课表头部Views数量
        int childs=topweeks.getChildCount();

        //设置月份
        //设置周数，这里需要开学日期时间！！
        int startMonth=sp.getInt("start_month",9);
        int startDay=sp.getInt("start_day",1);
        int term_totalWeek=sp.getInt("termweeks",18);
        DateHelper dateHelper=new DateHelper.Builder()
                .setStartDate(startMonth,startDay)
                .setTotalNum(term_totalWeek)
                .create();
        TextView txt_refresh=(TextView) topweeks.getChildAt(0);
        String html_="<small>刷新</small><br/><b><font color=\"gray\">%1$s月</font></b>";
        txt_refresh.setText(Html.fromHtml(String.format(html_,dateHelper.getCurrentMont())));

        //设置星期+相应日期
        String[] weekDaitz=dateHelper.returnWeekDayByDaitSetz();
        for(int i=1;i<childs;i++)
            ((TextView) topweeks.getChildAt(i))
                                .setText(Html.fromHtml(weekDaitz[i]));

        String weekth=dateHelper.getGapStr();
        topbarLayout.setTitle(weekth)
                .addLeftTextView(getString(R.string.main_menu), getColor(R.color.text_gray), 18, v -> drawerLayout.openDrawer(Gravity.LEFT))
                .addRightTextView(getString(R.string.main_ouput), getColor(R.color.text_gray), 18, v -> exportSche());

        scheName=findViewById(R.id.change_current_sche);
        scheTime=findViewById(R.id.change_current_time);
        scheName.setOnClickListener(v->popupList(scheName));

        //初始化时检查sp是否存储有课表名称
        initializeSideBar();

        //设置刷新方法
        TextView refrsh=findViewById(R.id.schedul_refresh);
        refrsh.setOnClickListener(v->reloadProcess());
    }


    //加载课表视图数据
    private void InitWithMainViews(){
        linearLayout=findViewById(R.id.main);
        linearLayout.post(() -> {
            //初始化课表布局
            width__=linearLayout.getWidth();
            uiProcessor=new ScheUIProcessor(MainActivity.this,linearLayout,width__,totalClnnums);
            uiProcessor.setScheInformation(this::showDetails);
            try {
                //课表数据按流程进行加载
                uiProcessor.renderUI();
            } catch (IOException e) { e.printStackTrace(); }
        });

    }

    //加载侧边栏数据
    private void InitWithSideViews(){
        drawerLayout=findViewById(R.id.schedulest_drawer);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        initSideRecys();
    }

    //初始化侧边栏Recy
    private void initSideRecys(){
        CRecyclerView recy = findViewById(R.id.main_side_recy);
        MultiTypeAdapter mul=new MultiTypeAdapter();
        UniBinder unibin=new UniBinder();
        List<Object> ojs=new ArrayList<>();

        ojs.add(new Unimodel(0,getString(R.string.main_side_schemak)));
        ojs.add(new Unimodel(1,getString(R.string.main_side_manage)));
        ojs.add(new Unimodel(5,getString(R.string.main_side_note)));
        ojs.add(new Unimodel(4,"思维导图"));
        ojs.add(new Unimodel(2,getString(R.string.main_side_setting)));
        ojs.add(new Unimodel(3,getString(R.string.main_side_about)));
        //ojs.add(new Unimodel(6,"云桌面"));


        unibin.setJustify(txt->{
            txt.setGravity(Gravity.START);
            int pa=DisplayHelper.dp2px(this,8);
            txt.setPadding(pa,pa,0,pa);
            txt.setTextSize(18);
        });

        unibin.setClickListener((v, uni) -> {
            int id_=uni.id;
            switch (id_){
                case 0:
                    FragmentContainerAct.startActivityWithLoadUrl(this, SchePrevFrag.class);
                    break;
                case 1:
                    startAct2Act(this,ManageAct.class);
                    break;
                case 2:
                    startAct2Act(this,SettingActivity.class);
                    break;
                case 3:
                    startAct2Act(this,AboutActivity.class);
                    break;
                case 4:
                    QuickNoteActivity.startActivityWithLoadUrl(this,QuickNoteActivity.class,"https://www.hazukieq.top/quickmind/mind.html","","");
                    break;
                case 5:
                    QuickMindActivity.startActivityWithLoadUrl(this,QuickMindActivity.class,"https://www.hazukieq.top/quickote/editor.html","","");
                    //FragmentContainerAct.startActivityWithLoadUrl(this,Mindmap.class);
                    break;
                case 6:
                    //FragmentContainerAct.startActivityWithLoadUrl(this, Mindmap.class,false);
                    QuickNoteActivity.startActivityWithLoadUrl(this,QuickMindActivity.class,"http://cloud.hazukieq.top","","");
                    break;
                default:
            }
        });

        mul.register(Unimodel.class,unibin);
        recy.setAdapter(mul);
        mul.setItems(ojs);
    }

    //更新侧边栏头部数据
    private void updateSidebarTexts(String shce,String time){
        scheName.setText(""+shce);
        scheTime.setText(""+time);
    }


    //初始化时检查record_id,并更新边栏数据
    private void initializeSideBar(){
        boolean inValue=scts.size()>0;
        Log.i("MainActivity-initializeSideBar>>>","inValue="+inValue);
        //record_name默认为二进制，当为二进制时则说明未加载或索引总表长度为0
        String record_name=sp.getStringValue("record_name",Statics.record_name_default);

        if(!record_name.equals(Statics.record_name_default)){
            ScheWithTimeModel sct_=FileHelper.getSctByName(getApplicationContext(),record_name);
            updateSidebarTexts(sct_.getScheName(),sct_.getTimeName());
        }else{
            updateSidebarTexts(getString(R.string.main_empty_table),getString(R.string.main_default_time_table));
            sp.setStringvalue("record_name",Statics.record_name_default);
        }
    }




    //刷新数据，切换课表
    private void reloadProcess(){
        try {
            linearLayout.removeAllViews();
            uiProcessor.renderUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //清空数据，切换课表
    private void changeScheProcess(String name){
        try{
            Log.i("MainActivity-changeScheProcess>>","name="+name);
            uiProcessor.changeSche(name);
            DisplayHelper.Infost(this,getString(R.string.main_toast_success_change));
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    /*
     * 导出截图
     */
    private void exportSche(){
        ScrollView scrollView=new ScrollView(this);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(DisplayHelper.dp2px(this,280),0);
        params.weight=4;
        scrollView.setBackgroundColor(getColor(R.color.dim_bg));

        Bitmap topw=ScreenShotHelper.screenshot(topweeks,Bitmap.Config.ARGB_8888);
        Bitmap sch= ScreenShotHelper.screenshot(linearLayout,Bitmap.Config.ARGB_8888);
        Map<Integer,Bitmap> map= new HashMap<>();
        map.put(0,topw);
        map.put(topweeks.getHeight(),sch);
        Bitmap mergeBitmap=ScreenShotHelper.merge(linearLayout.getWidth(),linearLayout.getHeight()+topweeks.getHeight(),map,Bitmap.Config.ARGB_8888);

        Crialoghue crialoh=new Crialoghue.ImgBuilder()
                .addTitle(getString(R.string.main_output_screenshot))
                .addDrawableSrc(mergeBitmap)
                .onConfirm((dialog, view) -> {
                    if(ScreenShotHelper.saveImg(this,mergeBitmap,getString(R.string.main_exportSche_name))) DisplayHelper.Infost(this,"导出成功");
                    else{
                        if(ScreenShotHelper.saveImgBySys(this,mergeBitmap,getString(R.string.main_exportSche_name))) DisplayHelper.Infost(this,"导出成功");
                        else DisplayHelper.Infost(this,getString(R.string.main_ungranted_permis_tip));
                    }
                    dialog.dismiss();
                }).build(this);
        crialoh.show();
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
                    if(cls_lq) pinding.classTime.setText(Timetable.exportTimes(times,cls.startCl,cls.clNums));//cls_time.setText(Timetable.exportTimes(times,cls.startCl,cls.clNums));
                    else pinding.classTime.setText(cls_l);//cls_time.setText(cls_l);

                    //地点
                    pinding.classPlace.setText(cls.clRoom);//cls_place.setText(cls.clRoom);
                    //备注
                    pinding.classPlanote.setText(cls.plaNote);//cls_planote.setText(cls.plaNote);
                })
                .addScheObj(cls)
                .build(this);
        cbottomsh.show();
        cbottomsh.setOnCancelListener(dialog -> v.setBackgroundColor(getColor(ColorSeletor.getColorByIndex(cls.color))));
    }


    public  void popupList(View attatchedView) {
        CPoWin cpow=new CPoWin.Builder()
                .addWidth(100)
                .addView(R.layout.popup_lay, MainActivity.this, (pop,viewGroup) -> {
                    TextView txt=viewGroup.findViewById(R.id.popup_create);
                    txt.setText("空表");
                    txt.setOnClickListener(v->{
                        updateSidebarTexts(getString(R.string.main_empty_table),getString(R.string.main_default_time_table));
                        //更新sp值
                       sp.setStringvalue("record_name",Statics.record_name_default);
                        //刷新课表
                        changeScheProcess(Statics.record_name_default);
                        pop.dismiss();
                    });

                    RecyclerView recy_=viewGroup.findViewById(R.id.popup_recy);
                    recy_.setLayoutManager(new LinearLayoutManager(this));
                    MultiTypeAdapter muDp=new MultiTypeAdapter();
                    List<Object> viewList=new ArrayList<>();
                    int pa=DisplayHelper.dp2px(this,8);
                    recy_.setPadding(pa,pa,pa,pa);

                    for(ScheWithTimeModel sct:scts){
                        viewList.add(new Unimodel(scts.indexOf(sct),sct.getScheName()));
                    }

                    UniBinder unib = new UniBinder();
                    unib.setClickListener((v, uni) -> {
                        //更新sp值
                        sp.setStringvalue("record_name",uni.title);
                        //读取数据
                        ScheWithTimeModel sct=FileHelper.getSctByName(getApplicationContext(),uni.title);

                        //更新侧边栏文字
                        updateSidebarTexts(sct.getScheName(), sct.getTimeName());
                        //刷新课表
                        changeScheProcess(uni.title);
                        pop.dismiss();
                    });
                    muDp.register(Unimodel.class, unib);
                    muDp.setItems(viewList);
                    recy_.setAdapter(muDp);
                })
                .build(this);

        //当记录课表数据为0时，也展示弹窗
        cpow.showAsDropDown(attatchedView,0,0);
    }

    //处理Resume周期中更新UI视图的任务
    private void resh(){
        scts.clear();
        scts.addAll(FileHelper.getRecordedScts(getApplicationContext()));

        String record=sp.getStringValue("record_name",Statics.record_name_default);
        ScheWithTimeModel mSct=FileHelper.getSctByName(getApplicationContext(),record);
        if(mSct.getScheName().equals("空表")) sp.setStringvalue("record_name",Statics.record_name_default);
        updateSidebarTexts(record,mSct.getTimeName());

        reloadProcess();
        Log.i("MainActivity-onResume>>>","record_name="+record+", sctList has updated,sctListSize="+scts.size());
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            //设置周数，这里需要开学日期时间！！
            int startMonth=sp.getInt("start_month",9);
            int startDay=sp.getInt("start_day",1);
            int term_totalWeek=sp.getInt("termweeks",18);
            DateHelper dateHelper=new DateHelper.Builder()
                    .setStartDate(startMonth,startDay)
                    .setTotalNum(term_totalWeek)
                    .create();
            String weekth=dateHelper.getGapStr();
            topbarLayout.setTitle(weekth);
            if(linearLayout!=null&&linearLayout.getWidth()>0&&linearLayout.getHeight()>0) resh();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}