package com.hxd.gobus.mvp.model;

import com.hxd.gobus.bean.User;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.ISplashContract;
import com.hxd.gobus.utils.GsonUtils;
import com.hxd.gobus.utils.SPUtils;

import javax.inject.Inject;

/**
 * @author: wangqingbin
 * @date: 2019/7/31 15:16
 */
@ActivityScope
public class SplashModel extends BaseModel implements ISplashContract.Model {

    @Inject
    public SplashModel(){

    }

    @Override
    public void onDestroy() {

    }

    /**
     * true 已经登录过  false 没有登录过
     * @return
     */
    @Override
    public boolean isAlreadyLogin() {
        boolean isAlreadyLogin = (boolean) SPUtils.getParams("isAlreadyLogin", false);
        return isAlreadyLogin;
    }
}
