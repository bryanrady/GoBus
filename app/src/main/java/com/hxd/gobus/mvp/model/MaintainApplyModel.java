package com.hxd.gobus.mvp.model;

import com.hxd.gobus.bean.Maintain;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.AttachParamsManager;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.params.MaintainParamsManager;
import com.hxd.gobus.core.http.params.RepairParamsManager;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IMaintainApplyContract;
import com.hxd.gobus.mvp.contract.IRepairApplyContract;

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
public class MaintainApplyModel extends BaseModel implements IMaintainApplyContract.Model {

    @Inject
    public MaintainApplyModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Observable<String> queryVehicleUpKeepList() {
        return RxRetrofitClient
                .create()
                .url(Constant.QUERY_CAR_UPKEEP_INFOS)
                .raw(HttpParamsManager.getBaseParams(MaintainParamsManager.queryVehicleUpKeepList()).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> addMaintainApply(Maintain maintain) {
        return RxRetrofitClient
                .create()
                .url(Constant.ADD_UPHOLD_CAR_INFO)
                .raw(HttpParamsManager.getBaseParams(MaintainParamsManager.addMaintainApplyParams(maintain)).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> updateMaintainApply(Maintain maintain) {
        return RxRetrofitClient
                .create()
                .url(Constant.UPDATE_UPHOLD_CAR_INFO)
                .raw(HttpParamsManager.getBaseParams(MaintainParamsManager.updateMaintainApplyParams(maintain)).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> queryMaintainApplyDetail(Integer upkeepApplyId) {
        return RxRetrofitClient
                .create()
                .url(Constant.QUERY_UPHOLD_CAR_DETAIL)
                .raw(HttpParamsManager.getBaseParams(MaintainParamsManager.queryMaintainApplyDetailParams(upkeepApplyId)).toString())
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
