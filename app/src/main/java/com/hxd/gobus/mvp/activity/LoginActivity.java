package com.hxd.gobus.mvp.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bryanrady.lib_permission.annotation.NeedPermission;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.User;
import com.hxd.gobus.core.ioc.CheckNetwork;
import com.hxd.gobus.mvp.contract.ILoginContract;
import com.hxd.gobus.mvp.presenter.LoginPresenter;
import com.hxd.gobus.utils.AppManager;
import com.hxd.gobus.utils.StatusBarUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: wangqingbin
 * @date: 2019/7/25 11:09
 */

public class LoginActivity extends BaseActivity<LoginPresenter> implements ILoginContract.View {

    @BindView(R.id.et_login_userName)
    EditText etUserName;

    @BindView(R.id.et_login_userPwd)
    EditText etUserPwd;

    @BindView(R.id.chk_login_remember_password)
    AppCompatCheckBox chkRememberPwd;

    @BindView(R.id.tv_login_forget_password)
    TextView tvForgetPwd;

    @BindView(R.id.btn_login)
    Button btnLogin;

    private long exitTime = 0;

    @CheckNetwork
    @OnClick(R.id.btn_login)
    void login() {
        String username = etUserName.getText().toString().trim();
        String password = etUserPwd.getText().toString().trim();
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            showShortToast(getString(R.string.username_password_not_null));
            return;
        }
        mPresenter.login(username,password,chkRememberPwd.isChecked());
    }

    @CheckNetwork
    @OnClick(R.id.tv_login_forget_password)
    void forgetPassword() {
        showShortToast(getString(R.string.contact_administrator));
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void doBusiness() {
        requestMultiplePermission();
        mPresenter.showLoginInfo();
        mPresenter.getCheckedStatus();
    }

    @NeedPermission({
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void requestMultiplePermission() {
    }

    @Override
    public void loginSuccess() {
        startActivity(MainActivity.class);
    }

    @Override
    public void isCheck(boolean isCheck) {
        chkRememberPwd.setChecked(isCheck);
    }

    @Override
    public void showUserInfo(User user) {
        etUserName.setText(user.getLoginName());
        etUserPwd.setText(user.getLoginPassword());
    }

    @Override
    public void showUsername(User user) {
        etUserName.setText(user.getLoginName());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showShortToast(getString(R.string.press_exit_program));
                exitTime = System.currentTimeMillis();
            } else {
                AppManager.getInstance().finishAllActivity();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
