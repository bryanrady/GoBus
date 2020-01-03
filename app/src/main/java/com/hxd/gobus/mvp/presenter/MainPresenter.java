package com.hxd.gobus.mvp.presenter;

import com.hxd.gobus.bean.ApkInfo;
import com.hxd.gobus.bean.User;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IMainContract;
import com.hxd.gobus.mvp.model.MainModel;
import com.hxd.gobus.utils.AppUtil;
import com.hxd.gobus.utils.GsonUtils;


import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * @author: wangqingbin
 * @date: 2019/7/16 16:38
 */
@ActivityScope
public class MainPresenter extends BasePresenter<IMainContract.View,MainModel> {

    @Inject
    public MainPresenter() {}

    public void initUser(){
        User user = getModel().getUser();
        if (user != null){
            User.setInstance(user);
        }
    }

    public void checkApkUpdate(){
        getView().showLoading();
        getModel().checkApkUpdate().subscribe(new ObserverImpl() {
            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onComplete() {
                getView().dismissLoading();
            }

            @Override
            protected void handleResponse(String response) {
                ApkInfo info = GsonUtils.jsonToObject(response, ApkInfo.class);
                if(info != null){
                    if (Integer.parseInt(info.getVersionCode()) > AppUtil.getAppVersionCode()) {
                        getView().showUpdateDialog(info);
                    }
                }
            }

            @Override
            protected void handleError(String errorMsg) {
                getView().showToast(errorMsg);
            }
        });
    }

}
