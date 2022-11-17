package com.hazukie.scheduleviews.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;

import androidx.activity.result.ActivityResultCallback;
import androidx.fragment.app.FragmentActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;


public class ScreenShotHelper {

    /**
     *
     * @param rootLay 待生成图片布局宽度
     * @param config 输出图片配置
     * @return 返回图片
     */
    public static Bitmap screenshot(View rootLay, Bitmap.Config config){
        Bitmap bitmap=Bitmap.createBitmap(rootLay.getWidth(),rootLay.getHeight(),config);
        Canvas canvas=new Canvas(bitmap);
        rootLay.draw(canvas);

        return bitmap;
    }

    /**
     * 根据布局生成Bitmap工具
     * @param width 待生成图片布局宽度
     * @param height 待生成图片布局长度
     * @param rootLay 待生成图片布局
     * @param config 输出图片配置
     * @return 返回图片
     */
    public static Bitmap screenshot(int width,int height,View rootLay,Bitmap.Config config){
        Bitmap bitmap=Bitmap.createBitmap(width,height,config);
        Canvas canvas=new Canvas(bitmap);
        rootLay.draw(canvas);

        return bitmap;
    }

    /**
     *
     * @param totalWidth 总宽度
     * @param totalHeight 总长度
     * @param maps 合并图片列表,map需要按顺序排列
     * @param config 输出图片背景配置
     * @return 返回图片
     */
    public static Bitmap merge(int totalWidth, int totalHeight, Map<Integer,Bitmap> maps,Bitmap.Config config){
        Bitmap kungBitmap=Bitmap.createBitmap(totalWidth,totalHeight,config);
        Canvas kungCanvas=new Canvas(kungBitmap);
        kungCanvas.drawColor(Color.WHITE);
        Paint paint=new Paint();

        if(maps.size()>0){
            for(int key:maps.keySet()){
                Bitmap bit=maps.get(key);
                kungCanvas.drawBitmap(bit,0,key,paint);
            }
        }
        return kungBitmap;
    }

    /**
     * @param context 上下文
     * @param bitmap 图片
     * @param bitName 名称
     * @return 调用IO流写入本地相册中,需要外部读写权限
     */
    public static boolean saveImg(Context context, Bitmap bitmap, String bitName) {
        String fileName;
        File file;
        if (Build.BRAND.equals("xiaomi")) {
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + bitName;
        } else if (Build.BRAND.equals("Huawei")) {
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera" + bitName;
        } else {
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + bitName;
        }

        file = new File(fileName);

        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                outputStream.flush();
                outputStream.close();
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), bitName, null);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**调用系统本地相册API保存
     *
     * @param context 上下文
     * @param bitmap 图片
     * @param bitName 图片名
     * @return 返回保存成功如否
     */
    public static boolean saveImgBySys(Context context,Bitmap bitmap, String bitName){
        MediaStore.Images.Media.insertImage(context.getContentResolver(),bitmap,bitName,"");
        return true;
    }


}
