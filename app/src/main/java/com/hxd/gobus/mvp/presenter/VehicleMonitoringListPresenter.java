package com.hxd.gobus.mvp.presenter;

import com.hxd.gobus.bean.RealTimeMonitoring;
import com.hxd.gobus.bean.list.RealTimeMonitoringList;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IVehicleMonitoringListContract;
import com.hxd.gobus.mvp.model.VehicleMonitoringListModel;
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
public class VehicleMonitoringListPresenter extends BasePresenter<IVehicleMonitoringListContract.View,VehicleMonitoringListModel> {

    private int mPageNumber = 1;
    private int mPageSize = 10;
    private List<RealTimeMonitoring> mList;

    @Inject
    public VehicleMonitoringListPresenter() {
        if (mList == null){
            mList = new ArrayList<>();
        }
    }

    public void getNewData(){
        mPageNumber = 1;
        getModel().getRealTimeMonitoringList(mPageNumber,mPageSize).subscribe(new ObserverImpl() {

            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onComplete() {
            }

            @Override
            protected void handleResponse(String response) {
                RealTimeMonitoringList jsonToObject = GsonUtils.jsonToObject(response, RealTimeMonitoringList.class);
                if (jsonToObject != null){
                    List<RealTimeMonitoring> tempList = jsonToObject.getJsonArray();
                    if (tempList != null && tempList.size() > 0){
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
        getModel().getRealTimeMonitoringList(mPageNumber,mPageSize).subscribe(new ObserverImpl() {

            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onComplete() {
            }

            @Override
            protected void handleResponse(String response) {
                RealTimeMonitoringList jsonToObject = GsonUtils.jsonToObject(response, RealTimeMonitoringList.class);
                if (jsonToObject != null){
                    List<RealTimeMonitoring> tempList = jsonToObject.getJsonArray();
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

}
