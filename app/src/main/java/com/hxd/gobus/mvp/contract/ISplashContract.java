package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.mvp.IBaseView;

/**
 * @author: wangqingbin
 * @date: 2019/7/31 15:13
 */

public interface ISplashContract {

    interface View extends IBaseView {
        void intoMain();

        void intoLogin();
    }

    interface Model{
        boolean isAlreadyLogin();
    }
}
