package com.hxd.gobus.mvp.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Driver;
import com.hxd.gobus.bean.KeyCode;
import com.hxd.gobus.bean.StringInfo;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.bean.VehicleLicense;
import com.hxd.gobus.config.VehicleConfig;
import com.hxd.gobus.mvp.adapter.AttachLookRecyclerAdapter;
import com.hxd.gobus.mvp.contract.IVehicleApprovalSaveContract;
import com.hxd.gobus.mvp.presenter.VehicleApprovalSavePresenter;
import com.hxd.gobus.utils.DateUtil;
import com.hxd.gobus.utils.DateUtils;
import com.hxd.gobus.views.ChooseDialog;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: wangqingbin
 * @date: 2019/9/17 17:28
 */

public class VehicleApprovalSaveActivity extends BaseActivity<VehicleApprovalSavePresenter> implements
        IVehicleApprovalSaveContract.View,AttachLookRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.tv_vehicle_apply_approval_type)
    TextView tv_vehicle_apply_approval_type;

    @BindView(R.id.tv_vehicle_apply_approval_time)
    TextView tv_vehicle_apply_approval_time;

    @BindView(R.id.tv_vehicle_apply_approval_apply_unit)
    TextView tv_vehicle_apply_approval_apply_unit;

    @BindView(R.id.tv_vehicle_apply_approval_apply_name)
    TextView tv_vehicle_apply_approval_apply_name;

    @BindView(R.id.tv_vehicle_apply_approval_apply_time)
    TextView tv_vehicle_apply_approval_apply_time;

    @BindView(R.id.tv_vehicle_apply_approval_destination)
    TextView tv_vehicle_apply_approval_destination;

    @BindView(R.id.tv_vehicle_apply_approval_vehicle_reason)
    TextView tv_vehicle_apply_approval_vehicle_reason;

    @BindView(R.id.tv_vehicle_apply_approval_vehicle_type)
    TextView tv_vehicle_apply_approval_vehicle_type;

    @BindView(R.id.rl_vehicle_apply_approval_designated_vehicle)
    RelativeLayout rl_vehicle_apply_approval_designated_vehicle;

    @BindView(R.id.tv_vehicle_apply_approval_designated_vehicle)
    TextView tv_vehicle_apply_approval_designated_vehicle;

    @BindView(R.id.tv_vehicle_apply_approval_designated_vehicle_tip)
    TextView tv_vehicle_apply_approval_designated_vehicle_tip;

    @BindView(R.id.rl_vehicle_apply_approval_designated_driver)
    RelativeLayout rl_vehicle_apply_approval_designated_driver;

    @BindView(R.id.tv_vehicle_apply_approval_designated_driver)
    TextView tv_vehicle_apply_approval_designated_driver;

    @BindView(R.id.tv_vehicle_apply_approval_designated_driver_tip)
    TextView tv_vehicle_apply_approval_designated_driver_tip;

    @BindView(R.id.tv_vehicle_apply_approval_accompany_person)
    TextView tv_vehicle_apply_approval_accompany_person;

    @BindView(R.id.rl_vehicle_apply_approval_predict_vehicle_time)
    RelativeLayout rl_vehicle_apply_approval_predict_vehicle_time;

    @BindView(R.id.tv_vehicle_apply_approval_predict_vehicle_time)
    TextView tv_vehicle_apply_approval_predict_vehicle_time;

    @BindView(R.id.rl_vehicle_apply_approval_predict_use_time)
    RelativeLayout rl_vehicle_apply_approval_predict_use_time;

    @BindView(R.id.tv_vehicle_apply_approval_predict_use_time)
    TextView tv_vehicle_apply_approval_predict_use_time;

    @BindView(R.id.et_vehicle_apply_approval_remark)
    EditText et_vehicle_apply_approval_remark;

    @BindView(R.id.ll_attach)
    LinearLayout ll_attach;

    @BindView(R.id.attach_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_vehicle_apply_approval_status)
    TextView tv_vehicle_apply_approval_status;

    @BindView(R.id.et_vehicle_apply_approval_advice)
    EditText et_vehicle_apply_approval_advice;

    @BindView(R.id.btn_vehicle_apply_approval_save)
    Button btn_vehicle_apply_approval_save;

    @OnClick({R.id.ll_toolbar_back,R.id.tv_toolbar_right_title,R.id.rl_vehicle_apply_approval_designated_vehicle,
            R.id.rl_vehicle_apply_approval_designated_driver,R.id.rl_vehicle_apply_approval_predict_vehicle_time,
            R.id.rl_vehicle_apply_approval_predict_use_time, R.id.btn_vehicle_apply_approval_save})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.tv_toolbar_right_title:
                break;
            case R.id.rl_vehicle_apply_approval_designated_vehicle:
                mPresenter.queryAvailableVehicle();
                break;
            case R.id.rl_vehicle_apply_approval_designated_driver:
                mPresenter.queryDesignatedDriver();
                break;
            case R.id.rl_vehicle_apply_approval_predict_vehicle_time:
                mPvTime.show();
                break;
            case R.id.rl_vehicle_apply_approval_predict_use_time:
                mPresenter.queryVehicleDuration();
                break;
            case R.id.btn_vehicle_apply_approval_save:
                String predictVehicleTime = tv_vehicle_apply_approval_predict_vehicle_time.getText().toString().trim();
                String remark = et_vehicle_apply_approval_remark.getText().toString().trim();
                String approvalAdvice = et_vehicle_apply_approval_advice.getText().toString().trim();
                if (TextUtils.isEmpty(approvalAdvice)){
                    showShortToast("审核意见不能为空!");
                    return;
                }
                if (mUpdateVehicle != null){
                    if (mDesignatedVehicle != null){
                        mUpdateVehicle.setInfoManageId(mDesignatedVehicle);
                    }
                    if (mAssignDriver != null){
                        mUpdateVehicle.setAssignDriver(mAssignDriver);
                    }
                    if (!TextUtils.isEmpty(predictVehicleTime)){
                        mUpdateVehicle.setPredictStartDate(predictVehicleTime);
                    }
                    if (mVehicleDuration != null){
                        mUpdateVehicle.setPredictDuration(mVehicleDuration);
                    }
                    if (!TextUtils.isEmpty(remark)){
                        mUpdateVehicle.setRemareks(remark);
                    }
                    mPresenter.saveApprovalInfo(mUpdateVehicle,mStatus,approvalAdvice);
                }

                break;
        }
    }

    private Vehicle mUpdateVehicle;
    private Integer mDesignatedVehicle;
    private Integer mAssignDriver;
    private String mVehicleDuration;
    private String mStatus;
    private AttachLookRecyclerAdapter mAdapter;
    private TimePickerView mPvTime;

    @Override
    protected int bindLayout() {
        return R.layout.activity_vehicle_approval_save;
    }

    @Override
    protected void initView() {
        initToolbar();

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        initTimePicker();
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.VISIBLE);
        mTvCenterTitle.setText("用车审核");
        mTvRightTitle.setVisibility(View.GONE);
        mTvRightTitle.setText("附件管理");
    }

    private void initTimePicker() {//Dialog 模式下，在底部弹出
        mPvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String time = DateUtil.strByDate(date, DateUtil.FORMAT_1);
                tv_vehicle_apply_approval_predict_vehicle_time.setText(time);
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {
                Log.i("pvTime", "onTimeSelectChanged");
            }
        })
                .setType(new boolean[]{true, true, true, true, true, true})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                })
                .setItemVisibleCount(5) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .build();

        Dialog mDialog = mPvTime.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);
            params.leftMargin = 0;
            params.rightMargin = 0;
            mPvTime.getDialogContainerLayout().setLayoutParams(params);
            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
    }

    @Override
    protected void doBusiness() {
        mPresenter.getVehicleApprovalData(getIntent());
    }

    @Override
    public void initVehicleData(Vehicle vehicle,String approvalStatus) {
        mUpdateVehicle = vehicle;
        mDesignatedVehicle = vehicle.getInfoManageId();
        mAssignDriver = vehicle.getAssignDriver();
        mVehicleDuration = vehicle.getPredictDuration();
        mStatus = approvalStatus;

        tv_vehicle_apply_approval_type.setText("用车管理审核");
        tv_vehicle_apply_approval_time.setText(DateUtils.getCurTimeFormat(DateUtils.FORMAT_1));
        tv_vehicle_apply_approval_apply_name.setText(vehicle.getApplyPersonName());
        if (vehicle.getApplyDate() != null){
            if (vehicle.getApplyDate().contains(" ")){
                String[] split = vehicle.getApplyDate().split(" ");
                tv_vehicle_apply_approval_apply_time.setText(split[0]);
            }else{
                tv_vehicle_apply_approval_apply_time.setText(vehicle.getApplyDate());
            }
        }
        tv_vehicle_apply_approval_apply_unit.setText(vehicle.getApplyUnitName());
        tv_vehicle_apply_approval_destination.setText(vehicle.getDestination());
        tv_vehicle_apply_approval_vehicle_reason.setText(vehicle.getUseReason());
        tv_vehicle_apply_approval_vehicle_type.setText(vehicle.getUseCarTypeName());
        if (vehicle.getInfoManageId() != null){
            tv_vehicle_apply_approval_designated_vehicle.setText(vehicle.getCarNumber());
            tv_vehicle_apply_approval_designated_vehicle_tip.setText("");
        }
        if (vehicle.getAssignDriverName() != null){
            tv_vehicle_apply_approval_designated_driver.setText(vehicle.getAssignDriverName());
            tv_vehicle_apply_approval_designated_driver_tip.setText("");
        }
        tv_vehicle_apply_approval_accompany_person.setText(vehicle.getTravelPartner());
        tv_vehicle_apply_approval_predict_vehicle_time.setText(vehicle.getPredictStartDate());
        tv_vehicle_apply_approval_predict_use_time.setText(vehicle.getPredictDurationName());
        et_vehicle_apply_approval_remark.setText(vehicle.getRemareks());

        if (VehicleConfig.APPROVAL_DISAGREE.equals(mStatus)){
            tv_vehicle_apply_approval_status.setText("退回");
            tv_vehicle_apply_approval_status.setTextColor(getResources().getColor(R.color.approval_not_through));
            et_vehicle_apply_approval_advice.setText("不同意!");
        }else if(VehicleConfig.APPROVAL_AGREE.equals(mStatus)){
            tv_vehicle_apply_approval_status.setText("通过");
            tv_vehicle_apply_approval_status.setTextColor(getResources().getColor(R.color.approval_complete));
            et_vehicle_apply_approval_advice.setText("同意!");
        }

    }

    @Override
    public void showVehicleDurationDialog(List<KeyCode> list) {
        List<StringInfo> stringInfoList = mPresenter.transferKeyCodeList(list);
        if (stringInfoList != null && stringInfoList.size() > 0){
            ChooseDialog.Builder builder = new ChooseDialog.Builder(this,stringInfoList);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setOnDataChooseDialogListener(new ChooseDialog.Builder.OnChooseDialogListener() {

                @Override
                public String onChoose(List<StringInfo> list) {
                    for(StringInfo stringInfo : list){
                        tv_vehicle_apply_approval_predict_use_time.setText(stringInfo.getTitle());
                        mVehicleDuration = stringInfo.getCodeType();
                    }
                    return null;
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    @Override
    public void showVehicleLicenseDialog(List<VehicleLicense> list) {
        List<StringInfo> stringInfoList = mPresenter.transferLicenseList(list);
        if (stringInfoList != null && stringInfoList.size() > 0){
            ChooseDialog.Builder builder = new ChooseDialog.Builder(this,stringInfoList);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setOnDataChooseDialogListener(new ChooseDialog.Builder.OnChooseDialogListener() {

                @Override
                public String onChoose(List<StringInfo> list) {
                    for(StringInfo stringInfo : list){
                        tv_vehicle_apply_approval_designated_vehicle.setText(stringInfo.getTitle());
                        tv_vehicle_apply_approval_designated_vehicle_tip.setText("");
                        mDesignatedVehicle = stringInfo.getId();
                    }
                    return null;
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    @Override
    public void showDesignatedDriverDialog(List<Driver> list) {
        List<StringInfo> stringInfoList = mPresenter.transferDriverList(list);
        if (stringInfoList != null && stringInfoList.size() > 0){
            ChooseDialog.Builder builder = new ChooseDialog.Builder(this,stringInfoList);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setOnDataChooseDialogListener(new ChooseDialog.Builder.OnChooseDialogListener() {

                @Override
                public String onChoose(List<StringInfo> list) {
                    for(StringInfo stringInfo : list){
                        tv_vehicle_apply_approval_designated_driver.setText(stringInfo.getTitle());
                        tv_vehicle_apply_approval_designated_driver_tip.setText("");
                        mAssignDriver = stringInfo.getId();
                    }
                    return null;
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    @Override
    public void saveApprovalSuccess() {
        Intent intent = new Intent();
        intent.putExtra("approval_success",VehicleConfig.APPROVAL_SUCCESS);
        setResult(RESULT_OK,intent);
        defaultFinish();
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
