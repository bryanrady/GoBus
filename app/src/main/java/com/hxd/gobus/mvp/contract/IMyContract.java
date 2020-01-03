package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.bean.StringInfo;
import com.hxd.gobus.mvp.IBaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:37
 */

public interface IMyContract {

    interface View extends IBaseView {
        void showAdminListDialog(List<StringInfo> numberList);
    }

    interface Model{
        Observable<String> queryAdmin();
    }

}
