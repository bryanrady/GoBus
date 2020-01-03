package com.hxd.gobus.core.http.retrofit;


import com.hxd.gobus.core.http.callback.IError;
import com.hxd.gobus.core.http.callback.IFailure;
import com.hxd.gobus.core.http.callback.IRequest;
import com.hxd.gobus.core.http.callback.ISuccess;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by wangqingbin on 2018/11/6.
 */

public class RetrofitClientBuilder {

    private HashMap<String,Object> mParams;
    private String mUrl;
    private IRequest mRequest;
    private ISuccess mSuccess;
    private IFailure mFailure;
    private IError mError;
    private RequestBody mBody;

    //上传下载
    private File mFile;

    private String mDownloadDir;
    private String mExtension;
    private String mFilename;

    RetrofitClientBuilder(){

    }

    public final RetrofitClientBuilder url(String url){
        this.mUrl=url;
        return this;
    }

    public final RetrofitClientBuilder params(HashMap<String,Object> params){
        this.mParams=params;
        return this;
    }

    public final RetrofitClientBuilder success(ISuccess success){
        this.mSuccess=success;
        return this;
    }

    public final RetrofitClientBuilder request(IRequest request){
        this.mRequest=request;
        return this;
    }

    public final RetrofitClientBuilder error(IError error ){
        this.mError=error;
        return this;
    }

    public final RetrofitClientBuilder failure(IFailure failure){
        this.mFailure=failure;
        return this;
    }

    public final RetrofitClientBuilder raw(String raw){
        this.mBody=RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),raw);
        return this;
    }

    //上传
    public final RetrofitClientBuilder file(File file){
        this.mFile=file;
        return this;
    }

    public final RetrofitClientBuilder file(String file){
        this.mFile=new File(file);
        return this;
    }

    //下载
    public final RetrofitClientBuilder dir(String dir){
        this.mDownloadDir=dir;
        return this;
    }

    public final RetrofitClientBuilder extension(String extension){
        this.mExtension=extension;
        return this;
    }

    public final RetrofitClientBuilder filename(String filename){
        this.mFilename=filename;
        return this;
    }


    public final RetrofitClient build(){
        return new RetrofitClient(mParams,mUrl,mRequest,mSuccess,mFailure,mError,mBody,mFile,mDownloadDir,mExtension,mFilename);
    }
}
