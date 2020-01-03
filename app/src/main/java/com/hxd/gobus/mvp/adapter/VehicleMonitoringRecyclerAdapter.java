package com.hxd.gobus.mvp.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hxd.gobus.R;
import com.hxd.gobus.bean.RealTimeData;
import com.hxd.gobus.bean.RealTimeMonitoring;

import java.util.List;

/**
 * @author: wangqingbin
 * @date: 2019/8/12 15:24
 */

public class VehicleMonitoringRecyclerAdapter extends BaseQuickAdapter<RealTimeMonitoring,BaseViewHolder> {

    public VehicleMonitoringRecyclerAdapter(int layoutResId, @Nullable List<RealTimeMonitoring> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RealTimeMonitoring item) {
        helper.setText(R.id.tv_vehicle_monitoring_item_license,item.getVehicleMark());
        if("0".equals(item.getUseCarStatus())){
            helper.setText(R.id.tv_vehicle_monitoring_item_name,"未领取");
        }else if("1".equals(item.getUseCarStatus())){
            if(!TextUtils.isEmpty(item.getReceiveCarPersonName())){
                helper.setText(R.id.tv_vehicle_monitoring_item_name,item.getReceiveCarPersonName());
            }else{
                helper.setText(R.id.tv_vehicle_monitoring_item_name,"使用中");
            }
        }else if("2".equals(item.getUseCarStatus())){
            helper.setText(R.id.tv_vehicle_monitoring_item_name,"已归还");
        }
        helper.setText(R.id.tv_vehicle_monitoring_item_time,item.getVehicleName());
        if(!TextUtils.isEmpty(item.getRealTimeLocation())){
            helper.setText(R.id.tv_vehicle_monitoring_item_company_name,item.getRealTimeLocation());
        }else{
            helper.setText(R.id.tv_vehicle_monitoring_item_company_name,"未知位置");
        }

    }
}
