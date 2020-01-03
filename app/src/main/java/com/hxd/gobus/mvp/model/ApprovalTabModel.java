package com.hxd.gobus.mvp.model;

import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.di.scopes.FragmentScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IAlreadyDoContract;
import com.hxd.gobus.mvp.contract.IApprovalTabContract;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:45
 */
@FragmentScope
public class ApprovalTabModel extends BaseModel implements IApprovalTabContract.Model {

    @Inject
    public ApprovalTabModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Observable<String> queryList(String positionArguments) {
        return RxRetrofitClient
                .create()
                .url(Constant.TODO_URL)
                .raw(HttpParamsManager.getBaseParams(HttpParamsManager.queryTodoAlreadyDoListParams(positionArguments)).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
