package com.hxd.gobus.mvp.presenter;

import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.hxd.gobus.BusApp;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.download.DownloadListener;
import com.hxd.gobus.core.http.retrofit.RetrofitClient;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IFilePreviewContract;
import com.hxd.gobus.mvp.model.FilePreviewModel;
import com.hxd.gobus.utils.FileUtil;

import java.io.File;

import javax.inject.Inject;

/**
 * @author: wangqingbin
 * @date: 2019/7/25 10:26
 */
@ActivityScope
public class FilePreviewPresenter extends BasePresenter<IFilePreviewContract.View,FilePreviewModel> {

    private String mDownloadUrl;
    private String mFileName;
    private String mNativeFilePath;
    private String mFileSuffix;

    private String mDataStatus; //0 open 1 download

    @Inject
    public FilePreviewPresenter(){

    }

    public String getDataStatus(){
        return mDataStatus;
    }

    public void getIntentData(Intent intent) {
        mFileName = intent.getStringExtra("fileName");
        mNativeFilePath = intent.getStringExtra("nativePath");
        mDownloadUrl = intent.getStringExtra("fileUrl");
        if (!TextUtils.isEmpty(mNativeFilePath)){
            File localFile = new File(mNativeFilePath);
            if (localFile.exists()){
                if (!TextUtils.isEmpty(mFileName)){
                    mFileSuffix = FileUtil.getFileNameNoEx(mNativeFilePath);
                    getView().initOpenFileData(mFileName,mFileSuffix,mNativeFilePath);
                    mDataStatus = "0";
                }
            }else{
                if (!TextUtils.isEmpty(mDownloadUrl) && !TextUtils.isEmpty(mFileName)){
                    checkNativeFile();
                }
            }
        }else{
            if (!TextUtils.isEmpty(mDownloadUrl) && !TextUtils.isEmpty(mFileName)){
                checkNativeFile();
            }
        }
    }

    public void openFile(String path){
        String fileSuffix = "."+FileUtil.getFileNameNoEx(path);
        FileUtil.openFile2(BusApp.getContext(),new File(path),fileSuffix);
    }

    private void checkNativeFile() {
        String nativeFileDir;
        if(TextUtils.isEmpty(Environment.getExternalStorageDirectory().getAbsolutePath())){
            nativeFileDir = BusApp.getContext().getCacheDir().getAbsolutePath() + "/gobus/attach";
        } else{
            nativeFileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/gobus/attach";
        }
        File nativeFile = new File(nativeFileDir, mFileName);
        if (!nativeFile.exists()){
            mFileSuffix = FileUtil.getFileNameNoEx(mDownloadUrl);
            getView().initDownloadFileData(mFileName,mFileSuffix);
            mDataStatus = "1";
        }else{
            mFileSuffix = FileUtil.getFileNameNoEx(nativeFile.getAbsolutePath());
            getView().initOpenFileData(mFileName,mFileSuffix,nativeFile.getAbsolutePath());
            mDataStatus = "0";
        }
    }

    public void downFile() {
        String downloadDir;
        if(TextUtils.isEmpty(Environment.getExternalStorageDirectory().getAbsolutePath())){
            downloadDir = BusApp.getContext().getCacheDir().getAbsolutePath() + "/gobus/attach";
        } else{
            downloadDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/gobus/attach";
        }
        if (TextUtils.isEmpty(mDownloadUrl) || TextUtils.isEmpty(mFileName)){
            return;
        }
        String downloadUrl;
        if ("doc".equals(mFileSuffix) || "docx".equals(mFileSuffix) ||"pdf".equals(mFileSuffix)){
        //    downloadUrl = "http://192.168.19.34:9999/xuni/ImagesUploaded/" + mDownloadUrl;
            downloadUrl = Constant.PDFDOC_DOWNLOAD_URL + mDownloadUrl;
        }else{
        //    downloadUrl = "http://192.168.19.34:9999/xuni/" + mDownloadUrl;
            downloadUrl = Constant.ATTACH_PICTURE_DOWNLOAD_URL + mDownloadUrl;
        }
        RetrofitClient.create()
                .url(downloadUrl)
                .dir(downloadDir)
                .filename(mFileName)
                .extension(mFileSuffix)
                .build()
                .download(new DownloadListener() {
                    @Override
                    public void onStart() {
                        getView().startDownload();
                    }

                    @Override
                    public void onProgress(int progress,long currentLength,long total) {
                        double totalM = total/(1024*1024);
                        double curM = currentLength/(1024*1024);
                        getView().showDownloadProgress(progress,curM,totalM);
                    }

                    @Override
                    public void onFinish(String path) {
                        getView().downloadFinished(path);
                        mDataStatus = "0";
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                        getView().downloadFailure();
                    }

                    @Override
                    public void onError(int errorCode, String message) {
                        getView().showToast(message);
                        getView().downloadFailure();
                    }
                });
    }

}
