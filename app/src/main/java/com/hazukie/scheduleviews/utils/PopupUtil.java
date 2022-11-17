package com.hazukie.scheduleviews.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.drakeet.multitype.MultiTypeAdapter;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.custom.CRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class PopupUtil {
    private Context context;
    private View v;

    public PopupUtil(Context context){
        this.context=context;
        v= LayoutInflater.from(context).inflate(R.layout.popup_lay,null);
    }

    public void initDefaultViews(OntrolPopList ontrolPopList){
        TextView txt=v.findViewById(R.id.popup_create);
        CRecyclerView crecy=v.findViewById(R.id.popup_recy);
        crecy.setLayoutManager(new LinearLayoutManager(context));
        MultiTypeAdapter multiAdp=new MultiTypeAdapter();
        List<Object> objs=new ArrayList<>();
        crecy.setAdapter(multiAdp);
        ontrolPopList.control(txt,crecy,multiAdp,objs);
        multiAdp.setItems(objs);
    }

    public PopupWindow initDefaultPopup(int width){
        return createPopupWindow(v,width);
    }


    public  interface  OntrolPopList {
      void control(TextView textView,CRecyclerView crecy, MultiTypeAdapter multiAdp, List<Object> viewlist);
    }

    private PopupWindow createPopupWindow(View view,int width){
        int w200=DisplayHelper.dp2px(context, width);
        PopupWindow pv=new PopupWindow(view,w200, ViewGroup.LayoutParams.WRAP_CONTENT);
        pv.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        pv.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pv.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pv.setFocusable(true);
        return pv;
    }
}
