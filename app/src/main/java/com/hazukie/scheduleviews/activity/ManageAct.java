package com.hazukie.scheduleviews.activity;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.base.BaseActivity;
import com.hazukie.scheduleviews.fragments.ScheManageFrag;
import com.hazukie.scheduleviews.fragments.TimeManageFrag;
import com.hazukie.scheduleviews.utils.StatusHelper;

public class ManageAct extends BaseActivity {
    private TextView shceTab;
    private TextView timeTab;
    private TimeManageFrag timemanageFrag;
    private ScheManageFrag schemakeFrag;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        StatusHelper.controlStatusLightOrDark(this, StatusHelper.Mode.Status_Dark_Text);
        inits();
    }

    private void inits(){
        shceTab=findViewById(R.id.activity_sche_tab);
        timeTab=findViewById(R.id.activity_time_tab);
        TextView returnTab = findViewById(R.id.activity_manage_return);
        returnTab.setOnClickListener(v2->finish());


        timeTab.setTextColor(getColor(R.color.text_gray));
        shceTab.setTextColor(getColor(R.color.iosbutton_cancel));

        showFragment1();

        shceTab.setOnClickListener(v -> {
            timeTab.setTextColor(getColor(R.color.text_gray));
            shceTab.setTextColor(getColor(R.color.qmuibtn_text));
            showFragment1();
        });

        timeTab.setOnClickListener(v1->{
            shceTab.setTextColor(getColor(R.color.text_gray));
            timeTab.setTextColor(getColor(R.color.iosbutton_cancel));
            showFragment2();
        });

    }


    private void showFragment1(){
        FragmentTransaction transactin=getSupportFragmentManager().beginTransaction();
        if(schemakeFrag==null){
            schemakeFrag=new ScheManageFrag();
            transactin.add(R.id.activity_manage_root,schemakeFrag);
        }
        try{
            hideAllFrags(transactin);
            transactin.show(schemakeFrag);
            transactin.commit();
            schemakeFrag.reshData();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showFragment2(){
        FragmentTransaction transactin=getSupportFragmentManager().beginTransaction();
        if(timemanageFrag==null){
            timemanageFrag=new TimeManageFrag();
            transactin.add(R.id.activity_manage_root,timemanageFrag);
        }
        try{
            hideAllFrags(transactin);
            transactin.show(timemanageFrag);
            transactin.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void hideAllFrags(FragmentTransaction transactio){
        if(timemanageFrag!=null) transactio.hide(timemanageFrag);
        if(schemakeFrag!=null) transactio.hide(schemakeFrag);
    }
}
