package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.mvp.IBaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/8/15 12:04
 */

public interface IReturnRegistrationDetailContract {

    interface View extends IBaseView {
        void initVehicleData(Vehicle vehicle);
        void showAttachList(List<Attach> list);
    }

    interface Model{
        Observable<String> queryReturnRegistrationDetail(int managementId);
        Observable<String> queryAttachList(String dataId,String attachType);
    }

}
