package com.hxd.gobus.mvp.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bryanrady.lib_permission.annotation.NeedPermission;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseLazyFragment;
import com.hxd.gobus.bean.StringInfo;
import com.hxd.gobus.bean.User;
import com.hxd.gobus.mvp.activity.MaintainRecordActivity;
import com.hxd.gobus.mvp.activity.PersonalDataActivity;
import com.hxd.gobus.mvp.activity.RepairRecordActivity;
import com.hxd.gobus.mvp.activity.SettingActivity;
import com.hxd.gobus.mvp.activity.VehicleRecordActivity;
import com.hxd.gobus.mvp.contract.IMyContract;
import com.hxd.gobus.mvp.presenter.MyPresenter;
import com.hxd.gobus.utils.ToastUtils;
import com.hxd.gobus.views.ChooseDialog;
import com.hxd.gobus.views.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: wangqingbin
 * @date: 2019/7/25 10:23
 */

public class MyFragment extends BaseLazyFragment<MyPresenter> implements IMyContract.View{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.rl_my_data)
    RelativeLayout rl_my_data;

    @BindView(R.id.iv_my_head)
    CircleImageView iv_my_head;

    @BindView(R.id.tv_my_name)
    TextView tv_my_name;

    @BindView(R.id.tv_my_unit)
    TextView tv_my_unit;

    @BindView(R.id.rl_my_vehicle)
    RelativeLayout rl_my_vehicle;

    @BindView(R.id.rl_my_repair)
    RelativeLayout rl_my_repair;

    @BindView(R.id.rl_my_maintenance)
    RelativeLayout rl_my_maintenance;

    @BindView(R.id.rl_my_use_help)
    RelativeLayout rl_my_use_help;

    @BindView(R.id.rl_my_setting)
    RelativeLayout rl_my_setting;

    @BindView(R.id.btn_my_connect_admin)
    Button btn_my_connect_admin;

    @OnClick({R.id.rl_my_data,R.id.rl_my_vehicle,R.id.rl_my_repair, R.id.rl_my_maintenance,
            R.id.rl_my_use_help,R.id.rl_my_setting,R.id.btn_my_connect_admin})
    void doClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.rl_my_data:
                intent = new Intent(mContext, PersonalDataActivity.class);
                intent.putExtra("TAG", "MyFragment");
                intent.putExtra("user", User.getInstance());
                startActivity(intent);
                break;
            case R.id.rl_my_vehicle:
                startActivity(new Intent(mContext, VehicleRecordActivity.class));
                break;
            case R.id.rl_my_repair:
                startActivity(new Intent(mContext, RepairRecordActivity.class));
                break;
            case R.id.rl_my_maintenance:
                startActivity(new Intent(mContext, MaintainRecordActivity.class));
                break;
            case R.id.rl_my_use_help:
                ToastUtils.showShortToast(mContext,"此功能暂时没实现!");
                break;
            case R.id.rl_my_setting:
                intent = new Intent(mContext,SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_my_connect_admin:
                mPresenter.getAdminList();
                break;
        }
    }

    @Override
    protected void lazyLoad(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_main_my;
    }

    @Override
    protected void initView(View view) {
        initToolbar();
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.GONE);
        mTvCenterTitle.setText("我 的");
        mTvCenterTitle.setTextColor(getResources().getColor(R.color.grey_6));
    }

    @Override
    protected void initData() {
        tv_my_name.setText(User.getInstance().getName());
        tv_my_unit.setText(User.getInstance().getUnitName());
    }

    @Override
    public void showAdminListDialog(List<StringInfo> numberList) {
        ChooseDialog.Builder builder = new ChooseDialog.Builder(mContext, numberList);
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
                    String phoneNumber = stringInfo.getTitle();
                    if(!TextUtils.isEmpty(phoneNumber)){
                        callPhone(phoneNumber);
                    }
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

    @NeedPermission(Manifest.permission.CALL_PHONE)
    private void callPhone(String phoneNumber) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNumber);
        intent.setData(data);
        startActivity(intent);
    }

}
