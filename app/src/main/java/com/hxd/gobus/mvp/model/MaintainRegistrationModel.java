package com.hxd.gobus.mvp.model;

import com.hxd.gobus.bean.Maintain;
import com.hxd.gobus.bean.Repair;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.params.MaintainParamsManager;
import com.hxd.gobus.core.http.params.RepairParamsManager;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IMaintainRegistrationContract;
import com.hxd.gobus.mvp.contract.IRepairRegistrationContract;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: wangqingbin
 * @date: 2019/8/12 15:36
 */
@ActivityScope
public class MaintainRegistrationModel extends BaseModel implements IMaintainRegistrationContract.Model {

    @Inject
    public MaintainRegistrationModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Observable<String> updateMaintainRegistration(Maintain maintain) {
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

}
