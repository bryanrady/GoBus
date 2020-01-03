package com.hxd.gobus.mvp.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Repair;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.bean.list.AttachList;
import com.hxd.gobus.config.VehicleConfig;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IRepairApplyDetailContract;
import com.hxd.gobus.mvp.contract.IVehicleApplyDetailContract;
import com.hxd.gobus.mvp.model.RepairApplyDetailModel;
import com.hxd.gobus.mvp.model.VehicleApplyDetailModel;
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
public class RepairApplyDetailPresenter extends BasePresenter<IRepairApplyDetailContract.View,RepairApplyDetailModel> {

    private Repair mRepair;
    private List<Attach> mAttachList;

    @Inject
    public RepairApplyDetailPresenter() {

    }

    public void getVehicleData(Intent intent){
        int repairId = intent.getIntExtra("repairId",0);
        if (repairId != 0){
            queryRepairApplyDetail(repairId);
        }
    }

    private void queryRepairApplyDetail(int repairId){
        getView().showLoading();
        getModel().queryRepairApplyDetail(repairId).subscribe(new ObserverImpl() {

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
                mRepair = GsonUtils.jsonToObject(response, Repair.class);
                if (mRepair != null){
                    String dataId = mRepair.getRepairId().toString();
                    if (!TextUtils.isEmpty(dataId)){
                        getAttachList(dataId);
                    }
                    getView().initRepairData(mRepair);
                }
            }

            @Override
            protected void handleError(String errorMsg) {
                getView().showToast(errorMsg);
            }
        });
    }

    public void lookAudit(){
        if (mRepair != null){
            getView().intoApprovalPage(mRepair);
        }
    }

    public void repairRegistration(){
        if (mRepair != null){
            getView().intoRegistrationPage(mRepair);
        }
    }

    private void getAttachList(String dataId){
        getView().showLoading();
        getModel().queryAttachList(dataId, VehicleConfig.ATTACH_TYPE_REPAIR).subscribe(new ObserverImpl() {

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
