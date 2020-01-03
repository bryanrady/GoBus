package com.hxd.gobus.mvp.model;

import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IReturnRegistrationContract;
import com.hxd.gobus.mvp.contract.IVehicleDataDetailContract;

import javax.inject.Inject;

/**
 * @author: wangqingbin
 * @date: 2019/8/12 15:36
 */
@ActivityScope
public class VehicleDataDetailModel extends BaseModel implements IVehicleDataDetailContract.Model {

    @Inject
    public VehicleDataDetailModel(){

    }

    @Override
    public void onDestroy() {

    }
}
