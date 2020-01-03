package com.hxd.gobus.core.http.interceptor;

import android.text.TextUtils;
import android.util.Log;

import com.hxd.gobus.config.Constant;
import com.hxd.gobus.utils.LogUtils;
import com.hxd.gobus.utils.SPUtils;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Retrofit2 Cookie拦截器。用于保存和设置Cookies
 */
public class CookieInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        String cookie = (String) SPUtils.getParams("Cookie", "");
        LogUtils.d("CookieInterceptor cookie11==", cookie);
        if (!TextUtils.isEmpty(cookie) && !original.url().equals(Constant.LOGIN_URL)) {
            Request request = original.newBuilder()
                    .addHeader("Cookie", cookie)
                    .build();
            LogUtils.d("CookieInterceptor add Cookie header==", request.headers().toString());

            Response response = chain.proceed(request);
            List<String> headers = response.headers("Set-Cookie");
            for (String header : headers) {
                if (header.contains(";")){
                    cookie = header.split(";")[0];
                    LogUtils.d("CookieInterceptor request cookie==", cookie);
                    if (!TextUtils.isEmpty(cookie)) {
                        SPUtils.setParams("Cookie",cookie);
                    }
                }
            }
            return chain.proceed(request);
        }else{
            Response response = chain.proceed(original);
            List<String> headers = response.headers("Set-Cookie");
            for (String header : headers) {
                if (header.contains(";")){
                    cookie = header.split(";")[0];
                    LogUtils.d("CookieInterceptor original cookie==", cookie);
                    if (!TextUtils.isEmpty(cookie)) {
                        SPUtils.setParams("Cookie",cookie);
                    }
                }
            }
            return chain.proceed(original);
        }
    }

}
