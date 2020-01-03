package com.hxd.gobus.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hxd.gobus.BusApp;

import java.util.Map;

/**
 * @description SharedPreferences的一个工具类 调用setParam就能保存 String, Integer,
 * Boolean, Float, Long类型的参数 同样调用getParam就能获取到保存在手机里面的数据
 *
 * @author wangqingbin
 * @time 2019/7/10 16:22
 */
public class SPUtils {

    private static final String FILE_NAME = "sp_gobus";

    private static SharedPreferences getSharedPreferences() {
        return BusApp.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * @description 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @author wangqingbin
     * @time 2019/7/10 16:27
     * @param key
     * @param object
     * @return
     */
    public static void setParams(String key, Object object) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        String type = object.getClass().getSimpleName();
        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }
        editor.commit();
    }

    /**
     * @description 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @author wangqingbin
     * @time 2019/7/10 16:29
     * @param key           键
     * @param defaultValue 默认值
     * @return
     */
    public static Object getParams(String key, Object defaultValue) {
        String type = defaultValue.getClass().getSimpleName();
        if ("String".equals(type)) {
            return getSharedPreferences().getString(key, (String) defaultValue);
        } else if ("Integer".equals(type)) {
            return getSharedPreferences().getInt(key, (Integer) defaultValue);
        } else if ("Boolean".equals(type)) {
            return getSharedPreferences().getBoolean(key, (Boolean) defaultValue);
        } else if ("Float".equals(type)) {
            return getSharedPreferences().getFloat(key, (Float) defaultValue);
        } else if ("Long".equals(type)) {
            return getSharedPreferences().getLong(key, (Long) defaultValue);
        }
        return null;
    }

    /**
     * @description 查询某个key是否已经存在
     * @author wangqingbin
     * @time 2019/7/10 16:30
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        return getSharedPreferences().contains(key);
    }

    /**
     * @description 返回所有的键值对
     * @author wangqingbin
     * @time 2019/7/10 16:31
     * @param
     * @return
     */
    public static Map<String, ?> getAll() {
        return getSharedPreferences().getAll();
    }
}
