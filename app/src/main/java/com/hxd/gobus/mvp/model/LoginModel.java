package com.hxd.gobus.mvp.model;

import com.hxd.gobus.bean.User;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.ILoginContract;
import com.hxd.gobus.utils.GsonUtils;
import com.hxd.gobus.utils.SPUtils;
import com.hxd.gobus.utils.SharePreferenceManager;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:45
 */
@ActivityScope
public class LoginModel extends BaseModel implements ILoginContract.Model {

    @Inject
    public LoginModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Observable<String> verifyUser(String loginName, String loginPassword) {
        return RxRetrofitClient
                .create()
                .url(Constant.LOGIN_URL)
                .raw(HttpParamsManager.getBaseParams(HttpParamsManager.loginParams(loginName, loginPassword)).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void saveCheckStatus(boolean isChecked) {
        SPUtils.setParams("isChecked",isChecked);
    }

    @Override
    public boolean getCheckStatus() {
        return (boolean) SPUtils.getParams("isChecked",false);
    }

    @Override
    public void saveUser(User user) {
        SPUtils.setParams("userJson", GsonUtils.objectToJson(user));
    }

    @Override
    public void setCachedPsw(String password) {
        SharePreferenceManager.setCachedPsw(password);
    }

    @Override
    public User getUser() {
        String userJson = (String) SPUtils.getParams("userJson", "");
        User user = GsonUtils.jsonToObject(userJson, User.class);
        return user;
    }

    @Override
    public void setAlreadyLogin() {
        SPUtils.setParams("isAlreadyLogin", true);
    }

}
