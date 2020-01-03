package com.hxd.gobus.mvp.model;

import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.AttachParamsManager;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.params.VehicleParamsManager;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IReturnRegistrationContract;
import com.hxd.gobus.mvp.contract.IVehicleRegistrationContract;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: wangqingbin
 * @date: 2019/8/12 15:36
 */
@ActivityScope
public class ReturnRegistrationModel extends BaseModel implements IReturnRegistrationContract.Model {

    @Inject
    public ReturnRegistrationModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Observable<String> queryReturnRegistrationDetail(int managementId) {
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
    public Observable<String> updateReturnRegistration(Vehicle vehicle) {
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
