package com.hxd.gobus.mvp.model;

import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.params.MaintainParamsManager;
import com.hxd.gobus.core.http.params.VehicleParamsManager;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IMaintainRecordContract;
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
public class MaintainRecordModel extends BaseModel implements IMaintainRecordContract.Model {

    @Inject
    public MaintainRecordModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Observable<String> getMaintainList(String vehicleMark, String startDate, String endDate, String remarks, int page, int rows) {
        return RxRetrofitClient
                .create()
                .url(Constant.QUERY_UPHOLD_CAR_LIST)
                .raw(HttpParamsManager.getBaseParams(
                        MaintainParamsManager.queryMaintainListParams(vehicleMark,startDate,endDate,remarks,page, rows)).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> deleteMaintainApply(Integer upkeepApplyId) {
        return RxRetrofitClient
                .create()
                .url(Constant.DELETE_UPHOLD_CAR_INFO)
                .raw(HttpParamsManager.getBaseParams(
                        MaintainParamsManager.deleteMaintainApplyParams(upkeepApplyId))
                        .toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
