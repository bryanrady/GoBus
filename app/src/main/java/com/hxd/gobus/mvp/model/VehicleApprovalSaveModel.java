package com.hxd.gobus.mvp.model;

import com.hxd.gobus.bean.Authority;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.AttachParamsManager;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.params.VehicleParamsManager;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IVehicleApprovalSaveContract;

import org.json.JSONObject;

import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:45
 */
@ActivityScope
public class VehicleApprovalSaveModel extends BaseModel implements IVehicleApprovalSaveContract.Model {

    @Inject
    public VehicleApprovalSaveModel(){

    }

    @Override
    public void onDestroy() {

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
    public Observable<String> saveVehicleApproval(Vehicle vehicle,String approvalStatus, String approvalAdvice, Authority authority) {
        return RxRetrofitClient
                .create()
                .url(Constant.INSERT_USE_CAR_APPROVAL)
                .raw(HttpParamsManager.getBaseParams(VehicleParamsManager.saveVehicleApprovalParams(
                        vehicle, approvalStatus, approvalAdvice, authority)).toString())
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

}
