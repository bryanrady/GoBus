package com.hxd.gobus.di.modules;

import com.hxd.gobus.di.scopes.FragmentScope;
import com.hxd.gobus.mvp.fragment.HomeFragment;
import com.hxd.gobus.mvp.fragment.ContactFragment;
import com.hxd.gobus.mvp.fragment.MessageFragment;
import com.hxd.gobus.mvp.fragment.MyFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * 核心功能：注入式Fragment的封装类，新建Fragment必须来这里注入
 * @author: wangqingbin
 * @date: 2019/7/11 20:24
 */
@Module
public abstract class MainFragmentModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = MainFragmentViewModule.class)
    abstract HomeFragment provideHomeFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = MainFragmentViewModule.class)
    abstract MessageFragment provideMessageFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = MainFragmentViewModule.class)
    abstract ContactFragment provideContactFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = MainFragmentViewModule.class)
    abstract MyFragment provideMyFragment();

}
