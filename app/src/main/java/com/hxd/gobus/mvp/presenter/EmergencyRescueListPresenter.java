package com.hxd.gobus.mvp.presenter;

import com.hxd.gobus.bean.RescuePerson;
import com.hxd.gobus.bean.list.RescuePersonList;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IEmergencyRescueListContract;
import com.hxd.gobus.mvp.model.EmergencyRescueListModel;
import com.hxd.gobus.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * @author: wangqingbin
 * @date: 2019/8/12 15:36
 */
@ActivityScope
public class EmergencyRescueListPresenter extends BasePresenter<IEmergencyRescueListContract.View,EmergencyRescueListModel> {

    private List<String> mList;

    @Inject
    public EmergencyRescueListPresenter() {
        if (mList == null){
            mList = new ArrayList<>();
        }
    }

    public void getRescueList(){
        getView().showLoading();
        getModel().queryRescueList().subscribe(new ObserverImpl() {

            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onComplete() {
                getView().dismissLoading();
            }

            @Override
            protected void handleResponse(String response) {
                RescuePersonList jsonToObject = GsonUtils.jsonToObject(response, RescuePersonList.class);
                if(jsonToObject != null){
                    List<RescuePerson> rescuePersonList = jsonToObject.getJsonArray();
                    if(rescuePersonList != null && rescuePersonList.size() > 0){
                        if (getView() != null){
                            getView().showRescueList(rescuePersonList);
                        }
                    }
                }
            }

            @Override
            protected void handleError(String errorMsg) {
                getView().showToast(errorMsg);
                getView().dismissLoading();
            }
        });
    }

}
