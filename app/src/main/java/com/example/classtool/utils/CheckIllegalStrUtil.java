package com.example.classtool.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckIllegalStrUtil {
    public static boolean isIllegalStr(String str){
        if(str.isEmpty()){
            return false;
        }

        Pattern p=Pattern.compile("[a-zA-Z\u4e00-\u9fa5|\\！|\\，|\\。|\\（|\\）|\\《\\》|\\”|\\“|\\？|\\：|\\；|\\【|\\】]");
        Matcher m=p.matcher(str);
        if(m.find()){
            return true;
            }

        return false;
    }
}
