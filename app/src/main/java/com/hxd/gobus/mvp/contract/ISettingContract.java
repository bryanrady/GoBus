package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.bean.ApkInfo;
import com.hxd.gobus.mvp.IBaseView;

import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:37
 */

public interface ISettingContract {

    interface View extends IBaseView {
        void intoLogin();
        void showUpdateDialog(ApkInfo apkInfo);
    }

    interface Model{
        Observable<String> logout();
        void setNotAlreadyLogin();
        Observable<String> checkApkUpdate();
    }

}
