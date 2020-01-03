package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Repair;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.bean.VehicleLicense;
import com.hxd.gobus.mvp.IBaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/8/15 12:04
 */

public interface IRepairApplyContract {

    interface View extends IBaseView {
        void initRepairData(Repair repair);
        void showVehicleLicenseDialog(List<VehicleLicense> list);
        void saveCommitSuccess();

        void showAttachList(List<Attach> list);
    }

    interface Model{
        Observable<String> queryAvailableVehicle();
        Observable<String> addRepairApply(Repair repair);
        Observable<String> updateRepairApply(Repair repair);
        Observable<String> queryRepairApplyDetail(Integer repairId);

        Observable<String> queryUUID();
        Observable<String> queryAttachList(String dataId,String attachType);
        Observable<String> uploadAttach(String dataId,String attachType,String filePath,String fileSuffix);
        Observable<String> deleteAttach(int attachId);
    }

}
