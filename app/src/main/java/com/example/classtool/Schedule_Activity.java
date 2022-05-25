package com.example.classtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.drakeet.multitype.MultiTypeAdapter;
import com.example.classtool.base.BasicActivity;
import com.example.classtool.base.OnItemClick;
import com.example.classtool.binders.SchedulCardBinder;
import com.example.classtool.models.ClassLabel;
import com.example.classtool.models.Class_cardmodel;
import com.example.classtool.models.FindSort;
import com.example.classtool.models.QTime;
import com.example.classtool.binders.UniversalBinder;
import com.example.classtool.models.SchedulModel;
import com.example.classtool.utils.CalculatLayViews;
import com.example.classtool.utils.FilesUtil;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.skin.QMUISkinHelper;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.skin.QMUISkinValueBuilder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIDrawableHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.util.ArrayList;
import java.util.List;

public class Schedule_Activity extends BasicActivity {

    private TextView classl_1,classl_2,classl_3,classl_4,classl_5,classl_6,classl_7,classl_8,classl_9,classl_10,classl_11,classl_12,classl_13,classl_14,classl_15,classl_16,classl_17,classl_18,schedul_refresh,output;
    private ArrayList<String> classSas=new ArrayList<>();
    private GridLayout gridClasses;
    private ImageButton popup_close_btn;
    private TextView titleq;
    private DrawerLayout drawerLayout;
    private TextView menu;
    private ArrayList<Object> alls=new ArrayList<>();
    private RecyclerView recy;
    private MultiTypeAdapter multiTypeAdapter;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private QMUISkinValueBuilder builder;
    private QMUIFrameLayout frameLayout;
    private View mainlayout;
    private QMUIFullScreenPopup popups;
    private EditText title_edit;
    private QMUIRoundButton popup_save_btn;
    private LinearLayout topweek,classLay,fillia;
    private ScrollView classScro;


    private List<Class_cardmodel> classqall=new ArrayList<>();
    private List<TextView> textViews=new ArrayList<>();
    private  List<ClassLabel> cls=new ArrayList<>();
    private CalculatLayViews calculatLayViews;
    private String current_sche="临时课表";
    private String current_sche_time="武鸣校区作息时间";
    private boolean isGranted=false;
    String[] weeks=new String[]{
            "星期一","星期二","星期三","星期四","星期五","星期六","星期日",
    };
    private  String[] start_classes=new String[]{
            "第1节课", "第2节课", "第3节课", "第4节课", "第5节课",
            "第6节课", "第7节课", "第8节课", "第9节课",
            "第10节课", "第11节课", "第12节课", "第13节课"
    };

    private  String[] class_nums=new String[]{
            "1节","2节","3节","4节","5节",
    };

    private static String[] Clme=new String[]{
            "08:20<br>09:00","09:05<br>09:45","09:55<br>10:35","10:45<br>11:25","11:30<br>12:10",
            "14:00<br>14:40","14:45<br>15:25","15:35<br>16:15","16:20<br>17:00",
            "19:00<br>19:45","19:55<br>20:40","20:50<br>21:35",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedulest);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        sp= PreferenceManager.getDefaultSharedPreferences(Schedule_Activity.this);
        editor=sp.edit();
        gridClasses=(GridLayout) findViewById(R.id.detailClasses);
        topweek=(LinearLayout)findViewById(R.id.topweeks);
        fillia=(LinearLayout)findViewById(R.id.filia);
        classLay=(LinearLayout)findViewById(R.id.ClassLay);
        classScro=(ScrollView)findViewById(R.id.scheScro);

        inits();
        int firstL=sp.getInt("firstL",0);
        if(firstL==0){
            editor.putInt("firstL",1);
            editor.commit();
            initClTime();
        }

