package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.bean.KeyCode;
import com.hxd.gobus.bean.Maintain;
import com.hxd.gobus.mvp.IBaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/9/21 11:00
 */

public interface IMaintainRegistrationContract {

    interface View extends IBaseView {
        void initMaintainData(Maintain maintain);
        void showMaintainStatusDialog(List<KeyCode> list);
        void saveMaintainRegistrationSuccess();
    }

    interface Model{
        Observable<String> queryCodeValue(String key);
        Observable<String> updateMaintainRegistration(Maintain maintain);
    }

}
