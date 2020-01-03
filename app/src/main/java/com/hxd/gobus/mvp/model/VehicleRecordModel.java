package com.hxd.gobus.mvp.model;

import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.params.VehicleParamsManager;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IVehicleRecordContract;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: wangqingbin
 * @date: 2019/8/12 15:36
 */
@ActivityScope
public class VehicleRecordModel extends BaseModel implements IVehicleRecordContract.Model {

    @Inject
    public VehicleRecordModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Observable<String> getVehicleList(String vehicleMark, String startDate, String endDate, String useReason, int page, int rows) {
        return RxRetrofitClient
                .create()
                .url(Constant.QUERY_USE_CAR_LIST)
                .raw(HttpParamsManager.getBaseParams(
                        VehicleParamsManager.queryVehicleListParams(vehicleMark,startDate,endDate,useReason,page,rows))
                        .toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> deleteVehicleApply(Integer managementId) {
        return RxRetrofitClient
                .create()
                .url(Constant.DELATE_USE_CAR)
                .raw(HttpParamsManager.getBaseParams(
                        VehicleParamsManager.deleteVehicleApplyParams(managementId))
                        .toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
