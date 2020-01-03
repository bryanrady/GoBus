package com.hxd.gobus.mvp.model;

import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.params.RepairParamsManager;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IEmergencyRescueListContract;
import com.hxd.gobus.mvp.contract.IVehicleDataListContract;

import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: wangqingbin
 * @date: 2019/8/12 15:36
 */
@ActivityScope
public class EmergencyRescueListModel extends BaseModel implements IEmergencyRescueListContract.Model {

    @Inject
    public EmergencyRescueListModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Observable<String> queryRescueList() {
        return RxRetrofitClient
                .create()
                .url(Constant.EMERGENCY_RESCUE_LIST_URL)
                .raw(HttpParamsManager.getBaseParams(new JSONObject()).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
