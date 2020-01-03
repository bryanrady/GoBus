package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.mvp.IBaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/8/15 12:04
 */

public interface IReturnRegistrationListContract {

    interface View extends IBaseView {
        void showData(List<Vehicle> list);

        void overRefresh();

        void setLoadMoreEnabled(boolean flag);
    }

    interface Model{
        Observable<String> getReturnRegistrationList(int page, int rows);
    }

}
