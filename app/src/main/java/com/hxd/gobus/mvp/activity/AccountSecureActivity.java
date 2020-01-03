package com.hxd.gobus.mvp.activity;


import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.User;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: wangqingbin
 * @date: 2019/9/21 15:46
 */

public class AccountSecureActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.rl_setting_phone_number)
    RelativeLayout rl_setting_phone_number;

    @BindView(R.id.tv_setting_phone_number)
    TextView tv_setting_phone_number;

    @BindView(R.id.rl_setting_modify_password)
    RelativeLayout rl_setting_modify_password;

    @OnClick({R.id.ll_toolbar_back,R.id.rl_setting_phone_number,R.id.rl_setting_modify_password})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.rl_setting_phone_number:
                break;
            case R.id.rl_setting_modify_password:
                startActivity(ModifyPasswordActivity.class);
                break;
        }
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_account_secure;
    }

    @Override
    protected void initView() {
        initToolbar();
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.VISIBLE);
        mTvCenterTitle.setText("账号与安全");
        mTvRightTitle.setVisibility(View.GONE);
    }

    @Override
    protected void doBusiness() {
        tv_setting_phone_number.setText(User.getInstance().getMobilePhone());
    }

}
