package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.bean.KeyCode;
import com.hxd.gobus.bean.Repair;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.mvp.IBaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/9/21 11:00
 */

public interface IRepairRegistrationContract {

    interface View extends IBaseView {
        void initRepairData(Repair repair);
        void showRepairStatusDialog(List<KeyCode> list);
        void saveRepairRegistrationSuccess();
    }

    interface Model{
        Observable<String> queryCodeValue(String key);
        Observable<String> updateRepairRegistration(Repair repair);
    }

}
