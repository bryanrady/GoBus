package com.hxd.gobus.mvp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Authority;
import com.hxd.gobus.bean.DatumDetail;
import com.hxd.gobus.bean.Maintain;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.config.VehicleConfig;
import com.hxd.gobus.mvp.adapter.ApprovalItemRecyclerAdapter;
import com.hxd.gobus.mvp.adapter.AttachLookRecyclerAdapter;
import com.hxd.gobus.mvp.contract.IMaintainApprovalContract;
import com.hxd.gobus.mvp.presenter.MaintainApprovalPresenter;
import com.hxd.gobus.views.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: wangqingbin
 * @date: 2019/9/20 17:16
 */

public class MaintainApprovalActivity extends BaseActivity<MaintainApprovalPresenter> implements IMaintainApprovalContract.View,
        AttachLookRecyclerAdapter.OnItemClickListener, ApprovalItemRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.civ_approval_apply_person_head)
    CircleImageView civ_approval_apply_person_head;

    @BindView(R.id.tv_approval_apply_person_name)
    TextView tv_approval_apply_person_name;

    @BindView(R.id.tv_approval_data_status)
    TextView tv_approval_data_status;

    @BindView(R.id.tv_approval_data_color_status)
    TextView tv_approval_data_color_status;


    @BindView(R.id.tv_maintain_approval_detail_record_number)
    TextView tv_maintain_approval_detail_record_number;

    @BindView(R.id.tv_maintain_approval_detail_apply_time)
    TextView tv_maintain_approval_detail_apply_time;

    @BindView(R.id.tv_maintain_approval_detail_vehicle_name)
    TextView tv_maintain_approval_detail_vehicle_name;

    @BindView(R.id.tv_maintain_approval_detail_vehicle_license)
    TextView tv_maintain_approval_detail_vehicle_license;

    @BindView(R.id.tv_maintain_approval_detail_estimated_cost)
    TextView tv_maintain_approval_detail_estimated_cost;

    @BindView(R.id.tv_maintain_approval_detail_maintain_status)
    TextView tv_maintain_approval_detail_maintain_status;

    @BindView(R.id.tv_maintain_approval_detail_situation_description)
    TextView tv_maintain_approval_detail_situation_description;

    @BindView(R.id.tv_maintain_approval_detail_handle_person)
    TextView tv_maintain_approval_detail_handle_person;

    @BindView(R.id.tv_maintain_approval_detail_maintain_cost)
    TextView tv_maintain_approval_detail_maintain_cost;

    @BindView(R.id.tv_maintain_approval_detail_maintain_time)
    TextView tv_maintain_approval_detail_maintain_time;

    @BindView(R.id.tv_maintain_approval_detail_cost_bearing)
    TextView tv_maintain_approval_detail_cost_bearing;

    @BindView(R.id.tv_maintain_approval_detail_vehicle_description)
    TextView tv_maintain_approval_detail_vehicle_description;

    @BindView(R.id.tv_maintain_approval_detail_remark)
    TextView tv_maintain_approval_detail_remark;

    @BindView(R.id.attach_recyclerView)
    RecyclerView mAttachRecyclerView;

    @BindView(R.id.approval_recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.ll_maintain_approval_audit)
    LinearLayout mLlAudit;

    @BindView(R.id.btn_maintain_approval_disagree)
    Button btn_maintain_approval_disagree;

    @BindView(R.id.btn_maintain_approval_agree)
    Button btn_maintain_approval_agree;

    @OnClick({R.id.ll_toolbar_back,R.id.tv_toolbar_right_title,R.id.btn_maintain_approval_disagree,R.id.btn_maintain_approval_agree})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.tv_toolbar_right_title:
                break;
            case R.id.btn_maintain_approval_disagree:
                mPresenter.permissionAuthority(VehicleConfig.APPROVAL_DISAGREE);
                break;
            case R.id.btn_maintain_approval_agree:
                mPresenter.permissionAuthority(VehicleConfig.APPROVAL_AGREE);
                break;
        }
    }

    private AttachLookRecyclerAdapter mAttachAdapter;

    @Override
    protected int bindLayout() {
        return R.layout.activity_maintain_approva_detaill;
    }

    @Override
    protected void initView() {
        initToolbar();

        mAttachRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAttachRecyclerView.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        //    mRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, R.drawable.recycleview_divider));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.VISIBLE);
        mTvCenterTitle.setText("保养审核");
        mTvRightTitle.setVisibility(View.GONE);
        mTvRightTitle.setText("查看附件");
    }

    @Override
    protected void doBusiness() {
        mPresenter.getIntentData(getIntent());
    }

    @Override
    public void initMaintainData(int source,String positionFlag,Maintain maintain) {
        //加载头像

        tv_approval_apply_person_name.setText(maintain.getApplyPersonName());
        String approvalStatus = maintain.getApprovalStatus();
        if (BaseConfig.SIGN_TO_DO.equals(positionFlag)){
            if (source == BaseConfig.SOURCE_TODO){
                if ("1".equals(approvalStatus)){
                    tv_approval_data_status.setText("审核中");
                    tv_approval_data_color_status.setText("审核中");
                    tv_approval_data_color_status.setTextColor(getResources().getColor(R.color.approval_in_progress));
                    tv_approval_data_color_status.setBackgroundResource(R.drawable.btn_frame_approval_in_progress);

                    mLlAudit.setVisibility(View.VISIBLE);
                }else if("2".equals(approvalStatus)){
                    tv_approval_data_status.setText("审核完成");
                    tv_approval_data_color_status.setText("审核完成");
                    tv_approval_data_color_status.setTextColor(getResources().getColor(R.color.approval_complete));
                    tv_approval_data_color_status.setBackgroundResource(R.drawable.btn_frame_approval_complete);

                    mLlAudit.setVisibility(View.GONE);
                }
            }
        }else{
            mLlAudit.setVisibility(View.GONE);
            if(source == BaseConfig.SOURCE_LOOK_DETAIL){
                if ("1".equals(approvalStatus)){
                    tv_approval_data_status.setText("审核中");
                    tv_approval_data_color_status.setText("审核中");
                    tv_approval_data_color_status.setTextColor(getResources().getColor(R.color.approval_in_progress));
                    tv_approval_data_color_status.setBackgroundResource(R.drawable.btn_frame_approval_in_progress);
                }else if("2".equals(approvalStatus)){
                    tv_approval_data_status.setText("审核完成");
                    tv_approval_data_color_status.setText("审核完成");
                    tv_approval_data_color_status.setTextColor(getResources().getColor(R.color.approval_complete));
                    tv_approval_data_color_status.setBackgroundResource(R.drawable.btn_frame_approval_complete);
                }
            }
        }

        tv_maintain_approval_detail_record_number.setText(maintain.getRecordNumber());
        if (maintain.getApplyDate() != null){
            if (maintain.getApplyDate().contains(" ")){
                String[] split = maintain.getApplyDate().split(" ");
                tv_maintain_approval_detail_apply_time.setText(split[0]);
            }else{
                tv_maintain_approval_detail_apply_time.setText(maintain.getApplyDate());
            }
        }
        tv_maintain_approval_detail_vehicle_name.setText(maintain.getVehicleName());
        tv_maintain_approval_detail_vehicle_license.setText(maintain.getVehicleMark());
        if (maintain.getPlanUpkeepCost() != null){
            tv_maintain_approval_detail_estimated_cost.setText(maintain.getPlanUpkeepCost().toString());
        }
        tv_maintain_approval_detail_maintain_status.setText(maintain.getUpkeepTransactStatusStr());
        tv_maintain_approval_detail_situation_description.setText(maintain.getUpkeepDetails());
        tv_maintain_approval_detail_handle_person.setText(maintain.getUpkeepTransactor());
        if (maintain.getActualUpkeepCost() != null){
            tv_maintain_approval_detail_maintain_cost.setText(maintain.getActualUpkeepCost().toString());
        }
        if (maintain.getActualUpkeepTime() != null){
            if (maintain.getActualUpkeepTime().contains(" ")){
                String[] split = maintain.getActualUpkeepTime().split(" ");
                tv_maintain_approval_detail_maintain_time.setText(split[0]);
            }else{
                tv_maintain_approval_detail_maintain_time.setText(maintain.getActualUpkeepTime());
            }
        }
        tv_maintain_approval_detail_cost_bearing.setText(maintain.getCostPayer());
        tv_maintain_approval_detail_vehicle_description.setText(maintain.getVehicleDescription());
        tv_maintain_approval_detail_remark.setText(maintain.getRemarks());

        if (maintain.getDatumDetailInfoList() != null && maintain.getDatumDetailInfoList().size() > 0){
            initRecyclerData(maintain.getDatumDetailInfoList());
        }
    }

    @Override
    public void intoApprovalSavePage(Authority authority,String approvalStatus) {
        Intent intent = new Intent(this,PublicApprovalSaveActivity.class);
        intent.putExtra("authority",authority);
        intent.putExtra("approvalStatus",approvalStatus);
        startActivityForResult(intent,VehicleConfig.REQUEST_CODE_INTO_PUBLIC_APPROVAL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case VehicleConfig.REQUEST_CODE_INTO_PUBLIC_APPROVAL:
                if (resultCode == RESULT_OK) {
                    mPresenter.queryMaintainApprovalData();
                    mPresenter.updateTodo();
                    if(data != null) {
                        Bundle bundle = data.getExtras();
                        if (bundle != null){
                            String approval_success = bundle.getString("approval_success");
                            if (VehicleConfig.APPROVAL_SUCCESS.equals(approval_success)){
                                mLlAudit.setVisibility(View.GONE);
                            }
                        }
                    }
                }
                break;
        }
    }

    private void initRecyclerData(List<DatumDetail> datumDetailList){
        ApprovalItemRecyclerAdapter adapter = new ApprovalItemRecyclerAdapter(R.layout.recycler_audit_step_item, datumDetailList);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showAttachList(List<Attach> list) {
        mAttachAdapter = new AttachLookRecyclerAdapter(this,list);
        mAttachAdapter.setOnItemClickListener(this);
        mAttachRecyclerView.setAdapter(mAttachAdapter);
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

    @Override
    public void onItemReminderClick(DatumDetail item) {
        mPresenter.reminder(item.getTitle(),item.getWorkNo());
    }

}
