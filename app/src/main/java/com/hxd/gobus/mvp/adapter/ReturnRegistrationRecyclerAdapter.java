package com.hxd.gobus.mvp.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hxd.gobus.BusApp;
import com.hxd.gobus.R;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.config.VehicleConfig;
import com.hxd.gobus.utils.DateUtils;

import java.util.List;

/**
 * 车辆实时数据适配器
 * @author: wangqingbin
 * @date: 2019/8/12 15:24
 */

public class ReturnRegistrationRecyclerAdapter extends BaseQuickAdapter<Vehicle,BaseViewHolder> {

    public ReturnRegistrationRecyclerAdapter(int layoutResId, @Nullable List<Vehicle> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Vehicle item) {
        helper.setText(R.id.tv_return_registration_item_license,item.getCarNumber());
        helper.setText(R.id.tv_return_registration_item_name,item.getApplyPersonName());
        if (item.getApplyDate() != null){
            helper.setText(R.id.tv_return_registration_item_time, DateUtils.getStrDateFormat(item.getApplyDate(),DateUtils.FORMAT_3));
        }else{
            helper.setText(R.id.tv_return_registration_item_time,item.getApplyDate());
        }
        helper.setText(R.id.tv_return_registration_item_company_name,item.getDestination());
        if (!VehicleConfig.VEHICLE_ALREADY_RETURN.equals(item.getUseCarStatus())){
            helper.setText(R.id.tv_return_registration_item_registration_detail,"登 记");
            helper.setTextColor(R.id.tv_return_registration_item_registration_detail,
                    BusApp.getContext().getResources().getColor(R.color.white));
            helper.setBackgroundRes(R.id.tv_return_registration_item_registration_detail, R.drawable.btn_frame_filling_green);
        }else {
            helper.setText(R.id.tv_return_registration_item_registration_detail,"详 情");
            helper.setTextColor(R.id.tv_return_registration_item_registration_detail,
                    BusApp.getContext().getResources().getColor(R.color.colorAccent));
            helper.setBackgroundRes(R.id.tv_return_registration_item_registration_detail, R.drawable.btn_frame_green);
        }
    }
}
