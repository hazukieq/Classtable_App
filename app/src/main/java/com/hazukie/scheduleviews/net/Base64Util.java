package com.hazukie.scheduleviews.net;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.print.PrintAttributes;
import android.util.Base64;
import android.util.Log;

import com.hazukie.scheduleviews.fileutil.FileRootTypes;
import com.hazukie.scheduleviews.fileutil.NetFileOpts;
import com.hazukie.scheduleviews.utils.DateHelper;
import com.hazukie.scheduleviews.utils.DisplayHelper;
import com.hazukie.scheduleviews.utils.ScreenShotHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Base64Util {
    /**
     * 将图片base64数据转化为bitmap
     *
     * @param imgBase64
     * @return
     * @throws Exception
     */
    public static Bitmap base64ToPicture(String imgBase64) throws Exception {
        //处理头部
        if (imgBase64.contains(",")) {
            imgBase64 = imgBase64.split(",")[1];
        }
        //解码开始
        byte[] decode = Base64.decode(imgBase64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decode, 0, decode.length);
    }


    /**
     * 将图片保存到相册并通知刷新
     *
     * @param mContext 上下文
     * @param bitmap 图片
     */
    public static void savePictureToAlbum(Context mContext,Bitmap bitmap,String imgName)  {
        if (bitmap == null) {
            return;
        }
        //把文件插入到系统图库
        //发送广播，通知图库更新
        DateHelper dateHelper=new DateHelper();
        String na=dateHelper.getDai()+imgName;
        Log.i(TAG, "savePictureToAlbum: na="+na);
        ScreenShotHelper.saveImgBySys(mContext,bitmap,na);
        DisplayHelper.Infost(mContext,"图片已保存到相册！");
    }

    //Convert Png to Pdf
    public static void PngToPdf(Context context,Bitmap v,String fileName){
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

        DateHelper dateHelper=new DateHelper();
        String na=dateHelper.getDai()+fileName;

        int tag=0;
        String[] existedFile_lists= NetFileOpts.getInstance(context).getPublicFileList("思维导图_PDF");
        if(existedFile_lists!=null&&existedFile_lists.length>0){
            for (String existedFile_list : existedFile_lists) {
                if (existedFile_list.startsWith(na)) {
                    Log.i( "PngToPdf>>","compare="+existedFile_list);
                    tag+=1;
                }
            }
            Log.i( "PngToPdf>>","tag="+tag);
            if(tag>0) na+="("+tag+")";
        }


        File file = NetFileOpts.getInstance(context).getPublicFile("思维导图_Pdf",na+".pdf");

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
        DisplayHelper.Infost(context,"pdf文件已导出到Documents目录下！");
    }

}
