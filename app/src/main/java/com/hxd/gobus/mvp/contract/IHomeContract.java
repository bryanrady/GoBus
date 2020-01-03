package com.hxd.gobus.mvp.contract;
import com.hxd.gobus.bean.DataItem;
import com.hxd.gobus.mvp.IBaseView;

import java.util.List;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:37
 */

public interface IHomeContract {

    interface View extends IBaseView {
        void showHomeItem(List<DataItem> dataItems);
    }

    interface Model{
        List<DataItem> makeHomeItemData();
    }

}
