package com.hxd.gobus.bean.event;

/**
 * 定义消息事件类型 String类型
 * @author: wangqingbin
 * @date: 2019/7/12 11:33
 */

public class StringEvent {

    private String message;

    public StringEvent(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
