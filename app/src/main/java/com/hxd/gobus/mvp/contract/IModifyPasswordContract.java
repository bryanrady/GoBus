package com.hxd.gobus.mvp.contract;
import com.hxd.gobus.mvp.IBaseView;

import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:37
 */

public interface IModifyPasswordContract {

    interface View extends IBaseView {
        void commitNewPasswordSuccess();
    }

    interface Model{
        Observable<String> modifyPassword(String oldPwd, String newPwd);
        void clearUserInfo();
        void setNotLogin();
    }

}
