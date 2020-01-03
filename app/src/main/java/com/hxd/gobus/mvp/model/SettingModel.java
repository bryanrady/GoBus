package com.hxd.gobus.mvp.model;

import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.ISettingContract;
import com.hxd.gobus.utils.SPUtils;

import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:45
 */
@ActivityScope
public class SettingModel extends BaseModel implements ISettingContract.Model {

    @Inject
    public SettingModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Observable<String> logout() {
        return RxRetrofitClient
                .create()
                .url(Constant.LOGOUT_URL)
                .raw(HttpParamsManager.getBaseParams(HttpParamsManager.logoutParams()).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void setNotAlreadyLogin() {
        SPUtils.setParams("isAlreadyLogin", false);
    }

    @Override
    public Observable<String> checkApkUpdate() {
        return RxRetrofitClient
                .create()
                .url(Constant.APK_UPDATE_URL)
                .raw(HttpParamsManager.getBaseParams(new JSONObject()).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
