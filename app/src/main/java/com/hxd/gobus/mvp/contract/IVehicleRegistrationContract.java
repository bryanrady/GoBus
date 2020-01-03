package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Driver;
import com.hxd.gobus.bean.KeyCode;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.bean.VehicleLicense;
import com.hxd.gobus.mvp.IBaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/8/15 12:04
 */

public interface IVehicleRegistrationContract {

    interface View extends IBaseView {
        void initVehicleData(Vehicle vehicle);
        void showVehicleLicenseDialog(List<VehicleLicense> list);
        void showDesignatedDriverDialog(List<Driver> list);
        void showIsUseTheCarDialog(List<KeyCode> list);
        void saveVehicleRegistrationSuccess();

        void showAttachList(List<Attach> list);
    }

    interface Model{
        Observable<String> queryAvailableVehicle();
        Observable<String> queryDesignatedDriver();
        Observable<String> queryCodeValue(String key);
        Observable<String> queryVehicleRegistrationDetail(int managementId);
        Observable<String> updateVehicleRegistration(Vehicle vehicle);

        Observable<String> queryAttachList(String dataId,String attachType);
        Observable<String> uploadAttach(String dataId,String attachType,String filePath,String fileSuffix);
        Observable<String> deleteAttach(int attachId);
    }

}
