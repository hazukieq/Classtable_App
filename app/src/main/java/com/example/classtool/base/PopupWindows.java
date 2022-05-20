package com.example.classtool.base;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;


import com.example.classtool.R;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PopupWindows {
    private QMUIPopup mNormalPopup;
    private Context context;
    private  String[] listItems=new String[]{};
    public PopupWindows(Context context, QMUIPopup popup){
            this.mNormalPopup=popup;
            this.context=context;

    }

    public void showPopup(View attachView,String[] items,int height,int width,int offsetYIfTop,int edgeProtection){
        this.listItems=items;
        List<String> data = new ArrayList<>();
        Collections.addAll(data, listItems);
        ArrayAdapter adapter = new ArrayAdapter<>(context, R.layout.simple_list_item, data);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(root.getContext(), "Item " + (i + 1), Toast.LENGTH_SHORT).show();
                if (mNormalPopup != null) {
                    mNormalPopup.dismiss();
                }

                Clickpopup(i);

            }
        };
        mNormalPopup = QMUIPopups.listPopup(context,
                QMUIDisplayHelper.dp2px(context, width),//160
                QMUIDisplayHelper.dp2px(context, height),//120
                adapter,
                onItemClickListener)
                .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                .preferredDirection(QMUIPopup.DIRECTION_TOP)
                .shadow(true)
                .edgeProtection(QMUIDisplayHelper.dp2px(context, edgeProtection))
                .offsetYIfTop(QMUIDisplayHelper.dp2px(context, offsetYIfTop))
                .skinManager(QMUISkinManager.defaultInstance(context))
                .onDismiss(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                    }
                })
                .show(attachView);
    }

    //重写这个方法，处理具体点击事件
        public void  Clickpopup(int i){

            //        switch (i){
//            case 0:
//                Intent inli=new Intent();
//                inli.setClass(getActivity(), SettingActivity.class);
//                startActivity(inli);
//                break;
//            case 1:
//                Intent inl=new Intent();
//                inl.setClass(getActivity(), AboutActivity.class);
//                startActivity(inl);
//                break;
//            default:
//                break;
//        }
        }



}
