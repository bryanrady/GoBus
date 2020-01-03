package com.hxd.gobus.mvp.contract;

import com.hxd.gobus.mvp.IBaseView;

/**
 * @author: wangqingbin
 * @date: 2019/7/31 15:13
 */

public interface IFilePreviewContract {

    interface View extends IBaseView {
        void initOpenFileData(String fileName,String fileSuffix,String filePath);
        void initDownloadFileData(String fileName,String fileSuffix);

        void startDownload();
        void showDownloadProgress(int progress,double current,double total);
        void downloadFinished(String path);
        void downloadFailure();
    }

    interface Model{

    }
}
