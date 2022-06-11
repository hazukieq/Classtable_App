package com.example.classtool.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.classtool.R;
import com.example.classtool.Schedule_Activity;
import com.example.classtool.base.PopupWindows;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.layout.QMUIRelativeLayout;
import com.qmuiteam.qmui.skin.QMUISkinHelper;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.skin.QMUISkinValueBuilder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

public class ShowDialogUtil {
    private Context getContext;

    public ShowDialogUtil(Context getContext){
        this.getContext=getContext;
    }

    public void ShowDialog(View AttachView , int str){
        QMUISkinValueBuilder builder = QMUISkinValueBuilder.acquire();
        QMUIFrameLayout frameLayout = new QMUIFrameLayout(getContext);
        frameLayout.setBackground(
                QMUIResHelper.getAttrDrawable(getContext, com.qmuiteam.qmui.R.attr.qmui_skin_support_popup_bg));
        builder.background(com.qmuiteam.qmui.R.attr.qmui_skin_support_popup_bg);
        QMUISkinHelper.setSkinValue(frameLayout, builder);
        frameLayout.setRadius(QMUIDisplayHelper.dp2px(getContext, 12));
        int padding = QMUIDisplayHelper.dp2px(getContext, 20);
        frameLayout.setPadding(padding, padding, padding, padding);


        ScrollView scrollView=new ScrollView(getContext);

        TextView textView = new TextView(getContext);
        textView.setLineSpacing(QMUIDisplayHelper.dp2px(getContext, 4), 1.0f);
        textView.setPadding(padding, padding, padding, padding);
        textView.setText(getContext.getText(str));
        textView.setTextColor(
                QMUIResHelper.getAttrColor(getContext, R.attr.app_skin_common_title_text_color));

        builder.clear();
        builder.textColor(R.attr.app_skin_common_title_text_color);
        QMUISkinHelper.setSkinValue(textView, builder);
        textView.setGravity(Gravity.LEFT);

        builder.release();

        int size = QMUIDisplayHelper.dp2px(getContext, 250);
        int height=QMUIDisplayHelper.dp2px(getContext,400);
        ViewGroup.LayoutParams scrollp=new ViewGroup.LayoutParams(size,height);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(size, height);
        scrollView.addView(textView,scrollp);
        frameLayout.addView(scrollView, lp);

        QMUIPopups.fullScreenPopup(getContext)
                .addView(frameLayout)
                .closeBtn(false)
                .skinManager(QMUISkinManager.defaultInstance(getContext))
                .onBlankClick(new QMUIFullScreenPopup.OnBlankClickListener() {
                    @Override
                    public void onBlankClick(QMUIFullScreenPopup popup) {
                        popup.dismiss();
                        //Toast.makeText(getContext(), "点击到空白区域", Toast.LENGTH_SHORT).show();
                    }
                })
                .onDismiss(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {

                        //Toast.makeText(getContext(), "onDismiss", Toast.LENGTH_SHORT).show();
                    }
                })
                .show(AttachView);
    }

    public  void Qhowpop(View AttachV,String title,String hint){
        QMUISkinValueBuilder builder = QMUISkinValueBuilder.acquire();
        QMUIFrameLayout frameLayout = new QMUIFrameLayout(getContext);
        frameLayout.setBackground(
                QMUIResHelper.getAttrDrawable(getContext, com.qmuiteam.qmui.R.attr.qmui_skin_support_popup_bg));
        builder.background(com.qmuiteam.qmui.R.attr.qmui_skin_support_popup_bg);
        QMUISkinHelper.setSkinValue(frameLayout, builder);
        frameLayout.setRadius(QMUIDisplayHelper.dp2px(getContext, 12));
        int padding = QMUIDisplayHelper.dp2px(getContext, 8);
        frameLayout.setPadding(padding, padding, padding, padding);
        int size = QMUIDisplayHelper.dp2px(getContext, 300);
        int height = QMUIDisplayHelper.dp2px(getContext, 300);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(size, height);

        View mainlayout = View.inflate(getContext, R.layout.set_title_popup, null);
       QMUIRoundButton popup_save_btn=(QMUIRoundButton) mainlayout.findViewById(R.id.title_set_save_btn);
        EditText title_edit=(EditText) mainlayout.findViewById(R.id.set_title_popup_title);
        TextView change_title=(TextView)mainlayout.findViewById(R.id.changeTitle);
        ImageButton popup_close_btn=(ImageButton)mainlayout.findViewById(R.id.title_popup_close_btn);

        change_title.setText(title);
        title_edit.setHint(hint);

        Glide.with(mainlayout).load(R.drawable.ic_action_close).into(popup_close_btn);
        frameLayout.addView(mainlayout, lp);
        QMUIFullScreenPopup popups = new QMUIFullScreenPopup(getContext);


        popups.addView(frameLayout)
                .skinManager(QMUISkinManager.defaultInstance(getContext))
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

               onSave(v,edi,popups);

            }
        });
    }

    public void onSave(View saveView,String EditgetStr,QMUIFullScreenPopup pop){
        //重写本方法
    }
}

