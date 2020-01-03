package com.hxd.gobus.mvp.model;

import com.hxd.gobus.bean.User;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IMainContract;
import com.hxd.gobus.utils.GsonUtils;
import com.hxd.gobus.utils.SPUtils;

import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:45
 */
@ActivityScope
public class MainModel extends BaseModel implements IMainContract.Model {

    @Inject
    public MainModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public User getUser() {
        String userJson = (String) SPUtils.getParams("userJson", "");
        if (userJson != null){
            User user = GsonUtils.jsonToObject(userJson, User.class);
            return user;
        }
        return null;
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
