package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Authority;
import com.hxd.gobus.bean.Driver;
import com.hxd.gobus.bean.KeyCode;
import com.hxd.gobus.bean.StringInfo;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.bean.VehicleLicense;
import com.hxd.gobus.mvp.IBaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/8/15 12:04
 */

public interface IVehicleApprovalSaveContract {

    interface View extends IBaseView {
        void initVehicleData(Vehicle vehicle,String approvalStatus);
        void showVehicleLicenseDialog(List<VehicleLicense> list);
        void showDesignatedDriverDialog(List<Driver> list);
        void showVehicleDurationDialog(List<KeyCode> list);
        void saveApprovalSuccess();
        void showAttachList(List<Attach> list);
    }

    interface Model{
        Observable<String> queryCodeValue(String key);
        Observable<String> queryAvailableVehicle();
        Observable<String> queryDesignatedDriver();
        Observable<String> saveVehicleApproval(Vehicle vehicle, String approvalStatus, String approvalAdvice, Authority authority);
        Observable<String> queryAttachList(String dataId,String attachType);
    }

}
