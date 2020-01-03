package com.hxd.gobus.mvp.presenter;

import com.hxd.gobus.bean.Repair;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.bean.list.RepairList;
import com.hxd.gobus.bean.list.VehicleList;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IMaintainRecordContract;
import com.hxd.gobus.mvp.contract.IRepairRecordContract;
import com.hxd.gobus.mvp.model.MaintainRecordModel;
import com.hxd.gobus.mvp.model.RepairRecordModel;
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
public class RepairRecordPresenter extends BasePresenter<IRepairRecordContract.View,RepairRecordModel> {

    private int mPageNumber = 1;
    private int mPageSize = 10;
    private List<Repair> mList;
    private String mVehicleMark;
    private String mStartDate;
    private String mEndDate;
    private String mRemarks;

    @Inject
    public RepairRecordPresenter() {
        if (mList == null){
            mList = new ArrayList<>();
        }
    }

    public void setVehicleMark(String vehicleMark) {
        this.mVehicleMark = vehicleMark;
    }

    public void setStartDate(String startDate) {
        this.mStartDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.mEndDate = endDate;
    }

    public void setRemarks(String remarks) {
        this.mRemarks = remarks;
    }

    public void getNewData(){
        mPageNumber = 1;
        getModel().getRepairList(mVehicleMark,mStartDate,mEndDate,mRemarks,mPageNumber,mPageSize).subscribe(new ObserverImpl() {

            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onComplete() {

            }

            @Override
            protected void handleResponse(String response) {
                RepairList jsonToObject = GsonUtils.jsonToObject(response, RepairList.class);
                if (jsonToObject != null){
                    List<Repair> tempList = jsonToObject.getJsonArray();
                    if (tempList != null && tempList.size() >= 0){
                        mList.clear();
                        mList.addAll(0,tempList);
                        if (getView() != null){
                            getView().showData(mList);
                        }
                    }
                    if (getView() != null){
                        getView().overRefresh();
                        getView().setLoadMoreEnabled(true);
                    }
                }
            }

            @Override
            protected void handleError(String errorMsg) {
                if (getView() != null){
                    getView().showToast(errorMsg);
                    getView().overRefresh();
                }
            }
        });

    }

    public void getMoreData(){
        mPageNumber++;
        getModel().getRepairList(mVehicleMark,mStartDate,mEndDate,mRemarks,mPageNumber,mPageSize).subscribe(new ObserverImpl() {

            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onComplete() {

            }

            @Override
            protected void handleResponse(String response) {
                RepairList jsonToObject = GsonUtils.jsonToObject(response, RepairList.class);
                if (jsonToObject != null){
                    List<Repair> tempList = jsonToObject.getJsonArray();
                    if (tempList != null && tempList.size() >= 0){
                        mList.addAll(tempList);
                        if (getView() != null){
                            getView().showData(mList);
                        }
                    }
                    if (getView() != null){
                        getView().overRefresh();
                    }
                    if (mList == null || mList.size() == 0 || mList.size() < mPageNumber * mPageSize) {
                        getView().setLoadMoreEnabled(false);
                    } else {
                        getView().setLoadMoreEnabled(true);
                    }
                }
            }

            @Override
            protected void handleError(String errorMsg) {
                getView().showToast(errorMsg);
            }
        });
    }

    public void deleteRepairApply(final int position){
        Repair repair = mList.get(position);
        if (checkApprovalStatus(repair)){
            if (repair != null && repair.getRepairId() != null){
                getView().showLoading();
                getModel().deleteRepairApply(repair.getRepairId()).subscribe(new ObserverImpl() {

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
                        getView().deleteSuccess(position);
                        getView().showToast(response);
                    }

                    @Override
                    protected void handleError(String errorMsg) {
                        getView().showToast(errorMsg);
                    }
                });
            }
        }
    }

    private boolean checkApprovalStatus(Repair repair){
        if("1".equals(repair.getApprovalStatus())){
            getView().deleteNotSuccess("信息已提交审核，请勿删除！");
            return false;
        }else if("2".equals(repair.getApprovalStatus())){
            getView().deleteNotSuccess("信息已审核完成，请勿删除！");
            return false;
        }
        return true;
    }

}
