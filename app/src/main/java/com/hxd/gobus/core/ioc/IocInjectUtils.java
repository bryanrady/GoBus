package com.hxd.gobus.core.ioc;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import com.hxd.gobus.BusApp;

import java.lang.reflect.Method;
import butterknife.OnClick;

/**
 * @description IOC注入方式在初始化界面和点击的时候做网络校验
 * @author wangqingbin
 * @time 2019/7/10 16:13
 */
public class IocInjectUtils {

    static final String NETWORK_NOT_CONNECT = "您的网络连接有问题!";

    static {

    }

    /**
     * @description
     * @author wangqingbin
     * @time 2019/7/10 16:13
     * @param context   上下文
     * @param model     注入的那个类的实例s
     * @return
     */
    public static void injectNetwork(Context context,Object model){
        Class<?> modelClass = model.getClass();
        Method[] declaredMethods = modelClass.getDeclaredMethods();
        for (Method method : declaredMethods){
            CheckNetwork checkNetwork = method.getAnnotation(CheckNetwork.class);
            String hint = null;
            if (checkNetwork != null) {
                hint = checkNetwork.value();
            }
            if (!isNetConnected(BusApp.getContext())) {
                Toast.makeText(context, hint, Toast.LENGTH_SHORT).show();
                return;
            }
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                int[] values = onClick.value();
                for (int viewId : values) {
                    try {
                        Method findViewByIdMethod = modelClass.getMethod("findViewById", int.class);
                        View view = (View) findViewByIdMethod.invoke(context, viewId);
                        if (view != null) {
                            view.setOnClickListener(new DeclaredOnClickListener(method, model, hint));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    private static class DeclaredOnClickListener implements View.OnClickListener {

        //设置点击事件的方法
        private Method mMethod;
        //在那个类中
        private Object mObject;
        //是否检查网络
        private String mNoNetHint;

        public DeclaredOnClickListener(Method method, Object o, String hint) {
            this.mMethod = method;
            this.mObject = o;
            this.mNoNetHint = hint;
        }

        @Override
        public void onClick(View v) {
            try {
                mMethod.setAccessible(true);
                if (mNoNetHint != null && !isNetConnected(BusApp.getContext())) {
                    Toast.makeText(v.getContext(), mNoNetHint, Toast.LENGTH_SHORT).show();
                    return;
                }
                mMethod.invoke(mObject, v);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    mMethod.invoke(mObject, (Object[]) null);//当方法体里面没有参数时候调用改方法，执行没有方法体的修饰的函数
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * @description 检测网络是否连接
     * @author wangqingbin
     * @time 2019/7/10 16:33
     * @param context
     * @return
     */
    private static boolean isNetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] infos = cm.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo net : infos) {
                    if (net.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
