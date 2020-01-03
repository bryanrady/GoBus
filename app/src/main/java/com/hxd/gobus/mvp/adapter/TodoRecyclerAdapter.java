package com.hxd.gobus.mvp.adapter;

import android.support.annotation.Nullable;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hxd.gobus.R;
import com.hxd.gobus.bean.Todo;
import com.hxd.gobus.config.BaseConfig;

import java.util.List;

/**
 * @author: wangqingbin
 * @date: 2019/9/18 15:18
 */

public class TodoRecyclerAdapter extends BaseQuickAdapter<Todo,BaseViewHolder> {

    public TodoRecyclerAdapter(int layoutResId, @Nullable List<Todo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Todo item) {
        if (BaseConfig.DATUMTYPE_VEHICLE.equals(item.getDatumType())){
            helper.setImageResource(R.id.iv_todo_item_icon,R.mipmap.ic_todo_vehicle_approval);
            helper.setText(R.id.tv_todo_item_name, item.getTitle());
            helper.setText(R.id.tv_todo_item_date, item.getCreateDate());
        }else if(BaseConfig.DATUMTYPE_REPAIR.equals(item.getDatumType())){
            helper.setImageResource(R.id.iv_todo_item_icon,R.mipmap.ic_todo_repair_approval);
            helper.setText(R.id.tv_todo_item_name, item.getTitle());
            helper.setText(R.id.tv_todo_item_date, item.getCreateDate());
        }else if(BaseConfig.DATUMTYPE_MAINTAIN.equals(item.getDatumType())){
            helper.setImageResource(R.id.iv_todo_item_icon,R.mipmap.ic_todo_maintain_approval);
            helper.setText(R.id.tv_todo_item_name, item.getTitle());
            helper.setText(R.id.tv_todo_item_date, item.getCreateDate());
        }else{
            helper.setImageResource(R.id.iv_todo_item_icon,R.mipmap.ic_todo_vehicle_approval);
            helper.setText(R.id.tv_todo_item_name, item.getTitle());
            helper.setText(R.id.tv_todo_item_date, item.getCreateDate());
        }
    }

}