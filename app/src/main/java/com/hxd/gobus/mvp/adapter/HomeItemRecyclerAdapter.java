package com.hxd.gobus.mvp.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hxd.gobus.R;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.bean.DataItem;

import java.util.List;

/**
 * @author: wangqingbin
 * @date: 2019/7/30 19:23
 */

public class HomeItemRecyclerAdapter extends BaseQuickAdapter<DataItem,BaseViewHolder> {

    private final List<DataItem> mList;

    public HomeItemRecyclerAdapter(int layoutResId, @Nullable List<DataItem> data) {
        super(layoutResId, data);
        this.mList = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, DataItem item) {
        helper.setBackgroundRes(R.id.iv_home_item_icon,item.getIcon());
        helper.setText(R.id.tv_home_item_title,item.getTitle());
    }
}
