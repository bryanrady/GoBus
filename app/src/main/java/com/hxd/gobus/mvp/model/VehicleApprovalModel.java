package com.hxd.gobus.mvp.model;

import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.AttachParamsManager;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.params.VehicleParamsManager;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IVehicleApprovalContract;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:45
 */
@ActivityScope
public class VehicleApprovalModel extends BaseModel implements IVehicleApprovalContract.Model {

    @Inject
    public VehicleApprovalModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Observable<String> queryVehicleApprovalData(Integer datumDataId,String datumType) {
        return RxRetrofitClient
                .create()
                .url(Constant.QUERY_USE_CAR_APPROVAL)
                .raw(HttpParamsManager.getBaseParams(
                        VehicleParamsManager.queryVehicleApprovalParams(datumDataId, datumType)).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> permissionAuthority(Integer datumDataId, String datumType) {
        return RxRetrofitClient
                .create()
                .url(Constant.VERIFY_URL)
                .raw(HttpParamsManager.getBaseParams(
                        VehicleParamsManager.permissionAuthorityParams(datumDataId, datumType)).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> updateTodoList(Integer ids) {
        return RxRetrofitClient
                .create()
                .url(Constant.UPDATE_TODO_URL)
                .raw(HttpParamsManager.getBaseParams(HttpParamsManager.updateTodoParams(ids)).toString())
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
    public Observable<String> queryNextReviewer(int datumDataId, String datumType) {
        return RxRetrofitClient
                .create()
                .url(Constant.QUERY_NEXT_APPROVAL_PERSONAL_INFO)
                .raw(HttpParamsManager.getBaseParams(HttpParamsManager.queryNextReviewer(datumDataId,datumType)).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> reminder(String title, String workNo) {
        return RxRetrofitClient
                .create()
                .url(Constant.REMINDERS_URL)
                .raw(HttpParamsManager.getBaseParams(HttpParamsManager.reminder(title,workNo)).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
