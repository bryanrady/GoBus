package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.mvp.IBaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/9/23 18:08
 */

public interface ITravelTaskContract {

    interface View extends IBaseView {
        void initTravelTaskData(Vehicle vehicle);
        void showAttachList(List<Attach> list);
    }

    interface Model{
        Observable<String> queryTravelTask(Integer managementId);
        Observable<String> queryAttachList(String dataId,String attachType);
    }

}
