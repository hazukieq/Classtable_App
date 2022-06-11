package com.example.classtool.base;

import android.app.Application;

import com.hjq.toast.ToastUtils;
import com.hjq.toast.style.ViewToastStyle;
import com.hjq.toast.style.WhiteToastStyle;

public class QApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        iniToasty();
    }

    private void iniToasty(){
        ToastUtils.init(this);
    }
}
