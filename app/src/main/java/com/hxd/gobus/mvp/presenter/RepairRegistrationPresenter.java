package com.hxd.gobus.mvp.presenter;

import android.content.Intent;

import com.hxd.gobus.bean.KeyCode;
import com.hxd.gobus.bean.Repair;
import com.hxd.gobus.bean.StringInfo;
import com.hxd.gobus.bean.list.KeyCodeList;
import com.hxd.gobus.config.VehicleConfig;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IRepairRegistrationContract;
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
public class RepairRegistrationPresenter extends BasePresenter<IRepairRegistrationContract.View,RepairRegistrationModel> {

    private Repair mRepair;
    private List<KeyCode> mRepairStatusList;

    @Inject
    public RepairRegistrationPresenter() {

    }

    public void getIntentData(Intent intent){
        mRepair = (Repair) intent.getSerializableExtra("repair");
        if (mRepair != null){
            getView().initRepairData(mRepair);
        }
    }

    public void queryRepairStatus(){
        if (mRepairStatusList != null && mRepairStatusList.size() > 0){
            getView().showRepairStatusDialog(mRepairStatusList);
        }else{
            getView().showLoading();
            getModel().queryCodeValue(VehicleConfig.KEY_REPAIR_STATUS).subscribe(new ObserverImpl() {

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
                        mRepairStatusList = jsonToObject.getJsonArray();
                        if (mRepairStatusList != null && mRepairStatusList.size() > 0){
                            getView().showRepairStatusDialog(mRepairStatusList);
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

    public void updateRepairRegistration(Repair repair) {
        getView().showLoading();
        getModel().updateRepairRegistration(repair).subscribe(new ObserverImpl() {

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
                getView().saveRepairRegistrationSuccess();
            }

            @Override
            protected void handleError(String errorMsg) {
                getView().showToast(errorMsg);
            }
        });
    }
}
