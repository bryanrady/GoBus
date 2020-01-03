package com.hxd.gobus.mvp.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.mvp.contract.IModifyPasswordContract;
import com.hxd.gobus.mvp.presenter.ModifyPasswordPresenter;
import com.hxd.gobus.utils.AppManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: wangqingbin
 * @date: 2019/9/21 16:45
 */

public class ModifyPasswordActivity extends BaseActivity<ModifyPasswordPresenter> implements IModifyPasswordContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.et_modify_old_password)
    EditText etOldPwd;

    @BindView(R.id.et_modify_new_password)
    EditText etNewPwd;

    @BindView(R.id.et_modify_confirm_password)
    EditText etConfirmPwd;

    @BindView(R.id.btn_modify_password_commit)
    Button btnCommit;

    @OnClick({R.id.ll_toolbar_back,R.id.btn_modify_password_commit})
    void doClick(View view) {
        switch (view.getId()) {
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.btn_modify_password_commit:
                String oldPwd = etOldPwd.getText().toString();
                String newPwd = etNewPwd.getText().toString();
                String confirmPwd = etConfirmPwd.getText().toString();
                mPresenter.commitNewPassword(oldPwd,newPwd,confirmPwd);
                break;
        }
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_modify_password;
    }

    @Override
    protected void initView() {
        initToolbar();
    }

    @Override
    protected void doBusiness() {

    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.VISIBLE);
        mTvCenterTitle.setText("修改密码");
        mTvRightTitle.setVisibility(View.GONE);
    }

    @Override
    public void commitNewPasswordSuccess() {
        AppManager.getInstance().finishActivity();
        startActivity(LoginActivity.class);
    }

}
