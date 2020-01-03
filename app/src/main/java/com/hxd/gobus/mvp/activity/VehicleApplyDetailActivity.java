package com.hxd.gobus.mvp.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.mvp.adapter.AttachLookRecyclerAdapter;
import com.hxd.gobus.mvp.contract.IVehicleApplyDetailContract;
import com.hxd.gobus.mvp.presenter.VehicleApplyDetailPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用车申请页面
 * @author: wangqingbin
 * @date: 2019/8/15 12:04
 */

public class VehicleApplyDetailActivity extends BaseActivity<VehicleApplyDetailPresenter> implements
        IVehicleApplyDetailContract.View,AttachLookRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.tv_vehicle_apply_detail_apply_name)
    TextView tv_vehicle_apply_detail_apply_name;

    @BindView(R.id.tv_vehicle_apply_detail_apply_time)
    TextView tv_vehicle_apply_detail_apply_time;

    @BindView(R.id.tv_vehicle_apply_detail_apply_unit)
    TextView tv_vehicle_apply_detail_apply_unit;

    @BindView(R.id.tv_vehicle_apply_detail_destination)
    TextView tv_vehicle_apply_detail_destination;

    @BindView(R.id.tv_vehicle_apply_detail_vehicle_type)
    TextView tv_vehicle_apply_detail_vehicle_type;

    @BindView(R.id.tv_vehicle_apply_detail_vehicle_range)
    TextView tv_vehicle_apply_detail_vehicle_range;

    @BindView(R.id.tv_vehicle_apply_detail_predict_vehicle_time)
    TextView tv_vehicle_apply_detail_predict_vehicle_time;

    @BindView(R.id.tv_vehicle_apply_detail_designated_vehicle)
    TextView tv_vehicle_apply_detail_designated_vehicle;

    @BindView(R.id.tv_vehicle_apply_detail_predict_use_time)
    TextView tv_vehicle_apply_detail_predict_use_time;

    @BindView(R.id.tv_vehicle_apply_detail_vehicle_reason)
    TextView tv_vehicle_apply_detail_vehicle_reason;

    @BindView(R.id.tv_vehicle_apply_detail_designated_driver)
    TextView tv_vehicle_apply_detail_designated_driver;

    @BindView(R.id.tv_vehicle_apply_detail_accompany_person)
    TextView tv_vehicle_apply_detail_accompany_person;

    @BindView(R.id.tv_vehicle_apply_detail_remark)
    TextView tv_vehicle_apply_detail_remark;

    @BindView(R.id.ll_attach)
    LinearLayout ll_attach;

    @BindView(R.id.attach_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.ll_vehicle_detail_look)
    LinearLayout ll_vehicle_detail_look;

    @BindView(R.id.btn_vehicle_apply_detail_look)
    Button btn_vehicle_apply_detail_look;

    @OnClick({R.id.ll_toolbar_back,R.id.tv_toolbar_right_title,R.id.btn_vehicle_apply_detail_look})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.tv_toolbar_right_title:
                break;
            case R.id.btn_vehicle_apply_detail_look:
                mPresenter.lookAudit();
                break;
        }
    }

    private AttachLookRecyclerAdapter mAdapter;

    @Override
    protected int bindLayout() {
        return R.layout.activity_vehicle_apply_detail;
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
        mTvCenterTitle.setText("用车详情");
        mTvRightTitle.setVisibility(View.GONE);
        mTvRightTitle.setText("查看附件");
    }

    @Override
    protected void doBusiness() {
        mPresenter.getVehicleData(getIntent());
    }

    @Override
    public void initVehicleData(Vehicle vehicle) {
        tv_vehicle_apply_detail_apply_name.setText(vehicle.getApplyPersonName());
        if (vehicle.getApplyDate() != null){
            if (vehicle.getApplyDate().contains(" ")){
                String[] split = vehicle.getApplyDate().split(" ");
                tv_vehicle_apply_detail_apply_time.setText(split[0]);
            }else{
                tv_vehicle_apply_detail_apply_time.setText(vehicle.getApplyDate());
            }
        }
        tv_vehicle_apply_detail_apply_unit.setText(vehicle.getApplyUnitName());
        tv_vehicle_apply_detail_destination.setText(vehicle.getDestination());
        tv_vehicle_apply_detail_vehicle_type.setText(vehicle.getUseCarTypeName());
        tv_vehicle_apply_detail_vehicle_range.setText(vehicle.getUseCarRangeName());
        tv_vehicle_apply_detail_predict_vehicle_time.setText(vehicle.getPredictStartDate());
        tv_vehicle_apply_detail_designated_vehicle.setText(vehicle.getCarNumber());
        tv_vehicle_apply_detail_predict_use_time.setText(vehicle.getPredictDurationName());
        tv_vehicle_apply_detail_vehicle_reason.setText(vehicle.getUseReason());
        tv_vehicle_apply_detail_designated_driver.setText(vehicle.getAssignDriverName());
        tv_vehicle_apply_detail_accompany_person.setText(vehicle.getTravelPartner());
        tv_vehicle_apply_detail_remark.setText(vehicle.getRemareks());

        if ("1".equals(vehicle.getApprovalStatus())){
            ll_vehicle_detail_look.setVisibility(View.VISIBLE);
        }else{
            ll_vehicle_detail_look.setVisibility(View.GONE);
        }
    }

    @Override
    public void intoApprovalPage(Vehicle vehicle) {
        Intent intent = new Intent(this,VehicleApprovalActivity.class);
        intent.putExtra("source", BaseConfig.SOURCE_LOOK_DETAIL);
        intent.putExtra("positionFlag",BaseConfig.SIGN_ALREADY_DO);
        intent.putExtra("datumDataId", vehicle.getManagementId());
        intent.putExtra("datumType", vehicle.getDatumType());
        startActivity(intent);
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
