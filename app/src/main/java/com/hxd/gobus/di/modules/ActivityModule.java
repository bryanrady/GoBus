package com.hxd.gobus.di.modules;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.activity.AccountSecureActivity;
import com.hxd.gobus.mvp.activity.AlreadyDoActivity;
import com.hxd.gobus.mvp.activity.ChooseContactActivity;
import com.hxd.gobus.mvp.activity.ChooseLocationActivity;
import com.hxd.gobus.mvp.activity.EmergencyRescueListActivity;
import com.hxd.gobus.mvp.activity.FilePreviewActivity;
import com.hxd.gobus.mvp.activity.LoginActivity;
import com.hxd.gobus.mvp.activity.MainActivity;
import com.hxd.gobus.mvp.activity.MaintainApplyActivity;
import com.hxd.gobus.mvp.activity.MaintainApplyDetailActivity;
import com.hxd.gobus.mvp.activity.MaintainApprovalActivity;
import com.hxd.gobus.mvp.activity.MaintainRecordActivity;
import com.hxd.gobus.mvp.activity.MaintainRegistrationActivity;
import com.hxd.gobus.mvp.activity.MobileApprovalActivity;
import com.hxd.gobus.mvp.activity.ModifyPasswordActivity;
import com.hxd.gobus.mvp.activity.PersonalDataActivity;
import com.hxd.gobus.mvp.activity.ImagePreviewActivity;
import com.hxd.gobus.mvp.activity.PublicApprovalSaveActivity;
import com.hxd.gobus.mvp.activity.RepairApplyActivity;
import com.hxd.gobus.mvp.activity.RepairApplyDetailActivity;
import com.hxd.gobus.mvp.activity.RepairApprovalActivity;
import com.hxd.gobus.mvp.activity.RepairRecordActivity;
import com.hxd.gobus.mvp.activity.RepairRegistrationActivity;
import com.hxd.gobus.mvp.activity.ReturnRegistrationActivity;
import com.hxd.gobus.mvp.activity.ReturnRegistrationDetailActivity;
import com.hxd.gobus.mvp.activity.ReturnRegistrationListActivity;
import com.hxd.gobus.mvp.activity.SearchLocationActivity;
import com.hxd.gobus.mvp.activity.SettingActivity;
import com.hxd.gobus.mvp.activity.SplashActivity;
import com.hxd.gobus.mvp.activity.TodoActivity;
import com.hxd.gobus.mvp.activity.TravelTaskActivity;
import com.hxd.gobus.mvp.activity.VehicleApplyActivity;
import com.hxd.gobus.mvp.activity.VehicleApplyDetailActivity;
import com.hxd.gobus.mvp.activity.VehicleApprovalActivity;
import com.hxd.gobus.mvp.activity.VehicleApprovalSaveActivity;
import com.hxd.gobus.mvp.activity.VehicleDataDetailActivity;
import com.hxd.gobus.mvp.activity.VehicleDataListActivity;
import com.hxd.gobus.mvp.activity.VehicleLocationDetailActivity;
import com.hxd.gobus.mvp.activity.VehicleMonitoringListActivity;
import com.hxd.gobus.mvp.activity.VehicleRecordActivity;
import com.hxd.gobus.mvp.activity.VehicleRegistrationActivity;
import com.hxd.gobus.mvp.activity.VehicleRegistrationDetailActivity;
import com.hxd.gobus.mvp.activity.VehicleRegistrationListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * 核心功能：用于注入Activity，将每个Activity的component添加到map，此module用来统一管理app component 的
 * 子activity component注入式Activity的封装类，新建Activity必须来这里注入
 * @author: wangqingbin
 * @date: 2019/7/11 20:15
 */

@Module
public abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract SplashActivity provideSplashActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract LoginActivity provideLoginActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class,MainFragmentModule.class})
    abstract MainActivity provideMainActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract TodoActivity provideTodoActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract AlreadyDoActivity provideAlreadyDoActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ApprovalTabFragmentModule.class})
    abstract MobileApprovalActivity provideMobileApprovalActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract PersonalDataActivity providePersonalDataActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract SettingActivity provideSettingActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract AccountSecureActivity provideAccountSecureActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract ModifyPasswordActivity provideModifyPasswordActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract PublicApprovalSaveActivity providePublicApprovalSaveActivity();

    /*****************************   用车申请 start       ******************************/

    @ActivityScope
    @ContributesAndroidInjector
    abstract ChooseLocationActivity provideChooseLocationActivity();
    @ActivityScope
    @ContributesAndroidInjector
    abstract SearchLocationActivity provideSearchLocationActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract ImagePreviewActivity providePlusImageActivity();
    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract FilePreviewActivity provideFilePreviewActivity();
    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract VehicleRecordActivity provideVehicleRecordActivity();
    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract VehicleApplyActivity provideVehicleApplyActivity();
    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract VehicleApplyDetailActivity provideVehicleApplyDetailActivity();
    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract VehicleApprovalActivity provideVehicleApprovalActivity();
    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract VehicleApprovalSaveActivity provideVehicleApprovalSaveActivity();

    /*****************************   用车申请 end       ******************************/


    /*****************************   维修申请 start       ******************************/
    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract RepairRecordActivity provideRepairRecordActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract RepairApplyActivity provideRepairApplyActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract RepairApplyDetailActivity provideRepairApplyDetailActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract RepairApprovalActivity provideRepairApprovalActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract RepairRegistrationActivity provideRepairRegistrationActivity();

    /*****************************   维修申请 end       ******************************/



    /*****************************   保养申请 start       ******************************/

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract MaintainRecordActivity provideMaintainRecordActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract MaintainApplyActivity provideMaintainApplyActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract MaintainApplyDetailActivity provideMaintainApplyDetailActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract MaintainApprovalActivity provideMaintainApprovalActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract MaintainRegistrationActivity provideMaintainRegistrationActivity();

    /*****************************   保养申请 end       ******************************/


    /*****************************   用车登记 start       ******************************/

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract VehicleRegistrationListActivity provideVehicleRegistrationListActivity();
    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract VehicleRegistrationActivity provideVehicleRegistrationActivity();
    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract VehicleRegistrationDetailActivity provideVehicleRegistrationDetailActivity();

    /*****************************   用车登记 end       ******************************/


    /*****************************   用车归还 start       ******************************/

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract ReturnRegistrationListActivity provideReturnRegistrationListActivity();
    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract ReturnRegistrationActivity provideReturnRegistrationActivity();
    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract ReturnRegistrationDetailActivity provideReturnRegistrationDetailActivity();

    /*****************************   用车归还 end       ******************************/



    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract ChooseContactActivity provideChooseContactActivity();


    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract VehicleMonitoringListActivity provideVehicleMonitoringListActivity();
    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract VehicleLocationDetailActivity provideVehicleLocationDetailActivity();
    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract TravelTaskActivity provideTravelTaskActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract EmergencyRescueListActivity provideEmergencyRescueListActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract VehicleDataListActivity provideVehicleDataListActivity();
    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityViewModule.class})
    abstract VehicleDataDetailActivity provideVehicleDataDetailActivity();

}
