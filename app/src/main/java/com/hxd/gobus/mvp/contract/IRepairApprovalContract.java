package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Authority;
import com.hxd.gobus.bean.Repair;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.mvp.IBaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:37
 */

public interface IRepairApprovalContract {

    interface View extends IBaseView {
        void initRepairData(int source, String positionFlag, Repair repair);
        void intoApprovalSavePage(Authority authority,String approvalStatus);
        void showAttachList(List<Attach> list);
    }

    interface Model{
        Observable<String> queryRepairApprovalData(Integer datumDataId, String datumType);
        Observable<String> permissionAuthority(Integer datumDataId, String datumType);
        Observable<String> updateTodoList(Integer ids);
        Observable<String> queryAttachList(String dataId,String attachType);
        Observable<String> queryNextReviewer(int datumDataId,String datumType);
        Observable<String> reminder(String title,String workNo);
    }

}
