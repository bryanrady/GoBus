package com.hxd.gobus.mvp.contract;
import com.hxd.gobus.bean.ApkInfo;
import com.hxd.gobus.bean.User;
import com.hxd.gobus.mvp.IBaseView;

import java.io.File;

import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:37
 */

public interface IMainContract {

    interface View extends IBaseView {
        void showUpdateDialog(ApkInfo apkInfo);
    }

    interface Model{
        User getUser();
        Observable<String> checkApkUpdate();
    }

}
