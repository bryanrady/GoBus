package com.hxd.gobus.bean.event;

import cn.jpush.im.android.api.model.Conversation;

/**
 * @author: wangqingbin
 * @date: 2019/7/26 16:36
 */

public class ConversationEvent {

    private int eventType;
    private Conversation conversation;

    public ConversationEvent(int eventType, Conversation conversation) {
        this.eventType = eventType;
        this.conversation = conversation;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public int getEventType() {
        return eventType;
    }
}
