package com.hxd.gobus.mvp.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Repair;
import com.hxd.gobus.bean.StringInfo;
import com.hxd.gobus.bean.VehicleLicense;
import com.hxd.gobus.bean.list.AttachList;
import com.hxd.gobus.bean.list.VehicleLicenseList;
import com.hxd.gobus.config.VehicleConfig;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IRepairApplyContract;
import com.hxd.gobus.mvp.model.RepairApplyModel;
import com.hxd.gobus.utils.FileUtil;
import com.hxd.gobus.utils.GsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * @author: wangqingbin
 * @date: 2019/8/16 9:17
 */
@ActivityScope
public class RepairApplyPresenter extends BasePresenter<IRepairApplyContract.View,RepairApplyModel> {

    private List<VehicleLicense> mVehicleLicenseList;
    private String mDataStatus;

    private String mDataId;
    private List<Attach> mAttachList;

    @Inject
    public RepairApplyPresenter() {

    }

    public void getVehicleData(Intent intent){
        int repairId = intent.getIntExtra("repairId",0);
        if (repairId != 0){
            mDataStatus = VehicleConfig.APPLY_UPDATE;
            queryRepairApplyDetail(repairId);
        }else{
            mDataStatus = VehicleConfig.APPLY_ADD;
            queryUUID();
        }
    }

    public void queryUUID(){
        getView().showLoading();
        getModel().queryUUID().subscribe(new ObserverImpl() {

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
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    mDataId = jsonObject.optString("uuid");
                    if (!TextUtils.isEmpty(mDataId)){
                        getAttachList(mDataId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void handleError(String errorMsg) {
                getView().showToast(errorMsg);
            }
        });
    }

    public String getDataStatus(){
        return mDataStatus;
    }

    public String getDataId(){
        return mDataId;
    }

    public void addRepairApply(Repair repair){
        getView().showLoading();
        getModel().addRepairApply(repair).subscribe(new ObserverImpl() {

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
                getView().saveCommitSuccess();
            }

            @Override
            protected void handleError(String errorMsg) {
                getView().showToast(errorMsg);
            }
        });
    }

    public void updateRepairApply(Repair repair){
        getView().showLoading();
        getModel().updateRepairApply(repair).subscribe(new ObserverImpl() {

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
                getView().saveCommitSuccess();
            }

            @Override
            protected void handleError(String errorMsg) {
                getView().showToast(errorMsg);
            }
        });
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
                Repair repair = GsonUtils.jsonToObject(response, Repair.class);
                if (repair != null){
                    mDataId = repair.getRepairId().toString();
                    if (!TextUtils.isEmpty(mDataId)){
                        getAttachList(mDataId);
                    }
                    getView().initRepairData(repair);
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
                        }else{
                            getView().showToast("当前没有需要维修的车辆!");
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
            String vehicleName = vehicleLicense.getVehicleName();

            StringInfo stringInfo = new StringInfo();
            stringInfo.setId(infoManageId);
            stringInfo.setTitle(vehicleMark);
            stringInfo.setRemark(vehicleName);

            stringInfoList.add(stringInfo);
        }
        return stringInfoList;
    }

    private void getAttachList(String dataId){
        getView().showLoading();
        getModel().queryAttachList(dataId,VehicleConfig.ATTACH_TYPE_REPAIR).subscribe(new ObserverImpl() {

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
        getModel().uploadAttach(mDataId,VehicleConfig.ATTACH_TYPE_REPAIR,filePath,fileSuffix).subscribe(new ObserverImpl() {

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

}
