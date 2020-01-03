package com.hxd.gobus.mvp.activity;

import android.app.Dialog;
import android.content.DialogInterface;
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
import com.hxd.gobus.bean.KeyCode;
import com.hxd.gobus.bean.Maintain;
import com.hxd.gobus.bean.StringInfo;
import com.hxd.gobus.mvp.contract.IMaintainRegistrationContract;
import com.hxd.gobus.mvp.presenter.MaintainRegistrationPresenter;
import com.hxd.gobus.utils.DateUtil;
import com.hxd.gobus.utils.NumberValidationUtils;
import com.hxd.gobus.views.ChooseDialog;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: wangqingbin
 * @date: 2019/9/21 10:59
 */

public class MaintainRegistrationActivity extends BaseActivity<MaintainRegistrationPresenter> implements IMaintainRegistrationContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.tv_maintain_registration_record_number)
    TextView tv_maintain_registration_record_number;

    @BindView(R.id.tv_maintain_registration_apply_name)
    TextView tv_maintain_registration_apply_name;

    @BindView(R.id.tv_maintain_registration_apply_time)
    TextView tv_maintain_registration_apply_time;

    @BindView(R.id.tv_maintain_registration_vehicle_name)
    TextView tv_maintain_registration_vehicle_name;

    @BindView(R.id.tv_maintain_registration_vehicle_license)
    TextView tv_maintain_registration_vehicle_license;

    @BindView(R.id.tv_maintain_registration_estimated_cost)
    TextView tv_maintain_registration_estimated_cost;

    @BindView(R.id.rl_maintain_registration_maintain_status)
    RelativeLayout rl_maintain_registration_maintain_status;

    @BindView(R.id.tv_maintain_registration_maintain_status)
    TextView tv_maintain_registration_maintain_status;

    @BindView(R.id.et_maintain_registration_maintain_content)
    EditText et_maintain_registration_maintain_content;

    @BindView(R.id.et_maintain_registration_handle_person)
    EditText et_maintain_registration_handle_person;

    @BindView(R.id.et_maintain_registration_actual_cost)
    EditText et_maintain_registration_actual_cost;

    @BindView(R.id.rl_maintain_registration_actual_maintain_time)
    RelativeLayout rl_maintain_registration_actual_maintain_time;

    @BindView(R.id.tv_maintain_registration_actual_maintain_time)
    TextView tv_maintain_registration_actual_maintain_time;

    @BindView(R.id.tv_maintain_registration_actual_maintain_time_tip)
    TextView tv_maintain_registration_actual_maintain_time_tip;

    @BindView(R.id.et_maintain_registration_cost_bearing)
    EditText et_maintain_registration_cost_bearing;

    @BindView(R.id.et_maintain_registration_remark)
    EditText et_maintain_registration_remark;

    @BindView(R.id.btn_maintain_registration_save)
    Button btn_maintain_registration_save;

    @OnClick({R.id.ll_toolbar_back,R.id.rl_maintain_registration_maintain_status,R.id.rl_maintain_registration_actual_maintain_time, R.id.btn_maintain_registration_save})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.rl_maintain_registration_maintain_status:
                mPresenter.queryMaintainStatus();
                break;
            case R.id.rl_maintain_registration_actual_maintain_time:
                mPvTime.show();
                break;
            case R.id.btn_maintain_registration_save:
                checkSaveParams();
                break;
        }
    }

    private String mMaintainStatus;
    private Maintain mUpdateMaintain;
    private TimePickerView mPvTime;

    @Override
    protected int bindLayout() {
        return R.layout.activity_maintain_registration;
    }

    @Override
    protected void initView() {
        initToolbar();

        initTimePicker();
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.VISIBLE);
        mTvCenterTitle.setText("保养登记");
        mTvRightTitle.setVisibility(View.GONE);
    }

    @Override
    protected void doBusiness() {
        mPresenter.getIntentData(getIntent());
    }

    @Override
    public void initMaintainData(Maintain maintain) {
        mUpdateMaintain = maintain;

        tv_maintain_registration_record_number.setText(maintain.getRecordNumber());
        tv_maintain_registration_apply_name.setText(maintain.getApplyPersonName());
        if (maintain.getApplyDate() != null){
            if (maintain.getApplyDate().contains(" ")){
                String[] split = maintain.getApplyDate().split(" ");
                tv_maintain_registration_apply_time.setText(split[0]);
            }else{
                tv_maintain_registration_apply_time.setText(maintain.getApplyDate());
            }
        }
        tv_maintain_registration_vehicle_name.setText(maintain.getVehicleName());
        tv_maintain_registration_vehicle_license.setText(maintain.getVehicleMark());
        if (maintain.getPlanUpkeepCost() != null){
            tv_maintain_registration_estimated_cost.setText(maintain.getPlanUpkeepCost().toString());
        }
        mMaintainStatus = "0";
        tv_maintain_registration_maintain_status.setText("未处理");
        et_maintain_registration_maintain_content.setText(maintain.getUpkeepDetails());
        et_maintain_registration_handle_person.setText(maintain.getUpkeepTransactor());
        if (maintain.getActualUpkeepCost() != null){
            et_maintain_registration_actual_cost.setText(maintain.getActualUpkeepCost().toString());
        }
        if (maintain.getActualUpkeepTime() != null){
            if (maintain.getActualUpkeepTime().contains(" ")){
                String[] split = maintain.getActualUpkeepTime().split(" ");
                tv_maintain_registration_actual_maintain_time.setText(split[0]);
            }else{
                tv_maintain_registration_actual_maintain_time.setText(maintain.getActualUpkeepTime());
            }
        }
        et_maintain_registration_cost_bearing.setText(maintain.getCostPayer());
        et_maintain_registration_remark.setText(maintain.getRemarks());
    }

    @Override
    public void showMaintainStatusDialog(List<KeyCode> list) {
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
                        tv_maintain_registration_maintain_status.setText(stringInfo.getTitle());
                        mMaintainStatus = stringInfo.getCodeType();
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
    public void saveMaintainRegistrationSuccess() {
        defaultFinish();
    }

    private void checkSaveParams(){
        if (mMaintainStatus == null){
            showShortToast("处理状态不能为空!");
            return;
        }
        String content = et_maintain_registration_maintain_content.getText().toString().trim();
        String handle = et_maintain_registration_handle_person.getText().toString().trim();
        String actualCost = et_maintain_registration_actual_cost.getText().toString().trim();
        if (!TextUtils.isEmpty(actualCost)){
            if(NumberValidationUtils.isNumeric(actualCost)){
                if(actualCost.length() > 9 || actualCost.length() < 1){
                    showShortToast("本次实际费用整数部分必须是1-9位的数!");
                    return;
                }
            }else if(NumberValidationUtils.isDecimal(actualCost)){
                String s1 = actualCost.substring(0, actualCost.indexOf("."));
                String s2 = actualCost.substring(actualCost.indexOf(".")+1, actualCost.length());
                if(s1.length() > 6 || s1.length() < 1){
                    showShortToast("本次实际费用整数部分必须是1-6位的数!");
                    return;
                }
                if(s2.length() > 2 || s2.length() < 1){
                    showShortToast("本次实际费用小数部分必须是1-2位的数!");
                    return;
                }
            }else{
                showShortToast("请输入正确的金额");
                return;
            }
        }
        String maintainTime = tv_maintain_registration_actual_maintain_time.getText().toString().trim();
        String costBearing = et_maintain_registration_cost_bearing.getText().toString().trim();
        String remark = et_maintain_registration_remark.getText().toString().trim();

        if(mUpdateMaintain != null){
            mUpdateMaintain.setUpkeepTransactStatus(mMaintainStatus);
            if (!TextUtils.isEmpty(content)){
                mUpdateMaintain.setUpkeepDetails(content);
            }
            if (!TextUtils.isEmpty(handle)){
                mUpdateMaintain.setUpkeepTransactor(handle);
            }
            if (!TextUtils.isEmpty(actualCost)){
                mUpdateMaintain.setActualUpkeepCost(new BigDecimal(actualCost));
            }
            if (!TextUtils.isEmpty(maintainTime)){
                mUpdateMaintain.setActualUpkeepTime(maintainTime);
            }
            if (!TextUtils.isEmpty(costBearing)){
                mUpdateMaintain.setCostPayer(costBearing);
            }
            if (!TextUtils.isEmpty(remark)){
                mUpdateMaintain.setRemarks(remark);
            }
            mUpdateMaintain.setUpkeepTransactStatus("1");
            mPresenter.updateMaintainRegistration(mUpdateMaintain);
        }

    }

    private void initTimePicker() {//Dialog 模式下，在底部弹出
        mPvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String time = DateUtil.strByDate(date, DateUtil.FORMAT_3);
                tv_maintain_registration_actual_maintain_time.setText(time);
                tv_maintain_registration_actual_maintain_time_tip.setText("");
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {
                Log.i("pvTime", "onTimeSelectChanged");
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
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

}
