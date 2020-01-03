package com.hxd.gobus.mvp.presenter;

import com.hxd.gobus.bean.list.TodoList;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.FragmentScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IMessageContract;
import com.hxd.gobus.mvp.model.MessageModel;
import com.hxd.gobus.utils.GsonUtils;

import java.util.List;
import javax.inject.Inject;
import cn.jpush.im.android.api.model.Conversation;
import io.reactivex.disposables.Disposable;

/**
 * @author: wangqingbin
 * @date: 2019/7/25 10:26
 */
@FragmentScope
public class MessagePresenter extends BasePresenter<IMessageContract.View,MessageModel>{

    @Inject
    public MessagePresenter(){

    }

    public void getTodoCount(){
        getView().showLoading();
        getModel().queryTodoCount().subscribe(new ObserverImpl() {
            @Override
            protected void handleResponse(String response) {
                TodoList jsonToObject = GsonUtils.jsonToObject(response, TodoList.class);
                if (jsonToObject != null){
                    int total = jsonToObject.getTotal();
                    if (getView() != null){
                        getView().showTodoMessage(total);
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

    public void getConversationList() {
        List<Conversation> conversationList = getModel().loadConversationData();
        if (conversationList != null && conversationList.size()>0){
            getView().showConversationList(conversationList);
        }
    }

}
