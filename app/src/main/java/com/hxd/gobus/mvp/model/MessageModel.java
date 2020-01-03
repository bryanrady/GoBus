package com.hxd.gobus.mvp.model;

import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.di.scopes.FragmentScope;
import com.hxd.gobus.mvp.BaseModel;
import com.hxd.gobus.mvp.contract.IMessageContract;

import java.util.List;

import javax.inject.Inject;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:45
 */
@FragmentScope
public class MessageModel extends BaseModel implements IMessageContract.Model {

    @Inject
    public MessageModel(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Observable<String> queryTodoCount() {
        return RxRetrofitClient
                .create()
                .url(Constant.TODO_COUNT_URL)
                .raw(HttpParamsManager.getBaseParams(HttpParamsManager.queryTodoCountParams()).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public List<Conversation> loadConversationData() {
        List<Conversation> conversationList = JMessageClient.getConversationList();
        return conversationList;
    }
}
