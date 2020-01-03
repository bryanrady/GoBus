package com.hxd.gobus.core.http.rx;


import com.hxd.gobus.core.http.HttpMethod;
import com.hxd.gobus.core.http.RetrofitCreator;

import java.io.File;
import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by wangqingbin on 2018/11/6.
 */

public class RxRetrofitClient {

    private final HashMap<String,Object> PARAMS;
    private final String URL;
    private final RequestBody BODY;
    //上传下载
    private final File FILE;

    public RxRetrofitClient(
            HashMap<String, Object> params,
            String url,
            RequestBody body,
            File file) {
        this.PARAMS = params;
        this.URL = url;
        this.BODY = body;
        this.FILE=file;
    }

    public static RxRetrofitClientBuilder create(){
        return new RxRetrofitClientBuilder();
    }

    /**
     * 真正的网络操作
     * @param method
     */
    private Observable<String> request(HttpMethod method){
        final RxRetrofitService service = RetrofitCreator.getRxRetrofitService();
        Observable<String> observable = null;
        switch(method){
            case GET:
                observable = service.get(URL,PARAMS);
                break;
            case POST:
                observable = service.post(URL,PARAMS);
                break;
            case PUT:
                observable = service.put(URL,PARAMS);
                break;
            case DELETE:
                observable = service.delete(URL,PARAMS);
                break;
            case UPLOAD:
                final RequestBody requestBody = RequestBody.create(MultipartBody.FORM, FILE);
                final MultipartBody.Part part = MultipartBody.Part.createFormData(
                        "file", FILE.getName(), requestBody);
                observable = service.upload(URL, part);
                break;
            case POST_RAW:
                observable = service.postRaw(URL,BODY);
                break;
            default:
                break;
        }
        return observable;
    }

    //各种请求
    public final Observable<String> get(){
        return request(HttpMethod.GET);
    }
    public final Observable<String> post(){
        return request(HttpMethod.POST);
    }
    public final Observable<String> put(){
        return request(HttpMethod.PUT);
    }
    public final Observable<String> delete(){
        return request(HttpMethod.DELETE);
    }
    public final Observable<String> upload(){
        return request(HttpMethod.UPLOAD);
    }
    public final Observable<String> postRaw(){
        return request(HttpMethod.POST_RAW);
    }

    public final Observable<ResponseBody> download(){
        return RetrofitCreator.getRxRetrofitService().download(URL);
    }

}
