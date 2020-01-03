package com.hxd.gobus.di.modules;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import com.hxd.gobus.bean.User;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.utils.GsonUtils;
import com.hxd.gobus.utils.SPUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author: wangqingbin
 * @date: 2019/7/11 19:59
 */
@Module
public class AppModule {

    @Provides
    @Singleton
    public Context provideApplicationContext(Application application){
        return application;
    }

    /**
     * 提供一个空的presenter给不需要presenter的view
     * @return
     */
    @Singleton
    @Provides
    @Nullable
    public BasePresenter provideBasePresenter() {
        return null;
    }

    @Singleton
    @Provides
    @Nullable
    public User provideUser() {
        String userStr = (String) SPUtils.getParams("user", null);
        if (userStr != null){
            User user = GsonUtils.jsonToObject(userStr, User.class);
            if (user != null){
                return user;
            }
        }
        return null;
    }

}
