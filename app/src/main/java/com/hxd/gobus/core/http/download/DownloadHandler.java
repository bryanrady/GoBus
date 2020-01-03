package com.hxd.gobus.core.http.download;

import android.os.AsyncTask;

import com.hxd.gobus.core.http.RetrofitCreator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 文件下载
 * Created by wangqingbin on 2018/6/6.
 */

public class DownloadHandler {
    private final String URL;
    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final String FILENAME;

    public DownloadHandler(String url, String downloadDir, String extension, String filename) {
        this.URL = url;
        this.DOWNLOAD_DIR = downloadDir;
        this.EXTENSION = extension;
        this.FILENAME = filename;
    }

    public final void handleDownload(final DownloadListener listener){
        RetrofitCreator.getRetrofitService().download(URL).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            //开始保存文件,开一个异步任务来做
                            SaveFileTask task = new SaveFileTask(listener);
                            task.executeOnExecutor(
                                    AsyncTask.THREAD_POOL_EXECUTOR,
                                    DOWNLOAD_DIR,
                                    EXTENSION,
                                    response.body(),
                                    FILENAME);
                        }else{
                            listener.onError(response.code(),response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        listener.onFailure(t);
                    }
                });
    }
}
