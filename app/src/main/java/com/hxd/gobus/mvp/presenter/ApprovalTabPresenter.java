package com.hxd.gobus.mvp.presenter;

import com.hxd.gobus.bean.Todo;
import com.hxd.gobus.bean.list.TodoList;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.di.scopes.FragmentScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IAlreadyDoContract;
import com.hxd.gobus.mvp.contract.IApprovalTabContract;
import com.hxd.gobus.mvp.model.AlreadyDoModel;
import com.hxd.gobus.mvp.model.ApprovalTabModel;
import com.hxd.gobus.utils.GsonUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * @author: wangqingbin
 * @date: 2019/7/25 10:26
 */
@FragmentScope
public class ApprovalTabPresenter extends BasePresenter<IApprovalTabContract.View,ApprovalTabModel> {

    @Inject
    public ApprovalTabPresenter(){

    }

    public void queryList(String positionArguments){
        getView().showLoading();
        getModel().queryList(positionArguments).subscribe(new ObserverImpl() {
            @Override
            protected void handleResponse(String response) {
                TodoList jsonToObject = GsonUtils.jsonToObject(response, TodoList.class);
                if (jsonToObject != null){
                    List<Todo> todoList = jsonToObject.getUntreatedInfos();
                    if (todoList != null && todoList.size() > 0){
                        if (getView() != null){
                            getView().showList(todoList);
                        }
                    }
                }
            }

            @Override
            protected void handleError(String errorMsg) {
                if (getView() != null){
                    getView().showToast(errorMsg);
                }
            }

            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onComplete() {
                getView().dismissLoading();
            }

        });
    }

}
