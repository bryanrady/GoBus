package com.hxd.gobus.mvp.presenter;
import com.hxd.gobus.di.scopes.FragmentScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IHomeContract;
import com.hxd.gobus.mvp.model.HomeModel;

import javax.inject.Inject;

/**
 * @author: wangqingbin
 * @date: 2019/7/25 10:26
 */
@FragmentScope
public class HomePresenter extends BasePresenter<IHomeContract.View,HomeModel> {

    @Inject
    public HomePresenter(){
    }

    public void getHomeItemData(){
        getView().showHomeItem(getModel().makeHomeItemData());
    }


}
