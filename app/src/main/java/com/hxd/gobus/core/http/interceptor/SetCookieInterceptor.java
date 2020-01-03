package com.hxd.gobus.core.http.interceptor;

import android.text.TextUtils;

import com.hxd.gobus.config.Constant;
import com.hxd.gobus.utils.LogUtils;
import com.hxd.gobus.utils.SPUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author: wangqingbin
 * @date: 2019/9/23 9:57
 */

public class SetCookieInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        String cookie = (String) SPUtils.getParams("Cookie", "");
        LogUtils.d("SetCookieInterceptor get cookie==", cookie);
        if (!TextUtils.isEmpty(cookie)) {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("Cookie", cookie)
                    .build();
            LogUtils.d("SetCookieInterceptor add Cookie ==", newRequest.headers().toString());
            return chain.proceed(newRequest);
        }
        return chain.proceed(chain.request());
    }

}
