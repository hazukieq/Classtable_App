package com.hazukie.scheduleviews.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.utils.DisplayHelper;

public class CRecyclerView extends RecyclerView {
    private int maxHeight=300;
    private boolean isScroll=false;
    public CRecyclerView(@NonNull Context context) {
        super(context);
        init(context,null);
    }

    public CRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public void init(Context context,AttributeSet attrs){
        TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.CRecyclerView);
        if(a!=null){
            try{
                maxHeight=a.getLayoutDimension(R.styleable.CRecyclerView_maxHeight,300);
            }finally {
                a.recycle();
            }
        }
        maxHeight= DisplayHelper.dp2px(getContext(),maxHeight);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if(maxHeight>0) heightSpec=MeasureSpec.makeMeasureSpec(maxHeight,MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, heightSpec);
    }

    public void setMaxHeight(int maxHeight){
        this.maxHeight=DisplayHelper.dp2px(getContext(),maxHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if(isScroll) return false;
        else return super.onInterceptTouchEvent(e);
        //return super.onInterceptTouchEvent(e);
    }

    public void setCustomScroll(boolean is){
        this.isScroll=is;
    }
}
