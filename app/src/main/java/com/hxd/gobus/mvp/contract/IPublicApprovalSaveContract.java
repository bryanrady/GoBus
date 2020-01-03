package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.bean.Authority;
import com.hxd.gobus.bean.StringInfo;
import com.hxd.gobus.mvp.IBaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/9/20 14:55
 */

public interface IPublicApprovalSaveContract {

    interface View extends IBaseView {
        void initPublicData(Authority authority,String approvalStatus);
        void saveApprovalSuccess();
    }

    interface Model{
        Observable<String> saveApproval(String approvalStatus, String approvalAdvice, Authority authority);
    }

}
