package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Maintain;
import com.hxd.gobus.bean.Repair;
import com.hxd.gobus.mvp.IBaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/8/15 12:04
 */

public interface IMaintainApplyDetailContract {

    interface View extends IBaseView {
        void initMaintainData(Maintain maintain);
        void intoApprovalPage(Maintain maintain);
        void intoRegistrationPage(Maintain maintain);

        void showAttachList(List<Attach> list);
    }

    interface Model{
        Observable<String> queryMaintainApplyDetail(int upkeepApplyId);
        Observable<String> queryAttachList(String dataId,String attachType);
    }

}
