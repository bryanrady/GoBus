package com.hxd.gobus.mvp.presenter;

import android.content.Intent;

import com.hxd.gobus.bean.RealTimeData;
import com.hxd.gobus.bean.RealTimeMonitoring;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IVehicleLocationDetailContract;
import com.hxd.gobus.mvp.model.VehicleLocationDetailModel;
import com.hxd.gobus.utils.GsonUtils;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * @author: wangqingbin
 * @date: 2019/8/12 15:36
 */
@ActivityScope
public class VehicleLocationDetailPresenter extends BasePresenter<IVehicleLocationDetailContract.View,VehicleLocationDetailModel> {

    @Inject
    public VehicleLocationDetailPresenter() {

    }

    public void getIntentData(Intent intent){
        RealTimeMonitoring realTimeMonitoring = (RealTimeMonitoring) intent.getSerializableExtra("realTimeMonitoring");
        if (realTimeMonitoring != null){
            getView().initRealTimeMonitoring(realTimeMonitoring);
        }
    }

}
