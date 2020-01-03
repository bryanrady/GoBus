package com.hxd.gobus.mvp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseLazyFragment;
import com.hxd.gobus.bean.DataItem;
import com.hxd.gobus.mvp.activity.EmergencyRescueListActivity;
import com.hxd.gobus.mvp.activity.MaintainRecordActivity;
import com.hxd.gobus.mvp.activity.MobileApprovalActivity;
import com.hxd.gobus.mvp.activity.RepairRecordActivity;
import com.hxd.gobus.mvp.activity.ReturnRegistrationListActivity;
import com.hxd.gobus.mvp.activity.VehicleDataListActivity;
import com.hxd.gobus.mvp.activity.VehicleMonitoringListActivity;
import com.hxd.gobus.mvp.activity.VehicleRecordActivity;
import com.hxd.gobus.mvp.activity.VehicleRegistrationListActivity;
import com.hxd.gobus.mvp.adapter.HomeItemRecyclerAdapter;
import com.hxd.gobus.mvp.contract.IHomeContract;
import com.hxd.gobus.mvp.presenter.HomePresenter;

import java.util.List;

import butterknife.BindView;

/**
 * @author: wangqingbin
 * @date: 2019/7/25 10:23
 */

public class HomeFragment extends BaseLazyFragment<HomePresenter> implements IHomeContract.View,
        BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.recycler_home)
    RecyclerView mRecyclerView;

    @Override
    protected void lazyLoad(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_main_home;
    }

    @Override
    protected void initView(View view) {
        initToolbar();
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.GONE);
        mTvCenterTitle.setText("首 页");
        mTvCenterTitle.setTextColor(getResources().getColor(R.color.grey_6));
    }

    @Override
    protected void initData() {
        mPresenter.getHomeItemData();
    }

    @Override
    public void showHomeItem(List<DataItem> dataItems) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        HomeItemRecyclerAdapter adapter = new HomeItemRecyclerAdapter(R.layout.recycler_home_item, dataItems);
        adapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        switch (position){
            case 0:
                startActivity(new Intent(mContext, VehicleRecordActivity.class));
                break;
            case 1:
                startActivity(new Intent(mContext, RepairRecordActivity.class));
                break;
            case 2:
                startActivity(new Intent(mContext, MaintainRecordActivity.class));
                break;
            case 3:
                startActivity(new Intent(mContext, MobileApprovalActivity.class));
                break;
            case 4:
                startActivity(new Intent(mContext, VehicleRegistrationListActivity.class));
                break;
            case 5:
                startActivity(new Intent(mContext, ReturnRegistrationListActivity.class));
                break;
            case 6:
                startActivity(new Intent(mContext, VehicleMonitoringListActivity.class));
                break;
            case 7:
                startActivity(new Intent(mContext, EmergencyRescueListActivity.class));
                break;
            case 8:
                startActivity(new Intent(mContext, VehicleDataListActivity.class));
                break;
        }
    }
}
