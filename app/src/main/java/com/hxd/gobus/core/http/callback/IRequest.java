package com.hxd.gobus.core.http.callback;

/**
 * Created by wangqingbin on 2018/6/6.
 */

public interface IRequest {

    /**
     * 请求开始
     */
    void onRequestStart();


    /**
     * 请求结束
     */
    void onRequestEnd();


}
