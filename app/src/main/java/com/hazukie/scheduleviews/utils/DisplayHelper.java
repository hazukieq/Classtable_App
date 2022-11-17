package com.hazukie.scheduleviews.utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.hazukie.cskheui.Crialoghue.Clicks;
import com.hazukie.cskheui.Crialoghue.Crialoghue;

public class DisplayHelper {
    public static int dp2px(Context context,float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

    public static int sp2px(Context context,float spValue){
        final float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue*fontScale+0.5f);
    }

    public static void Infost(Context context,String contens){
        Toast.makeText(context, contens, Toast.LENGTH_SHORT).show();
    }

    public static void showBack(FragmentActivity context){
        Crialoghue ch=new Crialoghue.TxtBuilder()
                .addTitle("提示")
                .addContent("数据似乎还没有保存，确认返回吗？")
                .onConfirm((crialoghue, view) -> {
                    crialoghue.dismiss();
                    context.finish();
                })
                .build(context);
        ch.show();
    }
}
