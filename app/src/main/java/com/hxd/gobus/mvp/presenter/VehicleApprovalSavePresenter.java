package com.hxd.gobus.mvp.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Authority;
import com.hxd.gobus.bean.Driver;
import com.hxd.gobus.bean.KeyCode;
import com.hxd.gobus.bean.StringInfo;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.bean.VehicleKeyCode;
import com.hxd.gobus.bean.VehicleLicense;
import com.hxd.gobus.bean.list.AttachList;
import com.hxd.gobus.bean.list.KeyCodeList;
import com.hxd.gobus.bean.list.VehicleLicenseList;
import com.hxd.gobus.config.VehicleConfig;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IVehicleApprovalSaveContract;
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
public class VehicleApprovalSavePresenter extends BasePresenter<IVehicleApprovalSaveContract.View,VehicleApprovalSaveModel> {

    private String mApprovalStatus;
    private Vehicle mVehicle;
    private Authority mAuthority;
    private List<KeyCode> mVehicleDurationCodeList;
    private List<VehicleLicense> mVehicleLicenseList;
    private List<Driver> mDesignatedDriverList;
    private List<Attach> mAttachList;

    @Inject
    public VehicleApprovalSavePresenter(){

    }

    public void getVehicleApprovalData(Intent intent) {
        mVehicle = (Vehicle) intent.getSerializableExtra("vehicle");
        mAuthority = (Authority) intent.getSerializableExtra("authority");
        mApprovalStatus = intent.getStringExtra("approvalStatus");

        if (mVehicle != null && !TextUtils.isEmpty(mApprovalStatus)){
            String dataId = mVehicle.getManagementId().toString();
            if (!TextUtils.isEmpty(dataId)){
                getAttachList(dataId);
            }
            getView().initVehicleData(mVehicle,mApprovalStatus);
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

    public void queryVehicleDuration(){
        if (mVehicleDurationCodeList != null && mVehicleDurationCodeList.size() > 0){
            getView().showVehicleDurationDialog(mVehicleDurationCodeList);
        }else{
            getView().showLoading();
            getModel().queryCodeValue(VehicleConfig.KEY_TRANSPORT_PREDICT_DURATION).subscribe(new ObserverImpl() {

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
                        mVehicleDurationCodeList = jsonToObject.getJsonArray();
                        if (mVehicleDurationCodeList != null && mVehicleDurationCodeList.size() > 0){
                            getView().showVehicleDurationDialog(mVehicleDurationCodeList);
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

    public void queryAvailableVehicle(){
        if (mVehicleLicenseList != null && mVehicleLicenseList.size() > 0){
            getView().showVehicleLicenseDialog(mVehicleLicenseList);
        }else{
            getView().showLoading();
            getModel().queryAvailableVehicle().subscribe(new ObserverImpl() {

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
                    VehicleLicenseList jsonToObject = GsonUtils.jsonToObject(response, VehicleLicenseList.class);
                    if (jsonToObject != null){
                        mVehicleLicenseList = jsonToObject.getJsonArray();
                        if (mVehicleLicenseList != null && mVehicleLicenseList.size() > 0){
                            getView().showVehicleLicenseDialog(mVehicleLicenseList);
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

    public void queryDesignatedDriver(){
        if (mDesignatedDriverList != null && mDesignatedDriverList.size() > 0){
            getView().showDesignatedDriverDialog(mDesignatedDriverList);
        }else {
            getView().showLoading();
            getModel().queryDesignatedDriver().subscribe(new ObserverImpl() {

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
                    VehicleKeyCode jsonToObject = GsonUtils.jsonToObject(response, VehicleKeyCode.class);
                    if (jsonToObject != null){
                        mDesignatedDriverList = jsonToObject.getDrives();
                        if (mDesignatedDriverList != null && mDesignatedDriverList.size() > 0){
                            if (getView() != null){
                                getView().showDesignatedDriverDialog(mDesignatedDriverList);
                            }
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

    public List<StringInfo> transferLicenseList(List<VehicleLicense> vehicleLicenseList){
        List<StringInfo> stringInfoList = new ArrayList<>();
        for(VehicleLicense vehicleLicense : vehicleLicenseList){
            Integer infoManageId = vehicleLicense.getInfoManageId();
            String vehicleMark = vehicleLicense.getVehicleMark();
            StringInfo stringInfo = new StringInfo();
            stringInfo.setId(infoManageId);
            stringInfo.setTitle(vehicleMark);
            stringInfoList.add(stringInfo);
        }
        return stringInfoList;
    }

    public List<StringInfo> transferDriverList(List<Driver> driverList){
        List<StringInfo> stringInfoList = new ArrayList<>();
        for(Driver driver : driverList){
            Integer driverId = driver.getDriverId();
            String personName = driver.getPersonName();
            StringInfo stringInfo = new StringInfo();
            stringInfo.setId(driverId);
            stringInfo.setTitle(personName);
            stringInfoList.add(stringInfo);
        }
        return stringInfoList;
    }

    public void saveApprovalInfo(Vehicle vehicle,String approvalStatus,String approvalAdvice) {
        if (mAuthority != null){
            getView().showLoading();
            getModel().saveVehicleApproval(vehicle,approvalStatus,approvalAdvice,mAuthority).subscribe(new ObserverImpl() {

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
