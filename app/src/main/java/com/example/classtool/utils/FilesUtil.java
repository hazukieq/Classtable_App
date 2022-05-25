package com.example.classtool.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.classtool.models.Class_cardmodel;
import com.example.classtool.models.Class_colors_set;
import com.example.classtool.models.FindSort;
import com.example.classtool.models.QTime;
import com.example.classtool.models.Time_sets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilesUtil {

    public static void InitializeFiles(Context context){
        try{
           // File dir=context.getDir("课表助手数据",Context.MODE_PRIVATE);

            //if(!dir.exists()) dir.mkdir();

            File schedir=context.getDir("课表数据",Context.MODE_PRIVATE);
            File timedir=context.getDir("作息时间数据",Context.MODE_PRIVATE);
            File indexdir=context.getDir("索引",Context.MODE_PRIVATE);
            if(!schedir.exists()) schedir.mkdir();
            if(!timedir.exists()) timedir.mkdir();
            if(!indexdir.exists()) indexdir.mkdir();
           // Log.i( "InitializeFiles: ",schedir.getAbsolutePath());
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public  static  boolean writFile(Context context,@NonNull String classtemplate_name,@NonNull List<Class_cardmodel> alls) {
        File dir = context.getDir("课表数据",Context.MODE_PRIVATE);
        try {
            if (!dir.exists()) dir.mkdir();
            File file = new File(dir, classtemplate_name + ".txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            //   FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            if (alls.size() == 0){
                //writer.write("");
                return false;
            }
            else {
                for (Class_cardmodel cl : alls) {
                    String str =cl.getClass_date() + "," + cl.getClass_course() + "," + cl.getClass_startClass() + "," + cl.getClass_totalClass() + "," + cl.getClass_classPlace() + "," + cl.getLassColor()+",0,"+cl.getOtherNotes()+",无,0";
                    writer.write(str);
                    writer.newLine();
                }
            }

            writer.close();
//            writer.close();
        } catch(IOException e){
            e.printStackTrace();
        }
        return true;
    }


    public static List<Class_cardmodel> readFileData(Context context,@NonNull String fileName){
        List<String> allq=new ArrayList<>();
        List<Class_cardmodel> dall=new ArrayList<>();
        File dirPath=context.getDir("课表数据",Context.MODE_PRIVATE);
        try{
            if(!dirPath.exists()) dirPath.mkdir();
            File newdir=new File(dirPath,fileName+".txt");
            if(!newdir.exists()){
                newdir.createNewFile();
            }

            FileReader inputStream=new FileReader(newdir);
            BufferedReader reader=new BufferedReader(inputStream);
            String record;
            while ((record=reader.readLine())!=null){
                allq.add(record);
            }
            reader.close();
            if(allq.size()==0) {
            }else{
                for (String str : allq) {
                    String[] trs = str.split(",");
                    dall.add(new Class_cardmodel(trs[0], trs[1], trs[2], trs[3], trs[4], trs[5], Class_colors_set.Class_colors[FindSort.returnColorSort(Time_sets.colors, trs[5])], trs[7], trs[8], 0));
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        return dall;
    }


    public static  List<String> readSchedulAndTimeTag(Context context){
        List<String> allq=new ArrayList<>();
        File dirPath=context.getDir("索引",Context.MODE_PRIVATE);
        try{
            if(!dirPath.exists()) dirPath.mkdir();
            File newdir=new File(dirPath,"课表索引.txt");
            if(!newdir.exists()){
                newdir.createNewFile();
            }

            FileReader inputStream=new FileReader(newdir);
            BufferedReader reader=new BufferedReader(inputStream);
            String record;
            while ((record=reader.readLine())!=null){
                allq.add(record);
            }
            reader.close();


        }catch (IOException e){
            e.printStackTrace();
        }

        return allq;
    }

    public static  boolean AppendScheDulAndTimeTag(Context context,String tag){
        File dirPath=context.getDir("索引",Context.MODE_PRIVATE);//new File(Environment.getExternalStorageDirectory(),"/课表助手/索引");
        try{
            if(!dirPath.exists()) dirPath.mkdir();
            File newdir=new File(dirPath,"课表索引.txt");
            if(!newdir.exists()){
                newdir.createNewFile();
            }
            FileWriter out=new FileWriter(newdir,true);
            BufferedWriter writer=new BufferedWriter(out);
                    writer.write(tag);
                    writer.newLine();
                    writer.close();



        }catch (IOException e){
            e.printStackTrace();
        }

        return true;
    }

    public static  boolean RemoveScheDulAndTimeTag(Context context,List<String> walls,String tag){
        File dirPath=context.getDir("索引",Context.MODE_PRIVATE);//new File(Environment.getExternalStorageDirectory(),"/课表助手/索引");
        try{
            if(!dirPath.exists()) dirPath.mkdir();
            File newdir=new File(dirPath,"课表索引.txt");
            if(!newdir.exists()){
                newdir.createNewFile();
            }

            for(String str:walls){
                if(str.split(",")[0].equals(tag)){
                    walls.remove(str);
                }
            }

            FileWriter out=new FileWriter(newdir,false);
            BufferedWriter writer=new BufferedWriter(out);

            List<String> neowalls=new ArrayList<>();
            neowalls.addAll(walls);
            for(String strw:neowalls){
                writer.write(strw);
                writer.newLine();
                writer.close();
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        return true;
    }

    public static  List<String> readTimeTag(Context context){
        List<String> allq=new ArrayList<>();
        File dirPath=context.getDir("索引",Context.MODE_PRIVATE);//new File(Environment.getExternalStorageDirectory(),"/课表助手/索引");
        try{
            if(!dirPath.exists()) dirPath.mkdir();
            File newdir=new File(dirPath,"时间总索引.txt");
            if(!newdir.exists()){
                newdir.createNewFile();
            }

            FileReader inputStream=new FileReader(newdir);
            BufferedReader reader=new BufferedReader(inputStream);
            String record;
            while ((record=reader.readLine())!=null){
                allq.add(record);
            }
            reader.close();


        }catch (IOException e){
            e.printStackTrace();
        }

        return allq;
    }

    public static  boolean AppendTimeTag(Context context,String tag){
        File dirPath=context.getDir("索引",Context.MODE_PRIVATE);//new File(Environment.getExternalStorageDirectory(),"/课表助手/索引");
        try{
            if(!dirPath.exists()) dirPath.mkdir();
            File newdir=new File(dirPath,"时间总索引.txt");
            if(!newdir.exists()){
                newdir.createNewFile();
            }
            FileWriter out=new FileWriter(newdir,true);
            BufferedWriter writer=new BufferedWriter(out);

            writer.write(tag);
            writer.newLine();
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        return true;
    }

    public static  boolean AppendTimeTags(Context context,List<String> tagas){
        File dirPath=context.getDir("索引",Context.MODE_PRIVATE);//new File(Environment.getExternalStorageDirectory(),"/课表助手/索引");
        try{
            if(!dirPath.exists()) dirPath.mkdir();
            File newdir=new File(dirPath,"时间总索引.txt");
            if(!newdir.exists()){
                newdir.createNewFile();
            }
            FileWriter out=new FileWriter(newdir,false);
            BufferedWriter writer=new BufferedWriter(out);

            for(String tdf:tagas){
                writer.write(tdf);
                writer.newLine();
                writer.close();
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        return true;
    }



    public static  boolean AppendClassTime(Context context,List<QTime> allq,String name){

        File dirPath=context.getDir("作息时间数据",Context.MODE_PRIVATE);//new File(Environment.getExternalStorageDirectory(),"/课表助手/作息时间数据/");
        try{
            if(!dirPath.exists()) dirPath.mkdir();
            File newdir=new File(dirPath,name+".txt");
            if(!newdir.exists()){
                newdir.createNewFile();
            }
            FileWriter out=new FileWriter(newdir,false);
            BufferedWriter writer=new BufferedWriter(out);
            for(QTime qTime:allq){
                writer.write(qTime.getSorq()+","+qTime.getSortStr()+","+qTime.getStart2end().replace("-","<br/>"));
                writer.newLine();
            }
            writer.close();

        }catch (IOException e){
            e.printStackTrace();
        }

        return true;
    }



    public static  List<QTime> readClassTime(Context context,String filename){
        List<QTime> allq=new ArrayList<>();
        File dirPath=context.getDir("作息时间数据",Context.MODE_PRIVATE);//new File(Environment.getExternalStorageDirectory(),"/课表助手/作息时间数据");
        try{
            if(!dirPath.exists()) dirPath.mkdir();
            File newdir=new File(dirPath,filename+".txt");
            if(!newdir.exists()){
                newdir.createNewFile();
            }

            FileReader inputStream=new FileReader(newdir);
            BufferedReader reader=new BufferedReader(inputStream);
            String record;
            while ((record=reader.readLine())!=null){
                String[] strs=record.split(",");
                allq.add(new QTime(FindSort.returnColorSort(Time_sets.detail_real_numStrs,strs[0]),strs[1],strs[2].replace("<br/>","-")));
            }
            reader.close();


        }catch (IOException e){
            e.printStackTrace();
        }

        return allq;
    }

    public static  boolean deleteSingleFile(Context context,String fileName){
        File dirPath=context.getDir("作息时间数据",Context.MODE_PRIVATE);//new File(Environment.getExternalStorageDirectory(),"/课表助手/作息时间数据");
        File file=new File(dirPath,fileName+".txt");
        if(file.exists()&&file.isFile()){
            file.delete();
            return true;
        }return false;
    }

    public static  boolean deleteScheFile(Context context,String fileName){
        File dirPath=context.getDir("课表数据",Context.MODE_PRIVATE);//new File(Environment.getExternalStorageDirectory(),"课表数据");
        File file=new File(dirPath,fileName+".txt");
        if(file.exists()&&file.isFile()){
            file.delete();
            return true;
        }

        return false;
    }

    public static boolean renameFile(String oldName,String newName){
        File dirPath=new File(Environment.getExternalStorageDirectory(),"/课表助手/课表数据");
        File oldFile=new File(dirPath,oldName+".txt");
        String olpa=oldFile.getAbsolutePath();
        String neopa=olpa.replace(oldName,newName);

         oldFile.renameTo(new File(neopa));
         return  true;
    }

    public static  boolean saveImg(Context context,Bitmap bitmap,String bitName){
        String fileName;
        File file;
        if(Build.BRAND.equals("xiaomi")){
            fileName=Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/"+bitName;
        }else if(Build.BRAND.equals("Huawei")){
            fileName=Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera"+bitName;
        }else{
            fileName=Environment.getExternalStorageDirectory().getPath()+"/DCIM/"+bitName;
        }

        file=new File(fileName);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream outputStream;
        try{
            outputStream=new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.JPEG,90,outputStream)){
                outputStream.flush();
                outputStream.close();
                MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getAbsolutePath(),bitName,null);
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
