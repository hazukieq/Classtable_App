package com.hazukie.cskheui.Crialoghue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Clicks {
    public interface ConfirmListener{
        void doConfirm(Crialoghue crialoghue, View rootView);
    }

    public interface  CancelListener{
        void doCancel(Crialoghue cdialoh, View rootView);
    }

    public interface ConfirmListenerz{
        void doConfirm(Crialoghue crialoghue, ViewGroup rootView);
    }

    public interface  CancelListenerz{
        void doCancel(Crialoghue cdialoh, ViewGroup rootView);
    }

    static ViewGroup getView(Context context, int rootViewID){
        return (ViewGroup) LayoutInflater.from(context).inflate(rootViewID,null);
    }

    public interface InterceptViewz{
        void doIntercept(Crialoghue cdialoh, ViewGroup attachedView);
    }

    public interface InterceptLeViewz{
       void doIntercept(TextView leftile, TextView rightile, LinearLayout root);
    }

    public interface InterceptView{
        void doIntercept(Crialoghue cdialoh, View v);
    }
}
