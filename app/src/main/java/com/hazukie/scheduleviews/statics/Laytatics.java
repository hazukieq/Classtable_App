package com.hazukie.scheduleviews.statics;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.utils.DisplayHelper;

public class Laytatics {
    public  static LinearLayout getEditLay(Context context){
        LinearLayout lay=new LinearLayout(context);
        int with= DisplayHelper.dp2px(context,280);
        ViewGroup.LayoutParams lps=new ViewGroup.LayoutParams(with, ViewGroup.LayoutParams.WRAP_CONTENT);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.setLayoutParams(lps);

        int pa=DisplayHelper.dp2px(context,8);
        LinearLayout.LayoutParams eps=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        EditText eidt=new EditText(context);
        eidt.setHintTextColor(context.getColor(R.color.iosbutton_cancel));
        eidt.setHint("请按 0800-0900 格式输入");
        eidt.setTextSize(16);
        eidt.setTextColor(context.getColor(R.color.text_gray));
        eidt.setBackground(context.getDrawable(R.drawable.edit_bg));


        eidt.setPadding(pa,pa,pa,pa);
        eps.setMargins(pa,pa,pa,pa);
        eidt.setGravity(Gravity.CENTER_VERTICAL);
        eidt.setMaxLines(1);
        eidt.setLayoutParams(eps);

        lay.addView(eidt);
        return lay;
    }

    public static LinearLayout getLay(Context context) {
        LinearLayout lay=new LinearLayout(context);
        int with= DisplayHelper.dp2px(context,280);
        ViewGroup.LayoutParams lps=new ViewGroup.LayoutParams(with, ViewGroup.LayoutParams.WRAP_CONTENT);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.setLayoutParams(lps);

        int pa=DisplayHelper.dp2px(context,8);
        LinearLayout.LayoutParams eps=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        TextView txt=new TextView(context);
        txt.setTextColor(context.getColor(R.color.text_gray));
        txt.setTextSize(15);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setPadding(pa,pa,pa,pa);
        txt.setLayoutParams(eps);
        txt.setText(Html.fromHtml("<b><font color=\"#1b88ee\">说明</font></b><br/>每节课的开始、结束时间，每行只能输入一节课时间。<br/>示例：<br/>0930-1045<br/>1145-1230<br/><br/><br/>时间输入格式为：<b>小时分钟-小时分钟</b><br/>例:0200-0420<br/>"));

        EditText eidt=new EditText(context);
        eidt.setTextSize(16);
        eidt.setTextColor(context.getColor(R.color.text_gray));
        eidt.setBackground(context.getDrawable(R.drawable.edit_bg));


        eidt.setPadding(pa,pa,pa,pa);
        eps.setMargins(pa,pa,pa,pa);
        eidt.setGravity(Gravity.CENTER_VERTICAL);
        eidt.setHintTextColor(context.getColor(R.color.qmuibtn_text));
        eidt.setHint("请输入每节课具体时间");
        eidt.setLayoutParams(eps);


        lay.addView(eidt);
        lay.addView(txt);

        return lay;
    }
}