        LoadScheTags();
        initClassl();
        controlTexts();
    }

    //开始加载相关数据
    private void LoadScheTags(){
        int ScheSelected=sp.getInt("ScheSelected",0);
        List<String> sches=FilesUtil.readSchedulAndTimeTag(getApplicationContext());
        if(sches.size()>0){
            String[] setqs=sches.get(ScheSelected).split(",");
            current_sche=setqs[0];
            current_sche_time=setqs[1];
        }

        List<QTime> dall=FilesUtil.readClassTime(getApplicationContext(),current_sche_time);
        int y=dall.size();
        if(dall.size()==0||dall==null) y=12;
        editor.putInt("classLen",y);
        editor.commit();
        //int classLen=sp.getInt("classLen",y);
        classqall.addAll(FilesUtil.readFileData(getApplicationContext(),current_sche));
        initRecy();
        titleq.setText(current_sche);
        if(classqall.size()>0){
            initGrids();
        }
    }

    //提前创建默认作息时间文件
    private void initClTime(){
        List<QTime> dao=new ArrayList<>();
        for(int i=0;i<12;i++){

            if(i<5&&i>0){
                dao.add(new QTime(i,"上午第"+(i+1)+"节",Clme[i].replace("<br>","<br/>")));
            }else if(i>=5&&i<10){
                dao.add(new QTime(i,"下午第"+(i+1)+"节",Clme[i].replace("<br>","<br/>")));
            }else{
                dao.add(new QTime(i,"晚上第"+(i+1)+"节",Clme[i].replace("<br>","<br/>")));
            }

        }
        List<String> timetags=FilesUtil.readTimeTag(getApplicationContext());
        List<String> schetimtag=FilesUtil.readSchedulAndTimeTag(getApplicationContext());

        int f=0,y=0;
        for(String str:timetags){
            if(str.split(",")[0].equals("武鸣校区作息时间")){
                f+=1;
            }
        }

        if(f==0) FilesUtil.AppendTimeTag(getApplicationContext(), "武鸣校区作息时间,第1节课,5节,第6节课,4节,第10节课,3节");
        for(String tr:schetimtag){
            if(tr.split(",")[0].equals("临时课表")){
               y+=1;
            }

        }
        if(y==0)FilesUtil.AppendScheDulAndTimeTag(getApplicationContext(),"临时课表,武鸣校区作息时间");
        FilesUtil.AppendClassTime(getApplicationContext(),dao,"武鸣校区作息时间");

    }


    private void inits(){
        drawerLayout=(DrawerLayout) findViewById(R.id.schedulest_drawer);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        menu=(TextView) findViewById(R.id.menu);
        multiTypeAdapter=new MultiTypeAdapter();
        titleq=(TextView) findViewById(R.id.class_schedule_title);
        recy=(RecyclerView) findViewById(R.id.scehdul_recy);
        output=(TextView)findViewById(R.id.output);
        schedul_refresh=(TextView)findViewById(R.id.schedul_refresh);
        schedul_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SchedulReresh();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            output.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    QMUIDialog.CustomDialogBuilder dialogBuilder=new QMUIDialog.CustomDialogBuilder(Schedule_Activity.this);
                    dialogBuilder.setSkinManager(QMUISkinManager.defaultInstance(Schedule_Activity.this));
                    dialogBuilder.setLayout(R.layout.output_img_layout);
                    final QMUIDialog dialog=dialogBuilder.setTitle("导出课表截图").create();
                    ImageView imageView=(ImageView) dialog.findViewById(R.id.createFromViewDisplay);
                    TextView cancel=(TextView) dialog.findViewById(R.id.output_cancel);
                    TextView confirm=(TextView) dialog.findViewById(R.id.output_confirm);
                    int h=0;
                    for(int i=0;i<classScro.getChildCount();i++){
                        h+=classScro.getChildAt(i).getHeight();
                    }

                     Bitmap sche_bitmap= Bitmap.createBitmap(classScro.getWidth(),h,Bitmap.Config.RGB_565);
                    final Canvas sche_canvas=new Canvas(sche_bitmap);
                    classScro.draw(sche_canvas);

                    Bitmap top_bitmap=Bitmap.createBitmap(fillia.getWidth(),fillia.getHeight(),Bitmap.Config.RGB_565);
                    Canvas top_canvas=new Canvas(top_bitmap);
                    fillia.draw(top_canvas);

                    Bitmap week_bitmap=Bitmap.createBitmap(topweek.getWidth(),topweek.getHeight(),Bitmap.Config.RGB_565);
                    final Canvas week_canvas=new Canvas(week_bitmap);
                    topweek.draw(week_canvas);

                    int w=topweek.getWidth();
                    int he=week_bitmap.getHeight()+sche_bitmap.getHeight()+top_bitmap.getHeight();
                    Bitmap bitmap=Bitmap.createBitmap(w,he,Bitmap.Config.RGB_565);
                    Canvas canvas=new Canvas(bitmap);

                    Paint p=new Paint();
                    canvas.drawBitmap(top_bitmap,0,0,p);
                    canvas.drawBitmap(week_bitmap,0,top_bitmap.getHeight(),p);
                    canvas.drawBitmap(sche_bitmap,0,top_bitmap.getHeight()+week_bitmap.getHeight(),p);
                    imageView.setImageBitmap(bitmap);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                boolean isSave= FilesUtil.saveImg(Schedule_Activity.this,bitmap,"课表图片");
                                if(isSave){
                                    Toast.makeText(Schedule_Activity.this, "课表图片保存成功,请前往相册查看！", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(Schedule_Activity.this, "课表图片保存失败", Toast.LENGTH_SHORT).show();
                                }
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });

        }
    }

    private void initRecy(){
        alls.add(new SchedulModel(5,current_sche,current_sche_time));
        alls.add(new QTime(6,"","编辑课表标题"));
        alls.add(new QTime(0,"","课表时间设定"));
        alls.add(new QTime(1,"","周课表制作"));
        alls.add(new QTime(2,"","学期模板制作"));
        alls.add(new QTime(3,"","设置"));
        alls.add(new QTime(4,"","关于"));

        UniversalBinder universalBinder=new UniversalBinder();
        universalBinder.setOnItemClick(new OnItemClick() {
            @Override
            public void onClick(View v, int position, int sirtq, String sortq, String str) {
                Intent inq=new Intent();
                switch (sirtq){
                    case 0:
                        inq.setClass(Schedule_Activity.this,TimelistAct.class);
                       // drawerLayout.closeDrawers();
                        startActivity(inq);
                        break;
                    case 1:
                        inq.setClass(Schedule_Activity.this,MainActivity.class);
                        startActivity(inq);
                        break;
                    case 4:
                        inq.setClass(Schedule_Activity.this,AboutActivity.class);
                        startActivity(inq);
                        break;
                    case 6:
                        showpop(recy);
                        break;
                    case 3:
                        inq.setClass(Schedule_Activity.this,SettingActivity.class);
                        startActivity(inq);
                        break;
                    case 2:
                        new QMUIDialog.MessageDialogBuilder(Schedule_Activity.this)
                                .setTitle("学期模板制作")
                                .setSkinManager(QMUISkinManager.defaultInstance(Schedule_Activity.this))
                                .setMessage("此功能，作者并没有开发...敬请期待！")
                                .addAction("知道了", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                })
                                .create( R.style.DialogTheme2).show();
                        break;
                }
            }
        });

        SchedulCardBinder schedulCardBinder=new SchedulCardBinder();
        schedulCardBinder.setOnScheClick(new SchedulCardBinder.OnScheClick() {
            @Override
            public void onClick(TextView v,TextView qrtime, int position, int sirtq, String sche, String time) {
                switch (sirtq){
                    case 5:
                        List<String> sches=FilesUtil.readSchedulAndTimeTag(getApplicationContext());
                        String[]  s=new String[sches.size()];
                        String[] times=new String[sches.size()];
                        if(sches.size()>0){
                            for(int i=0;i<sches.size();i++) {
                                String[] d=sches.get(i).split(",");
                                s[i]=d[0];
                                times[i]=d[1];
                            }
                        }
                        showBottom("课表列表",s,times,v,qrtime,FindSort.returnColorSort(s,v.getText().toString()));
                        break;
                }
            }
        });

        multiTypeAdapter.register(QTime.class,universalBinder);
        multiTypeAdapter.register(SchedulModel.class,schedulCardBinder);
        recy.setAdapter(multiTypeAdapter);
        multiTypeAdapter.setItems(alls);
    }

    private void SchedulReresh(){
        int ScheSelected=sp.getInt("ScheSelected",0);
        List<String> sches=FilesUtil.readSchedulAndTimeTag(getApplicationContext());
        if(sches.size()>0){
            String[] setqs=sches.get(ScheSelected).split(",");
            current_sche=setqs[0];
            current_sche_time=setqs[1];
        }
        List<QTime> dall=FilesUtil.readClassTime(getApplicationContext(),current_sche_time);
        int y=dall.size();
        if(dall.size()==0||dall==null) y=12;
        editor.putInt("classLen",y);
        editor.commit();

        List<Class_cardmodel> newqall=FilesUtil.readFileData(getApplicationContext(),current_sche);
        classqall.clear();
        cls.clear();
        classqall.addAll(newqall);
        controlTexts();
        gridClasses.removeAllViews();
        initGrids();
        //Toast.makeText(Schedule_Activity.this, "课表数据刷新成功！", Toast.LENGTH_SHORT).show();
    }

    private void initClassl(){
        classl_1=(TextView) findViewById(R.id.sche_classl_1);
        classl_2=(TextView) findViewById(R.id.sche_classl_2);
        classl_3 =(TextView) findViewById(R.id.sche_classl_3);
        classl_4=(TextView) findViewById(R.id.sche_classl_4);
        classl_5=(TextView) findViewById(R.id.sche_classl_5);
        classl_6=(TextView) findViewById(R.id.sche_classl_6);
        classl_7=(TextView) findViewById(R.id.sche_classl_7);
        classl_8=(TextView) findViewById(R.id.sche_classl_8);
        classl_9=(TextView) findViewById(R.id.sche_classl_9);
        classl_10=(TextView) findViewById(R.id.sche_classl_10);
        classl_11=(TextView) findViewById(R.id.sche_classl_11);
        classl_12=(TextView) findViewById(R.id.sche_classl_12);
        classl_13=(TextView) findViewById(R.id.sche_classl_13);
        classl_14=(TextView) findViewById(R.id.sche_classl_14);
        classl_15=(TextView) findViewById(R.id.sche_classl_15);
        classl_16=(TextView) findViewById(R.id.sche_classl_16);
        classl_17=(TextView) findViewById(R.id.sche_classl_17);
        classl_18=(TextView) findViewById(R.id.sche_classl_18);

        textViews.add(classl_1);
        textViews.add(classl_2);
        textViews.add(classl_3);
        textViews.add(classl_4);
        textViews.add(classl_5);
        textViews.add(classl_6);
        textViews.add(classl_7);
        textViews.add(classl_8);
        textViews.add(classl_9);
        textViews.add(classl_10);
        textViews.add(classl_11);
        textViews.add(classl_12);
        textViews.add(classl_13);
        textViews.add(classl_14);
        textViews.add(classl_15);
        textViews.add(classl_16);
        textViews.add(classl_17);
        textViews.add(classl_18);

    }


    private void controlTexts(){
        int classLen=sp.getInt("classLen",12);
        Log.i( "controlTexts: ","classLen:"+classLen);
        int rest=textViews.size()-classLen;
        for(int i=0;i<textViews.size();i++){
            if(i<classLen){
                textViews.get(i).setVisibility(View.VISIBLE);
            }else if(i>=classLen&&i< textViews.size()){
                textViews.get(i).setVisibility(View.GONE);
            }
        }
        iniTexs();
    }

    private void iniTexs(){
        List<QTime> qimes=new ArrayList<>();
        int classLen=sp.getInt("classLen",12);
        qimes=FilesUtil.readClassTime(getApplicationContext(),current_sche_time);

        if(qimes.size()>0){
            for (int d = 0; d < classLen; d++) {
                textViews.get(d).setText(Html.fromHtml("<big>" + (d + 1) + "</big>" + "<br><font color='gray'><small>" + qimes.get(d).getStart2end().replace("-", "<br/>") + "</small></font><br>"));
            }
        }

    }

    private void initGrids(){
        int classLen=sp.getInt("classLen",12);
        calculatLayViews=new CalculatLayViews(classLen);
                gridClasses.setOrientation(GridLayout.HORIZONTAL);
                //gridClasses.setColumnCount(7);
                for(Class_cardmodel l:classqall){
                    int x= FindSort.returnColorSort(weeks,l.getClass_date().substring(0,3));
                    int y=FindSort.returnColorSort(start_classes,l.getClass_startClass());
                    int numcls=FindSort.returnColorSort(class_nums,l.getClass_totalClass())+1;
                    String course="<big>"+l.getClass_course()+"</big><br><br>"+l.getClass_classPlace()+"<br/><br/>"+l.getOtherNotes()+"<br/></font>";

                    cls.add(new ClassLabel(numcls,y,x,course,l.getClassColor()));
                }
                List<ClassLabel> lapall=calculatLayViews.Excheng_Classes(cls);
                List<ClassLabel> apall=calculatLayViews.ReturnHtmlindex(lapall);

                for(int h=0;h<apall.size();h++) {
                    ClassLabel l = apall.get(h);
                    int x=l.getWeek();
                    int y=l.getStart_class();
                    TextView textView = new TextView(this);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = 0;
                    params.height = 0;
                    if (l.getClass_nums() > 0) {
                        params.columnSpec = GridLayout.spec(x,1, 1f);
                        params.rowSpec = GridLayout.spec(y, l.getClass_nums(), 1f);
                        textView.setBackgroundColor(getColor(l.getColorq()));

                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                textView.setBackgroundColor(getColor(R.color.light_white));
                                new QMUIDialog.MessageDialogBuilder(Schedule_Activity.this)
                                        .setTitle("课程卡片")
                                        .setSkinManager(QMUISkinManager.defaultInstance(Schedule_Activity.this))
                                        .setMessage(Html.fromHtml(l.getSubjectplanotes().replace("<br><br>","<br><br>上课地点：").replace("<br/><br/>","<br/>备注：")+"上课时间："+weeks[l.getWeek()]+"第"+(l.getStart_class()+1)+"节<br/>共计节数："+l.getClass_nums()+"节"))
                                        .addAction("知道了", new QMUIDialogAction.ActionListener() {
                                            @Override
                                            public void onClick(QMUIDialog dialog, int index) {
                                                textView.setBackgroundColor(getColor(l.getColorq()));
                                                dialog.dismiss();
                                            }
                                        })
                                        .create( R.style.DialogTheme2).show();
                            }
                        });
                    } else {
                        params.columnSpec = GridLayout.spec(x, 1,1f);
                        params.rowSpec = GridLayout.spec(y,1, 1f);
                        textView.setBackgroundColor(getColor(R.color.white));
                    }

                    textView.setTextColor(getColor(com.qmuiteam.qmui.R.color.qmui_config_color_gray_2));
                    textView.setTextSize(10f);
                    textView.setGravity(Gravity.CENTER);
                    textView.setPadding(4,6,4,4);
                    textView.setText(Html.fromHtml(l.getSubjectplanotes()));
                    params.setMargins(3, 2, 3, 2);
                    gridClasses.addView(textView,params);
                }
        }


    private void showpop(View AttachV){
        builder = QMUISkinValueBuilder.acquire();
        frameLayout = new QMUIFrameLayout(Schedule_Activity.this);
        frameLayout.setBackground(
                QMUIResHelper.getAttrDrawable(Schedule_Activity.this, com.qmuiteam.qmui.R.attr.qmui_skin_support_popup_bg));
        builder.background(com.qmuiteam.qmui.R.attr.qmui_skin_support_popup_bg);
        QMUISkinHelper.setSkinValue(frameLayout, builder);
        frameLayout.setRadius(QMUIDisplayHelper.dp2px(Schedule_Activity.this, 12));
        int padding = QMUIDisplayHelper.dp2px(Schedule_Activity.this, 8);
        frameLayout.setPadding(padding, padding, padding, padding);
        int size = QMUIDisplayHelper.dp2px(Schedule_Activity.this, 300);
        int height = QMUIDisplayHelper.dp2px(Schedule_Activity.this, 300);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(size, height);

        mainlayout = View.inflate(Schedule_Activity.this, R.layout.set_title_popup, null);
        popup_save_btn=(QMUIRoundButton) mainlayout.findViewById(R.id.title_set_save_btn);
        title_edit=(EditText) mainlayout.findViewById(R.id.set_title_popup_title);
        popup_close_btn=(ImageButton)mainlayout.findViewById(R.id.title_popup_close_btn);

        Glide.with(mainlayout).load(R.drawable.ic_action_close).into(popup_close_btn);
        frameLayout.addView(mainlayout, lp);
        popups = new QMUIFullScreenPopup(Schedule_Activity.this);


        popups.addView(frameLayout)
                .skinManager(QMUISkinManager.defaultInstance(Schedule_Activity.this))
                .onBlankClick(new QMUIFullScreenPopup.OnBlankClickListener() {
                    @Override
                    public void onBlankClick(QMUIFullScreenPopup popup) {
                    }
                })
                .show(AttachV);

        popup_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popups.dismiss();
            }
        });

        popup_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edi=title_edit.getText().toString();
                titleq.setText(edi);
                //FilesUtil.renameFile("临时课表数据",edi);
                Toast.makeText(Schedule_Activity.this, "标题修改成功！", Toast.LENGTH_SHORT).show();
                popups.dismiss();
            }
        });
    }

    private void showBottom(String title,String[] values,String[] timeqs,TextView view,TextView view2,int checkposition){
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(Schedule_Activity.this);
        builder.setGravityCenter(true)
                .setSkinManager(QMUISkinManager.defaultInstance(Schedule_Activity.this))
                .setTitle(title)
                .setAddCancelBtn(true)
                .setAllowDrag(true)
                .setNeedRightMark(true)
                .setCheckedIndex(checkposition)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        view.setText(values[position]);
                        view2.setText(timeqs[position]);
                        editor.putInt("ScheSelected",position);
                        editor.commit();
                        dialog.dismiss();
                        titleq.setText(values[position]);
                        SchedulReresh();
                    }
                });

       for(int f=0;f<values.length;f++){
           builder.addItem(values[f]);
       }

        builder.build().show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        drawerLayout.closeDrawers();
        SchedulReresh();
    }


}