package com.hxd.gobus.mvp.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.config.VehicleConfig;
import com.hxd.gobus.mvp.adapter.VehicleRegistrationRecyclerAdapter;
import com.hxd.gobus.mvp.contract.IVehicleRegistrationListContract;
import com.hxd.gobus.mvp.presenter.VehicleRegistrationListPresenter;
import com.hxd.gobus.views.RecycleViewDivider;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用车登记记录页面
 * @author: wangqingbin
 * @date: 2019/8/15 12:04
 */

public class VehicleRegistrationListActivity extends BaseActivity<VehicleRegistrationListPresenter> implements
        OnRefreshListener,OnLoadMoreListener, IVehicleRegistrationListContract.View, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    @OnClick({R.id.ll_toolbar_back})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
        }
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_swipe_load_recycler;
    }

    @Override
    protected void initView() {
        initToolbar();

        initRefresh();
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.VISIBLE);
        mTvCenterTitle.setText("用车登记列表");
        mTvRightTitle.setVisibility(View.GONE);
    }

    private void initRefresh() {
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, R.drawable.recycleview_divider));
    }

    @Override
    protected void doBusiness() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSwipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        }, 100);
    }

    @Override
    public void showData(List<Vehicle> list) {
        VehicleRegistrationRecyclerAdapter adapter = new VehicleRegistrationRecyclerAdapter(R.layout.recycler_vehicle_registration_item, list);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void overRefresh() {
        mSwipeToLoadLayout.setRefreshing(false);
        mSwipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    public void setLoadMoreEnabled(boolean flag) {
        mSwipeToLoadLayout.setLoadMoreEnabled(flag);
    }

    @Override
    public void onLoadMore() {
        mPresenter.getMoreData();
    }

    @Override
    public void onRefresh() {
        mPresenter.getNewData();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Vehicle vehicle = (Vehicle) adapter.getItem(position);
        if("1".equals(vehicle.getUseCarStatus())){
            showToast("该车辆正在使用中，不可登记");
            return;
        }else if("2".equals(vehicle.getUseCarStatus())){
            showToast("该车辆已归还，不可登记");
            return;
        }
        Intent intent;
        if (VehicleConfig.VEHICLE_NOT_REGISTRATION.equals(vehicle.getUseCarStatus())){
            intent = new Intent(this,VehicleRegistrationActivity.class);
        }else{
            intent = new Intent(this,VehicleRegistrationDetailActivity.class);
        }
        intent.putExtra("managementId",vehicle.getManagementId());
        startActivity(intent);
    }
}
