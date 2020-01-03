package com.hxd.gobus.utils;

import java.util.regex.Pattern;

/**
 * 数字校验工具类
 * Created by wangqingbin on 2018/4/9.
 */

public class NumberValidationUtils {

    /**
     * 判断是不是整数
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public static boolean isPositiveNumeric(String string){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(string).matches();
    }

    /**
     * 判断是不是小数
     * @param str
     * @return
     */
    public static boolean isDecimal(String str){
        if(!str.contains(".")){
            return false;
        }
        if(str.charAt(0) == '.' || str.charAt(str.length()-1) == '.'){
            return false;
        }
        return true;
    }
}
