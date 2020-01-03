package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.bean.RescuePerson;
import com.hxd.gobus.mvp.IBaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/8/12 15:32
 */

public interface IEmergencyRescueListContract {

    interface View extends IBaseView {
        void showRescueList(List<RescuePerson> list);
    }

    interface Model{
        Observable<String> queryRescueList();
    }
}
