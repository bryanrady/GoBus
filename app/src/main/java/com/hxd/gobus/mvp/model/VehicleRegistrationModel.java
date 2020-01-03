package com.hxd.gobus.mvp.model;

import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.AttachParamsManager;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.params.VehicleParamsManager;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IVehicleApplyContract;
import com.hxd.gobus.mvp.contract.IVehicleRegistrationContract;

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
public class VehicleRegistrationModel extends BaseModel implements IVehicleRegistrationContract.Model {

    @Inject
    public VehicleRegistrationModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Observable<String> queryAvailableVehicle() {
        return RxRetrofitClient
                .create()
                .url(Constant.QUERY_USE_CAR_IS_CAN)
                .raw(HttpParamsManager.getBaseParams(VehicleParamsManager.queryAvailableVehicleParams()).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> queryDesignatedDriver() {
        return RxRetrofitClient
                .create()
                .url(Constant.QUERY_USE_CAR_SYSTEM_CODE_VALUE)
                .raw(HttpParamsManager.getBaseParams(new JSONObject()).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> queryCodeValue(String key) {
        return RxRetrofitClient
                .create()
                .url(Constant.QUERY_CODE_VALUE_LIST)
                .raw(HttpParamsManager.getBaseParams(HttpParamsManager.queryKeyCode(key)).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> queryVehicleRegistrationDetail(int managementId) {
        return RxRetrofitClient
                .create()
                .url(Constant.QUERY_USE_CAR_DETAIL)
                .raw(HttpParamsManager.getBaseParams(VehicleParamsManager.queryVehicleApplyDetailParams(managementId)).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> updateVehicleRegistration(Vehicle vehicle) {
        return RxRetrofitClient
                .create()
                .url(Constant.UPDATE_USE_CAR)
                .raw(HttpParamsManager.getBaseParams(VehicleParamsManager.updateVehicleApplyParams(vehicle)).toString())
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

}
