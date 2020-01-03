package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.bean.Repair;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.mvp.IBaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/8/12 15:32
 */

public interface IRepairRecordContract {

    interface View extends IBaseView {
        void showData(List<Repair> list);

        void overRefresh();

        void setLoadMoreEnabled(boolean flag);

        void deleteSuccess(int position);

        void deleteNotSuccess(String toast);
    }

    interface Model{
        Observable<String> getRepairList(String vehicleMark,String startDate,String endDate,String remarks,int page, int rows);

        Observable<String> deleteRepairApply(Integer repairId);
    }
}
