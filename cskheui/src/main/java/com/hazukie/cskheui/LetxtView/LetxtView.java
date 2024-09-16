package com.hazukie.cskheui.LetxtView;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hazukie.cskheui.R;

public class LetxtView extends LinearLayout {
    private TextView leftile,rightile;

    public LetxtView(Context context) {
        super(context);
    }

    public LetxtView(Context context,Builder builder) {
        this(context);
        inits();
        if(builder.interceptor!=null) builder.interceptor.doIntercept(leftile,rightile,this);
        leftile.setText(builder.leftile);
        rightile.setText(builder.rightentz);
    }

    private void inits(){
        LayoutInflater.from(getContext()).inflate(R.layout.__letxtview_lay,this,true);
        LinearLayout root= (LinearLayout) getChildAt(0);
        leftile=(TextView) root.getChildAt(0);
        rightile=(TextView) root.getChildAt(1);
    }

    public interface Interceptor{
        void  doIntercept(TextView leftile, TextView rightile, LinearLayout linearLayout);
    }

    public static  class Builder{
        private String leftile,rightentz;

        private Interceptor interceptor;

        public Builder addLeftile(String leftile){
            this.leftile=leftile;
            return this;
        }

        public Builder addRightile(String rightentz){
            this.rightentz=rightentz;
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor){
            this.interceptor=interceptor;
            return this;
        }

        public LetxtView create(Context context){
            return new LetxtView(context,this);
        }
    }
}
