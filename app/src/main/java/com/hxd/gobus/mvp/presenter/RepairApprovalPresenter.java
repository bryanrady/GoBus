package com.hxd.gobus.mvp.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Authority;
import com.hxd.gobus.bean.DatumDetail;
import com.hxd.gobus.bean.NextReviewer;
import com.hxd.gobus.bean.Repair;
import com.hxd.gobus.bean.Todo;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.bean.list.AttachList;
import com.hxd.gobus.bean.list.NextReviewerList;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.config.VehicleConfig;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IRepairApprovalContract;
import com.hxd.gobus.mvp.contract.IVehicleApprovalContract;
import com.hxd.gobus.mvp.model.RepairApprovalModel;
import com.hxd.gobus.mvp.model.VehicleApprovalModel;
import com.hxd.gobus.utils.GsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * @author: wangqingbin
 * @date: 2019/7/25 10:26
 */
@ActivityScope
public class RepairApprovalPresenter extends BasePresenter<IRepairApprovalContract.View,RepairApprovalModel> {

    private Integer mDatumDataId;
    private String mDatumType;
    private Integer mIds;
    private int mSource;
    private String mPositionFlag;

    private List<Attach> mAttachList;

    @Inject
    public RepairApprovalPresenter(){

    }

    public void queryRepairApprovalData(){
        if (mDatumDataId != null && !TextUtils.isEmpty(mDatumType)){
            getView().showLoading();
            getModel().queryRepairApprovalData(mDatumDataId,mDatumType).subscribe(new ObserverImpl() {

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
                    if (repair != null && mPositionFlag != null && mSource != 0){
                        String dataId = repair.getRepairId().toString();
                        if (!TextUtils.isEmpty(dataId)){
                            getAttachList(dataId);
                        }
                        if(mSource == BaseConfig.SOURCE_LOOK_DETAIL){
                            if (mDatumDataId != 0 && !TextUtils.isEmpty(mDatumType)){
                                getNextReviewer(mSource,mPositionFlag,repair,mDatumDataId,mDatumType);
                            }
                        }else{
                            getView().initRepairData(mSource,mPositionFlag,repair);
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

    private void getNextReviewer(int source,String positionFlag,Repair repair,int datumDataId,String datumType){
        getView().showLoading();
        getModel().queryNextReviewer(datumDataId,datumType).subscribe(new ObserverImpl() {

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
                NextReviewerList jsonToObject = GsonUtils.jsonToObject(response, NextReviewerList.class);
                if (jsonToObject != null){
                    List<NextReviewer> nextReviewerList = jsonToObject.getJsonArray();
                    if (nextReviewerList != null && nextReviewerList.size()>0){
                        List<DatumDetail> datumDetailInfoList = repair.getDatumDetailInfoList();
                        if (datumDetailInfoList != null && datumDetailInfoList.size() > 0){
                            for (NextReviewer reviewer : nextReviewerList){
                                DatumDetail datumDetail = new DatumDetail();
                                datumDetail.setApprovalUser(reviewer.getPersonName());
                                datumDetail.setTitle(reviewer.getTitle());
                                datumDetail.setWorkNo(reviewer.getWorkNo());
                                datumDetail.setReminder(true);
                                datumDetailInfoList.add(0,datumDetail);
                            }
                            repair.setDatumDetailInfoList(datumDetailInfoList);
                            getView().initRepairData(source,positionFlag,repair);
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

    public void reminder(String title,String workNo){
        getView().showLoading();
        getModel().reminder(title,workNo).subscribe(new ObserverImpl() {

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
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String data = jsonObject.optString("data");
                    if (!TextUtils.isEmpty(data)){
                        getView().showToast(data);
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



    public void getIntentData(Intent intent){
        mSource = intent.getIntExtra("source", 0);
        mPositionFlag = intent.getStringExtra("positionFlag");
        if (mSource == BaseConfig.SOURCE_TODO){
            Todo todo = (Todo) intent.getSerializableExtra("todo");
            if (todo != null){
                mDatumDataId = todo.getDataId();
                mDatumType = todo.getDatumType();

                mIds = todo.getUntreatedInfoId();
            }
        }else if(mSource == BaseConfig.SOURCE_LOOK_DETAIL){
            mDatumDataId = intent.getIntExtra("datumDataId", 0);
            mDatumType = intent.getStringExtra("datumType");
        }
        queryRepairApprovalData();
    }

    public void updateTodo(){
        if (mIds != null){
            getView().showLoading();
            getModel().updateTodoList(mIds).subscribe(new ObserverImpl() {

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

                }

                @Override
                protected void handleError(String errorMsg) {
                    getView().showToast(errorMsg);
                }
            });
        }
    }

    public void permissionAuthority(String approvalStatus){
        if (mDatumDataId != null && !TextUtils.isEmpty(mDatumType)){
            getView().showLoading();
            getModel().permissionAuthority(mDatumDataId,mDatumType).subscribe(new ObserverImpl() {

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
                    Authority authority = GsonUtils.jsonToObject(response, Authority.class);
                    if (authority != null){
                        getView().intoApprovalSavePage(authority,approvalStatus);
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
