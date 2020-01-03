package com.hxd.gobus.core.http;

import android.content.Context;

import com.hxd.gobus.core.http.RetrofitConfigKeys;
import com.hxd.gobus.core.http.RetrofitConfigurator;

import java.util.ArrayList;

import okhttp3.Interceptor;

/**
 * Created by wangqingbin on 2018/11/6.
 */

public class RetrofitInit {

    /**
     * 初始化配置信息
     * @param context
     * @return
     */
    public static RetrofitConfigurator init(Context context){
        getConfigurator().withApplicationContext(context);
        return getConfigurator();
    }

    /**
     * 获取配置实例
     * @return
     */
    public static RetrofitConfigurator getConfigurator() {
        return RetrofitConfigurator.getInstance();
    }

    /**
     * 获取主机地址
     * @return
     */
    public static String getApiHost(){
        return getConfiguration(RetrofitConfigKeys.API_HOST.name());
    }

    /**
     * 获取上下文
     * @return
     */
    public static Context getApplicationContext() {
        return getConfiguration(RetrofitConfigKeys.APPLICATION_CONTEXT.name());
    }

    /**
     * 获取拦截器
     * @return
     */
    public static ArrayList<Interceptor> getInterceptor(){
        return getConfiguration(RetrofitConfigKeys.INTERCEPTOR.name());
    }

    /**
     * 获取配置是否完成
     * @return
     */
    public static boolean getConfigReady(){
        return getConfiguration(RetrofitConfigKeys.CONFIG_READY.name());
    }

    /**
     * 后去配置信息
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getConfiguration(Object key) {
        return getConfigurator().getConfiguration(key);
    }

}
