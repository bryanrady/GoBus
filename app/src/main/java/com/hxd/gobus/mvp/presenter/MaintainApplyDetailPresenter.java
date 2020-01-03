package com.hxd.gobus.mvp.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Maintain;
import com.hxd.gobus.bean.Repair;
import com.hxd.gobus.bean.list.AttachList;
import com.hxd.gobus.config.VehicleConfig;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IMaintainApplyDetailContract;
import com.hxd.gobus.mvp.contract.IRepairApplyDetailContract;
import com.hxd.gobus.mvp.model.MaintainApplyDetailModel;
import com.hxd.gobus.mvp.model.RepairApplyDetailModel;
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
public class MaintainApplyDetailPresenter extends BasePresenter<IMaintainApplyDetailContract.View,MaintainApplyDetailModel> {

    private Maintain mMaintain;
    private List<Attach> mAttachList;

    @Inject
    public MaintainApplyDetailPresenter() {

    }

    public void getMaintainData(Intent intent){
        int upkeepApplyId = intent.getIntExtra("upkeepApplyId",0);
        if (upkeepApplyId != 0){
            queryMaintainApplyDetail(upkeepApplyId);
        }
    }

    private void queryMaintainApplyDetail(int upkeepApplyId){
        getView().showLoading();
        getModel().queryMaintainApplyDetail(upkeepApplyId).subscribe(new ObserverImpl() {

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
                mMaintain = GsonUtils.jsonToObject(response, Maintain.class);
                if (mMaintain != null){
                    String dataId = mMaintain.getUpkeepApplyId().toString();
                    if (!TextUtils.isEmpty(dataId)){
                        getAttachList(dataId);
                    }
                    getView().initMaintainData(mMaintain);
                }
            }

            @Override
            protected void handleError(String errorMsg) {
                getView().showToast(errorMsg);
            }
        });
    }

    public void lookAudit(){
        if (mMaintain != null){
            getView().intoApprovalPage(mMaintain);
        }
    }

    public void repairRegistration(){
        if (mMaintain != null){
            getView().intoRegistrationPage(mMaintain);
        }
    }

    private void getAttachList(String dataId){
        getView().showLoading();
        getModel().queryAttachList(dataId, VehicleConfig.ATTACH_TYPE_MAINTAIN).subscribe(new ObserverImpl() {

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
