package com.hxd.gobus.mvp.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hxd.gobus.R;
import com.hxd.gobus.bean.RescuePerson;

import java.util.List;

/**
 * @author: wangqingbin
 * @date: 2019/8/12 15:24
 */

public class EmergencyRescueRecyclerAdapter extends BaseQuickAdapter<RescuePerson,BaseViewHolder> {

    public EmergencyRescueRecyclerAdapter(int layoutResId, @Nullable List<RescuePerson> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RescuePerson item) {
        helper.setText(R.id.tv_emergency_rescue_item_name,item.getName());
        helper.setText(R.id.tv_emergency_rescue_item_phone,item.getMobilePhone());
        helper.setText(R.id.tv_emergency_rescue_item_call,"呼 叫");
    }
}
