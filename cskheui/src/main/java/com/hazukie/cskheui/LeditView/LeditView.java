package com.hazukie.cskheui.LeditView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hazukie.cskheui.R;

public class LeditView extends LinearLayout {
    private static final String TAG ="LeditView>>" ;
    private EditText ledit;
    private TextView left_tile;


    public LeditView(Context context) {
        super(context);
    }

    public LeditView(Context context,Builder builder){
        this(context);
        inits();
        if(builder.interceptor!=null) builder.interceptor.doIntercept(left_tile,ledit,this);
        left_tile.setText(builder.leftile);
        ledit.setHint(builder.hint);
        ledit.setText(builder.content);

    }
    public LeditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LeditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LeditView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void inits(){
        LayoutInflater.from(getContext()).inflate(R.layout.__leditview_lay,this,true);
        LinearLayout root=(LinearLayout) getRootView();
        LinearLayout linear= (LinearLayout) getChildAt(0);
        left_tile= (TextView) linear.getChildAt(0);
        ledit= (EditText) linear.getChildAt(1);
    }


    public interface  Interceptor{
       void  doIntercept(TextView leftile,EditText ledit,LinearLayout linearLayout);
    }

    public static class Builder{
        private String hint="",content="",leftile="";

        private Interceptor interceptor;

        public Builder addHint(String hint){
            this.hint=hint;
            return this;
        }

        public Builder addContent(String content){
            this.content=content;
            return this;
        }

        public Builder addLeftile(String leftile){
            this.leftile=leftile;
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor){
            this.interceptor=interceptor;
            return this;
        }

        public LinearLayout create(Context context){
            return new LeditView(context,this);
        }
    }
}
