package com.hxd.gobus.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

/**
 * @author: wangqingbin
 * @date: 2019/7/23 11:48
 */

public class LeakedUtils {

    public static void fixLeak(Context context){
        if (context == null) {
            return;
        }
        if ("huawei".equalsIgnoreCase(Build.MANUFACTURER)){
            fixLastSrvViewLeak(context);
        }
        fixNextServedViewLeak(context);
    }

    /**
     * https://www.jianshu.com/p/95242060320f
     * InputMethodManager.mLastSrvView  发生泄漏 而且还只是在华为手机上出现 这是华为对InputMethodManager做了修改的原因
     */
    public static void fixLastSrvViewLeak(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        try {
            Field mLastSrvViewField = imm.getClass().getDeclaredField("mLastSrvView");
            if (mLastSrvViewField != null){
                if (!mLastSrvViewField.isAccessible()) {
                    mLastSrvViewField.setAccessible(true);
                }
                mLastSrvViewField.set(imm,null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * InputMethodManager.mNextServedView  发生泄漏
     */
    public static void fixNextServedViewLeak(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        String [] viewArray = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field filed;
        Object filedObject;

        for (String view:viewArray) {
            try{
                filed = imm.getClass().getDeclaredField(view);
                if (!filed.isAccessible()) {
                    filed.setAccessible(true);
                }
                filedObject = filed.get(imm);
                if (filedObject != null && filedObject instanceof View) {
                    View fileView = (View) filedObject;
                    //去掉了会导致有输入框的界面无法弹出软键盘的问题
                    if (fileView.getContext() == context) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        filed.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        break;// 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                    }
                }
            }catch(Throwable t){
                t.printStackTrace();
            }
        }
    }

    public static void fixFragmentInputMethodManagerLeak(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) {
            return;
        }
        String [] viewArray = new String[]{"mCurRootView", "mServedView", "mNextServedView","mLastSrvView"};
        Field filed;
        Object filedObject;

        for (String view:viewArray) {
            try{
                filed = inputMethodManager.getClass().getDeclaredField(view);
                if (!filed.isAccessible()) {
                    filed.setAccessible(true);
                }
                filedObject = filed.get(inputMethodManager);
                if (filedObject != null && filedObject instanceof View) {
                    filed.set(inputMethodManager, null); // 置空，破坏掉path to gc节点
                }
            }catch(Throwable t){
                t.printStackTrace();
            }
        }
    }



}
