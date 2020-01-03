package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Maintain;
import com.hxd.gobus.bean.Repair;
import com.hxd.gobus.bean.VehicleLicense;
import com.hxd.gobus.bean.VehicleUpkeep;
import com.hxd.gobus.mvp.IBaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/8/15 12:04
 */

public interface IMaintainApplyContract {

    interface View extends IBaseView {
        void initMaintainData(Maintain maintain);
        void showVehicleUpKeepDialog(List<VehicleUpkeep> list);
        void saveCommitSuccess();

        void showAttachList(List<Attach> list);
    }

    interface Model{
        Observable<String> queryVehicleUpKeepList();
        Observable<String> addMaintainApply(Maintain maintain);
        Observable<String> updateMaintainApply(Maintain maintain);
        Observable<String> queryMaintainApplyDetail(Integer upkeepApplyId);

        Observable<String> queryUUID();
        Observable<String> queryAttachList(String dataId,String attachType);
        Observable<String> uploadAttach(String dataId,String attachType,String filePath,String fileSuffix);
        Observable<String> deleteAttach(int attachId);
    }

}
