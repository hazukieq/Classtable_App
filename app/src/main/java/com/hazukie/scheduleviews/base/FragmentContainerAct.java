package com.hazukie.scheduleviews.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.models.ClassLabel;
import com.hazukie.scheduleviews.utils.DisplayHelper;
import com.hazukie.scheduleviews.utils.StatusHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FragmentContainerAct extends BaseActivity {
    public static FragmentContainerAct instance;
    private String className;
    private boolean isTip=false;
    private List<ClassLabel> objs;
    private String param;
    private View topview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        StatusHelper.controlStatusLightOrDark(this, StatusHelper.Mode.Status_Dark_Text);
        topview=findViewById(R.id.fragment_container_top);
        getArguments();
        instance=this;

        if(!TextUtils.isEmpty(className)){
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction transaction=fm.beginTransaction();
            try{
                Object obj=null;
                Class<?> fragClass=Class.forName(className);
                Log.i( "onCreate>>>","class="+className);
                obj=fragClass.newInstance();
                Fragment fragment=(Fragment) obj;
                transaction.replace(R.id.fragment_container_lay,fragment);
                transaction.commit();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //读取网页链接
    private void getArguments() {
        Intent in_=getIntent();

        className = in_.getStringExtra("fragName");
        objs=new ArrayList<>();

        isTip= in_.getBooleanExtra("isTip",false);
        topview.setVisibility(isTip?View.GONE:View.VISIBLE);
        int color=in_.getIntExtra("topcolor",getColor(R.color.white));
        topview.setBackgroundColor(color);

        Bundle bun=in_.getExtras();
        objs=(List<ClassLabel>) bun.getSerializable("lis");
        param=bun.getString("para");
    }


    public List<ClassLabel> getObjs() {
       if(objs!=null) return objs;
       return new ArrayList<>();
    }

    public String getParam() {
        if(param!=null)return param;
        return "";
    }

    /**
     *
     * @param context 当前活动柄
     * @param fragName 包名+类名，例：com.hazukie.scheduleviews.fragments.SchemakeFrag;
     */
    public static void startActivityWithLoadUrl(Context context, Class<?> fragName,String param, List<ClassLabel> clss) {
        Intent intent = new Intent(context, FragmentContainerAct.class);
        Bundle bun=new Bundle();
        bun.putString("para",param);
        bun.putSerializable("lis", (Serializable) clss);
        intent.putExtras(bun);
        intent.putExtra("fragName", fragName.getName());
        context.startActivity(intent);
    }

    /**
     *
     * @param context 当前活动柄
     * @param fragName 包名+类名，例：com.hazukie.scheduleviews.fragments.SchemakeFrag;
     */
    public static void startActivityWithLoadUrl(Context context, Class<?> fragName) {
        Intent intent = new Intent(context, FragmentContainerAct.class);
        intent.putExtra("fragName", fragName.getName());
        context.startActivity(intent);
    }

    /**
     *
     * @param context 当前活动柄
     * @param fragName 包名+类名，例：com.hazukie.scheduleviews.fragments.SchemakeFrag;
     */
    public static void startActivityWithLoadUrl(Context context, Class<?> fragName,int topcolor) {
        Intent intent = new Intent(context, FragmentContainerAct.class);
        intent.putExtra("fragName", fragName.getName());
        intent.putExtra("topcolor",topcolor);
        context.startActivity(intent);
    }

    /**
     *
     * @param context 当前活动柄
     * @param fragName 包名+类名，例：com.hazukie.scheduleviews.fragments.SchemakeFrag;
     */
    public static void startActivityWithLoadUrl(Context context, Class<?> fragName,boolean isTip) {
        Intent intent = new Intent(context, FragmentContainerAct.class);
        intent.putExtra("fragName", fragName.getName());
        intent.putExtra("isTip",isTip);
        context.startActivity(intent);
    }

    public static void startAct2Activity(Context context,Class<?> ActName){
        Intent in=new Intent();
        in.setClass(context,ActName);
        context.startActivity(in);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&isTip){
            DisplayHelper.showBack(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
