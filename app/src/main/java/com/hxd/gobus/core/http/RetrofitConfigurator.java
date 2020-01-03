package com.hxd.gobus.core.http;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Interceptor;

/**
 * Created by wangqingbin on 2018/11/6.
 */

public class RetrofitConfigurator {

    private static final HashMap<Object,Object> CONFIGS = new HashMap<>();
    private static final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();

    private static final class Holder{
        private static final RetrofitConfigurator INSTANCE = new RetrofitConfigurator();
    }

    private RetrofitConfigurator(){
        CONFIGS.put(RetrofitConfigKeys.CONFIG_READY.name(),false);
    }

    public static RetrofitConfigurator getInstance(){
        return Holder.INSTANCE;
    }

    /**
     * 设置主机
     * @param host
     */
    public final RetrofitConfigurator withApiHost(String host){
        if(host != null){
            CONFIGS.put(RetrofitConfigKeys.API_HOST,host);
        }
        return this;
    }

    /**
     * 设置上下文
     * @param context
     * @return
     */
    public final RetrofitConfigurator withApplicationContext(Context context){
        CONFIGS.put(RetrofitConfigKeys.APPLICATION_CONTEXT,context);
        return this;
    }

    /**
     * 设置拦截器
     * @param interceptors
     * @return
     */
    public final RetrofitConfigurator withInterceptor(ArrayList<Interceptor> interceptors){
        INTERCEPTORS.addAll(interceptors);
        CONFIGS.put(RetrofitConfigKeys.INTERCEPTOR, INTERCEPTORS);
        return this;
    }

    /**
     * 配置完成 即可使用Retrofit的api
     */
    public final void configure(){
        CONFIGS.put(RetrofitConfigKeys.CONFIG_READY.name(),true);
    }

    /**
     * 获取Retrofit的所有配置信息
     * @return
     */
    public final HashMap<Object,Object> getConfigs(){
        return CONFIGS;
    }

    /**
     * 根据key值获取Retrofit的配置信息
     * @param key
     * @param <T>
     * @return
     */
    public final <T> T getConfiguration(Object key){
        checkConfiguration();
        final Object value = CONFIGS.get(key);
        if (value == null) {
            throw new NullPointerException(key.toString() + " is null");
        }
        return (T)CONFIGS.get(key);
    }

    /**
     * 检查配置是否完成
     */
    private void checkConfiguration(){
        final boolean isReady = (boolean)CONFIGS.get(RetrofitConfigKeys.CONFIG_READY.name());
        if(!isReady){
            throw new RuntimeException("Configuration is not ready,call configure()");
        }
    }
 }
