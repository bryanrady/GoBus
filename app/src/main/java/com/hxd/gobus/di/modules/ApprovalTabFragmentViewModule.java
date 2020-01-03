package com.hxd.gobus.di.modules;

import com.hxd.gobus.di.scopes.FragmentScope;
import com.hxd.gobus.mvp.contract.IApprovalTabContract;
import com.hxd.gobus.mvp.contract.IContactContract;
import com.hxd.gobus.mvp.contract.IHomeContract;
import com.hxd.gobus.mvp.contract.IMessageContract;
import com.hxd.gobus.mvp.contract.IMyContract;
import com.hxd.gobus.mvp.fragment.ApprovalTabFragment;
import com.hxd.gobus.mvp.fragment.ContactFragment;
import com.hxd.gobus.mvp.fragment.HomeFragment;
import com.hxd.gobus.mvp.fragment.MessageFragment;
import com.hxd.gobus.mvp.fragment.MyFragment;

import dagger.Binds;
import dagger.Module;

/**
 * 核心功能：注入式Fragment的封装类，新建Fragment必须来这里注入
 * @author: wangqingbin
 * @date: 2019/7/11 20:24
 */
@Module
public abstract class ApprovalTabFragmentViewModule {

    @FragmentScope
    @Binds
    abstract IApprovalTabContract.View provideIApprovalTabView(ApprovalTabFragment fragment);

}
