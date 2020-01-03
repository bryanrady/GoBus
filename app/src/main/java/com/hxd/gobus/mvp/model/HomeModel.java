package com.hxd.gobus.mvp.model;

import com.hxd.gobus.R;
import com.hxd.gobus.bean.DataItem;
import com.hxd.gobus.di.scopes.FragmentScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IHomeContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:45
 */
@FragmentScope
public class HomeModel extends BaseModel implements IHomeContract.Model {

    @Inject
    public HomeModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public List<DataItem> makeHomeItemData() {
        List<DataItem> dataItemList = new ArrayList<>();
        dataItemList.add(new DataItem("用车申请",R.mipmap.ic_home_vehicle_apply));
        dataItemList.add(new DataItem("维修申请",R.mipmap.ic_home_repair_apply));
        dataItemList.add(new DataItem("保养申请",R.mipmap.ic_home_maintain_apply));
        dataItemList.add(new DataItem("移动审批",R.mipmap.ic_home_mobile_approval));
        dataItemList.add(new DataItem("用车登记",R.mipmap.ic_home_vehicle_registration));
        dataItemList.add(new DataItem("还车登记",R.mipmap.ic_home_return_registration));
        dataItemList.add(new DataItem("车辆实时监控",R.mipmap.ic_home_vehicle_monitoring));
        dataItemList.add(new DataItem("紧急救援",R.mipmap.ic_home_emergency_rescue));
        dataItemList.add(new DataItem("车辆实时车况",R.mipmap.ic_home_vehicle_data));
        return dataItemList;
    }
}
