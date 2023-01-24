package com.hazukie.scheduleviews.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.print.PrintAttributes;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.hazukie.scheduleviews.fileutil.FileRootTypes;
import com.hazukie.scheduleviews.fileutil.Fileystem;
import com.hazukie.scheduleviews.fileutil.NetFileOpts;
import com.hazukie.scheduleviews.models.ClassLabel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
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
        File mImg=NetFileOpts.getInstance(context).getPublicFile("思维导图_图片",bitName);
        FileOutputStream outputStream;
        boolean isPng=bitName.endsWith("png");
        try {
            outputStream = new FileOutputStream(mImg);
            if(isPng){
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                    outputStream.flush();
                    outputStream.close();
                    MediaStore.Images.Media.insertImage(context.getContentResolver(), mImg.getAbsolutePath(), bitName, null);
                    return true;
                }
            }else{
                if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                    outputStream.flush();
                    outputStream.close();
                    MediaStore.Images.Media.insertImage(context.getContentResolver(), mImg.getAbsolutePath(), bitName, null);
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //MediaStore.Images.Media.insertImage(context.getContentResolver(),bitmap,bitName,"");
        return true;
    }


    public static boolean saveTXT(Context context, List<ClassLabel> clsLis,String fileName){
        Fileystem fileystem=Fileystem.getInstance(context);
        StringBuilder builder=new StringBuilder();
        for(ClassLabel cls:clsLis){
            builder.append(cls.toString()+"\n");
        }
        File file= NetFileOpts.getInstance(context).getPublicFile("课表导出",fileName);
        fileystem.putDataStr(file,builder.toString());
        return true;
    }

    public static boolean saveTXT(Context context, String fileName,String content){
        Fileystem fileystem=Fileystem.getInstance(context);

        File file= NetFileOpts.getInstance(context).getPublicFile("课表导出",fileName);
        fileystem.putDataStr(file,content);
        return true;
    }

    public static void ScheToPdf(Context context,Bitmap v,String fileName){
        PdfDocument document=new PdfDocument();

        int pageWidth = PrintAttributes.MediaSize.ISO_A4.getWidthMils() * 72 / 1000;

        float scale = (float) pageWidth / (float) v.getWidth();
        int pageHeight = (int) ((v.getHeight()+500) * scale);
        Log.i( "PngToPdf: ","width="+pageWidth+", height="+pageHeight);

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        PdfDocument.PageInfo pageInfo=new PdfDocument.PageInfo
                .Builder(pageWidth,pageHeight,1).create();
        PdfDocument.Page page=document.startPage(pageInfo);
        Canvas canvas=page.getCanvas();
        canvas.drawBitmap(v,matrix,paint);
        document.finishPage(page);

        File file = NetFileOpts.getInstance(context).getPublicFile("课表导出",fileName+".pdf");

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            document.writeTo(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            document.close();
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        DisplayHelper.Infost(context,"pdf文件已导出到文档公共目录下！");
    }
}
