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
import com.hxd.gobus.bean.Repair;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.mvp.adapter.AttachLookRecyclerAdapter;
import com.hxd.gobus.mvp.contract.IRepairApplyDetailContract;
import com.hxd.gobus.mvp.presenter.RepairApplyDetailPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: wangqingbin
 * @date: 2019/8/15 12:04
 */

public class RepairApplyDetailActivity extends BaseActivity<RepairApplyDetailPresenter> implements
        IRepairApplyDetailContract.View ,AttachLookRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.tv_repair_apply_detail_record_number)
    TextView tv_repair_apply_detail_record_number;

    @BindView(R.id.tv_repair_apply_detail_apply_name)
    TextView tv_repair_apply_detail_apply_name;

    @BindView(R.id.tv_repair_apply_detail_apply_time)
    TextView tv_repair_apply_detail_apply_time;

    @BindView(R.id.tv_repair_apply_detail_vehicle_name)
    TextView tv_repair_apply_detail_vehicle_name;

    @BindView(R.id.tv_repair_apply_detail_vehicle_license)
    TextView tv_repair_apply_detail_vehicle_license;

    @BindView(R.id.tv_repair_apply_detail_estimated_cost)
    TextView tv_repair_apply_detail_estimated_cost;

    @BindView(R.id.tv_repair_apply_detail_repair_status)
    TextView tv_repair_apply_detail_repair_status;

    @BindView(R.id.tv_repair_apply_detail_situation_description)
    TextView tv_repair_apply_detail_situation_description;

    @BindView(R.id.tv_repair_apply_detail_handle_person)
    TextView tv_repair_apply_detail_handle_person;

    @BindView(R.id.tv_repair_apply_detail_actual_cost)
    TextView tv_repair_apply_detail_actual_cost;

    @BindView(R.id.tv_repair_apply_detail_repair_time)
    TextView tv_repair_apply_detail_repair_time;

    @BindView(R.id.tv_repair_apply_detail_cost_bearing)
    TextView tv_repair_apply_detail_cost_bearing;

    @BindView(R.id.tv_repair_apply_detail_remark)
    TextView tv_repair_apply_detail_remark;

    @BindView(R.id.ll_attach)
    LinearLayout ll_attach;

    @BindView(R.id.attach_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.ll_repair_apply_detail_look)
    LinearLayout ll_repair_apply_detail_look;

    @BindView(R.id.btn_repair_apply_detail_look)
    Button btn_repair_apply_detail_look;

    @BindView(R.id.btn_repair_apply_detail_registration)
    Button btn_repair_apply_detail_registration;

    @OnClick({R.id.ll_toolbar_back,R.id.tv_toolbar_right_title,R.id.btn_repair_apply_detail_look,R.id.btn_repair_apply_detail_registration})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.tv_toolbar_right_title:
                break;
            case R.id.btn_repair_apply_detail_look:
                mPresenter.lookAudit();
                break;
            case R.id.btn_repair_apply_detail_registration:
                mPresenter.repairRegistration();
                break;
        }
    }

    private AttachLookRecyclerAdapter mAdapter;

    @Override
    protected int bindLayout() {
        return R.layout.activity_repair_apply_detail;
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
        mTvCenterTitle.setText("维修详情");
        mTvRightTitle.setVisibility(View.GONE);
        mTvRightTitle.setText("查看附件");
    }

    @Override
    protected void doBusiness() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getVehicleData(getIntent());
    }

    @Override
    public void initRepairData(Repair repair) {
        tv_repair_apply_detail_record_number.setText(repair.getRecordNumber());
        tv_repair_apply_detail_apply_name.setText(repair.getApplyPersonName());
        if (repair.getApplyDate() != null){
            if (repair.getApplyDate().contains(" ")){
                String[] split = repair.getApplyDate().split(" ");
                tv_repair_apply_detail_apply_time.setText(split[0]);
            }else{
                tv_repair_apply_detail_apply_time.setText(repair.getApplyDate());
            }
        }
        tv_repair_apply_detail_vehicle_name.setText(repair.getVehicleName());
        tv_repair_apply_detail_vehicle_license.setText(repair.getVehicleMark());
        if (repair.getEstimatedCosts() != null){
            tv_repair_apply_detail_estimated_cost.setText(repair.getEstimatedCosts().toString());
        }
        tv_repair_apply_detail_repair_status.setText(repair.getRepairStatusName());
        tv_repair_apply_detail_situation_description.setText(repair.getRepairDetail());
        tv_repair_apply_detail_handle_person.setText(repair.getHandlerName());
        if (repair.getActualCost() != null){
            tv_repair_apply_detail_actual_cost.setText(repair.getActualCost().toString());
        }
        if (repair.getRepairDate() != null){
            if (repair.getRepairDate().contains(" ")){
                String[] split = repair.getRepairDate().split(" ");
                tv_repair_apply_detail_repair_time.setText(split[0]);
            }else{
                tv_repair_apply_detail_repair_time.setText(repair.getRepairDate());
            }
        }
        tv_repair_apply_detail_cost_bearing.setText(repair.getCostPayer());
        tv_repair_apply_detail_remark.setText(repair.getRemarks());

        if ("1".equals(repair.getApprovalStatus())){
            ll_repair_apply_detail_look.setVisibility(View.VISIBLE);
            btn_repair_apply_detail_look.setVisibility(View.VISIBLE);
            btn_repair_apply_detail_registration.setVisibility(View.GONE);
        }else if("2".equals(repair.getApprovalStatus())){
            if ("0".equals(repair.getRepairStatus())){
                ll_repair_apply_detail_look.setVisibility(View.VISIBLE);
                btn_repair_apply_detail_look.setVisibility(View.GONE);
                btn_repair_apply_detail_registration.setVisibility(View.VISIBLE);
            }else{
                ll_repair_apply_detail_look.setVisibility(View.GONE);
                btn_repair_apply_detail_look.setVisibility(View.GONE);
                btn_repair_apply_detail_registration.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void intoApprovalPage(Repair repair) {
        Intent intent = new Intent(this,RepairApprovalActivity.class);
        intent.putExtra("source", BaseConfig.SOURCE_LOOK_DETAIL);
        intent.putExtra("positionFlag",BaseConfig.SIGN_ALREADY_DO);
        intent.putExtra("datumDataId", repair.getRepairId());
        intent.putExtra("datumType", repair.getDatumType());
        startActivity(intent);
    }

    @Override
    public void intoRegistrationPage(Repair repair) {
        Intent intent = new Intent(this,RepairRegistrationActivity.class);
        intent.putExtra("repair", repair);
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
