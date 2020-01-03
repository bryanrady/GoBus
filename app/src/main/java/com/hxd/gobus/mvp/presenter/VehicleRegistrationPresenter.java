package com.hxd.gobus.mvp.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.hxd.gobus.bean.Attach;
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
import com.hxd.gobus.mvp.contract.IVehicleApplyContract;
import com.hxd.gobus.mvp.contract.IVehicleRegistrationContract;
import com.hxd.gobus.mvp.model.VehicleApplyModel;
import com.hxd.gobus.mvp.model.VehicleRegistrationModel;
import com.hxd.gobus.utils.FileUtil;
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
public class VehicleRegistrationPresenter extends BasePresenter<IVehicleRegistrationContract.View,VehicleRegistrationModel> {

    private Vehicle mVehicle;
    private List<VehicleLicense> mVehicleLicenseList;
    private List<Driver> mDesignatedDriverList;
    private List<KeyCode> mIsUseTheCarList;

    private String mDataId;
    private List<Attach> mAttachList;

    @Inject
    public VehicleRegistrationPresenter() {

    }

    public void queryVehicleRegistrationDetail(Intent intent){
        int managementId = intent.getIntExtra("managementId",0);
        if (managementId != 0){
            getView().showLoading();
            getModel().queryVehicleRegistrationDetail(managementId).subscribe(new ObserverImpl() {

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
                        mDataId = mVehicle.getManagementId().toString();
                        if (!TextUtils.isEmpty(mDataId)){
                            getAttachList(mDataId);
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

    public void uploadAttach(String filePath){
        if (TextUtils.isEmpty(mDataId)){
            return;
        }
        String fileSuffix = FileUtil.getFileNameNoEx(filePath);
        getView().showLoading();
        getModel().uploadAttach(mDataId,VehicleConfig.ATTACH_TYPE_VEHICLE,filePath,fileSuffix).subscribe(new ObserverImpl() {

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
                Attach attach = GsonUtils.jsonToObject(response, Attach.class);
                if (attach != null){
                    Attach transferAttach = new Attach();
                    //目的是为了将客户端显示的文件名用后台的文件名来显示
                    transferAttach.setAttachId(attach.getAttachId());
                    transferAttach.setAttachName(attach.getAttachName());
                    transferAttach.setAttachAddress(attach.getAttachAddress());

                    transferAttach.setNativePath(filePath);

                    mAttachList.add(transferAttach);

//                    String localSavePath = FileUtil.getFileCachePath(BusApp.getContext())+attach.getAttachAddress();
//                    //将文件从sd卡中写到指定的附件缓存目录中区
//                    FileUtil.writeToDirectory(filePath,localSavePath);

                    getView().showAttachList(mAttachList);
                }
            }

            @Override
            protected void handleError(String errorMsg) {
                getView().showToast(errorMsg);
            }
        });
    }

    public void deleteAttach(int attachId,int position){
        getView().showLoading();
        getModel().deleteAttach(attachId).subscribe(new ObserverImpl() {

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
                if (mAttachList != null && mAttachList.size()>position){
                    mAttachList.remove(position);
                    getView().showAttachList(mAttachList);
                }
            }

            @Override
            protected void handleError(String errorMsg) {
                getView().showToast(errorMsg);
            }
        });
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

    public void queryIsUseTheCar(){
        if (mIsUseTheCarList != null && mIsUseTheCarList.size() > 0){
            getView().showIsUseTheCarDialog(mIsUseTheCarList);
        }else{
            getView().showLoading();
            getModel().queryCodeValue(VehicleConfig.KEY_TRANSPORT_IS_USE_CAR).subscribe(new ObserverImpl() {

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
                        mIsUseTheCarList = jsonToObject.getJsonArray();
                        if (mIsUseTheCarList != null && mIsUseTheCarList.size() > 0){
                            getView().showIsUseTheCarDialog(mIsUseTheCarList);
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

    public void updateVehicleRegistration(Vehicle vehicle) {
        getView().showLoading();
        getModel().updateVehicleRegistration(vehicle).subscribe(new ObserverImpl() {

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
                getView().saveVehicleRegistrationSuccess();
            }

            @Override
            protected void handleError(String errorMsg) {
                getView().showToast(errorMsg);
            }
        });
    }
}
