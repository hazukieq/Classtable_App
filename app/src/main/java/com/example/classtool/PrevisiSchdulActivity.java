package com.example.classtool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.classtool.models.ClassLabel;
import com.example.classtool.models.Class_cardmodel;
import com.example.classtool.models.FindSort;
import com.example.classtool.models.QTime;
import com.example.classtool.utils.CalculatLayViews;
import com.example.classtool.utils.FilesUtil;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.ArrayList;
import java.util.List;

public class PrevisiSchdulActivity extends AppCompatActivity {
    private TextView classl_1,classl_2,classl_3,classl_4,classl_5,classl_6,classl_7,classl_8,classl_9,classl_10,classl_11,classl_12,classl_13,classl_14,classl_15,classl_16,classl_17,classl_18,schedul_refresh;
    private ArrayList<String> classSas=new ArrayList<>();
    private TextView title,pre_detail_btn;
    private GridLayout GridClass;
    private List<TextView> textViews=new ArrayList<>();
    private List<Class_cardmodel> classqall=new ArrayList<>();
    private List<ClassLabel> cls=new ArrayList<>();
    private int classLen=12;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private CalculatLayViews calculatLayViews;
    private List<QTime> qimes=new ArrayList<>();
    private String current_time_temp;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previsit_schedul);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        sp= PreferenceManager.getDefaultSharedPreferences(PrevisiSchdulActivity.this);
        editor=sp.edit();
        title=(TextView)findViewById(R.id.pre_schedule_title);
        pre_detail_btn=(TextView)findViewById(R.id.pre_schedul_refresh);

        GridClass=(GridLayout) findViewById(R.id.predetailClasses);
        Intent inq=getIntent();
        Bundle bundle=inq.getExtras();

        List<Class_cardmodel> cool=(List<Class_cardmodel>) bundle.getSerializable("list");
        classqall.addAll(cool);
        current_time_temp=sp.getString("current_time_temp","武鸣校区作息时间");
        qimes= FilesUtil.readClassTime(current_time_temp);
        classLen=qimes.size();


        refresh();
        txtListeners();
    }

    private void txtListeners(){
        pre_detail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new QMUIDialog.MessageDialogBuilder(PrevisiSchdulActivity.this)
                        .setTitle("周课表详情")
                        .setSkinManager(QMUISkinManager.defaultInstance(PrevisiSchdulActivity.this))
                        .setMessage("作息时间模板："+current_time_temp+"\n"+"共计节数："+classLen+"\n")
                        .addAction("知道了", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .create( R.style.DialogTheme2).show();
            }
        });

    }


    private void refresh(){
        initClassl();
        controlTexts();
        initGrids(1);
    }

    private void initClassl(){
        classl_1=(TextView) findViewById(R.id.presche_classl_1);
        classl_2=(TextView) findViewById(R.id.presche_classl_2);
        classl_3 =(TextView) findViewById(R.id.presche_classl_3);
        classl_4=(TextView) findViewById(R.id.presche_classl_4);
        classl_5=(TextView) findViewById(R.id.presche_classl_5);
        classl_6=(TextView) findViewById(R.id.presche_classl_6);
        classl_7=(TextView) findViewById(R.id.presche_classl_7);
        classl_8=(TextView) findViewById(R.id.presche_classl_8);
        classl_9=(TextView) findViewById(R.id.presche_classl_9);
        classl_10=(TextView) findViewById(R.id.presche_classl_10);
        classl_11=(TextView) findViewById(R.id.presche_classl_11);
        classl_12=(TextView) findViewById(R.id.presche_classl_12);
        classl_13=(TextView) findViewById(R.id.presche_classl_13);
        classl_14=(TextView) findViewById(R.id.presche_classl_14);
        classl_15=(TextView) findViewById(R.id.presche_classl_15);
        classl_16=(TextView) findViewById(R.id.presche_classl_16);
        classl_17=(TextView) findViewById(R.id.presche_classl_17);
        classl_18=(TextView) findViewById(R.id.presche_classl_18);

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

        if(qimes.size()>0){
            for (int d = 0; d < classLen; d++) {
                textViews.get(d).setText(Html.fromHtml("<big>" + (d + 1) + "</big>" + "<br><font color='gray'><small>" + qimes.get(d).getStart2end().replace("-", "<br/>") + "</small></font><br>"));
            }
        }

    }

    private void initGrids(int type){
        int classLen=sp.getInt("classLen",12);
        calculatLayViews=new CalculatLayViews(classLen);
        switch (type){
            case 0:
                GridClass.setOrientation(GridLayout.VERTICAL);
                for(Class_cardmodel l:classqall){
                    int x= FindSort.returnColorSort(weeks,l.getClass_date().substring(0,3));
                    int y=FindSort.returnColorSort(start_classes,l.getClass_startClass());
                    int numcls=FindSort.returnColorSort(class_nums,l.getClass_totalClass())+1;
                    String course="<big>"+l.getClass_course()+"</big><br><br><font color='gray'>"+l.getClass_classPlace()+"<br/><br/>"+l.getOtherNotes()+"</font>";

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
                        params.columnSpec = GridLayout.spec(x, 1f);
                        params.rowSpec = GridLayout.spec(y, l.getClass_nums(), 1f);
                        textView.setBackgroundColor(getColor(l.getColorq()));
                    } else {
                        params.columnSpec = GridLayout.spec(x, 1f);
                        params.rowSpec = GridLayout.spec(y, 1f);
                        textView.setBackgroundColor(getColor(R.color.white));
                    }
                    textView.setTextColor(getColor(com.qmuiteam.qmui.R.color.qmui_config_color_gray_2));
                    textView.setTextSize(10f);
                    textView.setGravity(Gravity.CENTER);
                    textView.setPadding(4,6,4,4);
                    textView.setText(Html.fromHtml(l.getSubjectplanotes()));
                    params.setMargins(3, 2, 3, 2);
                    GridClass.addView(textView,params);
                }
                break;
            case 1:
                GridClass.removeAllViews();
                Log.i( "initGrids: ",String.valueOf(classLen));
                GridClass.setOrientation(GridLayout.VERTICAL);
                for(Class_cardmodel l:classqall){
                    int x= FindSort.returnColorSort(weeks,l.getClass_date().substring(0,3));
                    int y=FindSort.returnColorSort(start_classes,l.getClass_startClass());
                    int numcls=FindSort.returnColorSort(class_nums,l.getClass_totalClass())+1;
                    String course="<big>"+l.getClass_course()+"</big><br><br><font color='gray'>"+l.getClass_classPlace()+"<br/><br/>"+l.getOtherNotes()+"</font>";

                    cls.add(new ClassLabel(numcls,y,x,course,l.getClassColor()));
                }
                List<ClassLabel> slapall=calculatLayViews.Excheng_Classes(cls);
                List<ClassLabel> sapall=calculatLayViews.ReturnHtmlindex(slapall);

                for(int h=0;h<sapall.size();h++) {
                    ClassLabel l = sapall.get(h);
                    int x=l.getWeek();
                    int y=l.getStart_class();
                    TextView textView = new TextView(this);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = 0;
                    params.height = 0;
                    if (l.getClass_nums() > 0) {
                        params.columnSpec = GridLayout.spec(x, 1f);
                        params.rowSpec = GridLayout.spec(y, l.getClass_nums(), 1f);
                        textView.setBackgroundColor(getColor(l.getColorq()));
                    } else {
                        params.columnSpec = GridLayout.spec(x, 1f);
                        params.rowSpec = GridLayout.spec(y, 1f);
                        textView.setBackgroundColor(getColor(R.color.white));
                    }
                    textView.setTextColor(getColor(com.qmuiteam.qmui.R.color.qmui_config_color_gray_2));
                    textView.setTextSize(10f);
                    textView.setGravity(Gravity.CENTER);
                    textView.setPadding(4,6,4,4);
                    textView.setText(Html.fromHtml(l.getSubjectplanotes()));
                    params.setMargins(3, 2, 3, 2);
                    GridClass.addView(textView,params);
                }
                break;
        }
    }
}