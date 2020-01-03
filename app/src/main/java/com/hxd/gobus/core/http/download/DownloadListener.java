package com.hxd.gobus.core.http.download;

/**
 * @author: wangqingbin
 * @date: 2019/7/17 15:46
 */

public interface DownloadListener {

    void onStart();//下载开始

    void onProgress(int progress,long currentLength,long total);//下载进度

    void onFinish(String path);//下载完成

    void onFailure(Throwable t);//下载失败

    void onError(int errorCode,String message);//下载错误

}
