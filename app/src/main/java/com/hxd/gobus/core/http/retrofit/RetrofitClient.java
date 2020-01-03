package com.hxd.gobus.core.http.retrofit;

import com.hxd.gobus.core.http.HttpMethod;
import com.hxd.gobus.core.http.RetrofitCreator;
import com.hxd.gobus.core.http.callback.IError;
import com.hxd.gobus.core.http.callback.IFailure;
import com.hxd.gobus.core.http.callback.IRequest;
import com.hxd.gobus.core.http.callback.ISuccess;
import com.hxd.gobus.core.http.callback.RequestCallback;
import com.hxd.gobus.core.http.download.DownloadHandler;
import com.hxd.gobus.core.http.download.DownloadListener;

import java.io.File;
import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by wangqingbin on 2018/11/6.
 */

public class RetrofitClient {

    private final HashMap<String,Object> PARAMS;
    private final String URL;
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final RequestBody BODY;
    //上传下载
    private final File FILE;

    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final String FILENAME;

    public RetrofitClient(
            HashMap<String, Object> params,
            String url, IRequest request,
            ISuccess success,
            IFailure failure,
            IError error,
            RequestBody body,
            File file,
            String downloadDir,
            String extension,
            String filename) {
        this.PARAMS = params;
        this.URL = url;
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.BODY = body;

        this.FILE=file;
        this.DOWNLOAD_DIR=downloadDir;  ///sdcard/XXXX.ext
        this.EXTENSION=extension;
        this.FILENAME=filename;
    }

    public static RetrofitClientBuilder create(){
        return new RetrofitClientBuilder();
    }

    /**
     * 真正的网络操作
     * @param method
     */
    private void request(HttpMethod method){
        final RetrofitService service = RetrofitCreator.getRetrofitService();
        Call<String> call = null;
        if(REQUEST != null){
            REQUEST.onRequestStart();
        }
        switch(method){
            case GET:
                call=service.get(URL,PARAMS);
                break;
            case POST:
                call=service.post(URL,PARAMS);
                break;
            case PUT:
                call=service.put(URL,PARAMS);
                break;
            case DELETE:
                call=service.delete(URL,PARAMS);
                break;
            case UPLOAD:
                final RequestBody requestBody = RequestBody.create(MultipartBody.FORM, FILE);
                final MultipartBody.Part part = MultipartBody.Part.createFormData(
                        "file", FILE.getName(), requestBody);
                call = service.upload(URL, part);
                break;
            default:
                break;
        }
        if(call!=null){
            call.enqueue(getRequestCallback());
        }
    }

    private Callback<String> getRequestCallback(){
        return new RequestCallback(REQUEST,SUCCESS,FAILURE,ERROR);
    }

    //各种请求
    public final void get(){
        request(HttpMethod.GET);
    }
    public final void post(){
        request(HttpMethod.POST);
    }
    public final void put(){
        request(HttpMethod.PUT);
    }
    public final void delete(){
        request(HttpMethod.DELETE);
    }
    public final void upload(){
        request(HttpMethod.UPLOAD);
    }
    public final void download(DownloadListener listener){
        new DownloadHandler(URL,DOWNLOAD_DIR,EXTENSION,FILENAME).handleDownload(listener);
    }
}
