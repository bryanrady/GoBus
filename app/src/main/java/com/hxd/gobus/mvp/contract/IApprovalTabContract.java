package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.bean.Todo;
import com.hxd.gobus.mvp.IBaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/7/31 15:13
 */

public interface IApprovalTabContract {

    interface View extends IBaseView {
        void showList(List<Todo> list);
    }

    interface Model{
        Observable<String> queryList(String positionArguments);
    }
}
