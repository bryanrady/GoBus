package com.hxd.gobus.di.modules;

import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.activity.AlreadyDoActivity;
import com.hxd.gobus.mvp.activity.ChooseContactActivity;
import com.hxd.gobus.mvp.activity.EmergencyRescueListActivity;
import com.hxd.gobus.mvp.activity.FilePreviewActivity;
import com.hxd.gobus.mvp.activity.LoginActivity;
import com.hxd.gobus.mvp.activity.MainActivity;
import com.hxd.gobus.mvp.activity.MaintainApplyActivity;
import com.hxd.gobus.mvp.activity.MaintainApplyDetailActivity;
import com.hxd.gobus.mvp.activity.MaintainApprovalActivity;
import com.hxd.gobus.mvp.activity.MaintainRecordActivity;
import com.hxd.gobus.mvp.activity.MaintainRegistrationActivity;
import com.hxd.gobus.mvp.activity.ModifyPasswordActivity;
import com.hxd.gobus.mvp.activity.PersonalDataActivity;
import com.hxd.gobus.mvp.activity.PublicApprovalSaveActivity;
import com.hxd.gobus.mvp.activity.RepairApplyActivity;
import com.hxd.gobus.mvp.activity.RepairApplyDetailActivity;
import com.hxd.gobus.mvp.activity.RepairApprovalActivity;
import com.hxd.gobus.mvp.activity.RepairRecordActivity;
import com.hxd.gobus.mvp.activity.RepairRegistrationActivity;
import com.hxd.gobus.mvp.activity.ReturnRegistrationActivity;
import com.hxd.gobus.mvp.activity.ReturnRegistrationDetailActivity;
import com.hxd.gobus.mvp.activity.ReturnRegistrationListActivity;
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
import com.hxd.gobus.mvp.contract.IAlreadyDoContract;
import com.hxd.gobus.mvp.contract.IChooseContactContract;
import com.hxd.gobus.mvp.contract.IEmergencyRescueListContract;
import com.hxd.gobus.mvp.contract.IFilePreviewContract;
import com.hxd.gobus.mvp.contract.ILoginContract;
import com.hxd.gobus.mvp.contract.IMainContract;
import com.hxd.gobus.mvp.contract.IMaintainApplyContract;
import com.hxd.gobus.mvp.contract.IMaintainApplyDetailContract;
import com.hxd.gobus.mvp.contract.IMaintainApprovalContract;
import com.hxd.gobus.mvp.contract.IMaintainRecordContract;
import com.hxd.gobus.mvp.contract.IMaintainRegistrationContract;
import com.hxd.gobus.mvp.contract.IModifyPasswordContract;
import com.hxd.gobus.mvp.contract.IPersonalDataContract;
import com.hxd.gobus.mvp.contract.IPublicApprovalSaveContract;
import com.hxd.gobus.mvp.contract.IRepairApplyContract;
import com.hxd.gobus.mvp.contract.IRepairApplyDetailContract;
import com.hxd.gobus.mvp.contract.IRepairApprovalContract;
import com.hxd.gobus.mvp.contract.IRepairRecordContract;
import com.hxd.gobus.mvp.contract.IRepairRegistrationContract;
import com.hxd.gobus.mvp.contract.IReturnRegistrationContract;
import com.hxd.gobus.mvp.contract.IReturnRegistrationDetailContract;
import com.hxd.gobus.mvp.contract.IReturnRegistrationListContract;
import com.hxd.gobus.mvp.contract.ISettingContract;
import com.hxd.gobus.mvp.contract.ISplashContract;
import com.hxd.gobus.mvp.contract.ITodoContract;
import com.hxd.gobus.mvp.contract.ITravelTaskContract;
import com.hxd.gobus.mvp.contract.IVehicleApplyContract;
import com.hxd.gobus.mvp.contract.IVehicleApplyDetailContract;
import com.hxd.gobus.mvp.contract.IVehicleApprovalContract;
import com.hxd.gobus.mvp.contract.IVehicleApprovalSaveContract;
import com.hxd.gobus.mvp.contract.IVehicleDataDetailContract;
import com.hxd.gobus.mvp.contract.IVehicleDataListContract;
import com.hxd.gobus.mvp.contract.IVehicleLocationDetailContract;
import com.hxd.gobus.mvp.contract.IVehicleMonitoringListContract;
import com.hxd.gobus.mvp.contract.IVehicleRecordContract;
import com.hxd.gobus.mvp.contract.IVehicleRegistrationContract;
import com.hxd.gobus.mvp.contract.IVehicleRegistrationDetailContract;
import com.hxd.gobus.mvp.contract.IVehicleRegistrationListContract;

import dagger.Binds;
import dagger.Module;

/**
 * @author: wangqingbin
 * @date: 2019/7/11 20:25
 */
@Module
public abstract class ActivityViewModule {

    @ActivityScope
    @Binds
    abstract ISplashContract.View provideISplashView(SplashActivity activity);

    @ActivityScope
    @Binds
    abstract ILoginContract.View provideILoginView(LoginActivity activity);

    @ActivityScope
    @Binds
    abstract IMainContract.View provideIMainView(MainActivity activity);

