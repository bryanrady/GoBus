package com.hxd.gobus.utils;

import android.app.Activity;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.hxd.gobus.chat.utils.imagepicker.view.SystemBarTintManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author: wangqingbin
 * @date: 2019/7/22 9:47
 */

public class StatusBarUtils {

    /**
     * 沉浸式设置         4.4透明状态栏    5.0 变色状态栏
     * @param topView       顶部View ToolBar
     * @param bottomView    底部View NavigationBar
     * @param styleColor
     */
    public static void setToolBarStyle(Activity activity, View topView, View bottomView, int styleColor){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){   //4.4以后5.0之前
            //首先把状态栏和导航栏设置透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            //1.设置状态栏
            if(topView != null){
                int statusBarHeight = getStatusBarHeight(activity);

                //第一种
//                ViewGroup.LayoutParams layoutParams = topView.getLayoutParams();
//                layoutParams.height += statusBarHeight;
//                topView.setLayoutParams(layoutParams);

                //第二种
                topView.setPadding(0,topView.getPaddingTop() + statusBarHeight,0,0);

                topView.setBackgroundColor(styleColor);
            }

            //2.设置导航栏
            if (haveNavigation(activity) && bottomView != null) {
                int navigationBarHeight = getNavigationBarHeight(activity);

                //第一种
//                ViewGroup.LayoutParams layoutParams = bottomView.getLayoutParams();
//                layoutParams.height += navigationBarHeight;
//                bottomView.setLayoutParams(layoutParams);

                //第二种
                bottomView.setPadding(0,0,0,bottomView.getPaddingBottom() + navigationBarHeight);

                bottomView.setBackgroundColor(styleColor);
            }

        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){    //5.0之后
            //设置状态栏透明
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置导航栏透明 导航栏设置透明之后 设置颜色就不起作用了
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            //5.0之后可以直接调用设置状态栏和导航栏颜色的方法
            if (topView != null){
                activity.getWindow().setStatusBarColor(styleColor);
            }
            if (bottomView != null){
                activity.getWindow().setNavigationBarColor(styleColor);
            }

        }else{  //4.4以前 直接不考虑

        }
    }

    /**
     * 沉浸式设置和黑色文字设置
     * @param activity
     * @param isDarkColor    文字、图标是否为深色 （true 深色  false 浅色）
     * @param styleColor    状态栏、导航栏颜色
     */
    public static void setToolBarStyle(Activity activity, boolean isDarkColor, int styleColor){
        //设置状态栏颜色和导航栏颜色
        setStatusBarColor(activity, styleColor);

        //设置文字、图标颜色为深色  4.4以上才可以改文字图标颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(OSUtils.isMIUI()) {
                //小米MIUI系统
                setMIUIStatusBarTextMode(activity, isDarkColor);
            } else if(OSUtils.isFlyme()) {
                //魅族flyme系统
                setFlymeStatusBarTextMode(activity, isDarkColor);
            } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //其他系统 6.0以上，直接调用系统方法
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                //4.4以上6.0以下的其他系统，暂时没有修改状态栏的文字图标颜色的方法，有可以加上
            }
        }
    }

    /**
     * 沉浸式设置
     * @param activity
     * @param styleColor
     */
    private static void setStatusBarColor(Activity activity, int styleColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {    //5.0以上直接调用系统方法
            //设置状态栏透明
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置导航栏透明 导航栏设置透明之后 设置颜色就不起作用了
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            activity.getWindow().setStatusBarColor(styleColor);
            if (haveNavigation(activity)) {
                activity.getWindow().setNavigationBarColor(styleColor);
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //首先把状态栏和导航栏设置透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            //4.4使用SystemBarTint库使状态栏和导航栏变色
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(styleColor);
            if (haveNavigation(activity)) {
                tintManager.setNavigationBarTintEnabled(true);
                tintManager.setNavigationBarTintResource(styleColor);
            }
        }
    }

    /**
     * 设置Flyme系统状态栏的文字图标颜色
     * @param activity
     * @param isDarkColor 状态栏文字及图标是否为深色
     * @return
     */
    private static boolean setFlymeStatusBarTextMode(Activity activity, boolean isDarkColor) {
        Window window = activity.getWindow();
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (isDarkColor) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 设置MIUI系统状态栏的文字图标颜色（MIUIV6以上）
     * @param activity
     * @param isDarkColor 状态栏文字及图标是否为深色
     * @return
     */
    private static boolean setMIUIStatusBarTextMode(Activity activity, boolean isDarkColor) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (isDarkColor) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (isDarkColor) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取状态栏高度
     * @return
     */
    private static int getStatusBarHeight(Activity activity) {
        int statusBarHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            String status_bar_height = clazz.getField("status_bar_height").get(object).toString();
            statusBarHeight = Integer.parseInt(status_bar_height);
            //dp--px
            statusBarHeight = activity.getResources().getDimensionPixelSize(statusBarHeight);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 判断是否有导航栏
     * @param activity
     * @return
     */
    private static boolean haveNavigation(Activity activity) {
        //屏幕的高度  真实物理的屏幕
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getRealMetrics(displayMetrics);

        int heightDisplay = displayMetrics.heightPixels;
        //为了防止横屏
        int widthDisplay = displayMetrics.widthPixels;
        DisplayMetrics contentDisplayMetrics = new DisplayMetrics();
        display.getMetrics(contentDisplayMetrics);

        int contentDisplay = contentDisplayMetrics.heightPixels;
        int contentDisplayWidth = contentDisplayMetrics.widthPixels;

        //屏幕内容高度  显示内容的屏幕
        int w = widthDisplay-contentDisplayWidth;
        //哪一方大于0   就有导航栏
        int h = heightDisplay-contentDisplay;
        return w > 0 || h > 0;
    }

    /**
     * 获取导航栏高度
     * @return
     */
    private static int getNavigationBarHeight(Activity activity) {
        int navigationBarHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            String navigation_bar_height = clazz.getField("navigation_bar_height").get(object).toString();
            navigationBarHeight = Integer.parseInt(navigation_bar_height);
            //dp--px
            navigationBarHeight = activity.getResources().getDimensionPixelSize(navigationBarHeight);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return navigationBarHeight;
    }

}
