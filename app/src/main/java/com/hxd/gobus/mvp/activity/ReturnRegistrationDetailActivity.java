package com.hxd.gobus.mvp.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.mvp.adapter.AttachLookRecyclerAdapter;
import com.hxd.gobus.mvp.contract.IReturnRegistrationDetailContract;
import com.hxd.gobus.mvp.presenter.ReturnRegistrationDetailPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用车登记详情页面
 * @author: wangqingbin
 * @date: 2019/8/15 12:04
 */

public class ReturnRegistrationDetailActivity extends BaseActivity<ReturnRegistrationDetailPresenter>
        implements IReturnRegistrationDetailContract.View,AttachLookRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.tv_return_registration_detail_apply_name)
    TextView tv_return_registration_detail_apply_name;

    @BindView(R.id.tv_return_registration_detail_apply_time)
    TextView tv_return_registration_detail_apply_time;

    @BindView(R.id.tv_return_registration_detail_destination)
    TextView tv_return_registration_detail_destination;

    @BindView(R.id.tv_return_registration_detail_vehicle_type)
    TextView tv_return_registration_detail_vehicle_type;

    @BindView(R.id.tv_return_registration_detail_vehicle_range)
    TextView tv_return_registration_detail_vehicle_range;

    @BindView(R.id.tv_return_registration_detail_vehicle_reason)
    TextView tv_return_registration_detail_vehicle_reason;

    @BindView(R.id.tv_return_registration_detail_designated_vehicle)
    TextView tv_return_registration_detail_designated_vehicle;

    @BindView(R.id.tv_return_registration_detail_is_use_car)
    TextView tv_return_registration_detail_is_use_car;

    @BindView(R.id.tv_return_registration_detail_predict_start_time)
    TextView tv_return_registration_detail_predict_start_time;

    @BindView(R.id.tv_return_registration_detail_predict_end_time)
    TextView tv_return_registration_detail_predict_end_time;

    @BindView(R.id.tv_return_registration_detail_accompany_people)
    TextView tv_return_registration_detail_accompany_people;

    @BindView(R.id.tv_return_registration_detail_designated_driver)
    TextView tv_return_registration_detail_designated_driver;

    @BindView(R.id.tv_return_registration_detail_get_car)
    TextView tv_return_registration_detail_get_car;

    @BindView(R.id.tv_return_registration_detail_get_car_time)
    TextView tv_return_registration_detail_get_car_time;

    @BindView(R.id.tv_return_registration_detail_return_car)
    TextView tv_return_registration_detail_return_car;

    @BindView(R.id.tv_return_registration_detail_return_car_time)
    TextView tv_return_registration_detail_return_car_time;

    @BindView(R.id.tv_return_registration_detail_remark)
    TextView tv_return_registration_detail_remark;

    @BindView(R.id.ll_attach)
    LinearLayout ll_attach;

    @BindView(R.id.attach_recyclerView)
    RecyclerView mRecyclerView;

    @OnClick({R.id.ll_toolbar_back,R.id.tv_toolbar_right_title})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.tv_toolbar_right_title:
                break;
        }
    }

    private AttachLookRecyclerAdapter mAdapter;

    @Override
    protected int bindLayout() {
        return R.layout.activity_return_registration_detail;
    }

    @Override
    protected void initView() {
        initToolbar();

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.VISIBLE);
        mTvCenterTitle.setText("还车登记详情");
        mTvRightTitle.setVisibility(View.GONE);
        mTvRightTitle.setText("附件管理");
    }

    @Override
    protected void doBusiness() {
        mPresenter.queryReturnRegistrationDetail(getIntent());
    }

    @Override
    public void initVehicleData(Vehicle vehicle) {
        tv_return_registration_detail_apply_name.setText(vehicle.getApplyPersonName());
        if (vehicle.getApplyDate() != null){
            if (vehicle.getApplyDate().contains(" ")){
                String[] split = vehicle.getApplyDate().split(" ");
                tv_return_registration_detail_apply_time.setText(split[0]);
            }else{
                tv_return_registration_detail_apply_time.setText(vehicle.getApplyDate());
            }
        }
        tv_return_registration_detail_destination.setText(vehicle.getDestination());
        tv_return_registration_detail_vehicle_type.setText(vehicle.getUseCarTypeName());
        tv_return_registration_detail_vehicle_range.setText(vehicle.getUseCarRangeName());
        tv_return_registration_detail_vehicle_reason.setText(vehicle.getUseReason());
        tv_return_registration_detail_designated_vehicle.setText(vehicle.getCarNumber());
        tv_return_registration_detail_is_use_car.setText(vehicle.getIsUseCarName());
        tv_return_registration_detail_predict_start_time.setText(vehicle.getPredictStartDate());
        tv_return_registration_detail_predict_end_time.setText(vehicle.getPredictDurationName());

        tv_return_registration_detail_accompany_people.setText(vehicle.getTravelPartner());
        tv_return_registration_detail_designated_driver.setText(vehicle.getAssignDriverName());
        tv_return_registration_detail_get_car.setText(vehicle.getReceiveCarPersonName());
        tv_return_registration_detail_get_car_time.setText(vehicle.getReceiveCarDate());

        tv_return_registration_detail_return_car.setText(vehicle.getBackCarPersonName());
        tv_return_registration_detail_return_car_time.setText(vehicle.getBackCarDate());
        tv_return_registration_detail_remark.setText(vehicle.getRemareks());
    }

    @Override
    public void showAttachList(List<Attach> list) {
        if (list.size() > 0){
            mAdapter = new AttachLookRecyclerAdapter(this,list);
            mAdapter.setOnItemClickListener(this);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            ll_attach.setPadding(
                    0,
                    (int)getResources().getDimension(R.dimen.dp_16),
                    0,
                    (int)getResources().getDimension(R.dimen.dp_16));
        }
    }

    @Override
    public void onItemImageClick(int position, Attach item) {
        Intent intent = new Intent(this,ImagePreviewActivity.class);
        intent.putExtra("imageName",item.getAttachName());
        intent.putExtra("nativePath",item.getNativePath());
        intent.putExtra("imageUrl",item.getAttachAddress());
        startActivity(intent);
    }

    @Override
    public void onItemFileClick(int position, Attach item) {
        Intent intent = new Intent(this,FilePreviewActivity.class);
        intent.putExtra("fileName",item.getAttachName());
        intent.putExtra("nativePath",item.getNativePath());
        intent.putExtra("fileUrl",item.getAttachAddress());
        startActivity(intent);
    }
}
