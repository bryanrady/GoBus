package com.hxd.gobus.mvp.model;

import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.params.RepairParamsManager;
import com.hxd.gobus.core.http.params.VehicleParamsManager;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IMaintainRecordContract;
import com.hxd.gobus.mvp.contract.IRepairRecordContract;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: wangqingbin
 * @date: 2019/8/12 15:36
 */
@ActivityScope
public class RepairRecordModel extends BaseModel implements IRepairRecordContract.Model {

    @Inject
    public RepairRecordModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Observable<String> getRepairList(String vehicleMark, String startDate, String endDate, String remarks, int page, int rows) {
        return RxRetrofitClient
                .create()
                .url(Constant.QUERY_REPAIR_CAR_LIST)
                .raw(HttpParamsManager.getBaseParams(
                        RepairParamsManager.queryRepairListParams(vehicleMark,startDate,endDate,remarks,page, rows)).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> deleteRepairApply(Integer repairId) {
        return RxRetrofitClient
                .create()
                .url(Constant.DELATE_REPAIR_CAR)
                .raw(HttpParamsManager.getBaseParams(
                        RepairParamsManager.deleteRepairApplyParams(repairId)).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
