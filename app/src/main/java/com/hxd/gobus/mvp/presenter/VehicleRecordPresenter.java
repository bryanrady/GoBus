package com.hxd.gobus.mvp.presenter;

import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.bean.list.VehicleList;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IVehicleRecordContract;
import com.hxd.gobus.mvp.model.VehicleRecordModel;
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
public class VehicleRecordPresenter extends BasePresenter<IVehicleRecordContract.View,VehicleRecordModel> {

    private int mPageNumber = 1;
    private int mPageSize = 10;
    private List<Vehicle> mList;
    private String mVehicleMark;
    private String mStartDate;
    private String mEndDate;
    private String mUseReason;

    @Inject
    public VehicleRecordPresenter() {
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

    public void setUseReason(String useReason) {
        this.mUseReason = useReason;
    }

    public void getNewData(){
        mPageNumber = 1;
        getModel().getVehicleList(mVehicleMark,mStartDate,mEndDate,mUseReason,mPageNumber,mPageSize).subscribe(new ObserverImpl() {

            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onComplete() {
            }

            @Override
            protected void handleResponse(String response) {
                VehicleList jsonToObject = GsonUtils.jsonToObject(response, VehicleList.class);
                if (jsonToObject != null){
                    List<Vehicle> tempList = jsonToObject.getJsonArray();
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
        getModel().getVehicleList(mVehicleMark,mStartDate,mEndDate,mUseReason,mPageNumber,mPageSize).subscribe(new ObserverImpl() {

            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onComplete() {

            }

            @Override
            protected void handleResponse(String response) {
                VehicleList jsonToObject = GsonUtils.jsonToObject(response, VehicleList.class);
                if (jsonToObject != null){
                    List<Vehicle> tempList = jsonToObject.getJsonArray();
                    if (tempList != null && tempList.size() > 0){
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

    public void deleteVehicleApply(final int position){
        Vehicle vehicle = mList.get(position);
        if (checkApprovalStatus(vehicle)){
            if (vehicle != null && vehicle.getManagementId() != null){
                getView().showLoading();
                getModel().deleteVehicleApply(vehicle.getManagementId()).subscribe(new ObserverImpl() {

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

    private boolean checkApprovalStatus(Vehicle vehicle){
        if("1".equals(vehicle.getApprovalStatus())){
            getView().deleteNotSuccess("信息已提交审核，请勿删除！");
            return false;
        }else if("2".equals(vehicle.getApprovalStatus())){
            getView().deleteNotSuccess("信息已审核完成，请勿删除！");
            return false;
        }
        return true;
    }

}
