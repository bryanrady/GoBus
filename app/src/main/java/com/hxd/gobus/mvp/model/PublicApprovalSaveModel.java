package com.hxd.gobus.mvp.model;

import com.hxd.gobus.bean.Authority;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.params.VehicleParamsManager;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IPublicApprovalSaveContract;
import com.hxd.gobus.mvp.contract.IVehicleApprovalSaveContract;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:45
 */
@ActivityScope
public class PublicApprovalSaveModel extends BaseModel implements IPublicApprovalSaveContract.Model {

    @Inject
    public PublicApprovalSaveModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Observable<String> saveApproval(String approvalStatus, String approvalAdvice, Authority authority) {
        if (getRequestUrl(authority) != null){
            return RxRetrofitClient
                    .create()
                    .url(getRequestUrl(authority))
                    .raw(HttpParamsManager.getBaseParams(HttpParamsManager.savePublicApprovalParams(approvalStatus, approvalAdvice, authority)).toString())
                    .build()
                    .postRaw()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
        return null;
    }

    private String getRequestUrl(Authority authority) {
        String requestUrl = null;
        if (BaseConfig.DATUMTYPE_REPAIR.equals(authority.getDatumType())){
            requestUrl = Constant.INSERT_REPAIR_CAR_APPROVAL;
        }else if(BaseConfig.DATUMTYPE_MAINTAIN.equals(authority.getDatumType())){
            requestUrl = Constant.INSERT_UPHOLD_CAR_APPROVAL;
        }
        return requestUrl;
    }
}
