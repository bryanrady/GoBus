package com.hxd.gobus.mvp.presenter;

import android.content.Intent;

import com.hxd.gobus.bean.RealTimeData;
import com.hxd.gobus.bean.RealTimeMonitoring;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IVehicleApplyContract;
import com.hxd.gobus.mvp.contract.IVehicleDataDetailContract;
import com.hxd.gobus.mvp.model.VehicleApplyModel;
import com.hxd.gobus.mvp.model.VehicleDataDetailModel;

import javax.inject.Inject;

/**
 * @author: wangqingbin
 * @date: 2019/8/12 15:36
 */
@ActivityScope
public class VehicleDataDetailPresenter extends BasePresenter<IVehicleDataDetailContract.View,VehicleDataDetailModel> {

    @Inject
    public VehicleDataDetailPresenter() {

    }

    public void getIntentData(Intent intent){
        RealTimeData realTimeData = (RealTimeData) intent.getSerializableExtra("realTimeData");
        if (realTimeData != null){
            getView().initRealTimeData(realTimeData);
        }
    }

}
