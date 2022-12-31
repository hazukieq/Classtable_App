package com.hazukie.scheduleviews.activity;

import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hazukie.cskheui.Crialoghue.Crialoghue;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.base.BaseActivity;
import com.hazukie.scheduleviews.utils.DisplayHelper;
import com.hazukie.scheduleviews.utils.SpvalueStorage;
import com.hazukie.scheduleviews.utils.StatusHelper;

public class SettingActivity extends BaseActivity {
    private SpvalueStorage sp;
    private TextView startValue,termNumValue;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        StatusHelper.controlStatusLightOrDark(this, StatusHelper.Mode.Status_Dark_Text);
        inits();
        setPreValues();
    }

    private void inits(){
        sp=SpvalueStorage.getInstance(this);
        LinearLayout startDate=findViewById(R.id.setting_startDate);
        startDate.setOnClickListener(v->startiloh());
        startValue=findViewById(R.id.setting_startDate_value);

        LinearLayout termNum=findViewById(R.id.setting_weeks);
        termNum.setOnClickListener(v1->editWeekNum());
        termNumValue=findViewById(R.id.setting_weeks_value);


    }

    private void setPreValues(){
        int startMo=sp.getInt("start_month",9);
        int startDa=sp.getInt("start_day",1);
        startValue.setText(startMo+"月"+startDa+"日");

        int weekNum=sp.getInt("termweeks",18);
        termNumValue.setText(weekNum+"周");
    }

    private void startiloh(){
        Crialoghue startDiloh=new Crialoghue.HeditBuilder()
                .addTitle("设置开学日期")
                .addIsHtmlMode(true)
                .addIsBottomVisible(true)
                .addHint("请输入开学时间")
                .addBottomContents("请按照 0901（数字格式）输入日期;<br/><br/>例：0901表示9月1日，0301表示3月1日，1114表示11月11日<br/>")
                .onConfirm((crialoghue, view) -> {
                    EditText editText=(EditText) view;
                    String date=editText.getText().toString();
                    if(date.length()>4|date.length()<4) DisplayHelper.Infost(this,"内容须等于4个字长度，请重新输入！");
                    else{
                        try{
                            int startMonth=Integer.parseInt(date.substring(0,2));
                            int startDay=Integer.parseInt(date.substring(2,4));
                            sp.setIntValue("start_month",startMonth);
                            sp.setIntValue("start_day",startDay);
                            String formated=startMonth+"月"+startDay+"日";
                            startValue.setText(formated);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        crialoghue.dismiss();
                    }
                }).build(this);

        startDiloh.show();
    }

    private void editWeekNum(){
        Crialoghue cih=new Crialoghue.HeditBuilder()
                .addTitle("编辑周数")
                .addHint("输入两位或一位数字")
                .addInterceptor((crialoghue, viewGroup) -> {
                    EditText editText=(EditText) viewGroup.getChildAt(0);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                })
                .onConfirm((crialoghue, view) -> {
                    EditText ei=(EditText) view;
                    String num=ei.getText().toString();
                    if(num.length()<1||num.length()>2) DisplayHelper.Infost(this,"内容不能为空且不超过2个字长度，请重新输入！");
                    else{
                        int numth=Integer.parseInt(num);
                        sp.setIntValue("termweeks",numth);
                        termNumValue.setText(num+"周");
                        crialoghue.dismiss();
                    }
                }).build(this);

        cih.show();
    }
}
