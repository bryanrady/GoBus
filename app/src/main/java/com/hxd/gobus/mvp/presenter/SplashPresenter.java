package com.hxd.gobus.mvp.presenter;

import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.ISplashContract;
import com.hxd.gobus.mvp.model.SplashModel;

import javax.inject.Inject;

/**
 * @author: wangqingbin
 * @date: 2019/7/31 15:18
 */
@ActivityScope
public class SplashPresenter extends BasePresenter<ISplashContract.View,SplashModel> {

    @Inject
    public SplashPresenter(){}

    public void intoNextPage(){
        boolean isAlreadyLogin = getModel().isAlreadyLogin();
        if (isAlreadyLogin){
            getView().intoMain();
        }else{
            getView().intoLogin();
        }
    }

}
