package com.hxd.gobus.mvp.presenter;

import android.content.Intent;

import com.hxd.gobus.bean.KeyCode;
import com.hxd.gobus.bean.Maintain;
import com.hxd.gobus.bean.Repair;
import com.hxd.gobus.bean.StringInfo;
import com.hxd.gobus.bean.list.KeyCodeList;
import com.hxd.gobus.config.VehicleConfig;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IMaintainRegistrationContract;
import com.hxd.gobus.mvp.contract.IRepairRegistrationContract;
import com.hxd.gobus.mvp.model.MaintainRegistrationModel;
import com.hxd.gobus.mvp.model.RepairRegistrationModel;
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
public class MaintainRegistrationPresenter extends BasePresenter<IMaintainRegistrationContract.View,MaintainRegistrationModel> {

    private Maintain mMaintain;
    private List<KeyCode> mMaintainStatusList;

    @Inject
    public MaintainRegistrationPresenter() {

    }

    public void getIntentData(Intent intent){
        mMaintain = (Maintain) intent.getSerializableExtra("maintain");
        if (mMaintain != null){
            getView().initMaintainData(mMaintain);
        }
    }

    public void updateMaintainRegistration(Maintain maintain) {
        getView().showLoading();
        getModel().updateMaintainRegistration(maintain).subscribe(new ObserverImpl() {

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
                getView().showToast(response);
                getView().saveMaintainRegistrationSuccess();
            }

            @Override
            protected void handleError(String errorMsg) {
                getView().showToast(errorMsg);
            }
        });
    }

    public void queryMaintainStatus(){
        if (mMaintainStatusList != null && mMaintainStatusList.size() > 0){
            getView().showMaintainStatusDialog(mMaintainStatusList);
        }else{
            getView().showLoading();
            getModel().queryCodeValue(VehicleConfig.KEY_UPKEEP_TRANSACT_STATUS).subscribe(new ObserverImpl() {

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
                    KeyCodeList jsonToObject = GsonUtils.jsonToObject(response, KeyCodeList.class);
                    if (jsonToObject != null){
                        mMaintainStatusList = jsonToObject.getJsonArray();
                        if (mMaintainStatusList != null && mMaintainStatusList.size() > 0){
                            getView().showMaintainStatusDialog(mMaintainStatusList);
                        }
                    }

                }

                @Override
                protected void handleError(String errorMsg) {
                    getView().showToast(errorMsg);
                }
            });
        }
    }

    public List<StringInfo> transferKeyCodeList(List<KeyCode> keyCodeList){
        List<StringInfo> stringInfoList = new ArrayList<>();
        for(KeyCode keyCode : keyCodeList){
            String codeName = keyCode.getCodeName();
            String codeValue = keyCode.getCodeValue();
            StringInfo stringInfo = new StringInfo();
            stringInfo.setTitle(codeName);
            stringInfo.setCodeType(codeValue);
            stringInfoList.add(stringInfo);
        }
        return stringInfoList;
    }

}
