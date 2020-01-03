package com.hxd.gobus.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.util.Stack;

/**
 * 专门用来管理Activity
 * @author wangqingbin
 * @time 2019/7/12 10:43
 */
public class AppManager {

    private static Stack<AppCompatActivity> mStack;

    private volatile static AppManager mInstance;

    private AppManager() {
    }

    public static AppManager getInstance() {
        if (mInstance == null) {
            synchronized (AppManager.class) {
                if (mInstance == null) {
                    mInstance = new AppManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * @description 将Activity添加到堆栈
     * @author wangqingbin
     * @time 2019/7/12 10:44
     * @param
     * @return
     */
    public void addActivity(AppCompatActivity activity) {
        if (mStack == null) {
            mStack = new Stack<>();
        }
        mStack.add(activity);
    }

    /**
     * @description 获取当前Activity（堆栈中最后一个压入的）
     * @author wangqingbin
     * @time 2019/7/12 10:44
     * @param
     * @return
     */
    public AppCompatActivity getCurrentActivity() {
        if (mStack != null){
            AppCompatActivity activity = mStack.lastElement();
            return activity;
        }
        return null;
    }

    /**
     * @description 关闭当前Activity堆栈中最后一个压入的）
     * @author wangqingbin
     * @time 2019/7/12 10:45
     * @param
     * @return
     */
    public void finishActivity() {
        if (mStack != null){
            AppCompatActivity activity = mStack.lastElement();
            finishActivity(activity);
        }
    }

    /**
     * @description 关闭指定的Activity
     * @author wangqingbin
     * @time 2019/7/12 10:46
     * @param
     * @return
     */
    public void finishActivity(AppCompatActivity activity) {
        if (activity != null) {
            mStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * @description 关闭指定类名的Activity
     * @author wangqingbin
     * @time 2019/7/12 10:48
     * @param
     * @return
     */
    public void finishActivity(Class<?> cls) {
        if (mStack != null){
            for (AppCompatActivity activity : mStack) {
                if (activity.getClass().equals(cls)) {
                    finishActivity(activity);
                }
            }
        }
    }

    /**
     * @description 关闭所有的Activity
     * @author wangqingbin
     * @time 2019/7/12 10:49
     * @param
     * @return
     */
    public void finishAllActivity() {
        if(mStack != null){
            for (AppCompatActivity activity : mStack) {
                if (activity != null && !activity.isFinishing()){
                    activity.finish();
                }
            }
            mStack.clear();
        }
    }

    /**
     * @description 退出应用程序
     * @author wangqingbin
     * @time 2019/7/12 10:52
     * @param
     * @return
     */
    public void exit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