    @ActivityScope
    @Binds
    abstract IPersonalDataContract.View provideIPersonalDataView(PersonalDataActivity activity);

    @ActivityScope
    @Binds
    abstract ISettingContract.View provideISettingView(SettingActivity activity);

    @ActivityScope
    @Binds
    abstract IModifyPasswordContract.View provideIModifyPasswordView(ModifyPasswordActivity activity);

    @ActivityScope
    @Binds
    abstract ITodoContract.View provideITodoView(TodoActivity activity);

    @ActivityScope
    @Binds
    abstract IAlreadyDoContract.View provideIAlreadyDoView(AlreadyDoActivity activity);

    @ActivityScope
    @Binds
    abstract IVehicleApplyContract.View provideIVehicleApplyView(VehicleApplyActivity activity);

    @ActivityScope
    @Binds
    abstract IVehicleApplyDetailContract.View provideIVehicleApplyDetailView(VehicleApplyDetailActivity activity);

    @ActivityScope
    @Binds
    abstract IVehicleRecordContract.View provideIVehicleRecordView(VehicleRecordActivity activity);

    @ActivityScope
    @Binds
    abstract IFilePreviewContract.View provideIFilePreviewView(FilePreviewActivity activity);

    @ActivityScope
    @Binds
    abstract IVehicleApprovalContract.View provideIVehicleApprovalView(VehicleApprovalActivity activity);

    @ActivityScope
    @Binds
    abstract IVehicleApprovalSaveContract.View provideIVehicleApprovalSaveView(VehicleApprovalSaveActivity activity);

    @ActivityScope
    @Binds
    abstract IRepairRecordContract.View provideIRepairRecordView(RepairRecordActivity activity);
    @ActivityScope
    @Binds
    abstract IRepairApplyContract.View provideIRepairApplyView(RepairApplyActivity activity);
    @ActivityScope
    @Binds
    abstract IRepairApplyDetailContract.View provideIRepairApplyDetailView(RepairApplyDetailActivity activity);
    @ActivityScope
    @Binds
    abstract IRepairApprovalContract.View provideIRepairApprovalView(RepairApprovalActivity activity);
    @ActivityScope
    @Binds
    abstract IRepairRegistrationContract.View provideIRepairRegistrationView(RepairRegistrationActivity activity);
    @ActivityScope
    @Binds
    abstract IPublicApprovalSaveContract.View provideIPublicApprovalSaveView(PublicApprovalSaveActivity activity);

    @ActivityScope
    @Binds
    abstract IMaintainRecordContract.View provideIMaintainRecordView(MaintainRecordActivity activity);
    @ActivityScope
    @Binds
    abstract IMaintainApplyContract.View provideIMaintainApplyView(MaintainApplyActivity activity);
    @ActivityScope
    @Binds
    abstract IMaintainApplyDetailContract.View provideIMaintainApplyDetailView(MaintainApplyDetailActivity activity);
    @ActivityScope
    @Binds
    abstract IMaintainApprovalContract.View provideIMaintainApprovalView(MaintainApprovalActivity activity);
    @ActivityScope
    @Binds
    abstract IMaintainRegistrationContract.View provideIMaintainRegistrationView(MaintainRegistrationActivity activity);

    @ActivityScope
    @Binds
    abstract IVehicleRegistrationListContract.View provideIVehicleRegistrationListView(VehicleRegistrationListActivity activity);
    @ActivityScope
    @Binds
    abstract IVehicleRegistrationContract.View provideIVehicleRegistrationView(VehicleRegistrationActivity activity);
    @ActivityScope
    @Binds
    abstract IVehicleRegistrationDetailContract.View provideIVehicleRegistrationDetailView(VehicleRegistrationDetailActivity activity);
    @ActivityScope
    @Binds
    abstract IChooseContactContract.View provideIChooseContactView(ChooseContactActivity activity);


    @ActivityScope
    @Binds
    abstract IReturnRegistrationListContract.View provideIReturnRegistrationListView(ReturnRegistrationListActivity activity);
    @Binds
    abstract IReturnRegistrationContract.View provideIReturnRegistrationView(ReturnRegistrationActivity activity);
    @ActivityScope
    @Binds
    abstract IReturnRegistrationDetailContract.View provideIReturnRegistrationDetailView(ReturnRegistrationDetailActivity activity);

    @ActivityScope
    @Binds
    abstract IVehicleMonitoringListContract.View provideIVehicleMonitoringListView(VehicleMonitoringListActivity activity);
    @ActivityScope
    @Binds
    abstract IVehicleLocationDetailContract.View provideIVehicleLocationDetailView(VehicleLocationDetailActivity activity);
    @ActivityScope
    @Binds
    abstract ITravelTaskContract.View provideITravelTaskView(TravelTaskActivity activity);

    @ActivityScope
    @Binds
    abstract IEmergencyRescueListContract.View provideIEmergencyRescueListView(EmergencyRescueListActivity activity);

    @ActivityScope
    @Binds
    abstract IVehicleDataListContract.View provideIVehicleDataListView(VehicleDataListActivity activity);
    @ActivityScope
    @Binds
    abstract IVehicleDataDetailContract.View provideIVehicleDataDetailView(VehicleDataDetailActivity activity);
}
