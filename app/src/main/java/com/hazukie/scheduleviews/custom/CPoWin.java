package com.hazukie.scheduleviews.custom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import com.hazukie.scheduleviews.utils.DisplayHelper;

public class CPoWin extends PopupWindow {
    public CPoWin(Context context){
    }

    public CPoWin(Context context,Builder builder){
        this(context);
        setWidth(DisplayHelper.dp2px(context,builder.width));
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        if(builder.addView!=null){
            setContentView(builder.addView);
            if(builder.event!=null) builder.event.doIntercept(this,builder.addView);
        }
        setOutsideTouchable(true);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setFocusable(true);

    }


    public static class PopView{
        static View getView(Context context, int ResId){
            return LayoutInflater.from(context).inflate(ResId,null);
        };
    }

    public interface PopViewEvent{
        void doIntercept(CPoWin cpow,View view);
    }

    public static class Builder{
        private int width=120;

        private View addView;
        private PopViewEvent event;

        public Builder addWidth(int mwid){
            this.width=mwid;
            return this;
        }


        public Builder addView(ViewGroup viewGroup,PopViewEvent event){
            this.addView=viewGroup;
            this.event=event;
            return this;
        }

        public Builder addView(int resId,Context context,PopViewEvent event){
            this.addView=PopView.getView(context,resId);
            this.event=event;
            return this;
        }

        public CPoWin build(Context context){
            return new CPoWin(context,this);
        }
    }
}
