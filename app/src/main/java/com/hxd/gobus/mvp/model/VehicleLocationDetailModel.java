package com.hxd.gobus.mvp.model;

import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IVehicleLocationDetailContract;

import javax.inject.Inject;

/**
 * @author: wangqingbin
 * @date: 2019/8/12 15:36
 */
@ActivityScope
public class VehicleLocationDetailModel extends BaseModel implements IVehicleLocationDetailContract.Model {

    @Inject
    public VehicleLocationDetailModel(){

    }

    @Override
    public void onDestroy() {

    }

}
