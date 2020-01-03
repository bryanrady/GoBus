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
import com.hxd.gobus.mvp.contract.IReturnRegistrationContract;
import com.hxd.gobus.mvp.contract.IVehicleRegistrationContract;
import com.hxd.gobus.mvp.model.ReturnRegistrationModel;
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
public class ReturnRegistrationPresenter extends BasePresenter<IReturnRegistrationContract.View,ReturnRegistrationModel> {

    private Vehicle mVehicle;

    private String mDataId;
    private List<Attach> mAttachList;

    @Inject
    public ReturnRegistrationPresenter() {

    }

    public void queryVehicleRegistrationDetail(Intent intent){
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

    public void updateReturnRegistration(Vehicle vehicle) {
        getView().showLoading();
        getModel().updateReturnRegistration(vehicle).subscribe(new ObserverImpl() {

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
                getView().saveReturnRegistrationSuccess();
            }

            @Override
            protected void handleError(String errorMsg) {
                getView().showToast(errorMsg);
            }
        });
    }

}
