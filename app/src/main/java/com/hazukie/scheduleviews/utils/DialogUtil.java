package com.hazukie.scheduleviews.utils;

import android.content.Context;

import com.drakeet.multitype.MultiTypeAdapter;
import com.hazukie.cskheui.Crialoghue.Crialoghue;

import java.util.List;

public class DialogUtil {
    public static void showaloh(Context context, MultiTypeAdapter mdp, String title, List<Object> ob_,List<Object> subojs, Object hrcx, int indx_,boolean isSubList) {
        Crialoghue cioh=new Crialoghue.TxtBuilder()
                .addTitle("" + title)
                .addContent( "确定删除此" + title + "吗？")
                .onConfirm((cRialog, v) -> {
                    ob_.remove(hrcx);
                    if (isSubList) subojs.remove(hrcx);
                    mdp.notifyItemRemoved(indx_);
                    cRialog.dismiss();
                })
                .build(context);
        cioh.show();
    }
}
