package com.hxd.gobus.core.http.rx;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by wangqingbin on 2018/11/6.
 */

public class RxRetrofitClientBuilder {

    private HashMap<String,Object> mParams;
    private String mUrl;
    private RequestBody mBody;

    //上传下载
    private File mFile;

    RxRetrofitClientBuilder(){

    }

    public final RxRetrofitClientBuilder url(String url){
        this.mUrl=url;
        return this;
    }

    public final RxRetrofitClientBuilder params(HashMap<String,Object> params){
        this.mParams=params;
        return this;
    }

    public final RxRetrofitClientBuilder raw(String raw){
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),raw);
        return this;
    }

    //上传
    public final RxRetrofitClientBuilder file(File file){
        this.mFile=file;
        return this;
    }

    public final RxRetrofitClientBuilder file(String file){
        this.mFile=new File(file);
        return this;
    }

    public final RxRetrofitClient build(){
        return new RxRetrofitClient(mParams,mUrl,mBody,mFile);
    }
}
