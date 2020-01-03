package com.hxd.gobus.mvp.presenter;

import com.hxd.gobus.bean.ApkInfo;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.ISettingContract;
import com.hxd.gobus.mvp.model.SettingModel;
import com.hxd.gobus.utils.AppManager;
import com.hxd.gobus.utils.AppUtil;
import com.hxd.gobus.utils.GsonUtils;
import com.hxd.gobus.utils.SPUtils;

import javax.inject.Inject;

import cn.jpush.im.android.api.JMessageClient;
import io.reactivex.disposables.Disposable;

/**
 * @author: wangqingbin
 * @date: 2019/7/25 10:26
 */
@ActivityScope
public class SettingPresenter extends BasePresenter<ISettingContract.View,SettingModel> {

    @Inject
    public SettingPresenter(){

    }

    public void logout(){
        getView().showLoading();
        getModel().logout().subscribe(new ObserverImpl() {

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
                AppManager.getInstance().finishAllActivity();
                JMessageClient.logout();
                getModel().setNotAlreadyLogin();
                getView().intoLogin();
            }

            @Override
            protected void handleError(String errorMsg) {
                getView().showToast(errorMsg);
            }
        });
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
                    }else{
                        getView().showToast("您当前安装的已经是最新的版本^_^");
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
