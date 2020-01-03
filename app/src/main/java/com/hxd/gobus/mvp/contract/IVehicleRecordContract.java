package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.mvp.IBaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/8/12 15:32
 */

public interface IVehicleRecordContract {

    interface View extends IBaseView {
        void showData(List<Vehicle> list);

        void overRefresh();

        void setLoadMoreEnabled(boolean flag);

        void deleteSuccess(int position);

        void deleteNotSuccess(String toast);
    }

    interface Model{
        Observable<String> getVehicleList(String vehicleMark,String startDate,String endDate,String useReason,int page,int rows);

        Observable<String> deleteVehicleApply(Integer managementId);
    }
}
