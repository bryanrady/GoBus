package com.hxd.gobus.mvp.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Maintain;
import com.hxd.gobus.bean.StringInfo;
import com.hxd.gobus.bean.VehicleLicense;
import com.hxd.gobus.bean.VehicleUpkeep;
import com.hxd.gobus.bean.list.AttachList;
import com.hxd.gobus.bean.list.VehicleLicenseList;
import com.hxd.gobus.bean.list.VehicleUpKeepList;
import com.hxd.gobus.config.VehicleConfig;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IMaintainApplyContract;
import com.hxd.gobus.mvp.model.MaintainApplyModel;
import com.hxd.gobus.utils.FileUtil;
import com.hxd.gobus.utils.GsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * @author: wangqingbin
 * @date: 2019/8/16 9:17
 */
@ActivityScope
public class MaintainApplyPresenter extends BasePresenter<IMaintainApplyContract.View,MaintainApplyModel> {

    private List<VehicleUpkeep> mVehicleUpkeepList;
    private String mDataStatus;

    private String mDataId;
    private List<Attach> mAttachList;

    @Inject
    public MaintainApplyPresenter() {

    }

    public void getMaintainData(Intent intent){
        int upkeepApplyId = intent.getIntExtra("upkeepApplyId",0);
        if (upkeepApplyId != 0){
            mDataStatus = VehicleConfig.APPLY_UPDATE;
            queryMaintainApplyDetail(upkeepApplyId);
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

    public void addMaintainApply(Maintain maintain){
        getView().showLoading();
        getModel().addMaintainApply(maintain).subscribe(new ObserverImpl() {

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

    public void updateMaintainApply(Maintain maintain){
        getView().showLoading();
        getModel().updateMaintainApply(maintain).subscribe(new ObserverImpl() {

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
                Maintain maintain = GsonUtils.jsonToObject(response, Maintain.class);
                if (maintain != null){
                    mDataId = maintain.getUpkeepApplyId().toString();
                    if (!TextUtils.isEmpty(mDataId)){
                        getAttachList(mDataId);
                    }
                    getView().initMaintainData(maintain);
                }
            }

            @Override
            protected void handleError(String errorMsg) {
                getView().showToast(errorMsg);
            }
        });
    }

    public void queryVehicleUpKeepList(){
        if (mVehicleUpkeepList != null && mVehicleUpkeepList.size() > 0){
            getView().showVehicleUpKeepDialog(mVehicleUpkeepList);
        }else{
            getView().showLoading();
            getModel().queryVehicleUpKeepList().subscribe(new ObserverImpl() {

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
                    VehicleUpKeepList jsonToObject = GsonUtils.jsonToObject(response, VehicleUpKeepList.class);
                    if (jsonToObject != null){
                        mVehicleUpkeepList = jsonToObject.getJsonArray();
                        if (mVehicleUpkeepList != null && mVehicleUpkeepList.size() > 0){
                            getView().showVehicleUpKeepDialog(mVehicleUpkeepList);
                        }else{
                            getView().showToast("当前没有需要保养的车辆!");
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

    public List<StringInfo> transferUpKeepList(List<VehicleUpkeep> vehicleUpkeepList){
        List<StringInfo> stringInfoList = new ArrayList<>();
        for(VehicleUpkeep vehicleUpkeep : vehicleUpkeepList){
            Integer upkeepId = vehicleUpkeep.getUpkeepId();
            String vehicleMark = vehicleUpkeep.getVehicleMark();
            String vehicleName = vehicleUpkeep.getVehicleName();
            BigDecimal planUpkeepCost = vehicleUpkeep.getPlanUpkeepCost();

            StringInfo stringInfo = new StringInfo();
            stringInfo.setId(upkeepId);
            stringInfo.setTitle(vehicleMark);
            stringInfo.setRemark(vehicleName);
            stringInfo.setPlanUpkeepCost(planUpkeepCost);

            stringInfoList.add(stringInfo);
        }
        return stringInfoList;
    }

    private void getAttachList(String dataId){
        getView().showLoading();
        getModel().queryAttachList(dataId,VehicleConfig.ATTACH_TYPE_MAINTAIN).subscribe(new ObserverImpl() {

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
        getModel().uploadAttach(mDataId,VehicleConfig.ATTACH_TYPE_MAINTAIN,filePath,fileSuffix).subscribe(new ObserverImpl() {

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
