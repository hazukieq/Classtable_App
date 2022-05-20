package com.example.classtool.utils;

import android.os.Environment;
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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilesUtil {
    public static void createAllFileDir(){
        File firstDir=new File(Environment.getExternalStorageDirectory(), "/课表助手/");
        File schedulDir=new File(firstDir,"/课表数据/");
        File timeDir=new File(firstDir,"/作息时间数据");
        File indexDir=new File(firstDir,"/索引");

        if(!firstDir.exists()) firstDir.mkdir();
        if(firstDir.exists()&&(!schedulDir.exists())&&(!timeDir.exists())&&(!indexDir.exists())){
            schedulDir.mkdir();
            timeDir.mkdir();
            indexDir.mkdir();
        }
    }


    public  static  boolean writFile(@NonNull String classtemplate_name,@NonNull List<Class_cardmodel> alls) {
        File dir = new File(Environment.getExternalStorageDirectory(), "/课表助手/课表数据");
        Log.i( "writFilepath: ",dir.getAbsolutePath());

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


    public static List<Class_cardmodel> readFileData(@NonNull String fileName){
        List<String> allq=new ArrayList<>();
        List<Class_cardmodel> dall=new ArrayList<>();
        File dirPath=new File(Environment.getExternalStorageDirectory(),"/课表助手/课表数据");
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


    public static  List<String> readSchedulAndTimeTag(){
        List<String> allq=new ArrayList<>();
        File dirPath=new File(Environment.getExternalStorageDirectory(),"/课表助手/索引");
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

    public static  boolean AppendScheDulAndTimeTag(String tag){
        File dirPath=new File(Environment.getExternalStorageDirectory(),"/课表助手/索引");
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

    public static  boolean RemoveScheDulAndTimeTag(List<String> walls,String tag){
        File dirPath=new File(Environment.getExternalStorageDirectory(),"/课表助手/索引");
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

    public static  List<String> readTimeTag(){
        List<String> allq=new ArrayList<>();
        File dirPath=new File(Environment.getExternalStorageDirectory(),"/课表助手/索引");
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

    public static  boolean AppendTimeTag(String tag){
        File dirPath=new File(Environment.getExternalStorageDirectory(),"/课表助手/索引");
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

    public static  boolean AppendTimeTags(List<String> tagas){
        File dirPath=new File(Environment.getExternalStorageDirectory(),"/课表助手/索引");
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



    public static  boolean AppendClassTime(List<QTime> allq,String name){

        File dirPath=new File(Environment.getExternalStorageDirectory(),"/课表助手/作息时间数据/");
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



    public static  List<QTime> readClassTime(String filename){
        List<QTime> allq=new ArrayList<>();
        File dirPath=new File(Environment.getExternalStorageDirectory(),"/课表助手/作息时间数据");
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

    public static  boolean deleteSingleFile(String fileName){
        File dirPath=new File(Environment.getExternalStorageDirectory(),"/课表助手/作息时间数据");
        File file=new File(dirPath,fileName+".txt");
        if(file.exists()&&file.isFile()){
            file.delete();
            return true;
        }return false;
    }

    public static  boolean deleteScheFile(String fileName){
        File dirPath=new File(Environment.getExternalStorageDirectory(),"/课表助手/课表数据");
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

}
