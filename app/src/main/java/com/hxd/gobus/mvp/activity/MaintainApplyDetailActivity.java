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
import com.hxd.gobus.bean.Maintain;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.mvp.adapter.AttachLookRecyclerAdapter;
import com.hxd.gobus.mvp.contract.IMaintainApplyDetailContract;
import com.hxd.gobus.mvp.presenter.MaintainApplyDetailPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: wangqingbin
 * @date: 2019/8/15 12:04
 */

public class MaintainApplyDetailActivity extends BaseActivity<MaintainApplyDetailPresenter> implements
        IMaintainApplyDetailContract.View,AttachLookRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.tv_maintain_apply_detail_record_number)
    TextView tv_maintain_apply_detail_record_number;

    @BindView(R.id.tv_maintain_apply_detail_apply_name)
    TextView tv_maintain_apply_detail_apply_name;

    @BindView(R.id.tv_maintain_apply_detail_apply_time)
    TextView tv_maintain_apply_detail_apply_time;

    @BindView(R.id.tv_maintain_apply_detail_vehicle_name)
    TextView tv_maintain_apply_detail_vehicle_name;

    @BindView(R.id.tv_maintain_apply_detail_vehicle_license)
    TextView tv_maintain_apply_detail_vehicle_license;

    @BindView(R.id.tv_maintain_apply_detail_estimated_cost)
    TextView tv_maintain_apply_detail_estimated_cost;

    @BindView(R.id.tv_maintain_apply_detail_maintain_status)
    TextView tv_maintain_apply_detail_maintain_status;

    @BindView(R.id.tv_maintain_apply_detail_situation_description)
    TextView tv_maintain_apply_detail_situation_description;

    @BindView(R.id.tv_maintain_apply_detail_handle_person)
    TextView tv_maintain_apply_detail_handle_person;

    @BindView(R.id.tv_maintain_apply_detail_maintain_cost)
    TextView tv_maintain_apply_detail_maintain_cost;

    @BindView(R.id.tv_maintain_apply_detail_maintain_time)
    TextView tv_maintain_apply_detail_maintain_time;

    @BindView(R.id.tv_maintain_apply_detail_cost_bearing)
    TextView tv_maintain_apply_detail_cost_bearing;

    @BindView(R.id.tv_maintain_apply_detail_vehicle_description)
    TextView tv_maintain_apply_detail_vehicle_description;

    @BindView(R.id.tv_maintain_apply_detail_remark)
    TextView tv_maintain_apply_detail_remark;

    @BindView(R.id.ll_attach)
    LinearLayout ll_attach;

    @BindView(R.id.attach_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.ll_maintain_apply_detail_look)
    LinearLayout ll_maintain_apply_detail_look;

    @BindView(R.id.btn_maintain_apply_detail_look)
    Button btn_maintain_apply_detail_look;

    @BindView(R.id.btn_maintain_apply_detail_registration)
    Button btn_maintain_apply_detail_registration;

    @OnClick({R.id.ll_toolbar_back,R.id.tv_toolbar_right_title,R.id.btn_maintain_apply_detail_look,R.id.btn_maintain_apply_detail_registration})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.tv_toolbar_right_title:
                break;
            case R.id.btn_maintain_apply_detail_look:
                mPresenter.lookAudit();
                break;
            case R.id.btn_maintain_apply_detail_registration:
                mPresenter.repairRegistration();
                break;
        }
    }

    private AttachLookRecyclerAdapter mAdapter;

    @Override
    protected int bindLayout() {
        return R.layout.activity_maintain_apply_detail;
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
        mTvCenterTitle.setText("保养详情");
        mTvRightTitle.setVisibility(View.GONE);
        mTvRightTitle.setText("查看附件");
    }

    @Override
    protected void doBusiness() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getMaintainData(getIntent());
    }

    @Override
    public void initMaintainData(Maintain maintain) {
        tv_maintain_apply_detail_record_number.setText(maintain.getRecordNumber());
        tv_maintain_apply_detail_apply_name.setText(maintain.getApplyPersonName());
        if (maintain.getApplyDate() != null){
            if (maintain.getApplyDate().contains(" ")){
                String[] split = maintain.getApplyDate().split(" ");
                tv_maintain_apply_detail_apply_time.setText(split[0]);
            }else{
                tv_maintain_apply_detail_apply_time.setText(maintain.getApplyDate());
            }
        }
        tv_maintain_apply_detail_vehicle_name.setText(maintain.getVehicleName());
        tv_maintain_apply_detail_vehicle_license.setText(maintain.getVehicleMark());
        if (maintain.getPlanUpkeepCost() != null){
            tv_maintain_apply_detail_estimated_cost.setText(maintain.getPlanUpkeepCost().toString());
        }
        tv_maintain_apply_detail_maintain_status.setText(maintain.getUpkeepTransactStatusStr());
        tv_maintain_apply_detail_situation_description.setText(maintain.getUpkeepDetails());
        tv_maintain_apply_detail_handle_person.setText(maintain.getUpkeepTransactor());
        if (maintain.getActualUpkeepCost() != null){
            tv_maintain_apply_detail_maintain_cost.setText(maintain.getActualUpkeepCost().toString());
        }
        if (maintain.getActualUpkeepTime() != null){
            if (maintain.getActualUpkeepTime().contains(" ")){
                String[] split = maintain.getActualUpkeepTime().split(" ");
                tv_maintain_apply_detail_maintain_time.setText(split[0]);
            }else{
                tv_maintain_apply_detail_maintain_time.setText(maintain.getActualUpkeepTime());
            }
        }
        tv_maintain_apply_detail_cost_bearing.setText(maintain.getCostPayer());
        tv_maintain_apply_detail_vehicle_description.setText(maintain.getVehicleDescription());
        tv_maintain_apply_detail_remark.setText(maintain.getRemarks());

        if ("1".equals(maintain.getApprovalStatus())){
            ll_maintain_apply_detail_look.setVisibility(View.VISIBLE);
            btn_maintain_apply_detail_look.setVisibility(View.VISIBLE);
            btn_maintain_apply_detail_registration.setVisibility(View.GONE);
        }else if("2".equals(maintain.getApprovalStatus())){
            if ("0".equals(maintain.getUpkeepTransactStatus())){
                ll_maintain_apply_detail_look.setVisibility(View.VISIBLE);
                btn_maintain_apply_detail_look.setVisibility(View.GONE);
                btn_maintain_apply_detail_registration.setVisibility(View.VISIBLE);
            }else{
                ll_maintain_apply_detail_look.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void intoApprovalPage(Maintain maintain) {
        Intent intent = new Intent(this,MaintainApprovalActivity.class);
        intent.putExtra("source", BaseConfig.SOURCE_LOOK_DETAIL);
        intent.putExtra("positionFlag",BaseConfig.SIGN_ALREADY_DO);
        intent.putExtra("datumDataId", maintain.getUpkeepApplyId());
        intent.putExtra("datumType", maintain.getDatumType());
        startActivity(intent);
    }

    @Override
    public void intoRegistrationPage(Maintain maintain) {
        Intent intent = new Intent(this,MaintainRegistrationActivity.class);
        intent.putExtra("maintain", maintain);
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
