package com.hxd.gobus.di.components;

import android.app.Application;

import com.hxd.gobus.BusApp;
import com.hxd.gobus.di.modules.ActivityModule;
import com.hxd.gobus.di.modules.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * 注入AndroidInjectionModule，以确保Android的类(Activity、Fragment、Service、BroadcastReceiver及ContentProvider等)可以绑定
 * https://www.jianshu.com/p/f12d4ec0ae4es
 * @author: wangqingbin
 * @date: 2019/7/11 19:58
 */

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AndroidSupportInjectionModule.class,
        AppModule.class,
        ActivityModule.class})
public interface AppComponent{

    void inject(BusApp app);

    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

}
