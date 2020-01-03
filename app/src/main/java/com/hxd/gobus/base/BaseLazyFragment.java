package com.hxd.gobus.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.IBaseView;

public abstract class BaseLazyFragment<P extends BasePresenter> extends BaseFragment<P>{
    // 是否已经初始化
    private boolean mIsInited = false;
    // 是否已被创建
    private boolean mIsCreated = false;
    private Bundle mSavedInstanceState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIsCreated = true;
        if (getUserVisibleHint() && !mIsInited) {
            lazyLoad(mSavedInstanceState);
            mIsInited = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !mIsInited && mIsCreated) {
            lazyLoad(mSavedInstanceState);
            mIsInited = true;
        }
    }

    protected abstract void lazyLoad(@Nullable Bundle savedInstanceState);
}
