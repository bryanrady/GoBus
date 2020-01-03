package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.bean.list.TodoList;
import com.hxd.gobus.mvp.IBaseView;

import java.util.List;

import cn.jpush.im.android.api.model.Conversation;
import io.reactivex.Observable;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 14:37
 */

public interface IMessageContract {

    interface View extends IBaseView {

        void showTodoMessage(int total);

        void showConversationList(List<Conversation> conversationList);
    }

    interface Model{
        Observable<String> queryTodoCount();
        List<Conversation> loadConversationData();

    }

}
