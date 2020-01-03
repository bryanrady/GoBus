package com.hxd.gobus.core.http.callback;

/**
 * Created by wangqingbin on 2018/6/6.
 */
public interface IError {

    void onError(int code, String msg);

}
