package com.hxd.gobus.core.http.rx.databus;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 数据总线
 * Created by wangqingbin on 2018/11/6.
 */

public class RxBus {

    private Set<Object> mSubscribers;

    /**
     * 注册
     * @param subscriber
     */
    public synchronized void register(Object subscriber){
        mSubscribers.add(subscriber);
    }

    /**
     * 取消注册
     * @param subscriber
     */
    public synchronized void unRegister(Object subscriber){
        mSubscribers.remove(subscriber);
    }

    private static volatile RxBus mInstance;
    private RxBus(){
        //读写分离的集合
        mSubscribers = new CopyOnWriteArraySet<>();
    }

    public static synchronized RxBus getInstance(){
        if(mInstance == null){
            synchronized (RxBus.class){
                if(mInstance == null){
                    mInstance = new RxBus();
                }
            }
        }
        return mInstance;
    }

    /**
     * 主线程
     * @param function
     */
    public void chainMain(Function function){
        Observable.just("") //发一个空事件
                .subscribeOn(Schedulers.io())
                .map(function)//在这里进行网络访问
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object data) throws Exception {
                        //data会传送到总线上
                        if(data == null){
                            return;
                        }
                        send(data);//把数据送到P层
                    }
                });
    }

    /**
     * 子线程
     * @param function
     */
    public void chainBackground(Function function){
        Observable.just("") //发一个空事件
                .subscribeOn(Schedulers.io())
                .map(function)//在这里进行网络访问
                .observeOn(Schedulers.io())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object data) throws Exception {
                        //data会传送到总线上
                        if(data == null){
                            return;
                        }
                        send(data);//把数据送到P层
                    }
                });
    }

    private void send(Object data){
        for(Object subscriber : mSubscribers){
            //扫描注解,将数据发送到注册的对象标记方法的位置
            //subscriber表示层
            callMethodByAnnotion(subscriber,data);
        }
    }

    private void callMethodByAnnotion(Object target, Object data) {
        Method[] methodArray = target.getClass().getDeclaredMethods();
        for(int i=0;i<methodArray.length;i++){
            try {
                RegisterRxBus annotation = methodArray[i].getAnnotation(RegisterRxBus.class);
                if (annotation != null) {
                    Class paramType = methodArray[i].getParameterTypes()[0];
                    if (data.getClass().getName().equals(paramType.getName())) {
                        methodArray[i].invoke(target, new Object[]{data});
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

}
