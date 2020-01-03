package com.hxd.gobus.mvp.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hxd.gobus.R;
import com.hxd.gobus.bean.RealTimeData;

import java.util.List;

/**
 * @author: wangqingbin
 * @date: 2019/8/12 15:24
 */

public class VehicleDataRecyclerAdapter extends BaseQuickAdapter<RealTimeData,BaseViewHolder> {

    public VehicleDataRecyclerAdapter(int layoutResId, @Nullable List<RealTimeData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RealTimeData item) {
        helper.setText(R.id.tv_vehicle_data_item_license,item.getVehicleMark());
        helper.setText(R.id.tv_vehicle_data_item_name,item.getVehicleName());
        if(!TextUtils.isEmpty(item.getChargeStatusName())){
            helper.setText(R.id.tv_vehicle_data_item_charging_status,item.getChargeStatusName());
        }else{
            helper.setText(R.id.tv_vehicle_data_item_charging_status,"充电 未知");
        }
        if (item.getXhMileage() != null){
            helper.setText(R.id.tv_vehicle_data_item_life,"续航 "+item.getXhMileage()+"KM");
        }else{
            helper.setText(R.id.tv_vehicle_data_item_life,"续航 未知");
        }
        if (item.getSoc() != null){
            helper.setText(R.id.tv_vehicle_data_item_electricity,"电量 "+item.getSoc()+"%");
        }else{
            helper.setText(R.id.tv_vehicle_data_item_electricity,"电量 未知");
        }
        helper.setText(R.id.tv_vehicle_data_item_full,"充满 未知");
    }
}
