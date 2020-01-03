package com.hxd.gobus.core.http;

import com.hxd.gobus.core.http.interceptor.SaveCookieInterceptor;
import com.hxd.gobus.core.http.interceptor.SetCookieInterceptor;
import com.hxd.gobus.core.http.retrofit.RetrofitService;
import com.hxd.gobus.core.http.rx.RxRetrofitService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 *  产生一个全局的Retrofit客户端
 * Created by wangqingbin on 2018/11/6.
 */
public final class RetrofitCreator {

    private static final class RetrofitHolder{
        private static final String BASE_URL = RetrofitInit.getConfiguration(RetrofitConfigKeys.API_HOST);
        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OKHttpHolder.OK_HTTP_CLIENT)
                .build();
    }

    private static final class OKHttpHolder{
        private static final int TIME_OUT = 30;
        //https://www.cnblogs.com/ganchuanpu/p/8075859.html
        private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT,TimeUnit.SECONDS)
//                .cookieJar(new CookieJar() {
//                    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
//                    @Override
//                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//                    //    cookieStore.put(url, cookies);
//                        cookieStore.put(HttpUrl.parse(Constant.LOGIN_URL), cookies);
//                    }
//
//                    @Override
//                    public List<Cookie> loadForRequest(HttpUrl url) {
//                    //    List<Cookie> cookies = cookieStore.get(url);
//                        List<Cookie> cookies = cookieStore.get(HttpUrl.parse(Constant.LOGIN_URL));
//                        return cookies != null ? cookies : new ArrayList<Cookie>();
//                    }
//                })
                .addInterceptor(new SaveCookieInterceptor())
                .addInterceptor(new SetCookieInterceptor())
                .build();
    }

    private static final class RetrofitServiceHolder{
        private static final RetrofitService RETROFIT_SERVICE = RetrofitHolder.RETROFIT_CLIENT.create(RetrofitService.class);
    }

    public static RetrofitService getRetrofitService(){
        return RetrofitServiceHolder.RETROFIT_SERVICE;
    }

    private static final class RxRetrofitServiceHolder{
        private static final RxRetrofitService RETROFIT_SERVICE = RetrofitHolder.RETROFIT_CLIENT.create(RxRetrofitService.class);
    }

    public static RxRetrofitService getRxRetrofitService(){
        return RxRetrofitServiceHolder.RETROFIT_SERVICE;
    }
}
