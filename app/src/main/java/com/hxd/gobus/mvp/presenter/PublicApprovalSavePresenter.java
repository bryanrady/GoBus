package com.hxd.gobus.mvp.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.hxd.gobus.bean.Authority;
import com.hxd.gobus.bean.KeyCode;
import com.hxd.gobus.bean.StringInfo;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.bean.VehicleLicense;
import com.hxd.gobus.bean.list.KeyCodeList;
import com.hxd.gobus.bean.list.VehicleLicenseList;
import com.hxd.gobus.config.VehicleConfig;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IPublicApprovalSaveContract;
import com.hxd.gobus.mvp.contract.IVehicleApprovalSaveContract;
import com.hxd.gobus.mvp.model.PublicApprovalSaveModel;
import com.hxd.gobus.mvp.model.VehicleApprovalSaveModel;
import com.hxd.gobus.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * @author: wangqingbin
 * @date: 2019/7/25 10:26
 */
@ActivityScope
public class PublicApprovalSavePresenter extends BasePresenter<IPublicApprovalSaveContract.View,PublicApprovalSaveModel> {

    private String mApprovalStatus;
    private Authority mAuthority;

    @Inject
    public PublicApprovalSavePresenter(){

    }

    public void getPublicApprovalData(Intent intent) {
        mAuthority = (Authority) intent.getSerializableExtra("authority");
        mApprovalStatus = intent.getStringExtra("approvalStatus");
        if (mAuthority != null && !TextUtils.isEmpty(mApprovalStatus)){
            getView().initPublicData(mAuthority,mApprovalStatus);
        }
    }

    public void saveApprovalInfo(String approvalStatus,String approvalAdvice) {
        if (mAuthority != null){
            getView().showLoading();
            getModel().saveApproval(approvalStatus,approvalAdvice,mAuthority).subscribe(new ObserverImpl() {

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
                    if (getView() != null){
                        getView().showToast(response);
                        getView().saveApprovalSuccess();
                    }
                }

                @Override
                protected void handleError(String errorMsg) {
                    getView().showToast(errorMsg);
                }
            });
        }
    }
}
