package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.bean.RealTimeData;
import com.hxd.gobus.mvp.IBaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/8/12 15:32
 */

public interface IVehicleDataListContract {

    interface View extends IBaseView {
        void showData(List<RealTimeData> list);

        void overRefresh();

        void setLoadMoreEnabled(boolean flag);
    }

    interface Model{
        Observable<String> getRealTimeDataList(int page, int rows);
    }
}
