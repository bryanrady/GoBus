package com.hxd.gobus.mvp.model;

import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.ITodoContract;
import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:45
 */
@ActivityScope
public class TodoModel extends BaseModel implements ITodoContract.Model {

    @Inject
    public TodoModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Observable<String> queryTodoList() {
        return RxRetrofitClient
                .create()
                .url(Constant.TODO_URL)
                .raw(HttpParamsManager.getBaseParams(HttpParamsManager.queryTodoListParams()).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
