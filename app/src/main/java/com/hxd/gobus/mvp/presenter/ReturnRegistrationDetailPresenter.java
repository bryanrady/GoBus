package com.hxd.gobus.mvp.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.bean.list.AttachList;
import com.hxd.gobus.config.VehicleConfig;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IReturnRegistrationDetailContract;
import com.hxd.gobus.mvp.contract.IVehicleRegistrationDetailContract;
import com.hxd.gobus.mvp.model.ReturnRegistrationDetailModel;
import com.hxd.gobus.mvp.model.VehicleRegistrationDetailModel;
import com.hxd.gobus.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * @author: wangqingbin
 * @date: 2019/8/12 15:36
 */
@ActivityScope
public class ReturnRegistrationDetailPresenter extends BasePresenter<IReturnRegistrationDetailContract.View,ReturnRegistrationDetailModel> {

    private List<Attach> mAttachList;

    @Inject
    public ReturnRegistrationDetailPresenter() {

    }

    public void queryReturnRegistrationDetail(Intent intent){
        int managementId = intent.getIntExtra("managementId",0);
        if (managementId != 0){
            getView().showLoading();
            getModel().queryReturnRegistrationDetail(managementId).subscribe(new ObserverImpl() {

                @Override
                public void onSubscribe(Disposable d) {
                    addDispose(d);
                }

                @Override
                public void onComplete() {
                    getView().dismissLoading();
                }

                @Override
                protected void handleResponse(String response) {
                    Vehicle vehicle = GsonUtils.jsonToObject(response, Vehicle.class);
                    if (vehicle != null){
                        String dataId = vehicle.getManagementId().toString();
                        if (!TextUtils.isEmpty(dataId)){
                            getAttachList(dataId);
                        }
                        getView().initVehicleData(vehicle);
                    }
                }

                @Override
                protected void handleError(String errorMsg) {
                    getView().showToast(errorMsg);
                }
            });
        }
    }

    private void getAttachList(String dataId){
        getView().showLoading();
        getModel().queryAttachList(dataId, VehicleConfig.ATTACH_TYPE_VEHICLE).subscribe(new ObserverImpl() {

            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onComplete() {
                getView().dismissLoading();
            }

            @Override
            protected void handleResponse(String response) {
                AttachList jsonToObject = GsonUtils.jsonToObject(response, AttachList.class);
                if (jsonToObject != null){
                    mAttachList = jsonToObject.getJsonArray();
                    if (mAttachList == null){
                        mAttachList = new ArrayList<>();
                    }
                    getView().showAttachList(mAttachList);
                }
            }

            @Override
            protected void handleError(String errorMsg) {
                getView().showToast(errorMsg);
            }
        });
    }

}
