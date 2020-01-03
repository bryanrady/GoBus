package com.hxd.gobus.di.modules;

import com.hxd.gobus.di.scopes.FragmentScope;
import com.hxd.gobus.mvp.fragment.ApprovalTabFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * 核心功能：注入式Fragment的封装类，新建Fragment必须来这里注入
 * @author: wangqingbin
 * @date: 2019/7/11 20:24
 */
@Module
public abstract class ApprovalTabFragmentModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = ApprovalTabFragmentViewModule.class)
    abstract ApprovalTabFragment provideApprovalTabFragment();

}
