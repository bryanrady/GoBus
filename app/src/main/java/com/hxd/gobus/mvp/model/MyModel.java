package com.hxd.gobus.mvp.model;

import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.params.VehicleParamsManager;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.di.scopes.FragmentScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IMyContract;

import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:45
 */
@FragmentScope
public class MyModel extends BaseModel implements IMyContract.Model {

    @Inject
    public MyModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Observable<String> queryAdmin() {
        return RxRetrofitClient
                .create()
                .url(Constant.QUERY_ADMIN_CONTACTS)
                .raw(HttpParamsManager.getBaseParams(new JSONObject()).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
