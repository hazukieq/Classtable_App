package com.hazukie.scheduleviews.utils;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 状态栏处理助手
 */
public  class StatusHelper {

    public enum  Mode{
        Status_Light_Text,Status_Dark_Text
    }
    /**
     * @param type_ 0表示状态栏文字为黑色，1表示状态栏文字为白色
     */
    public static void controlStatusLightOrDark(Activity activity,Mode type_){
        Window window=activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        View windowDecorView=window.getDecorView();
        if (type_ == Mode.Status_Light_Text)
            windowDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_VISIBLE);
        else
            windowDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);

    }
}
