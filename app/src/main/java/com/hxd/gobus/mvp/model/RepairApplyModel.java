package com.hxd.gobus.mvp.model;

import com.hxd.gobus.bean.Repair;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.AttachParamsManager;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.params.RepairParamsManager;
import com.hxd.gobus.core.http.params.VehicleParamsManager;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IRepairApplyContract;
import com.hxd.gobus.mvp.contract.IVehicleApplyContract;

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
public class RepairApplyModel extends BaseModel implements IRepairApplyContract.Model {

    @Inject
    public RepairApplyModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Observable<String> queryAvailableVehicle() {
        return RxRetrofitClient
                .create()
                .url(Constant.QUERY_USE_CAR_IS_CAN)
                .raw(HttpParamsManager.getBaseParams(RepairParamsManager.queryAvailableVehicleParams()).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> addRepairApply(Repair repair) {
        return RxRetrofitClient
                .create()
                .url(Constant.ADD_REPAIR_CAR)
                .raw(HttpParamsManager.getBaseParams(RepairParamsManager.addRepairApplyParams(repair)).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> updateRepairApply(Repair repair) {
        return RxRetrofitClient
                .create()
                .url(Constant.UPDATE_REPAIR_CAR)
                .raw(HttpParamsManager.getBaseParams(RepairParamsManager.updateRepairApplyParams(repair)).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> queryRepairApplyDetail(Integer repairId) {
        return RxRetrofitClient
                .create()
                .url(Constant.QUERY_REPAIR_CAR_DETAIL)
                .raw(HttpParamsManager.getBaseParams(RepairParamsManager.queryRepairApplyDetailParams(repairId)).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public Observable<String> queryAttachList(String dataId, String attachType) {
        return RxRetrofitClient
                .create()
                .url(Constant.QUERY_ATTACH)
                .raw(HttpParamsManager.getBaseParams(AttachParamsManager.queryAttachListParams(dataId,attachType)).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> uploadAttach(String dataId, String attachType, String filePath, String fileSuffix) {
        return RxRetrofitClient
                .create()
                .url(Constant.UPLOAD_ATTACH)
                .raw(HttpParamsManager.getBaseParams(AttachParamsManager
                        .uploadAttachParams(dataId,attachType,filePath,fileSuffix)).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> deleteAttach(int attachId) {
        return RxRetrofitClient
                .create()
                .url(Constant.DELETE_ATTACH)
                .raw(HttpParamsManager.getBaseParams(AttachParamsManager.deleteAttachParams(attachId)).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> queryUUID() {
        return RxRetrofitClient
                .create()
                .url(Constant.QUERY_UUID)
                .raw(HttpParamsManager.getBaseParams(new JSONObject()).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
