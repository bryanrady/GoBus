package com.hxd.gobus.mvp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.ApkInfo;
import com.hxd.gobus.mvp.contract.ISettingContract;
import com.hxd.gobus.mvp.presenter.SettingPresenter;
import com.hxd.gobus.service.DownloadApkService;
import com.hxd.gobus.utils.AppUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: wangqingbin
 * @date: 2019/8/20 19:38
 */

public class SettingActivity extends BaseActivity<SettingPresenter> implements ISettingContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.rl_setting_account_secure)
    RelativeLayout rl_setting_account_secure;

    @BindView(R.id.rl_setting_update_version)
    RelativeLayout rl_setting_update_version;

    @BindView(R.id.tv_setting_apk_version_name)
    TextView tv_setting_apk_version_name;

    @BindView(R.id.btn_setting_exit)
    Button btn_setting_exit;

    @OnClick({R.id.ll_toolbar_back,R.id.rl_setting_account_secure,R.id.rl_setting_update_version,R.id.btn_setting_exit})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.rl_setting_account_secure:
                startActivity(AccountSecureActivity.class);
                break;
            case R.id.rl_setting_update_version:
                mPresenter.checkApkUpdate();
                break;
            case R.id.btn_setting_exit:
                mPresenter.logout();
                break;
        }
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        initToolbar();
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.VISIBLE);
        mTvCenterTitle.setTextColor(getResources().getColor(R.color.grey_6));
        mTvCenterTitle.setText("设置");
        mTvRightTitle.setVisibility(View.GONE);
    }

    @Override
    protected void doBusiness() {
        tv_setting_apk_version_name.setText(AppUtil.getAppVersionName());
    }

    @Override
    public void intoLogin() {
        startActivity(LoginActivity.class);
    }

    @Override
    public void showUpdateDialog(final ApkInfo apkInfo) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.version_upgrade));
        builder.setMessage(apkInfo.getVersionContent());
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.update_now), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent(mContext,DownloadApkService.class);
                intent.putExtra("apkUrl",apkInfo.getDownloadUrl());
                intent.putExtra("fileName","gobus");
                startService(intent);
            }
        });
        builder.setNegativeButton(getString(R.string.not_update_now), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

}
