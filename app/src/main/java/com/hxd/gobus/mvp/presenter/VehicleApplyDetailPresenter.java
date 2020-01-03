package com.hxd.gobus.mvp.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.KeyCode;
import com.hxd.gobus.bean.StringInfo;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.bean.VehicleLicense;
import com.hxd.gobus.bean.list.AttachList;
import com.hxd.gobus.bean.list.KeyCodeList;
import com.hxd.gobus.bean.list.VehicleLicenseList;
import com.hxd.gobus.config.VehicleConfig;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IVehicleApplyContract;
import com.hxd.gobus.mvp.contract.IVehicleApplyDetailContract;
import com.hxd.gobus.mvp.model.VehicleApplyDetailModel;
import com.hxd.gobus.mvp.model.VehicleApplyModel;
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
public class VehicleApplyDetailPresenter extends BasePresenter<IVehicleApplyDetailContract.View,VehicleApplyDetailModel> {

    private Vehicle mVehicle;
    private List<Attach> mAttachList;

    @Inject
    public VehicleApplyDetailPresenter() {

    }

    public void getVehicleData(Intent intent){
        int managementId = intent.getIntExtra("managementId",0);
        if (managementId != 0){
            queryVehicleApplyDetail(managementId);
        }
    }

    private void queryVehicleApplyDetail(int managementId){
        getView().showLoading();
        getModel().queryVehicleApplyDetail(managementId).subscribe(new ObserverImpl() {

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
                mVehicle = GsonUtils.jsonToObject(response, Vehicle.class);
                if (mVehicle != null){
                    String dataId = mVehicle.getManagementId().toString();
                    if (!TextUtils.isEmpty(dataId)){
                        getAttachList(dataId);
                    }
                    getView().initVehicleData(mVehicle);
                }
            }

            @Override
            protected void handleError(String errorMsg) {
                getView().showToast(errorMsg);
            }
        });
    }

    public void lookAudit(){
        if (mVehicle != null){
            getView().intoApprovalPage(mVehicle);
        }

    }

    private void getAttachList(String dataId){
        getView().showLoading();
        getModel().queryAttachList(dataId,VehicleConfig.ATTACH_TYPE_VEHICLE).subscribe(new ObserverImpl() {

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
