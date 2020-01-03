package com.hxd.gobus.mvp.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bryanrady.lib_permission.annotation.NeedPermission;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.RescuePerson;
import com.hxd.gobus.mvp.adapter.EmergencyRescueRecyclerAdapter;
import com.hxd.gobus.mvp.contract.IEmergencyRescueListContract;
import com.hxd.gobus.mvp.presenter.EmergencyRescueListPresenter;
import com.hxd.gobus.views.RecycleViewDivider;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 紧急救援页面
 * @author: wangqingbin
 * @date: 2019/8/15 12:04
 */

public class EmergencyRescueListActivity extends BaseActivity<EmergencyRescueListPresenter> implements IEmergencyRescueListContract.View,BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

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
        return R.layout.activity_emergency_rescue;
    }

    @Override
    protected void initView() {
        initToolbar();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, R.drawable.recycleview_divider));
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.VISIBLE);
        mTvCenterTitle.setText("紧急求救");
        mTvRightTitle.setVisibility(View.GONE);
    }

    @Override
    protected void doBusiness() {
        mPresenter.getRescueList();
    }

    @Override
    public void showRescueList(List<RescuePerson> list) {
        EmergencyRescueRecyclerAdapter adapter = new EmergencyRescueRecyclerAdapter(R.layout.recycler_emergency_rescue_item, list);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        RescuePerson rescuePerson = (RescuePerson) adapter.getItem(position);
        String phoneNumber = rescuePerson.getMobilePhone();
        if (!TextUtils.isEmpty(phoneNumber)){
            callPhone(phoneNumber);
        }else{
            showToast("请联系管理员配置此人的电话号码!");
        }
    }

    @NeedPermission(Manifest.permission.CALL_PHONE)
    private void callPhone(String phoneNumber) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNumber);
        intent.setData(data);
        startActivity(intent);
    }

}
