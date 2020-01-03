package com.hxd.gobus.mvp.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import com.bryanrady.lib_permission.annotation.NeedPermission;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Driver;
import com.hxd.gobus.bean.KeyCode;
import com.hxd.gobus.bean.StringInfo;
import com.hxd.gobus.bean.User;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.bean.VehicleLicense;
import com.hxd.gobus.config.PictureSelectorConfig;
import com.hxd.gobus.config.VehicleConfig;
import com.hxd.gobus.mvp.adapter.AttachAddRecyclerAdapter;
import com.hxd.gobus.mvp.contract.IVehicleApplyContract;
import com.hxd.gobus.mvp.presenter.VehicleApplyPresenter;
import com.hxd.gobus.utils.DateUtil;
import com.hxd.gobus.utils.DateUtils;
import com.hxd.gobus.utils.FileUtil;
import com.hxd.gobus.views.BottomPopupDialog;
import com.hxd.gobus.views.ChooseDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用车申请页面
 * @author: wangqingbin
 * @date: 2019/8/15 12:04
 */

public class VehicleApplyActivity extends BaseActivity<VehicleApplyPresenter> implements IVehicleApplyContract.View,
        AttachAddRecyclerAdapter.OnItemClickListener,BottomPopupDialog.OnDialogListener{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.tv_vehicle_apply_name)
    TextView tv_vehicle_apply_name;

    @BindView(R.id.tv_vehicle_apply_time)
    TextView tv_vehicle_apply_time;

    @BindView(R.id.tv_vehicle_apply_unit)
    TextView tv_vehicle_apply_unit;

    @BindView(R.id.rl_vehicle_apply_destination)
    RelativeLayout rl_vehicle_apply_destination;

    @BindView(R.id.tv_vehicle_apply_destination)
    TextView tv_vehicle_apply_destination;

    @BindView(R.id.tv_vehicle_apply_destination_tip)
    TextView tv_vehicle_apply_destination_tip;

    @BindView(R.id.rl_vehicle_apply_vehicle_type)
    RelativeLayout rl_vehicle_apply_vehicle_type;

    @BindView(R.id.tv_vehicle_apply_vehicle_type)
    TextView tv_vehicle_apply_vehicle_type;

    @BindView(R.id.tv_vehicle_apply_vehicle_type_tip)
    TextView tv_vehicle_apply_vehicle_type_tip;

    @BindView(R.id.rl_vehicle_apply_vehicle_range)
    RelativeLayout rl_vehicle_apply_vehicle_range;

    @BindView(R.id.tv_vehicle_apply_vehicle_range)
    TextView tv_vehicle_apply_vehicle_range;

    @BindView(R.id.tv_vehicle_apply_vehicle_range_tip)
    TextView tv_vehicle_apply_vehicle_range_tip;

    @BindView(R.id.rl_vehicle_apply_predict_vehicle_time)
    RelativeLayout rl_vehicle_apply_predict_vehicle_time;

    @BindView(R.id.tv_vehicle_apply_predict_vehicle_time)
    TextView tv_vehicle_apply_predict_vehicle_time;

    @BindView(R.id.tv_vehicle_apply_predict_vehicle_time_tip)
    TextView tv_vehicle_apply_predict_vehicle_time_tip;

    @BindView(R.id.rl_vehicle_apply_designated_vehicle)
    RelativeLayout rl_vehicle_apply_designated_vehicle;

    @BindView(R.id.tv_vehicle_apply_designated_vehicle)
    TextView tv_vehicle_apply_designated_vehicle;

    @BindView(R.id.tv_vehicle_apply_designated_vehicle_tip)
    TextView tv_vehicle_apply_designated_vehicle_tip;

    @BindView(R.id.rl_vehicle_apply_predict_use_time)
    RelativeLayout rl_vehicle_apply_predict_use_time;

    @BindView(R.id.tv_vehicle_apply_predict_use_time)
    TextView tv_vehicle_apply_predict_use_time;

    @BindView(R.id.tv_vehicle_apply_predict_use_time_tip)
    TextView tv_vehicle_apply_predict_use_time_tip;

    @BindView(R.id.et_vehicle_apply_vehicle_reason)
    EditText et_vehicle_apply_vehicle_reason;

    @BindView(R.id.rl_vehicle_apply_designated_driver)
    RelativeLayout rl_vehicle_apply_designated_driver;

    @BindView(R.id.tv_vehicle_apply_designated_driver)
    TextView tv_vehicle_apply_designated_driver;

    @BindView(R.id.tv_vehicle_apply_designated_driver_tip)
    TextView tv_vehicle_apply_designated_driver_tip;

    @BindView(R.id.et_vehicle_apply_accompany_person)
    EditText et_vehicle_apply_accompany_person;

    @BindView(R.id.et_vehicle_apply_remark)
    EditText et_vehicle_apply_remark;

    @BindView(R.id.attach_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.btn_vehicle_apply_save)
    Button btn_vehicle_apply_save;

    @BindView(R.id.btn_vehicle_apply_commit)
    Button btn_vehicle_apply_commit;

    @OnClick({R.id.ll_toolbar_back,R.id.tv_toolbar_right_title,R.id.rl_vehicle_apply_destination, R.id.rl_vehicle_apply_vehicle_type,
            R.id.rl_vehicle_apply_vehicle_range, R.id.rl_vehicle_apply_predict_vehicle_time, R.id.rl_vehicle_apply_designated_vehicle,
            R.id.rl_vehicle_apply_predict_use_time, R.id.rl_vehicle_apply_designated_driver,R.id.btn_vehicle_apply_save,R.id.btn_vehicle_apply_commit})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.tv_toolbar_right_title:
                break;
            case R.id.rl_vehicle_apply_destination:
                startChooseLocation();
                break;
            case R.id.rl_vehicle_apply_vehicle_type:
                mPresenter.queryVehicleType();
                break;
            case R.id.rl_vehicle_apply_vehicle_range:
                mPresenter.queryVehicleRange();
                break;
            case R.id.rl_vehicle_apply_predict_vehicle_time:
                mPvTime.show();
                break;
            case R.id.rl_vehicle_apply_designated_vehicle:
                mPresenter.queryAvailableVehicle();
                break;
            case R.id.rl_vehicle_apply_predict_use_time:
                mPresenter.queryVehicleDuration();
                break;
            case R.id.rl_vehicle_apply_designated_driver:
                mPresenter.queryDesignatedDriver();
                break;
            case R.id.btn_vehicle_apply_save:
                checkAddParamsAndCommit("0");
                break;
            case R.id.btn_vehicle_apply_commit:
                checkAddParamsAndCommit("1");
                break;
        }
    }

    private String mVehicleType;
    private String mVehicleRange;
    private String mVehicleDuration;
    private Integer mAssignDriver;
    private Integer mDesignatedVehicle;
    private Vehicle mUpdateVehicle;
    private double mLatitude;
    private double mLongitude;
    private BottomPopupDialog mPopupDialog;
    private AttachAddRecyclerAdapter mAdapter;
    private List<Attach> mAttachList;
    private TimePickerView mPvTime;
    public static final int REQUEST_OPEN_SYSTEM_FOLDER = 10000;
    public static final int REQUEST_CHOOSE_DESTINATION = 10004;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPopupDialog != null && mPopupDialog.isShowing()){
            mPopupDialog.dismiss();
        }
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_vehicle_apply;
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
        mTvCenterTitle.setText("用车申请");
        mTvRightTitle.setVisibility(View.GONE);
        mTvRightTitle.setText("附件管理");
    }

    @Override
    protected void doBusiness() {
        tv_vehicle_apply_name.setText(User.getInstance().getName());
        tv_vehicle_apply_time.setText(DateUtils.getCurTimeFormat(DateUtils.FORMAT_3));
        tv_vehicle_apply_unit.setText(User.getInstance().getUnitName());
        mPresenter.getVehicleData(getIntent());
    }

    @Override
    public void initVehicleData(Vehicle vehicle) {
        mUpdateVehicle = vehicle;

        mVehicleType = vehicle.getUseCarType();
        mVehicleRange = vehicle.getUseCarRange();
        mDesignatedVehicle = vehicle.getInfoManageId();
        mVehicleDuration = vehicle.getPredictDuration();
        mAssignDriver = vehicle.getAssignDriver();

        tv_vehicle_apply_name.setText(vehicle.getApplyPersonName());
        if (vehicle.getApplyDate() != null){
            if (vehicle.getApplyDate().contains(" ")){
                String[] split = vehicle.getApplyDate().split(" ");
                tv_vehicle_apply_time.setText(split[0]);
            }else{
                tv_vehicle_apply_time.setText(vehicle.getApplyDate());
            }
        }
        tv_vehicle_apply_unit.setText(vehicle.getApplyUnitName());

        if (vehicle.getDestination() != null){
            tv_vehicle_apply_destination.setText(vehicle.getDestination());
            tv_vehicle_apply_destination_tip.setText("");
        }
        if (vehicle.getUseCarType() != null){
            tv_vehicle_apply_vehicle_type.setText(vehicle.getUseCarTypeName());
            tv_vehicle_apply_vehicle_type_tip.setText("");
        }
        if (vehicle.getUseCarRange() != null){
            tv_vehicle_apply_vehicle_range.setText(vehicle.getUseCarRangeName());
            tv_vehicle_apply_vehicle_range_tip.setText("");
        }
        if (vehicle.getPredictStartDate() != null){
            tv_vehicle_apply_predict_vehicle_time.setText(vehicle.getPredictStartDate());
            tv_vehicle_apply_predict_vehicle_time_tip.setText("");
        }
        if (vehicle.getInfoManageId() != null){
            tv_vehicle_apply_designated_vehicle.setText(vehicle.getCarNumber());
            tv_vehicle_apply_designated_vehicle_tip.setText("");
        }
        if (vehicle.getPredictDuration() != null){
            tv_vehicle_apply_predict_use_time.setText(vehicle.getPredictDurationName());
            tv_vehicle_apply_predict_use_time_tip.setText("");
        }
        et_vehicle_apply_vehicle_reason.setText(vehicle.getUseReason());
        if (vehicle.getAssignDriver() != null){
            tv_vehicle_apply_designated_driver.setText(vehicle.getAssignDriverName());
            tv_vehicle_apply_designated_driver_tip.setText("");
        }
        et_vehicle_apply_accompany_person.setText(vehicle.getTravelPartner());
        et_vehicle_apply_remark.setText(vehicle.getRemareks());
    }

    @NeedPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private void startChooseLocation(){
        Intent intent = new Intent(this,ChooseLocationActivity.class);
        startActivityForResult(intent,REQUEST_CHOOSE_DESTINATION);
    }

    @Override
    public void showVehicleTypeDialog(List<KeyCode> list) {
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
                        tv_vehicle_apply_vehicle_type.setText(stringInfo.getTitle());
                        tv_vehicle_apply_vehicle_type_tip.setText("");
                        mVehicleType = stringInfo.getCodeType();
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
    public void showVehicleRangeDialog(List<KeyCode> list) {
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
                        tv_vehicle_apply_vehicle_range.setText(stringInfo.getTitle());
                        tv_vehicle_apply_vehicle_range_tip.setText("");
                        mVehicleRange = stringInfo.getCodeType();
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
                        tv_vehicle_apply_predict_use_time.setText(stringInfo.getTitle());
                        tv_vehicle_apply_predict_use_time_tip.setText("");
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
                        tv_vehicle_apply_designated_vehicle.setText(stringInfo.getTitle());
                        tv_vehicle_apply_designated_vehicle_tip.setText("");
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
                        tv_vehicle_apply_designated_driver.setText(stringInfo.getTitle());
                        tv_vehicle_apply_designated_driver_tip.setText("");
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
    public void saveCommitSuccess() {
        defaultFinish();
    }

    private void initTimePicker() {//Dialog 模式下，在底部弹出
        mPvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String time = DateUtil.strByDate(date, DateUtil.FORMAT_1);
                tv_vehicle_apply_predict_vehicle_time.setText(time);
                tv_vehicle_apply_predict_vehicle_time_tip.setText("");
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

    private void checkAddParamsAndCommit(String approvalStatus){
        String applyTime = tv_vehicle_apply_time.getText().toString().trim();
        String destinationPlace = tv_vehicle_apply_destination.getText().toString().trim();
        if (TextUtils.isEmpty(destinationPlace)){
            showShortToast("目的地不能为空!");
            return;
        }
        if (TextUtils.isEmpty(mVehicleType)){
            showShortToast("用车类型不能为空!");
            return;
        }
//        if (TextUtils.isEmpty(mVehicleRange)){
//            showShortToast("用车范围不能为空!");
//            return;
//        }
        String predictUseTime = tv_vehicle_apply_predict_vehicle_time.getText().toString().trim();
        if (TextUtils.isEmpty(predictUseTime)){
            showShortToast("预计用车时间不能为空!");
            return;
        }
        if (mDesignatedVehicle == null){
            showShortToast("指定车辆不能为空!");
            return;
        }
        if (TextUtils.isEmpty(mVehicleDuration)){
            showShortToast("预计用时不能为空!");
            return;
        }
        String vehicleReason = et_vehicle_apply_vehicle_reason.getText().toString().trim();
        if (TextUtils.isEmpty(vehicleReason)){
            showShortToast("用车事由不能为空!");
            return;
        }
        String accompanyPerson = et_vehicle_apply_accompany_person.getText().toString().trim();
        String remark = et_vehicle_apply_remark.getText().toString().trim();

        String dataStatus = mPresenter.getDataStatus();
        if (dataStatus != null){
            if (VehicleConfig.APPLY_ADD.equals(dataStatus)){
                Vehicle vehicle = new Vehicle();
                if (mAttachList != null && mAttachList.size() > 0){
                    if (!TextUtils.isEmpty(mPresenter.getDataId())){
                        vehicle.setUuid(mPresenter.getDataId());
                    }
                }
                vehicle.setApplyPerson(User.getInstance().getPersonId());
                vehicle.setApplyDate(applyTime);
                vehicle.setApplyUnitId(User.getInstance().getUnitId());
                vehicle.setDestination(destinationPlace);
                if (mLongitude != 0d && mLatitude != 0d){
                    vehicle.setLat(new BigDecimal(mLatitude));
                    vehicle.setLng(new BigDecimal(mLongitude));
                }
                vehicle.setUseCarType(mVehicleType);
            //    vehicle.setUseCarRange(mVehicleRange);
                vehicle.setPredictStartDate(predictUseTime);
                vehicle.setInfoManageId(mDesignatedVehicle);
                vehicle.setPredictDuration(mVehicleDuration);
                vehicle.setUseReason(vehicleReason);
                if (mAssignDriver != null){
                    vehicle.setAssignDriver(mAssignDriver);
                }
                if (!TextUtils.isEmpty(accompanyPerson)){
                    vehicle.setTravelPartner(accompanyPerson);
                }
                if (!TextUtils.isEmpty(remark)){
                    vehicle.setRemareks(remark);
                }
                vehicle.setApprovalStatus(approvalStatus);
                mPresenter.addVehicleApply(vehicle);
            }else{
                if(mUpdateVehicle != null){
                    mUpdateVehicle.setApplyPerson(User.getInstance().getPersonId());
                    mUpdateVehicle.setApplyDate(applyTime);
                    mUpdateVehicle.setApplyUnitId(User.getInstance().getUnitId());
                    mUpdateVehicle.setDestination(destinationPlace);
                    if (mLongitude != 0d && mLatitude != 0d){
                        mUpdateVehicle.setLat(new BigDecimal(mLatitude));
                        mUpdateVehicle.setLng(new BigDecimal(mLongitude));
                    }
                    mUpdateVehicle.setUseCarType(mVehicleType);
                //    mUpdateVehicle.setUseCarRange(mVehicleRange);
                    mUpdateVehicle.setPredictStartDate(predictUseTime);
                    mUpdateVehicle.setInfoManageId(mDesignatedVehicle);
                    mUpdateVehicle.setPredictDuration(mVehicleDuration);
                    mUpdateVehicle.setUseReason(vehicleReason);
                    if (mAssignDriver != null){
                        mUpdateVehicle.setAssignDriver(mAssignDriver);
                    }
                    if (!TextUtils.isEmpty(accompanyPerson)){
                        mUpdateVehicle.setTravelPartner(accompanyPerson);
                    }
                    if (!TextUtils.isEmpty(remark)){
                        mUpdateVehicle.setRemareks(remark);
                    }
                    mUpdateVehicle.setApprovalStatus(approvalStatus);

                    mPresenter.updateVehicleApply(mUpdateVehicle);
                }
            }
        }
    }

    @Override
    public void showAttachList(List<Attach> list) {
        mAttachList = list;
        mAdapter = new AttachAddRecyclerAdapter(this,list);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemDelete(int position, Attach item) {
        mPresenter.deleteAttach(item.getAttachId(),position);
    }

    @Override
    public void onItemAdd() {
        mPopupDialog = new BottomPopupDialog(this);
        mPopupDialog.setOnDialogListener(this);
        mPopupDialog.show();
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
    public void onDialogClick(int type) {
        switch (type){
            case BottomPopupDialog.TYPE_SHOOTING:
                openCamera();
                break;
            case BottomPopupDialog.TYPE_FROM_ALBUM:
                openAlbum();
                break;
            case BottomPopupDialog.TYPE_FROM_FILE:
                openSystemFolder();
                break;
        }
    }

    @NeedPermission({Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void openCamera(){
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @NeedPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
    private void openAlbum(){
        PictureSelectorConfig.initSingleConfig(this);
    }

    @NeedPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
    private void openSystemFolder(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult( intent,REQUEST_OPEN_SYSTEM_FOLDER);
        } catch (android.content.ActivityNotFoundException ex) {
            showShortToast("Please install a File Manager.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    images = PictureSelector.obtainMultipleResult(data);
                    String localMediaPath;
                    if (images != null){
                        for (LocalMedia localMedia : images){
                            localMediaPath = localMedia.getPath();
                            if (!TextUtils.isEmpty(localMediaPath)){
                                if(!FileUtil.isCanUpload(localMediaPath)){
                                    showShortToast("上传的文件大小不能超过10M");
                                }else{
                                    mPresenter.uploadAttach(localMediaPath);
                                }
                            }else{
                                showShortToast("文件路径找不到!");
                                return;
                            }
                        }
                    }
//                    selectList.addAll(images);
//
////                    selectList = PictureSelector.obtainMultipleResult(data);
//                    // 例如 LocalMedia 里面返回三种path
//                    // 1.media.getPath(); 为原图path
//                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
//                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
//                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
//                    adapter.setList(selectList);
//                    adapter.notifyDataSetChanged();
                    break;
                case REQUEST_OPEN_SYSTEM_FOLDER:
                    Uri uri = data.getData();
                    String localFilePath;
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                        localFilePath = FileUtil.getPath(this, uri);
                    } else {//4.4以下下系统调用方法
                        localFilePath = FileUtil.getRealPathFromURI(this, uri);
                    }
                    if(TextUtils.isEmpty(localFilePath)){
                        showShortToast("文件路径找不到!");
                        return;
                    }
                    final File file = new File(localFilePath);
                    if (!file.exists()) {
                        showShortToast("权限拒绝或找不到文件!");
                        return;
                    }
                    if(!FileUtil.isCanUpload(localFilePath)){
                        showShortToast("上传的文件大小不能超过10M");
                        return;
                    }else{
                        mPresenter.uploadAttach(localFilePath);
                    }
                    break;
                case REQUEST_CHOOSE_DESTINATION:
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        mLatitude = bundle.getDouble("latitude");
                        mLongitude = bundle.getDouble("longitude");
                        String address = bundle.getString("address");
                        tv_vehicle_apply_destination.setText(address);
                        tv_vehicle_apply_destination_tip.setText("");
                    }
                    break;

            }
        }
    }

}
