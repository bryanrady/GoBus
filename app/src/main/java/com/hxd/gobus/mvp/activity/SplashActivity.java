package com.hxd.gobus.mvp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.mvp.contract.ISplashContract;
import com.hxd.gobus.mvp.presenter.SplashPresenter;

/**
 * @author: wangqingbin
 * @date: 2019/7/18 15:17
 */
public class SplashActivity extends BaseActivity<SplashPresenter> implements ISplashContract.View {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void doBusiness() {
        mPresenter.intoNextPage();
    }

    @Override
    public void intoMain() {
        startActivity(MainActivity.class);
        defaultFinish();
    }

    @Override
    public void intoLogin() {
        startActivity(LoginActivity.class);
        defaultFinish();
    }

}
