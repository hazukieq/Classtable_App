package com.hazukie.scheduleviews.net;


import android.content.Context;
import android.content.MutableContextWrapper;
import android.os.Looper;
import android.util.Log;

import com.hazukie.scheduleviews.custom.CnWebView;

import java.util.Stack;

public class WebStacker {
    //private static final String TAG = "WebStaker preloaded CnWebView nums==>";
    private static  WebStacker INSTANCE;
    private final Context context;
    private  WebStacker(Context context){
        this.context =context;
    }

    private static final Stack<CnWebView> mCnWebViewStack=new Stack<>();
    private static final int mCnWebView_nums=3;

    public static WebStacker getInstance(Context context){
        if(INSTANCE==null) {
            synchronized (WebStacker.class){
                if(INSTANCE==null) INSTANCE=new WebStacker(context);
            }
        }
        return INSTANCE;
    }


    public void preload(){
        Looper.myQueue().addIdleHandler(() -> {
            if(mCnWebViewStack.size()<mCnWebView_nums) {
                for (int i = 0; i < mCnWebView_nums; i++) {
                    mCnWebViewStack.push(createCnWebView());
                }
            }
            Log.i("WebStacker", "preload: "+mCnWebViewStack.size());
            return false;
        });
    }

    private CnWebView createCnWebView(){
        return new CnWebView(new MutableContextWrapper(context));
    }

    public CnWebView getCnWebView(Context context){
        if(mCnWebViewStack==null||mCnWebViewStack.isEmpty()){
            CnWebView cnWebView=createCnWebView();
            MutableContextWrapper wrapper=(MutableContextWrapper) cnWebView.getContext();
            wrapper.setBaseContext(context);
            return cnWebView;
        }
        CnWebView cnWebView=mCnWebViewStack.pop();
        MutableContextWrapper wrapper=(MutableContextWrapper) cnWebView.getContext();
        wrapper.setBaseContext(context);
        return cnWebView;
    }

    public void recycleCnWebView(CnWebView cnWebView){
        if(mCnWebViewStack!=null&&mCnWebViewStack.size()>0){
            cnWebView.releaseAll();
            MutableContextWrapper wrapper=(MutableContextWrapper)cnWebView.getContext();
            wrapper.setBaseContext(cnWebView.getContext().getApplicationContext());
            if(mCnWebViewStack.size()<mCnWebView_nums){
                mCnWebViewStack.push(cnWebView);
            }else{
               cnWebView.destroy();
            }
        }
    }

}
