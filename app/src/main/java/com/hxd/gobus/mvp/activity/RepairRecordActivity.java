package com.hxd.gobus.mvp.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.Repair;
import com.hxd.gobus.mvp.adapter.RepairRecordRecyclerAdapter;
import com.hxd.gobus.mvp.contract.IRepairRecordContract;
import com.hxd.gobus.mvp.presenter.RepairRecordPresenter;
import com.hxd.gobus.views.FilterPopupWindow;
import com.hxd.gobus.views.RecycleViewDivider;
import com.hxd.gobus.views.SearchBox;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.enums.PopupPosition;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: wangqingbin
 * @date: 2019/9/12 11:49
 */

public class RepairRecordActivity extends BaseActivity<RepairRecordPresenter> implements OnRefreshListener,OnLoadMoreListener,
        IRepairRecordContract.View ,RepairRecordRecyclerAdapter.OnSlidingViewClickListener,TextWatcher,FilterPopupWindow.OnFilterSureListener{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.rl_filter_layout)
    RelativeLayout rl_filter_layout;

    @BindView(R.id.rl_repair_record_filter)
    RelativeLayout rl_repair_record_filter;

    @BindView(R.id.tv_repair_record_filter)
    TextView tv_repair_record_filter;

    @BindView(R.id.iv_repair_record_filter)
    ImageView iv_repair_record_filter;

    @BindView(R.id.repair_record_searchBox)
    SearchBox mSearchBox;

    @BindView(R.id.btn_repair_record_search)
    Button btn_repair_record_search;

    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    @OnClick({R.id.ll_toolbar_back,R.id.tv_toolbar_right_title,R.id.rl_repair_record_filter,R.id.btn_repair_record_search})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.tv_toolbar_right_title:
                startActivity(RepairApplyActivity.class);
                break;
            case R.id.rl_repair_record_filter: ;
                if (mBasePopupView != null){
                    mBasePopupView.show();
                }else{
                    mBasePopupView = new XPopup.Builder(this)
                            .atView(rl_filter_layout)
                            .popupPosition(PopupPosition.Bottom)
                            .dismissOnTouchOutside(true)
                            .asCustom(mFilterPopupWindow)
                            .show();
                }
                //这里好像是反的
                if(mBasePopupView.isShow()){
                    iv_repair_record_filter.setBackground(getResources().getDrawable(R.mipmap.ic_filter_arrow_down));
                }else{
                    iv_repair_record_filter.setBackground(getResources().getDrawable(R.mipmap.ic_filter_arrow_up));
                }
                break;
            case R.id.btn_repair_record_search:
                isAutoRefresh = false;
                mPresenter.setRemarks(mRemarks);
                mSwipeToLoadLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeToLoadLayout.setRefreshing(true);
                    }
                }, 100);
                break;
        }
    }

    private RepairRecordRecyclerAdapter mAdapter;
    private String mRemarks;
    private FilterPopupWindow mFilterPopupWindow;
    private BasePopupView mBasePopupView;
    private boolean isAutoRefresh = true;

    @Override
    protected int bindLayout() {
        return R.layout.activity_swipe_load_recycler_repair;
    }

    @Override
    protected void initView() {
        initToolbar();

        initRefresh();

        mSearchBox.addTextChangedListener(this);
        mFilterPopupWindow = new FilterPopupWindow(this);
        mFilterPopupWindow.setOnFilterSureListener(this);
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.VISIBLE);
        mTvCenterTitle.setText("维修记录");
        mTvRightTitle.setVisibility(View.VISIBLE);
        mTvRightTitle.setText("新增");
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
    public void showData(List<Repair> list) {
        mAdapter = new RepairRecordRecyclerAdapter(this, list);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnSlidindViewClickListener(this);

        isAutoRefresh = true;
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
    public void deleteSuccess(int position) {
        if (mAdapter != null){
            mAdapter.removeData(position);
        }
    }

    @Override
    public void deleteNotSuccess(String toast) {
        showShortToast(toast);
    }

    @Override
    public void onLoadMore() {
        mPresenter.getMoreData();
    }

    @Override
    public void onRefresh() {
        if(isAutoRefresh){
            mSearchBox.setText("");
            mPresenter.setRemarks("");
            mFilterPopupWindow.setRefresh();
            mPresenter.setVehicleMark("");
            mPresenter.setStartDate("");
            mPresenter.setEndDate("");
        }
        mPresenter.getNewData();
    }

    @Override
    public void onItemClick(View view, int position, Repair repair) {
        Intent intent;
        if ("0".equals(repair.getApprovalStatus())){
            intent = new Intent(this,RepairApplyActivity.class);
        }else if ("1".equals(repair.getApprovalStatus())){
            intent = new Intent(this,RepairApplyDetailActivity.class);
        }else if ("2".equals(repair.getApprovalStatus())){
            intent = new Intent(this,RepairApplyDetailActivity.class);
        }else{
            intent = new Intent(this,RepairApplyActivity.class);
        }
        intent.putExtra("repairId",repair.getRepairId());
        startActivity(intent);
    }

    @Override
    public void onItemDeleteClick(View view, int position, Repair repair) {
        mPresenter.deleteRepairApply(position);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s != null){
            mRemarks = s.toString().trim();
        }
    }

    @Override
    public void onSure(String vehicleMark, String startDate, String endDate) {
        isAutoRefresh = false;
        iv_repair_record_filter.setBackground(getResources().getDrawable(R.mipmap.ic_filter_arrow_down));
        mPresenter.setVehicleMark(vehicleMark);
        mPresenter.setStartDate(startDate);
        mPresenter.setEndDate(endDate);
        mSwipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        }, 100);
    }

    @Override
    public void onReset() {
        mPresenter.setVehicleMark("");
        mPresenter.setStartDate("");
        mPresenter.setEndDate("");
    }
}

