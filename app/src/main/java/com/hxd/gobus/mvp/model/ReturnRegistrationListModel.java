package com.hxd.gobus.mvp.model;

import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.params.VehicleParamsManager;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IReturnRegistrationListContract;
import com.hxd.gobus.mvp.contract.IVehicleRegistrationListContract;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: wangqingbin
 * @date: 2019/8/12 15:36
 */
@ActivityScope
public class ReturnRegistrationListModel extends BaseModel implements IReturnRegistrationListContract.Model {

    @Inject
    public ReturnRegistrationListModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Observable<String> getReturnRegistrationList(int page, int rows) {
        return RxRetrofitClient
                .create()
                .url(Constant.QUERY_USE_CAR_LIST)
                .raw(HttpParamsManager.getBaseParams(
                        VehicleParamsManager.queryReturnRegistrationListParams(page, rows)).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
