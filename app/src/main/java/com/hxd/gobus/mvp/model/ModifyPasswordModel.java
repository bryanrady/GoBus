package com.hxd.gobus.mvp.model;

import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IModifyPasswordContract;
import com.hxd.gobus.utils.GsonUtils;
import com.hxd.gobus.utils.SPUtils;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:45
 */
@ActivityScope
public class ModifyPasswordModel extends BaseModel implements IModifyPasswordContract.Model {

    @Inject
    public ModifyPasswordModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Observable<String> modifyPassword(String oldPwd, String newPwd) {
        return RxRetrofitClient
                .create()
                .url(Constant.MODIFY_PASSWORD_URL)
                .raw(HttpParamsManager.getBaseParams(HttpParamsManager.modifyPasswordParams(oldPwd,newPwd)).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void clearUserInfo() {
        SPUtils.setParams("userJson", "");
    }

    @Override
    public void setNotLogin() {
        SPUtils.setParams("isAlreadyLogin", false);
    }
}
