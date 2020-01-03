package com.hxd.gobus.mvp.contract;
import com.hxd.gobus.bean.RealTimeData;
import com.hxd.gobus.mvp.IBaseView;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:37
 */

public interface IVehicleDataDetailContract {

    interface View extends IBaseView {
        void initRealTimeData(RealTimeData realTimeData);
    }

    interface Model{

    }

}
