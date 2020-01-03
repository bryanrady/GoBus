package com.hxd.gobus.mvp.contract;
import com.hxd.gobus.bean.RealTimeMonitoring;
import com.hxd.gobus.mvp.IBaseView;


/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:37
 */

public interface IVehicleLocationDetailContract {

    interface View extends IBaseView {
        void initRealTimeMonitoring(RealTimeMonitoring realTimeMonitoring);
    }

    interface Model{

    }

}
