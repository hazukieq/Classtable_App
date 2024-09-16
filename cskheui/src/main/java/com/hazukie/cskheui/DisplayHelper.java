package com.hazukie.cskheui;

import android.content.Context;
public class DisplayHelper {
    public static int[] dp2pxs(Context context,float left,float top,float right,float bottom){
        return new int[]{dp2px(context,left),dp2px(context,right),dp2px(context,top),dp2px(context,bottom)};
    }

    public static int dp2px(Context context, float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

    public static int sp2px(Context context,float spValue){
        final float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue*fontScale+0.5f);
    }
 /*   public static void Infost(Context context,String contens){
        Toast.makeText(context, contens, Toast.LENGTH_SHORT).show();
    }*/
}

