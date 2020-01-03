package com.hxd.gobus.core.http.interceptor;

import android.text.TextUtils;

import com.hxd.gobus.config.Constant;
import com.hxd.gobus.utils.LogUtils;
import com.hxd.gobus.utils.SPUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * https://www.2cto.com/kf/201609/548735.html
 * @author: wangqingbin
 * @date: 2019/9/23 9:57
 */

public class SaveCookieInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        List<String> headers = response.headers("Set-Cookie");
        for (String header : headers) {
            if (header.contains(";")){
                String cookie = header.split(";")[0];
                LogUtils.d("SaveCookieInterceptor receive cookie==", cookie);
                if (!TextUtils.isEmpty(cookie)) {
                    SPUtils.setParams("Cookie",cookie);
                }
            }
        }
        return response;
    }

}
